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

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Collection;
import java.util.Comparator;

/**
 * A class for binding results of {@link DatabaseMetaData#getPseudoColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getPseudoColumns(String, String, String, String, Collection)
 */
@XmlRootElement
@ChildOf(Table.class)
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PseudoColumn
        implements MetadataType {

    private static final long serialVersionUID = -5612575879670895510L;

    public static final Comparator<PseudoColumn> COMPARATOR =
            Comparator.comparing(PseudoColumn::getTableCat, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(PseudoColumn::getTableSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(PseudoColumn::getTableName)
                    .thenComparing(PseudoColumn::getColumnName);

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label("TABLE_NAME")
    private String tableName;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(nillable = false, required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(nillable = true, required = true)
    @Label("COLUMN_SIZE")
    private Integer columnSize;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement(nillable = false, required = true)
    @Label("NUM_PREC_RADIX")
    private int numPrecRadix;

    @XmlElement(nillable = false, required = true)
    @Label("COLUMN_USAGE")
    private String columnUsage;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("REMARKS")
    private String remarks;

    @XmlElement(nillable = false, required = true)
    @Label("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @XmlElement(nillable = false, required = true)
    @Label("IS_NULLABLE")
    private String isNullable;
}
