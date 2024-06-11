package com.bancobacana.cobranca;

import com.bancobacana.cobranca.processor.DebtProcessor;
import com.bancobacana.cobranca.serdes.DebtEmailSerde;
import com.bancobacana.cobranca.serdes.DebtSerde;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MailProcessorTest {

    static Properties config;

    @Mock
    DebtProcessor processor;

    private static final String DEBT_TOPIC = "debts";
    private static final String DEBT_OUTPUT_TOPIC = "debts-output";
    private static final String DEBT_EMAIL_TOPIC = "debt-emails";
    private static final String DEBT_JSON_PATH = "src/main/resources/debt.json";
    private static final String EXPIRED_DEBT_JSON_PATH = "src/main/resources/debt-expired.json";
    private static final String DEBT_EMAIL_JSON_PATH = "src/main/resources/debt-email.json";
    private static final String APPLICATION_ID = "cobranca-app";
    private static final String BOOTSTRAP_SERVERS = "dummy:1234";

    @BeforeAll
    public static void setup(){
        config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, DebtSerde.class.getName());
    }


    @Test
    public void shouldFilterCompatibleDebtStatus() throws IOException {
        StreamsBuilder builder = new StreamsBuilder();

        when(processor.filterEmailCompatibleDebts(any(), any())).thenCallRealMethod();

        builder.stream(DEBT_TOPIC, Consumed.with(Serdes.String(), new DebtSerde())).filter((k, v) -> processor.filterEmailCompatibleDebts(k,v))
                .to(DEBT_OUTPUT_TOPIC);

        Topology topology = builder.build();

        TopologyTestDriver testDriver = new TopologyTestDriver(topology, config);

        TestInputTopic<String, String> inputTopic =
                testDriver.createInputTopic(
                        DEBT_TOPIC, new StringSerializer(), new StringSerializer());

        TestOutputTopic<String, String> outputTopic =
                testDriver.createOutputTopic(
                        DEBT_OUTPUT_TOPIC, new StringDeserializer(), new StringDeserializer());

        String debtPayload = FileUtils.readFileToString(new File(DEBT_JSON_PATH), StandardCharsets.UTF_8);
        inputTopic.pipeInput(debtPayload);

        assertEquals(List.of(debtPayload), outputTopic.readValuesToList());
    }

    @Test
    public void shouldFilterOutIncompatibleDebtStatus() throws IOException {
        StreamsBuilder builder = new StreamsBuilder();
        when(processor.filterEmailCompatibleDebts(any(), any())).thenCallRealMethod();

        builder.stream(DEBT_TOPIC, Consumed.with(Serdes.String(), new DebtSerde())).filter((k, v) -> processor.filterEmailCompatibleDebts(k,v))
                .to(DEBT_OUTPUT_TOPIC);

        Topology topology = builder.build();

        TopologyTestDriver testDriver = new TopologyTestDriver(topology, config);

        TestInputTopic<String, String> inputTopic =
                testDriver.createInputTopic(
                        DEBT_TOPIC, new StringSerializer(), new StringSerializer());

        TestOutputTopic<String, String> outputTopic =
                testDriver.createOutputTopic(
                        DEBT_OUTPUT_TOPIC, new StringDeserializer(), new StringDeserializer());

        String expiredDebtPayload = FileUtils.readFileToString(new File(EXPIRED_DEBT_JSON_PATH), StandardCharsets.UTF_8);

        inputTopic.pipeInput(expiredDebtPayload);

        assertEquals(Collections.emptyList(), outputTopic.readValuesToList());
    }

    @Test
    public void shouldMapToDebtEmail() throws IOException {
        StreamsBuilder builder = new StreamsBuilder();

        when(processor.convertToMail(any(), any())).thenCallRealMethod();

        builder.stream(DEBT_TOPIC, Consumed.with(Serdes.String(), new DebtSerde())).map((k,v) -> processor.convertToMail(k,v))
                .to(DEBT_EMAIL_TOPIC);

        Topology topology = builder.build();

        TopologyTestDriver testDriver = new TopologyTestDriver(topology, config);

        TestInputTopic<String, String> inputTopic =
                testDriver.createInputTopic(
                        DEBT_TOPIC, new StringSerializer(), new StringSerializer());

        TestOutputTopic<String, String> outputTopic =
                testDriver.createOutputTopic(
                        DEBT_EMAIL_TOPIC, new StringDeserializer(), new StringDeserializer());

        String debtPayload = FileUtils.readFileToString(new File(DEBT_JSON_PATH), StandardCharsets.UTF_8);
        String debtEmailPayload = FileUtils.readFileToString(new File(DEBT_EMAIL_JSON_PATH), StandardCharsets.UTF_8);

        inputTopic.pipeInput(debtPayload);

        assertEquals(List.of(debtEmailPayload), outputTopic.readValuesToList());
    }

}