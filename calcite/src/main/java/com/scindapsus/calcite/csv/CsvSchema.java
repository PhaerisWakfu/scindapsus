package com.scindapsus.calcite.csv;

import com.google.common.collect.ImmutableMap;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.util.Source;
import org.apache.calcite.util.Sources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * @author wyh
 * @since 1.0
 */
public class CsvSchema extends AbstractSchema {

    private final Logger log = LoggerFactory.getLogger(CsvSchema.class);

    public static final String SUFFIX = ".csv";

    private Map<String, Table> tableMap;

    private final File dirFile;

    public CsvSchema(File dirFile) {
        this.dirFile = dirFile;
    }

    @Override
    protected Map<String, Table> getTableMap() {
        if (tableMap == null) {
            tableMap = createTableMap();
        }
        return tableMap;
    }

    private Map<String, Table> createTableMap() {
        final Source baseSource = Sources.of(dirFile);
        File[] files = dirFile.listFiles((dir, name) -> name.endsWith(SUFFIX));
        if (files == null) {
            log.warn("directory {} not found", dirFile);
            files = new File[0];
        }
        // Build a map from table name to table; each file becomes a table.
        final ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
        for (File file : files) {
            Source source = Sources.of(file);
            final Source sourceSansCsv = source.trimOrNull(SUFFIX);
            if (sourceSansCsv != null) {
                final Table table = new CsvScalableTable(source, null);
                builder.put(sourceSansCsv.relative(baseSource).path(), table);
            }
        }
        return builder.build();
    }

    /**
     * Looks for a suffix on a string and returns
     * either the string with the suffix removed
     * or the original string.
     */
    private static String trim(String s, String suffix) {
        String trimmed = trimOrNull(s, suffix);
        return trimmed != null ? trimmed : s;
    }

    /**
     * Looks for a suffix on a string and returns
     * either the string with the suffix removed
     * or null.
     */
    private static String trimOrNull(String s, String suffix) {
        return s.endsWith(suffix)
                ? s.substring(0, s.length() - suffix.length())
                : null;
    }
}
