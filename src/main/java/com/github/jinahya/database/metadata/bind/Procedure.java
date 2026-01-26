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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String)
 * @see ProcedureColumn
 */
@EqualsAndHashCode(callSuper = true)
public class Procedure
        extends AbstractMetadataType {

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
    
    public String getProcedureCat() {
        return procedureCat;
    }

    protected void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
    }

    // -------------------------------------------------------------------------------------------------- procedureSchem
    
    public String getProcedureSchem() {
        return procedureSchem;
    }

    protected void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
    }

    // --------------------------------------------------------------------------------------------------- procedureName
    public String getProcedureName() {
        return procedureName;
    }

    protected void setProcedureName(final String procedureName) {
        this.procedureName = procedureName;
    }

    // ---------------------------------------------------------------------------------------------------------- remark
    public String getRemarks() {
        return remarks;
    }

    protected void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // --------------------------------------------------------------------------------------------------- procedureType
    public Integer getProcedureType() {
        return procedureType;
    }

    protected void setProcedureType(final Integer procedureType) {
        this.procedureType = procedureType;
    }

    // ---------------------------------------------------------------------------------------------------- specificName
    public String getSpecificName() {
        return specificName;
    }

    protected void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    
    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_CAT")
    private String procedureCat;

    
    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_SCHEM")
    private String procedureSchem;

    @_ColumnLabel("PROCEDURE_NAME")
    private String procedureName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_ColumnLabel("PROCEDURE_TYPE")
    private Integer procedureType;

    // https://github.com/microsoft/mssql-jdbc/issues/2320
    @_ColumnLabel(COLUMN_LABEL_SPECIFIC_NAME)
    private String specificName;
}
