package com.scindapsus.dbzm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import java.util.stream.Collectors;

@Slf4j
public class ChangeDataCaptureListener {

    private static final String CHANGE_DATA_PAYLOAD = "payload";

    private final List<DebeziumEngine<ChangeEvent<String, String>>> engineList = new CopyOnWriteArrayList<>();

    private final Executor taskExecutor;

    private final List<CDCEvent> events;


    public ChangeDataCaptureListener(Executor taskExecutor, List<CDCEvent> events, List<Configuration> configurations) {
        this.taskExecutor = taskExecutor;
        this.events = events;
        List<DebeziumEngine<ChangeEvent<String, String>>> engines = configurations.stream()
                .map(configuration -> DebeziumEngine.create(Json.class)
                        .using(configuration.asProperties())
                        .notifying(this::receiveChangeEvent)
                        .build()).collect(Collectors.toList());
        this.engineList.addAll(engines);
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

    private void receiveChangeEvent(ChangeEvent<String, String> event) {
        Optional.ofNullable(event).ifPresent(ce -> {
            JSONObject key = Optional.ofNullable(ce.key())
                    .map(k -> JSON.parseObject(k).getJSONObject(CHANGE_DATA_PAYLOAD))
                    .orElse(null);
            Optional.ofNullable(ce.value()).ifPresent(v -> {
                ChangeData changeData = JSON.parseObject(v).getObject(CHANGE_DATA_PAYLOAD, ChangeData.class);
                if (!StringUtils.hasText(changeData.getOp())) {
                    return;
                }
                events.forEach(e -> e.onMessage(ce.destination(), key, changeData));
            });
        });
    }
}