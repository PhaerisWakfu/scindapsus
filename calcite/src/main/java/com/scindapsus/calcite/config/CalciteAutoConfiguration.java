package com.scindapsus.calcite.config;

import cn.hutool.core.io.FileUtil;
import com.scindapsus.calcite.CalciteDatasource;
import com.scindapsus.calcite.STHolder;
import com.scindapsus.calcite.Schema;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static com.scindapsus.calcite.constants.CommonConstants.CONFIG_PATH;

/**
 * @author wyh
 * @since 2022/10/14
 */
@Configuration
@EnableConfigurationProperties(CalciteProperties.class)
class CalciteAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    @Primary
    public CalciteDatasource calciteDatasource(CalciteProperties calciteProperties) throws IOException {
        List<Schema> schemas = calciteProperties.getSchemas();
        if (schemas == null || schemas.isEmpty()) {
            throw new IllegalArgumentException("Please set your schema config.");
        }
        schemas.forEach(s -> s.setName(s.getName().toUpperCase(Locale.ROOT)));
        String content = STHolder.getConfig(schemas, schemas.get(0).getName());
        String path = getPath();
        FileUtil.appendString(content, path, StandardCharsets.UTF_8);
        return new CalciteDatasource(path, true);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private String getPath() throws IOException {
        File file = FileUtil.file(CONFIG_PATH);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file.getAbsolutePath();
    }
}
