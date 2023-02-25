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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding the results of
 * {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctions(String, String, String)
 */
//@ChildOf(Schema.class)
@ParentOf(FunctionColumn.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Function extends AbstractMetadataType {

    private static final long serialVersionUID = -3318947900237453301L;

    public static final Comparator<Function> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Function::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Function::getFunctionName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Function::getSpecificName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    public static final Comparator<Function> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Function::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(Function::getFunctionName, nullsFirst(naturalOrder()))
                    .thenComparing(Function::getSpecificName, nullsFirst(naturalOrder()));

    public static final String COLUMN_NAME_FUNCTION_CAT = "FUNCTION_CAT";

    public static final String PROPERTY_NAME_FUNCTION_CAT = "functionCat";

    public static final String COLUMN_NAME_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    public static final String PROPERTY_NAME_FUNCTION_SCHEM = "functionSchem";

    public static final String COLUMN_NAME_FUNCTION_TYPE = "FUNCTION_TYPE";

    public static final String PROPERTY_NAME_FUNCTION_TYPE = "functionType";

    // ----------------------------------------------------------------------------------------------------- functionCat
    String getFunctionCatNonNull() {
        return Optional.ofNullable(getFunctionCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
        functionId = null;
    }

    // --------------------------------------------------------------------------------------------------- functionSchem
    String getFunctionSchemNonNull() {
        return Optional.ofNullable(getFunctionSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    public void setFunctionSchem(final String functionSchem) {
        this.functionSchem = functionSchem;
        functionId = null;
    }

    // ---------------------------------------------------------------------------------------------------- specificName
    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
        functionId = null;
    }

    // ------------------------------------------------------------------------------------------------------ functionId
    public FunctionId getFunctionId() {
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

    // -----------------------------------------------------------------------------------------------------------------
    @NullableBySpecification
    @ColumnLabel(COLUMN_NAME_FUNCTION_CAT)
    private String functionCat;

    @NullableBySpecification
    @ColumnLabel(COLUMN_NAME_FUNCTION_SCHEM)
    private String functionSchem;

    @ColumnLabel("FUNCTION_NAME")
    @EqualsAndHashCode.Exclude
    private String functionName;

    @NullableByVendor("PostgreSQL")
    @ColumnLabel("REMARKS")
    private String remarks;

    @ColumnLabel("FUNCTION_TYPE")
    private int functionType;

    @ColumnLabel("SPECIFIC_NAME")
    private String specificName;

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient FunctionId functionId;
}
