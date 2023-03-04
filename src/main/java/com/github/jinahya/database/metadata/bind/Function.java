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
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctions(String, String, String)
 */
@_ParentOf(FunctionColumn.class)
public class Function extends AbstractMetadataType {

    private static final long serialVersionUID = -3318947900237453301L;

    static final Comparator<Function> CASE_INSENSITIVE_ORDER =
            comparing(Function::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Function::getFunctionName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Function::getSpecificName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<Function> LEXICOGRAPHIC_ORDER =
            comparing(Function::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER)
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
        return Objects.equals(functionCat, that.functionCat) &&
               Objects.equals(functionSchem, that.functionSchem) &&
               Objects.equals(specificName, that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                functionCat,
                functionSchem,
                specificName
        );
    }

    public String getFunctionCat() {
        return functionCat;
    }

    public void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
        functionId = null;
    }

    public String getFunctionSchem() {
        return functionSchem;
    }

    public void setFunctionSchem(final String functionSchem) {
        this.functionSchem = functionSchem;
        functionId = null;
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

    public int getFunctionType() {
        return functionType;
    }

    public void setFunctionType(int functionType) {
        this.functionType = functionType;
    }

    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
        functionId = null;
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

    @_ColumnLabel("FUNCTION_TYPE")
    private int functionType;

    @_ColumnLabel("SPECIFIC_NAME")
    private String specificName;

    String getFunctionCatNonNull() {
        return Optional.ofNullable(getFunctionCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getFunctionSchemNonNull() {
        return Optional.ofNullable(getFunctionSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    FunctionId getFunctionId() {
        if (functionId == null) {
            functionId = FunctionId.of(
                    getFunctionCatNonNull(),
                    getFunctionSchemNonNull(),
                    getSpecificName()
            );
        }
        return functionId;
    }

    private SchemaId getSchemaId() {
        return getFunctionId().getSchemaId();
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient FunctionId functionId;
}
