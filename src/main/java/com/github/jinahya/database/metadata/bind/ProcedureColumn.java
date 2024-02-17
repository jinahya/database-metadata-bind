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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiPredicate;

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
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class ProcedureColumn
        extends AbstractMetadataType {

    private static final long serialVersionUID = 3894753719381358829L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<ProcedureColumn> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator.comparing(ProcedureColumn::getProcedureCat, ContextUtils.nulls(context, comparator))
                .thenComparing(ProcedureColumn::getProcedureSchem, ContextUtils.nulls(context, comparator))
                .thenComparing(ProcedureColumn::getProcedureName, ContextUtils.nulls(context, comparator))
                .thenComparing(ProcedureColumn::getSpecificName, ContextUtils.nulls(context, comparator));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static final BiPredicate<ProcedureColumn, Procedure> IS_OF = (c, p) -> {
        return Objects.equals(c.procedureCat, p.getProcedureCat()) &&
               Objects.equals(c.procedureSchem, p.getProcedureSchem()) &&
               Objects.equals(c.procedureName, p.getProcedureName());
    };

    // ----------------------------------------------------------------------------------------------------- COLUMN_TYPE
    public static final String COLUMN_LABEL_COLUMN_TYPE = "COLUMN_TYPE";

    public enum ColumnType
            implements _IntFieldEnum<ColumnType> {

        PROCEDURE_COLUMN_UNKNOWN(DatabaseMetaData.procedureColumnUnknown), // 0
        PROCEDURE_COLUMN_IN(DatabaseMetaData.procedureColumnIn),           // 1
        PROCEDURE_COLUMN_IN_OUT(DatabaseMetaData.procedureColumnInOut),    // 2
        PROCEDURE_COLUMN_RESULT(DatabaseMetaData.procedureColumnResult),   // 3
        PROCEDURE_COLUMN_OUT(DatabaseMetaData.procedureColumnOut),         // 4
        PROCEDURE_COLUMN_RETURN(DatabaseMetaData.procedureColumnReturn)    // 5
        ;

        ColumnType(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // -------------------------------------------------------------------------------------------------------- NULLABLE
    public static final String COLUMN_NAME_NULLABLE = "NULLABLE";

    public enum Nullable
            implements _IntFieldEnum<Nullable> {

        PROCEDURE_NO_NULLS(DatabaseMetaData.procedureNoNulls),// 0

        PROCEDURE_NULLABLE(DatabaseMetaData.procedureNullable), // 1

        PROCEDURE_NULLABLE_UNKNOWN(DatabaseMetaData.procedureNullableUnknown) // 2
        ;

        public static Nullable valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(Nullable.class, fieldValue);
        }

        Nullable(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // ---------------------------------------------------------------------------------------------------- procedureCat

    // -------------------------------------------------------------------------------------------------- procedureSchem

    // -----------------------------------------------------------------------------------------------------------------

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_CAT")
    private String procedureCat;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_SCHEM")
    private String procedureSchem;

    @_ColumnLabel("PROCEDURE_NAME")
    @EqualsAndHashCode.Include
    private String procedureName;

    @_ColumnLabel("COLUMN_NAME")
    @EqualsAndHashCode.Include
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_COLUMN_TYPE)
    private Integer columnType;

    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("PRECISION")
    private Integer precision;

    @_ColumnLabel("LENGTH")
    private Integer length;

    // https://issues.apache.org/jira/browse/DERBY-7103
    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCALE")
    private Integer scale;

    @_ColumnLabel("RADIX")
    private Integer radix;

    @_ColumnLabel("NULLABLE")
    private Integer nullable;

    @_ColumnLabel("REMARKS")
    private String remarks;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("COLUMN_DEF")
    private String columnDef;

    @_ReservedBySpecification
    @_ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @_ReservedBySpecification
    @_ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/2320
    @_ColumnLabel("SPECIFIC_NAME")
    @EqualsAndHashCode.Include
    private String specificName;
}
