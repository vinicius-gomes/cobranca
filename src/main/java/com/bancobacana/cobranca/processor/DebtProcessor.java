package com.bancobacana.cobranca.processor;

import com.bancobacana.cobranca.mail.MailService;
import com.bancobacana.cobranca.model.Debt;
import com.bancobacana.cobranca.model.DebtEmail;
import com.bancobacana.cobranca.model.DebtStatus;
import com.bancobacana.cobranca.serdes.DebtEmailSerde;
import com.bancobacana.cobranca.serdes.DebtSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DebtProcessor {

    @Autowired
    private StreamsBuilder builder;

    @Autowired
    private MailService mailService;

    @Bean
    public void streamTopic(){
        builder.stream("debts", Consumed.with(Serdes.String(), new DebtSerde()))
                .filter(this::filterEmailCompatibleDebts)
                .map(this::convertToMail)
                .peek((key,value) -> System.out.println(value.toString().toLowerCase()))
                .to("debt-emails", Produced.with(Serdes.String(), new DebtEmailSerde()));

        builder.stream("debt-emails", Consumed.with(Serdes.String(), new DebtEmailSerde()))
                .peek((key,value) -> mailService.sendEmail(value));
    }

    public KeyValue<String, DebtEmail> convertToMail(String key, Debt debt) {
        DebtEmail email = DebtEmail.convert(debt);
        return new KeyValue<>(key, email);
    }

    public boolean filterEmailCompatibleDebts(String key, Debt debt) {
        return debt.getStatus().equals(DebtStatus.CLOSED);
    }
}
