package com.scindapsus.calcite;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

import static com.scindapsus.calcite.constants.CommonConstants.*;


/**
 * @author wyh
 * @since 2022/8/3 11:00
 */
public class STHolder {

    private static final STGroupFile ST_GROUP_FILE = new STGroupFile(TEMP_PATH);

    /**
     * 获取calcite配置
     *
     * @param schemas schema列表
     * @param def     默认schema
     * @return instance
     */
    public static String getConfig(List<Schema> schemas, String def) {
        ST st = ST_GROUP_FILE.getInstanceOf(ST_NAME_GET_CONFIG);
        st.add(ST_ARG_SCHEMAS, schemas);
        st.add(ST_ARG_DEFAULT, def);
        return st.render();
    }
}
