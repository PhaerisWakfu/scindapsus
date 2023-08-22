package com.scindapsus.dbzm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wyh
 * @since 2023/8/22
 */
@AllArgsConstructor
@Getter
public enum OffsetBackingStoreEnum {

    FILE("org.apache.kafka.connect.storage.FileOffsetBackingStore"),

    MEMORY("org.apache.kafka.connect.storage.MemoryOffsetBackingStore"),

    KAFKA("org.apache.kafka.connect.storage.KafkaOffsetBackingStore");

    private final String storeClassName;
}
