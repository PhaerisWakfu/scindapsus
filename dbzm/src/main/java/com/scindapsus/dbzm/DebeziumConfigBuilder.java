package com.scindapsus.dbzm;

import com.scindapsus.dbzm.config.DebeziumProperties;
import io.debezium.config.Configuration;
import io.debezium.relational.history.FileDatabaseHistory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.kafka.connect.storage.FileOffsetBackingStore;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author wyh
 * @since 2023/8/21
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DebeziumConfigBuilder {

    /**
     * 获取数据源debezium连接配置
     *
     * @return debezium连接配置
     * @throws Exception 可能抛出的异常
     */
    public static Configuration build(DebeziumProperties.DatasourceProperties properties) throws Exception {
        checkFile(properties.getStorageFile());
        Class<?> connectorClass = Class.forName(properties.getConnectorType().getConnectorClassName());
        Configuration.Builder builder = Configuration.create()
                .with("name", connectorClass.getSimpleName())
                //连接器运行模式而不是数据的快照。当您不需要主题包含数据的一致快照，而只需要主题包含自连接器启动以来的更改时，此设置非常有用
                .with("snapshot.mode", "Schema_only")
                //监控的数据库类型
                .with("connector.class", connectorClass)
                //选择FileOffsetBackingStore时,意思把读取进度存到本地文件，因为我们不用kafka，当使用kafka时，选KafkaOffsetBackingStore 。
                .with("offset.storage", FileOffsetBackingStore.class)
                .with("database.history", FileDatabaseHistory.class.getName())
                .with("database.history.file.filename", properties.getHistoryFile())
                .with("offset.storage.file.filename", properties.getStorageFile())
                .with("offset.flush.interval.ms", properties.getFlushInterval())
                .with("database.server.id", properties.getServerId())
                .with("database.server.name", properties.getServerName())
                .with("database.hostname", properties.getHostname())
                .with("database.port", properties.getPort())
                .with("database.user", properties.getUser())
                .with("database.password", properties.getPassword());
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
