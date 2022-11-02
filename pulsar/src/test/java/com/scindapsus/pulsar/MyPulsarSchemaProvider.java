package com.scindapsus.pulsar;

import org.apache.pulsar.client.api.Schema;

/**
 * @author wyh
 * @since 2022/11/1
 */
public class MyPulsarSchemaProvider implements PulsarSchemaProvider<String> {

    @Override
    public Schema<String> get() {
        return Schema.STRING;
    }
}
