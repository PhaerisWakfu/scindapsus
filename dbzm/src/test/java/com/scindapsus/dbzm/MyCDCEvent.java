package com.scindapsus.dbzm;

import io.debezium.data.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wyh
 * @since 2023/8/21
 */
@Slf4j
@Component
public class MyCDCEvent implements CDCEvent {

    @Override
    public void onMessage(String destination, Map<String, Object> key, ChangeData value) {
        log.info("destination ==> {}", destination);
        log.info("operation type ==> {}", Envelope.Operation.forCode(value.getOp()));
        log.info("name before ==> {}", value.getBefore().get("name"));
        log.info("name after ==> {}", value.getAfter().get("name"));
    }
}
