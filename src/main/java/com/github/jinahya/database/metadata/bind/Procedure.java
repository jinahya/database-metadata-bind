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
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String)
 * @see ProcedureColumn
 */
public class Procedure
        extends AbstractMetadataType {

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final var that = (Procedure) obj;
        return Objects.equals(procedureCat, that.procedureCat) &&
               Objects.equals(procedureSchem, that.procedureSchem) &&
               Objects.equals(procedureName, that.procedureName) &&
               Objects.equals(specificName, that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), procedureCat, procedureSchem, procedureName, specificName);
    }

    private static final long serialVersionUID = -6262056388403934829L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Procedure> comparing(final Comparator<? super String> comparator) {
        return Comparator.comparing(Procedure::getProcedureCat, comparator)
                .thenComparing(Procedure::getProcedureSchem, comparator)
                .thenComparing(Procedure::getProcedureName, comparator)
                .thenComparing(Procedure::getSpecificName, comparator);
    }

    static Comparator<Procedure> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return comparing(ContextUtils.nullPrecedence(context, comparator));
    }

    // -------------------------------------------------------------------------------------------------- PROCEDURE_TYPE
    public static final String COLUMN_LABEL_PROCEDURE_TYPE = "PROCEDURE_TYPE";

    public static final int COLUMN_VALUE_PROCEDURE_TYPE_PROCEDURE_RESULT_UNKNOWN =
            DatabaseMetaData.procedureResultUnknown;

    public static final int COLUMN_VALUE_PROCEDURE_TYPE_PROCEDURE_NO_RESULT = DatabaseMetaData.procedureNoResult;

    public static final int COLUMN_VALUE_PROCEDURE_TYPE_PROCEDURE_RETURNS_RESULT =
            DatabaseMetaData.procedureReturnsResult;

    // --------------------------------------------------------------------------------------------------- SPECIFIC_NAME

    /**
     * A colum label of {@value}.
     */
    public static final String COLUMN_LABEL_SPECIFIC_NAME = "SPECIFIC_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_PROCEDURE_CAT = "PROCEDURE_CAT";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_PROCEDURE_SCHEM = "PROCEDURE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_PROCEDURE_NAME = "PROCEDURE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_REMARKS = "REMARKS";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    protected Procedure() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "procedureCat=" + procedureCat +
               ",procedureSchem=" + procedureSchem +
               ",procedureName=" + procedureName +
               ",remarks=" + remarks +
               ",procedureType=" + procedureType +
               ",specificName=" + specificName +
               '}';
    }

    // ---------------------------------------------------------------------------------------------------- procedureCat

    /**
     * Returns the value of {@code PROCEDURE_CAT} column.
     *
     * @return the value of {@code PROCEDURE_CAT} column.
     */
    public String getProcedureCat() {
        return procedureCat;
    }

    /**
     * Sets the value of {@code PROCEDURE_CAT} column.
     *
     * @param procedureCat the value of {@code PROCEDURE_CAT} column.
     */
    protected void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
    }

    // -------------------------------------------------------------------------------------------------- procedureSchem

    /**
     * Returns the value of {@code PROCEDURE_SCHEM} column.
     *
     * @return the value of {@code PROCEDURE_SCHEM} column.
     */
    public String getProcedureSchem() {
        return procedureSchem;
    }

    /**
     * Sets the value of {@code PROCEDURE_SCHEM} column.
     *
     * @param procedureSchem the value of {@code PROCEDURE_SCHEM} column.
     */
    protected void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
    }

    // --------------------------------------------------------------------------------------------------- procedureName

    /**
     * Returns the value of {@code PROCEDURE_NAME} column.
     *
     * @return the value of {@code PROCEDURE_NAME} column.
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * Sets the value of {@code PROCEDURE_NAME} column.
     *
     * @param procedureName the value of {@code PROCEDURE_NAME} column.
     */
    protected void setProcedureName(final String procedureName) {
        this.procedureName = procedureName;
    }

    // ---------------------------------------------------------------------------------------------------------- remark

    /**
     * Returns the value of {@code REMARKS} column.
     *
     * @return the value of {@code REMARKS} column.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of {@code REMARKS} column.
     *
     * @param remarks the value of {@code REMARKS} column.
     */
    protected void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // --------------------------------------------------------------------------------------------------- procedureType

    /**
     * Returns the value of {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     */
    public Integer getProcedureType() {
        return procedureType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     *
     * @param procedureType the value of {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     */
    protected void setProcedureType(final Integer procedureType) {
        this.procedureType = procedureType;
    }

    // ---------------------------------------------------------------------------------------------------- specificName

    /**
     * Returns the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     */
    public String getSpecificName() {
        return specificName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     *
     * @param specificName the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     */
    protected void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PROCEDURE_CAT)
    private String procedureCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PROCEDURE_SCHEM)
    private String procedureSchem;

    @_ColumnLabel(COLUMN_LABEL_PROCEDURE_NAME)
    private String procedureName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    @_ColumnLabel(COLUMN_LABEL_PROCEDURE_TYPE)
    private Integer procedureType;

    // https://github.com/microsoft/mssql-jdbc/issues/2320
    @_ColumnLabel(COLUMN_LABEL_SPECIFIC_NAME)
    private String specificName;
}
