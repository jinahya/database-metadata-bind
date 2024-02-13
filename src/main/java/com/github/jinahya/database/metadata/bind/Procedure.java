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

import jakarta.annotation.Nullable;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String)
 * @see ProcedureColumn
 */
@XmlRootElement
//@XmlType(name = "xxx")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Procedure extends AbstractMetadataType {

    private static final long serialVersionUID = -6262056388403934829L;

    // -----------------------------------------------------------------------------------------------------------------
    static final Comparator<Procedure> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Procedure::getProcedureCat, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Procedure::getProcedureSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Procedure::getProcedureName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Procedure::getSpecificName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<Procedure> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Procedure::getProcedureCat, nullsFirst(naturalOrder()))
                    .thenComparing(Procedure::getProcedureSchem, nullsFirst(naturalOrder()))
                    .thenComparing(Procedure::getProcedureName, nullsFirst(naturalOrder()))
                    .thenComparing(Procedure::getSpecificName, nullsFirst(naturalOrder()));

    // -------------------------------------------------------------------------------------------------- PROCEDURE_TYPE

    /**
     * A colum label of {@value}.
     */
    public static final String COLUMN_LABEL_PROCEDURE_TYPE = "PROCEDURE_TYPE";

    /**
     * Constants for the value of {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     */
    public enum ProcedureType implements _IntFieldEnum<ProcedureType> {

        /**
         * A value for
         * {@link DatabaseMetaData#procedureResultUnknown}({@value DatabaseMetaData#procedureResultUnknown}).
         */
        PROCEDURE_RESULT_UNKNOWN(DatabaseMetaData.procedureResultUnknown), // 0

        /**
         * A value for {@link DatabaseMetaData#procedureNoResult}({@value DatabaseMetaData#procedureNoResult}).
         */
        PROCEDURE_NO_RESULT(DatabaseMetaData.procedureNoResult), // 1

        /**
         * A value for
         * {@link DatabaseMetaData#procedureReturnsResult}({@value DatabaseMetaData#procedureReturnsResult}).
         */
        PROCEDURE_RETURNS_RESULT(DatabaseMetaData.procedureReturnsResult); // 2

        /**
         * Returns the value whose {@link #fieldValueAsInt() procedureType} matches to specified value.
         *
         * @param fieldValue the value of {@link #fieldValueAsInt() procedureType} to match.
         * @return a matched value.
         */
        public static ProcedureType valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(ProcedureType.class, fieldValue);
        }

        ProcedureType(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // --------------------------------------------------------------------------------------------------- SPECIFIC_NAME

    /**
     * A colum label of {@value}.
     */
    public static final String COLUMN_LABEL_SPECIFIC_NAME = "SPECIFIC_NAME";

    // -----------------------------------------------------------------------------------------------------------------
    public static final BiPredicate<Procedure, Catalog> IS_OF_CATALOG = (p, c) -> {
        return Objects.equals(p.procedureCat, c.getTableCat());
    };

    public static final BiPredicate<Procedure, Schema> IS_OF_SCHEMA = (p, s) -> {
        return Objects.equals(p.procedureCat, s.getTableCatalog()) &&
               Objects.equals(p.procedureSchem, s.getTableSchem());
    };

    // ---------------------------------------------------------------------------------------------------- procedureCat

    // -------------------------------------------------------------------------------------------------- procedureSchem

    // --------------------------------------------------------------------------------------------------- procedureType
    ProcedureType getProcedureTypeAsEnum() {
        return Optional.ofNullable(getProcedureType())
                .map(ProcedureType::valueOfFieldValue)
                .orElse(null);
    }

    void setProcedureTypeAsEnum(final ProcedureType procedureTypeAsEnum) {
        setProcedureType(
                Optional.ofNullable(procedureTypeAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_CAT")
    @EqualsAndHashCode.Include
    private String procedureCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_SCHEM")
    @EqualsAndHashCode.Include
    private String procedureSchem;

    @_ColumnLabel("PROCEDURE_NAME")
    @EqualsAndHashCode.Include
    private String procedureName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_ColumnLabel("PROCEDURE_TYPE")
    private Integer procedureType;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/2320
    @_ColumnLabel(COLUMN_LABEL_SPECIFIC_NAME)
    @EqualsAndHashCode.Include
    private String specificName;
}
