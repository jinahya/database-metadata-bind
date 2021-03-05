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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getFunctions(java.lang.String,
 * java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see java.sql.DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)
 * @see Context#getFunctions(String, String, String, Collection)
 * @see FunctionColumn
 */
@XmlRootElement
public class Function extends SchemaChild {

    private static final long serialVersionUID = -3318947900237453301L;

    // -------------------------------------------------------------------------------------- FUNCTION_CAT / functionCat
    public static final String COLUMN_NAME_FUNCTION_CAT = "FUNCTION_CAT";

    public static final String ATTRIBUTE_NAME_FUNCTION_CAT = "functionCat";

    // ---------------------------------------------------------------------------------- FUNCTION_SCHEM / functionSchem
    public static final String COLUMN_NAME_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    public static final String ATTRIBUTE_NAME_FUNCTION_SCHEM = "functionSchem";

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + ATTRIBUTE_NAME_FUNCTION_CAT + '=' + functionCat
               + ',' + ATTRIBUTE_NAME_FUNCTION_SCHEM + '=' + functionSchem
               + ",functionName=" + functionName
               + ",remarks=" + remarks
               + ",functionType=" + functionType
               + ",specificName=" + specificName
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Function that = (Function) obj;
        return functionType == that.functionType
               && Objects.equals(functionCat, that.functionCat)
               && Objects.equals(functionSchem, that.functionSchem)
               && Objects.equals(functionName, that.functionName)
               && Objects.equals(remarks, that.remarks)
               && Objects.equals(specificName, that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionCat,
                            functionSchem,
                            functionName,
                            remarks,
                            functionType,
                            specificName);
    }

    // ----------------------------------------------------------------------------------------------------- functionCat
    public String getFunctionCat() {
        return functionCat;
    }

    public void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
    }

    // --------------------------------------------------------------------------------------------------- functionSchem
    public String getFunctionSchem() {
        return functionSchem;
    }

    public void setFunctionSchem(final String functionSchem) {
        this.functionSchem = functionSchem;
    }

    // ---------------------------------------------------------------------------------------------------- functionName
    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    // --------------------------------------------------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ---------------------------------------------------------------------------------------------------- functionType
    public short getFunctionType() {
        return functionType;
    }

    public void setFunctionType(final short functionType) {
        this.functionType = functionType;
    }

    // ---------------------------------------------------------------------------------------------------- specificName
    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // ------------------------------------------------------------------------------------------------- functionColumns

    /**
     * Returns a list of function columns of this function.
     *
     * @return a list of function columns of this function.
     */
    List<FunctionColumn> getFunctionColumns() {
        if (functionColumns == null) {
            functionColumns = new ArrayList<>();
        }
        return functionColumns;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label(COLUMN_NAME_FUNCTION_CAT)
    private String functionCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label(COLUMN_NAME_FUNCTION_SCHEM)
    private String functionSchem;

    @XmlElement(required = true)
    @Label("FUNCTION_NAME")
    private String functionName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true)
    @Label("FUNCTION_TYPE")
    private short functionType;

    @XmlElement(required = true)
    @Label("SPECIFIC_NAME")
    private String specificName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<FunctionColumn> functionColumns;
}
