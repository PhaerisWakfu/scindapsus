package com.scindapsus.pulsar.tools;

import cn.hutool.core.util.ReflectUtil;
import com.scindapsus.pulsar.PulsarSchemaProvider;
import com.scindapsus.pulsar.exception.PulsarConfigException;
import org.apache.pulsar.client.api.Schema;

/**
 * @author wyh
 * @since 2022/11/7
 */
public class ClassUtil {

    private ClassUtil() {
    }

    /**
     * schema class 支持{@link Schema}与{@link PulsarSchemaProvider}
     *
     * @param schemaClassName 自定义schema类名
     * @return {@link Schema}
     */
    public static Schema<?> getSchema(String schemaClassName) {
        Schema<?> schema;
        Object schemaObj = ReflectUtil.newInstance(schemaClassName);
        if (schemaObj instanceof Schema) {
            schema = (Schema<?>) schemaObj;
        } else if (schemaObj instanceof PulsarSchemaProvider) {
            schema = ((PulsarSchemaProvider<?>) schemaObj).get();
        } else {
            throw new PulsarConfigException("unsuitable schema class");
        }
        return schema;
    }
}
