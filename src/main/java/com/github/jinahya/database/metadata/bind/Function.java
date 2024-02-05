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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;

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
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
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

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_CAT)
    @EqualsAndHashCode.Include
    private String functionCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_SCHEM)
    @EqualsAndHashCode.Include
    private String functionSchem;

    @_ColumnLabel("FUNCTION_NAME")
    @EqualsAndHashCode.Include
    private String functionName;

    @_ColumnLabel("REMARKS")
    private String remarks;

    @_ColumnLabel("FUNCTION_TYPE")
    private Integer functionType;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/849
    @_ColumnLabel("SPECIFIC_NAME")
    @EqualsAndHashCode.Include
    private String specificName;
}
