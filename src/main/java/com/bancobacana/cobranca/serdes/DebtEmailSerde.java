package com.bancobacana.cobranca.serdes;

import com.bancobacana.cobranca.model.DebtEmail;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class DebtEmailSerde implements Serde<DebtEmail> {

    private final JsonSerializer<DebtEmail> serializer;
    private final JsonDeserializer<DebtEmail> deserializer;

    public DebtEmailSerde(){
        this.deserializer = new JsonDeserializer<>(DebtEmail.class);
        this.serializer = new JsonSerializer<>();
    }

    @Override
    public Serializer<DebtEmail> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<DebtEmail> deserializer() {
        return deserializer;
    }
}
