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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getProcedureColumns(String, String, String, String)
 */
@_ChildOf(Procedure.class)
@Setter
@Getter
@ToString(callSuper = true)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@SuperBuilder(toBuilder = true)
public class ProcedureColumn extends AbstractMetadataType {

    private static final long serialVersionUID = 3894753719381358829L;

    static final Comparator<ProcedureColumn> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(ProcedureColumn::getProcedureCat, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(ProcedureColumn::getProcedureSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(ProcedureColumn::getProcedureName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(ProcedureColumn::getSpecificName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<ProcedureColumn> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(ProcedureColumn::getProcedureCat, nullsFirst(naturalOrder()))
                    .thenComparing(ProcedureColumn::getProcedureSchem, nullsFirst(naturalOrder()))
                    .thenComparing(ProcedureColumn::getProcedureName, nullsFirst(naturalOrder()))
                    .thenComparing(ProcedureColumn::getSpecificName, nullsFirst(naturalOrder()));

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcedureColumn)) return false;
        final ProcedureColumn that = (ProcedureColumn) o;
        return Objects.equals(columnType, that.columnType) &&
               Objects.equals(procedureCatNonNull(), that.procedureCatNonNull()) &&
               Objects.equals(procedureSchemNonNull(), that.procedureSchemNonNull()) &&
               Objects.equals(columnName, that.columnName) &&
               Objects.equals(specificName, that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                procedureCatNonNull(),
                procedureSchemNonNull(),
                columnName,
                columnType,
                specificName
        );
    }

    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_CAT")
    private String procedureCat;

    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_SCHEM")
    private String procedureSchem;

    @_ColumnLabel("PROCEDURE_NAME")
    private String procedureName;

    @_ColumnLabel("COLUMN_NAME")
    private String columnName;

    @_NotNull
    @_ColumnLabel("COLUMN_TYPE")
    private Integer columnType;

    @_NotNull
    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @_NullableBySpecification
    @_ColumnLabel("PRECISION")
    private Integer precision;

    @_NotNull
    @_ColumnLabel("LENGTH")
    private Integer length;

    // https://issues.apache.org/jira/browse/DERBY-7103
    @_NullableBySpecification
    @_ColumnLabel("SCALE")
    private Integer scale;

    @_NotNull
    @_ColumnLabel("RADIX")
    private Integer radix;

    @_NotNull
    @_ColumnLabel("NULLABLE")
    private Integer nullable;

    @_NullableByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7101
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_NullableBySpecification
    @_ColumnLabel("COLUMN_DEF")
    private String columnDef;

    @_Reserved
    @_ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @_Reserved
    @_ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @_NullableBySpecification
    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @_NotNull
    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @_ColumnLabel("SPECIFIC_NAME")
    private String specificName;

    String procedureCatNonNull() {
        final String procedureCat_ = getProcedureCat();
        if (procedureCat_ != null) {
            return procedureCat_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }

    String procedureSchemNonNull() {
        final String procedureSchem_ = getProcedureSchem();
        if (procedureSchem_ != null) {
            return procedureSchem_;
        }
        return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
    }
}
