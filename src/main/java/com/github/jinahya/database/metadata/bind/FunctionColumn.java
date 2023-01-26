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

/**
 * A class for binding results of {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String)
 */
@ChildOf(Function.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class FunctionColumn
        extends AbstractMetadataType {

    private static final long serialVersionUID = -7445156446214062680L;
    
    public static final Comparator<FunctionColumn> COMPARING_FUNCTION_CAT_FUNCTION_SCHEM_FUNCTION_NAME_SPECIFIC_NAME
            = Comparator.comparing(FunctionColumn::getFunctionCat, Comparator.nullsFirst(Comparator.naturalOrder()))
            .thenComparing(FunctionColumn::getFunctionSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
            .thenComparing(FunctionColumn::getFunctionName)
            .thenComparing(FunctionColumn::getSpecificName);

    public static final String COLUMN_LABEL_FUNCTION_CAT = "FUNCTION_CAT";

    public static final String COLUMN_LABEL_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    public static final String COLUMN_LABEL_FUNCTION_NAME = "FUNCTION_NAME";

    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    public static final String COLUMN_LABEL_COLUMN_TYPE = "COLUMN_TYPE";

    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    public static final String COLUMN_LABEL_IS_NULLABLE = "IS_NULLABLE";

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_FUNCTION_CAT)
    private String functionCat;

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_FUNCTION_SCHEM)
    private String functionSchem;

    @ColumnLabel(COLUMN_LABEL_FUNCTION_NAME)
    private String functionName;

    @ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @ColumnLabel("COLUMN_TYPE")
    private int columnType;

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @NullableBySpecification // > Null is returned for data types where the column size is not applicable.
    @ColumnLabel("PRECISION")
    private Integer precision;

    @ColumnLabel("LENGTH")
    private int length;

    // https://issues.apache.org/jira/browse/DERBY-7102
    @NullableBySpecification
    @ColumnLabel("SCALE")
    private Integer scale;

    @ColumnLabel("RADIX")
    private int radix;

    @ColumnLabel("NULLABLE")
    private int nullable;

    @NullableByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7100
    @ColumnLabel("REMARKS")
    private String remarks;

    @NullableBySpecification
    @ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @ColumnLabel("ORDINAL_POSITION")
    private int ordinalPosition;

    @ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @ColumnLabel("SPECIFIC_NAME")
    private String specificName;
}
