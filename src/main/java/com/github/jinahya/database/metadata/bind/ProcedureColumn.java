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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String,
 * java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ChildOf(Procedure.class)
@Setter
@Getter
//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class ProcedureColumn extends AbstractMetadataType {

    private static final long serialVersionUID = 3894753719381358829L;

    public static final Comparator<ProcedureColumn> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(ProcedureColumn::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(ProcedureColumn::getProcedureName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(ProcedureColumn::getSpecificName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    public static final Comparator<ProcedureColumn> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(ProcedureColumn::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(ProcedureColumn::getProcedureName, nullsFirst(naturalOrder()))
                    .thenComparing(ProcedureColumn::getSpecificName, nullsFirst(naturalOrder()));

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProcedureColumn)) return false;
        if (!super.equals(obj)) return false;
        final ProcedureColumn that = (ProcedureColumn) obj;
        return Objects.equals(getProcedureColumnId(), that.getProcedureColumnId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProcedureColumnId());
    }

    // ---------------------------------------------------------------------------------------------------- procedureCat
    String getProcedureCatNonNull() {
        return Optional.ofNullable(getProcedureCat())
                .orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
        procedureColumnId = null;
    }

    // -------------------------------------------------------------------------------------------------- procedureSchem
    String getProcedureSchemNonNull() {
        return Optional.ofNullable(getProcedureSchem())
                .orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    public void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
        procedureColumnId = null;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public void setColumnName(String columnName) {
        this.columnName = columnName;
        procedureColumnId = null;
    }

    // ------------------------------------------------------------------------------------------------------ columnType
    public void setColumnType(int columnType) {
        this.columnType = columnType;
        procedureColumnId = null;
    }

    // ---------------------------------------------------------------------------------------------------- specificName
    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
        procedureColumnId = null;
    }

    // ----------------------------------------------------------------------------------------------- procedureColumnId
    public ProcedureColumnId getProcedureColumnId() {
        if (procedureColumnId == null) {
            procedureColumnId = ProcedureColumnId.of(
                    getProcedureCatNonNull(),
                    getProcedureSchemNonNull(),
                    getSpecificName(),
                    getColumnName(),
                    getColumnType()
            );
        }
        return procedureColumnId;
    }

    private ProcedureId getProcedureId() {
        return getProcedureColumnId().getProcedureId();
    }

    private SchemaId getSchemaId() {
        return getProcedureId().getSchemaId();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @NullableBySpecification
    @ColumnLabel("PROCEDURE_CAT")
    private String procedureCat;

    @NullableBySpecification
    @ColumnLabel("PROCEDURE_SCHEM")
    private String procedureSchem;

    @ColumnLabel("PROCEDURE_NAME")
    private String procedureName;

    @ColumnLabel("COLUMN_NAME")
    private String columnName;

    @ColumnLabel("COLUMN_TYPE")
    private int columnType;

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @NullableBySpecification
    @ColumnLabel("PRECISION")
    private Integer precision;

    @ColumnLabel("LENGTH")
    private int length;

    // https://issues.apache.org/jira/browse/DERBY-7103
    @NullableBySpecification
    @ColumnLabel("SCALE")
    private Integer scale;

    @ColumnLabel("RADIX")
    private int radix;

    @ColumnLabel("NULLABLE")
    private int nullable;

    @NullableByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7101
    @ColumnLabel("REMARKS")
    private String remarks;

    @NullableBySpecification
    @ColumnLabel("COLUMN_DEF")
    private String columnDef;

    @Reserved
    @ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @Reserved
    @ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @NullableBySpecification
    @ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @ColumnLabel("ORDINAL_POSITION")
    private int ordinalPosition;

    @ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @ColumnLabel("SPECIFIC_NAME")
    private String specificName;

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient ProcedureColumnId procedureColumnId;
}
