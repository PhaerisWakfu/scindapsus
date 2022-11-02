package com.scindapsus.pulsar;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wyh
 * @since 2022/10/31
 */
public class ProducerListener implements MessageListener<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerListener.class);

    @Override
    public void received(Consumer<String> consumer, Message<String> msg) {
        LOGGER.info("sub[{}] name[{}] listener ->{}", consumer.getSubscription(), consumer.getConsumerName(), msg.getValue());
        try {
            consumer.acknowledge(msg);
        } catch (PulsarClientException e) {
            LOGGER.error(e.getMessage(), e);
            consumer.negativeAcknowledge(msg);
        }
    }
}
