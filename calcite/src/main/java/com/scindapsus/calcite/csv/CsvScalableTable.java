package com.scindapsus.calcite.csv;

import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.file.CsvEnumerator;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.util.ImmutableIntList;
import org.apache.calcite.util.Source;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Table based on a CSV file.
 *
 * <p>It implements the {@link ScannableTable} interface, so Calcite gets
 * data by calling the {@link #scan(DataContext)} method.
 */
public class CsvScalableTable extends CsvTable implements ScannableTable {
    /**
     * Creates a CsvScalableTable.
     */
    CsvScalableTable(Source source, RelProtoDataType protoRowType) {
        super(source, protoRowType);
    }

    @Override
    public String toString() {
        return "CsvScalableTable";
    }

    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        JavaTypeFactory typeFactory = root.getTypeFactory();
        final List<RelDataType> fieldTypes = getFieldTypes(typeFactory);
        final List<Integer> fields = ImmutableIntList.identity(fieldTypes.size());
        final AtomicBoolean cancelFlag = DataContext.Variable.CANCEL_FLAG.get(root);
        return new AbstractEnumerable<Object[]>() {
            @Override
            public Enumerator<Object[]> enumerator() {
                return new CsvEnumerator<>(source, cancelFlag, false, null,
                        CsvEnumerator.arrayConverter(fieldTypes, fields, false));
            }
        };
    }
}