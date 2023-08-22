package com.scindapsus.dbzm;

import com.scindapsus.dbzm.config.DebeziumProperties;
import io.debezium.config.Configuration;
import io.debezium.relational.history.FileDatabaseHistory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author wyh
 * @since 2023/8/21
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DebeziumConfigBuilder {

    /**
     * 获取数据源debezium连接配置
     * <p>
     * 具体配置及其作用详见{@link io.debezium.embedded.EmbeddedEngine}
     *
     * @param name       连接器名称
     * @param properties 连接器配置
     * @return debezium连接配置
     * @throws Exception 可能抛出的异常
     */
    public static Configuration build(String name, DebeziumProperties.DatasourceProperties properties) throws Exception {
        checkFile(properties.getStorageFile());
        Configuration.Builder builder = Configuration.create()
                .with("name", name)
                .with("snapshot.mode", properties.getSnapshotMode())
                .with("connector.class", properties.getConnectorType().getConnectorClassName())
                .with("offset.storage", properties.getOffsetBackingStoreType().getStoreClassName())
                .with("offset.storage.file.filename", properties.getStorageFile())
                .with("offset.flush.interval.ms", properties.getFlushInterval())
                .with("database.history", FileDatabaseHistory.class.getName())
                .with("database.history.file.filename", properties.getHistoryFile())
                .with("database.server.id", properties.getServerId())
                .with("database.server.name", properties.getServerName())
                .with("database.hostname", properties.getHostname())
                .with("database.port", properties.getPort())
                .with("database.user", properties.getUser())
                .with("database.password", properties.getPassword());
        Map<String, String> directProperties = properties.getDirectProperties();
        if (!CollectionUtils.isEmpty(directProperties)) {
            for (Map.Entry<String, String> entry : directProperties.entrySet()) {
                builder.with(entry.getKey(), entry.getValue());
            }
        }
        builder = StringUtils.hasText(properties.getDatabaseWhitelist())
                ? builder.with("database.whitelist", properties.getDatabaseWhitelist())
                : builder.with("table.whitelist", properties.getTableWhitelist());
        return builder.build();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void checkFile(String storageFile) throws IOException {
        String dir = storageFile.substring(0, storageFile.lastIndexOf("/"));
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(storageFile);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
}
