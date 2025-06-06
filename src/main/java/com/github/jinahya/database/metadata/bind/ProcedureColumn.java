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
import java.util.Optional;

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
        extends AbstractMetadataType
        implements HasIsNullableEnum {

    private static final long serialVersionUID = 3894753719381358829L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<ProcedureColumn> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator.comparing(ProcedureColumn::getProcedureCat, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(ProcedureColumn::getProcedureSchem, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(ProcedureColumn::getProcedureName, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(ProcedureColumn::getSpecificName, ContextUtils.nullPrecedence(context, comparator));
    }

    // ----------------------------------------------------------------------------------------------------- COLUMN_TYPE
    public static final String COLUMN_LABEL_COLUMN_TYPE = "COLUMN_TYPE";

    /**
     * Constants for {@value #COLUMN_LABEL_COLUMN_TYPE} column values.
     */
    public enum ColumnType
            implements _IntFieldEnum<ColumnType> {

        /**
         * A value for
         * {@link DatabaseMetaData#procedureColumnUnknown}({@value DatabaseMetaData#procedureColumnUnknown}).
         */
        PROCEDURE_COLUMN_UNKNOWN(DatabaseMetaData.procedureColumnUnknown), // 0

        /**
         * A value for {@link DatabaseMetaData#procedureColumnIn}({@value DatabaseMetaData#procedureColumnIn}).
         */
        PROCEDURE_COLUMN_IN(DatabaseMetaData.procedureColumnIn),           // 1

        /**
         * A value for {@link DatabaseMetaData#procedureColumnInOut}({@value DatabaseMetaData#procedureColumnInOut}).
         */
        PROCEDURE_COLUMN_IN_OUT(DatabaseMetaData.procedureColumnInOut),    // 2

        /**
         * A value for {@link DatabaseMetaData#procedureColumnResult}({@value DatabaseMetaData#procedureColumnResult}).
         */
        PROCEDURE_COLUMN_RESULT(DatabaseMetaData.procedureColumnResult),   // 3

        /**
         * A value for {@link DatabaseMetaData#procedureColumnOut}({@value DatabaseMetaData#procedureColumnOut}).
         */
        PROCEDURE_COLUMN_OUT(DatabaseMetaData.procedureColumnOut),         // 4

        /**
         * A value for {@link DatabaseMetaData#procedureColumnReturn}({@value DatabaseMetaData#procedureColumnReturn}).
         */
        PROCEDURE_COLUMN_RETURN(DatabaseMetaData.procedureColumnReturn)    // 5
        ;

        /**
         * Returns the value whose {@link #fieldValueAsInt() fieldValue} matches specified value.
         *
         * @param fieldValue a value of {@link #fieldValueAsInt() fieldValue} to match.
         * @return the value whose {@link #fieldValueAsInt() fieldValue} matches {@code fieldValue}.
         * @throws IllegalArgumentException no value matches.
         */
        public static ColumnType valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(ColumnType.class, fieldValue);
        }

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

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_NAME_NULLABLE = "NULLABLE";

    /**
     * Constants for {@value #COLUMN_NAME_NULLABLE} column.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum Nullable
            implements _IntFieldEnum<Nullable> {

        /**
         * A value for {@link DatabaseMetaData#procedureNoNulls}({@value DatabaseMetaData#procedureNoNulls}).
         */
        PROCEDURE_NO_NULLS(DatabaseMetaData.procedureNoNulls),                // 0

        /**
         * A value for {@link DatabaseMetaData#procedureNullable}({@value DatabaseMetaData#procedureNullable}).
         */
        PROCEDURE_NULLABLE(DatabaseMetaData.procedureNullable),               // 1

        /**
         * A value for
         * {@link DatabaseMetaData#procedureNullableUnknown}({@value DatabaseMetaData#procedureNullableUnknown}).
         */
        PROCEDURE_NULLABLE_UNKNOWN(DatabaseMetaData.procedureNullableUnknown) // 2
        ;

        /**
         * Returns the value whose {@link #fieldValueAsInt() fieldValue} matches specified value.
         *
         * @param fieldValue the {@link #fieldValueAsInt() fieldValue} value to match.
         * @return the value whose {@link #fieldValueAsInt() fieldValue} matches {@code fieldValue}.
         * @throws IllegalArgumentException if no value matches.
         */
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

    // ------------------------------------------------------------------------------------------------------ columnType

    public ColumnType getColumnTypeAsEnum() {
        return Optional.ofNullable(getColumnType())
                .map(ColumnType::valueOfFieldValue)
                .orElse(null);
    }

    public void setColumnTypeAsEnum(final ColumnType columnTypeAsEnum) {
        setColumnType(
                Optional.ofNullable(columnTypeAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // -------------------------------------------------------------------------------------------------------- nullable
    public Nullable getNullableAsEnum() {
        return Optional.ofNullable(getNullable())
                .map(Nullable::valueOfFieldValue)
                .orElse(null);
    }

    public void setNullableAsEnum(final Nullable nullableAsEnum) {
        setNullable(
                Optional.ofNullable(nullableAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition
    private boolean isOrdinalPositionZeroWhenColumnTypeIsProcedureColumnReturn() {
        if (ordinalPosition == null || columnType == null || columnType != DatabaseMetaData.procedureColumnReturn) {
            return true;
        }
        return ordinalPosition == 0;
    }

    // ------------------------------------------------------------------------------------------------------ isNullable

    public IsNullableEnum getIsNullableAsEnum() {
        return Optional.ofNullable(getIsNullable())
                .map(IsNullableEnum::valueOfFieldValue)
                .orElse(null);
    }

    public void setIsNullableAsEnum(final IsNullableEnum isNullableAsEnum) {
        setIsNullable(
                Optional.ofNullable(isNullableAsEnum)
                        .map(_FieldEnum::fieldValue)
                        .orElse(null)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_CAT")
    private String procedureCat;

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

    @_NullableBySpecification
    @_ColumnLabel("PRECISION")
    private Integer precision;

    @_ColumnLabel("LENGTH")
    private Integer length;

    // https://issues.apache.org/jira/browse/DERBY-7103
    @_NullableBySpecification
    @_ColumnLabel("SCALE")
    private Integer scale;

    @_ColumnLabel("RADIX")
    private Integer radix;

    @_ColumnLabel("NULLABLE")
    private Integer nullable;

    @_ColumnLabel("REMARKS")
    private String remarks;

    @_NullableBySpecification
    @_ColumnLabel("COLUMN_DEF")
    private String columnDef;

    @_ReservedBySpecification
    @_ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @_ReservedBySpecification
    @_ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @_NullableBySpecification
    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    // https://github.com/microsoft/mssql-jdbc/issues/2320
    @_ColumnLabel("SPECIFIC_NAME")
    @EqualsAndHashCode.Include
    private String specificName;
}
