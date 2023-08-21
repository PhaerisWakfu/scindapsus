package com.scindapsus.dbzm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wyh
 * @since 2023/8/21
 */
@Slf4j
@Component
public class MyCDCEvent implements CDCEvent {
    @Override
    public void listen(ChangeData data) {
        log.info("table ==>{}", data.getSource().getDb() + "." + data.getSource().getTable());
        log.info("operation type ==>{}", data.getOp());
        log.info("plan name before ==>{}", data.getBefore().get("name"));
        log.info("plan name after ==>{}", data.getAfter().get("name"));
    }
}
