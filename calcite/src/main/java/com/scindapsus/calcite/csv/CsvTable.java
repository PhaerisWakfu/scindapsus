package com.scindapsus.calcite.csv;

import org.apache.calcite.adapter.file.CsvEnumerator;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.util.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for table that reads CSV files.
 */
public abstract class CsvTable extends AbstractTable {
    protected final Source source;
    protected final RelProtoDataType protoRowType;
    private RelDataType rowType;
    private List<RelDataType> fieldTypes;

    /**
     * Creates a CsvTable.
     */
    CsvTable(Source source, RelProtoDataType protoRowType) {
        this.source = source;
        this.protoRowType = protoRowType;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        if (protoRowType != null) {
            return protoRowType.apply(typeFactory);
        }
        if (rowType == null) {
            rowType = CsvEnumerator.deduceRowType((JavaTypeFactory) typeFactory, source,
                    null, isStream());
        }
        return rowType;
    }

    /**
     * Returns the field types of this CSV table.
     */
    public List<RelDataType> getFieldTypes(RelDataTypeFactory typeFactory) {
        if (fieldTypes == null) {
            fieldTypes = new ArrayList<>();
            CsvEnumerator.deduceRowType((JavaTypeFactory) typeFactory, source,
                    fieldTypes, isStream());
        }
        return fieldTypes;
    }

    /**
     * Returns whether the table represents a stream.
     */
    protected boolean isStream() {
        return false;
    }
}