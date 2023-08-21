package com.scindapsus.dbzm;

import com.alibaba.fastjson.JSON;
import io.debezium.config.Configuration;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

@Slf4j
public class ChangeDataCaptureListener {

    private static final String CHANGE_DATA_NAME = "payload";

    private final List<DebeziumEngine<ChangeEvent<String, String>>> engineList = new CopyOnWriteArrayList<>();

    private final Executor taskExecutor;

    private final List<CDCEvent> events;


    public ChangeDataCaptureListener(Executor taskExecutor, List<CDCEvent> events, Configuration configuration) {
        this.taskExecutor = taskExecutor;
        this.events = events;
        this.engineList.add(DebeziumEngine.create(Json.class)
                .using(configuration.asProperties())
                .notifying(record -> receiveChangeEvent(record.value()))
                .build());
    }

    @PostConstruct
    private void start() {
        for (DebeziumEngine<ChangeEvent<String, String>> engine : engineList) {
            taskExecutor.execute(engine);
        }
    }

    @PreDestroy
    private void stop() {
        for (DebeziumEngine<ChangeEvent<String, String>> engine : engineList) {
            if (engine != null) {
                try {
                    engine.close();
                } catch (IOException e) {
                    log.error("engine close failed ===>", e);
                }
            }
        }
    }

    private void receiveChangeEvent(String value) {
        Optional.ofNullable(value).ifPresent(v -> {
            ChangeData changeData = JSON.parseObject(v).getObject(CHANGE_DATA_NAME, ChangeData.class);
            if (StringUtils.hasText(changeData.getOp())) {
                events.forEach(e -> e.listen(changeData));
            }
        });
    }
}