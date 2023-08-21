package com.scindapsus.dbzm;

/**
 * @author wyh
 * @since 2023/8/21
 */
public interface CDCEvent {

    void listen(ChangeData data);
}
