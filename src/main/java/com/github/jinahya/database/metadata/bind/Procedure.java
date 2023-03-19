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

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

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
@_ParentOf(ProcedureColumn.class)
@_ChildOf(Schema.class)
public class Procedure extends AbstractMetadataType {

    private static final long serialVersionUID = -6262056388403934829L;

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

    /**
     * A colum label of {@value}.
     * <p>
     * <blockquote
     * site="https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/DatabaseMetaData.html#getProcedures(java.lang.String,java.lang.String,java.lang.String)">
     * kind of procedure:
     * <ul>
     *   <li>{@link DatabaseMetaData#procedureResultUnknown procedureResultUnknown} - Cannot determine if a return value will be returned</li>
     *   <li>{@link DatabaseMetaData#procedureNoResult procedureNoResult} - Does not return a return value</li>
     *   <li>{@link DatabaseMetaData#procedureReturnsResult procedureReturnsResult} - Returns a return value</li>
     * </ul>
     * </blockquote>
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
         * @param procedureType the value of {@link #fieldValueAsInt() procedureType} to match.
         * @return a matched value.
         */
        public static ProcedureType valueOfProcedureType(final int procedureType) {
            return _IntFieldEnum.valueOfFieldValue(ProcedureType.class, procedureType);
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

    /**
     * A colum label of {@value}.
     * <p>
     * <blockquote
     * site="https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/DatabaseMetaData.html#getProcedures(java.lang.String,java.lang.String,java.lang.String)">The
     * name which uniquely identifies this procedure within its schema.</blockquote>
     */
    public static final String COLUMN_LABEL_SPECIFIC_NAME = "SPECIFIC_NAME";

    @Override
    public String toString() {
        return "Procedure{" +
               "procedureCat=" + procedureCat +
               ",procedureSchem=" + procedureSchem +
               ",procedureName=" + procedureName +
               ",remarks=" + remarks +
               ",procedureType=" + procedureType +
               ",specificName=" + specificName +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Procedure)) return false;
        final Procedure that = (Procedure) obj;
        return Objects.equals(procedureCatNonNull(), that.procedureCatNonNull()) &&
               Objects.equals(procedureSchemNonNull(), that.procedureSchemNonNull()) &&
               Objects.equals(procedureName, that.procedureName) &&
               Objects.equals(specificName, that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                procedureCatNonNull(),
                procedureSchemNonNull(),
                procedureName,
                specificName
        );
    }

    public String getProcedureCat() {
        return procedureCat;
    }

    public void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
    }

    public String getProcedureSchem() {
        return procedureSchem;
    }

    public void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
    }

    public Integer getProcedureType() {
        return procedureType;
    }

    public void setProcedureType(final Integer procedureType) {
        this.procedureType = procedureType;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(final String procedureName) {
        this.procedureName = procedureName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_CAT")
    private String procedureCat;

    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_SCHEM")
    private String procedureSchem;

    @_ColumnLabel("PROCEDURE_NAME")
    private String procedureName;

    @_NullableByVendor("HSQL")
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_NotNull
    @_ColumnLabel("PROCEDURE_TYPE")
    private Integer procedureType;

    @_ColumnLabel(COLUMN_LABEL_SPECIFIC_NAME)
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

    ProcedureType getProcedureTypeAsEnum() {
        return Optional.ofNullable(getProcedureType())
                .map(ProcedureType::valueOfProcedureType)
                .orElse(null);
    }

    void setProcedureTypeAsEnum(final ProcedureType procedureTypeAsEnum) {
        setProcedureType(
                Optional.ofNullable(procedureTypeAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }
}
