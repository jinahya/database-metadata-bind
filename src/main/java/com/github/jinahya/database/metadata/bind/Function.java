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
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding the result of
 * {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctions(String, String, String)
 */
//@ChildOf(Schema.class)
//@ParentOf(FunctionColumn.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Function
        extends AbstractMetadataType {

    private static final long serialVersionUID = -3318947900237453301L;

    public static final Comparator<Function> COMPARING_FUNCTION_CAT_FUNCTION_SCHEM_FUNCTION_NAME_SPECIFIC_NAME =
//             Comparator.comparing(Function::getFunctionCat, Comparator.nullsFirst(Comparator.naturalOrder()))
//            .thenComparing(Function::getFunctionSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
//            .thenComparing(Function::getFunctionName, Comparator.nullsFirst(Comparator.naturalOrder()))
//            .thenComparing(Function::getSpecificName, Comparator.nullsFirst(Comparator.naturalOrder()));
            Comparator.comparing(Function::getSchemaId)
                    .thenComparing(Function::getFunctionName, Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Function::getSpecificName, Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER));

    public static final String COLUMN_NAME_FUNCTION_CAT = "FUNCTION_CAT";

    public static final String ATTRIBUTE_NAME_FUNCTION_CAT = "functionCat";

    public static final String COLUMN_NAME_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    public static final String ATTRIBUTE_NAME_FUNCTION_SCHEM = "functionSchem";

    public static final String COLUMN_NAME_FUNCTION_TYPE = "FUNCTION_TYPE";

    public FunctionId getFunctionId() {
        return FunctionId.of(getFunctionCatNonNull(), getFunctionSchemNonNull(), getFunctionName(), getSpecificName());
    }

    SchemaId getSchemaId() {
        return getFunctionId().getSchemaId();
    }

    public List<FunctionColumn> getFunctionColumns(final Context context, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getFunctionColumns(
                getFunctionCatNonNull(),
                getFunctionSchemNonNull(),
                getFunctionName(),
                columnNamePattern
        );
    }

    String getFunctionCatNonNull() {
        return Optional.ofNullable(getFunctionCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getFunctionSchemNonNull() {
        return Optional.ofNullable(getFunctionSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

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
}
