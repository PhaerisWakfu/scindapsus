package com.scindapsus.dbzm.config;

import com.scindapsus.dbzm.CDCEvent;
import com.scindapsus.dbzm.ChangeDataCaptureListener;
import com.scindapsus.dbzm.DebeziumConfigBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
                                              io.debezium.config.Configuration configuration) {
        return new ChangeDataCaptureListener(taskExecutor, events, configuration);
    }

    @Bean
    public CDCEvent defaultEvent() {
        return data -> {
            if (log.isDebugEnabled()) log.debug("change data ===>\n{}", data);
        };
    }

    @Configuration
    @ConditionalOnClass(io.debezium.connector.mysql.MySqlConnector.class)
    static class Mysql {

        @Bean
        @ConditionalOnMissingBean(io.debezium.config.Configuration.class)
        public io.debezium.config.Configuration mysqlConfig(DebeziumProperties debeziumProperties) throws Exception {
            return DebeziumConfigBuilder.build(debeziumProperties, io.debezium.connector.mysql.MySqlConnector.class);
        }
    }

    @Configuration
    @ConditionalOnClass(io.debezium.connector.postgresql.PostgresConnector.class)
    static class Postgres {

        @Bean
        @ConditionalOnMissingBean(io.debezium.config.Configuration.class)
        public io.debezium.config.Configuration pgConfig(DebeziumProperties debeziumProperties) throws Exception {
            return DebeziumConfigBuilder.build(debeziumProperties, io.debezium.connector.postgresql.PostgresConnector.class);
        }
    }
}
