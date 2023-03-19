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
import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctions(String, String, String)
 */
@_ParentOf(FunctionColumn.class)
public class Function extends AbstractMetadataType {

    private static final long serialVersionUID = -3318947900237453301L;

    static final Comparator<Function> CASE_INSENSITIVE_ORDER =
            comparing(Function::getFunctionCat, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Function::getFunctionSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Function::getFunctionName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Function::getSpecificName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<Function> LEXICOGRAPHIC_ORDER =
            comparing(Function::getFunctionCat, nullsFirst(naturalOrder()))
                    .thenComparing(Function::getFunctionSchem, nullsFirst(naturalOrder()))
                    .thenComparing(Function::getFunctionName, nullsFirst(naturalOrder()))
                    .thenComparing(Function::getSpecificName, nullsFirst(naturalOrder()));

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_CAT = "FUNCTION_CAT";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_TYPE = "FUNCTION_TYPE";

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Function)) return false;
        final Function that = (Function) obj;
        return Objects.equals(functionCatNonNull(), that.functionCatNonNull()) &&
               Objects.equals(functionSchemNonNull(), that.functionSchemNonNull()) &&
               Objects.equals(specificName, that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                functionCatNonNull(),
                functionSchemNonNull(),
                specificName
        );
    }

    public String getFunctionCat() {
        return functionCat;
    }

    public void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
    }

    public String getFunctionSchem() {
        return functionSchem;
    }

    public void setFunctionSchem(final String functionSchem) {
        this.functionSchem = functionSchem;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @_NotNull
    public Integer getFunctionType() {
        return functionType;
    }

    public void setFunctionType(@_NotNull final Integer functionType) {
        this.functionType = functionType;
    }

    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_CAT)
    private String functionCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_SCHEM)
    private String functionSchem;

    @_ColumnLabel("FUNCTION_NAME")
    @EqualsAndHashCode.Exclude
    private String functionName;

    @_NullableByVendor("PostgreSQL")
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_NotNull
    @_ColumnLabel("FUNCTION_TYPE")
    private Integer functionType;

    @_ColumnLabel("SPECIFIC_NAME")
    private String specificName;

    String functionCatNonNull() {
        final String functionCat_ = getFunctionCat();
        if (functionCat_ != null) {
            return functionCat_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }

    String functionSchemNonNull() {
        final String functionSchem_ = getFunctionSchem();
        if (functionSchem_ != null) {
            return functionSchem_;
        }
        return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
    }
}
