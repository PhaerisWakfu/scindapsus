package com.scindapsus.pulsar;

import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author wyh
 * @since 2022/10/28
 */
@Component
public class PulsarSender {

    private final Producer<String> producer1;

    public PulsarSender(Producer<String> producer1) {
        this.producer1 = producer1;
    }

    public CompletableFuture<MessageId> send(String message) {
        return producer1.sendAsync(message);
    }
}
