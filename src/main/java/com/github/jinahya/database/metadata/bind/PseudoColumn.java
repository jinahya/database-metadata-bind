package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * A class for binding results of {@link DatabaseMetaData#getPseudoColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class PseudoColumn
        implements MetadataType,
                   ChildOf<Table> {

    private static final long serialVersionUID = -5612575879670895510L;

    public static final Comparator<PseudoColumn> COMPARATOR =
            Comparator.comparing(PseudoColumn::getTableCat, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(PseudoColumn::getTableSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(PseudoColumn::getTableName)
                    .thenComparing(PseudoColumn::getColumnName);

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    @Override
    public Table extractParent() {
        return Table.builder()
                .tableCat(getTableCat())
                .tableSchem(getTableSchem())
                .tableName(getTableName())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @NullableBySpecification
    @ColumnLabel("TABLE_CAT")
    private String tableCat;

    @NullableBySpecification
    @ColumnLabel("TABLE_SCHEM")
    private String tableSchem;

    @ColumnLabel("TABLE_NAME")
    private String tableName;

    @ColumnLabel("COLUMN_NAME")
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @NullableBySpecification
    @ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @ColumnLabel("NUM_PREC_RADIX")
    private int numPrecRadix;

    @ColumnLabel("COLUMN_USAGE")
    private String columnUsage;

    @NullableBySpecification
    @ColumnLabel("REMARKS")
    private String remarks;

    @ColumnLabel("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @ColumnLabel("IS_NULLABLE")
    private String isNullable;
}
