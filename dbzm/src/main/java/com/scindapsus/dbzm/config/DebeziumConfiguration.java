package com.scindapsus.dbzm.config;

import com.scindapsus.dbzm.CDCEvent;
import com.scindapsus.dbzm.ChangeDataCaptureListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author wyh
 * @since 2023/8/21
 */
@Slf4j
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(DebeziumProperties.class)
public class DebeziumConfiguration {

    @Bean
    @ConditionalOnBean(io.debezium.config.Configuration.class)
    public ChangeDataCaptureListener listener(Executor taskExecutor, List<CDCEvent> events,
                                              List<io.debezium.config.Configuration> configurations) {
        return new ChangeDataCaptureListener(taskExecutor, events, configurations);
    }

    @Bean
    public CDCEvent defaultPrintEvent() {
        return data -> {
            if (log.isDebugEnabled()) log.debug("change data ===>\n{}", data);
        };
    }
}
