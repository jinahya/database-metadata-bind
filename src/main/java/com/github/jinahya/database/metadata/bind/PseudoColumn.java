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

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;

/**
 * A class for binding results of {@link DatabaseMetaData#getPseudoColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getPseudoColumns(String, String, String, String, Collection)
 */
@XmlRootElement
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
    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("TABLE_CAT")
    private String tableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @ColumnLabel("TABLE_NAME")
    private String tableName;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @ColumnLabel("COLUMN_NAME")
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = false, required = true)
    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @XmlElement(nillable = true, required = true)
    @ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("NUM_PREC_RADIX")
    private int numPrecRadix;

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("COLUMN_USAGE")
    private String columnUsage;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("REMARKS")
    private String remarks;

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("IS_NULLABLE")
    private String isNullable;
}
