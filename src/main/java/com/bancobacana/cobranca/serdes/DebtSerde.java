package com.bancobacana.cobranca.serdes;

import com.bancobacana.cobranca.model.Debt;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class DebtSerde implements Serde<Debt> {

    private final JsonSerializer<Debt> serializer;
    private final JsonDeserializer<Debt> deserializer;

    public DebtSerde(){
        this.deserializer = new JsonDeserializer<>(Debt.class);
        this.serializer = new JsonSerializer<>();
    }

    @Override
    public Serializer<Debt> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<Debt> deserializer() {
        return deserializer;
    }
}
