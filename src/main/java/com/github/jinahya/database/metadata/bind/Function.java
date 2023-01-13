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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for binding the result of
 * {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see FunctionColumn
 */
@ParentOf(FunctionColumn.class)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Function
        implements MetadataType,
                   ChildOf<Schema> {

    private static final long serialVersionUID = -3318947900237453301L;

    public static final String COLUMN_NAME_FUNCTION_CAT = "FUNCTION_CAT";

    public static final String ATTRIBUTE_NAME_FUNCTION_CAT = "functionCat";

    public static final String COLUMN_NAME_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    public static final String ATTRIBUTE_NAME_FUNCTION_SCHEM = "functionSchem";

    public static final String COLUMN_NAME_FUNCTION_TYPE = "FUNCTION_TYPE";

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        {
            context.getFunctionColumns(
                    getFunctionCat(),
                    getFunctionSchem(),
                    getFunctionName(),
                    "%",
                    getFunctionColumns()::add
            );
            for (final FunctionColumn functionColumn : getFunctionColumns()) {
                functionColumn.retrieveChildren(context);
            }
        }
    }

    @Override
    public Schema extractParent() {
        return Schema.builder()
                .tableSchem(getFunctionCat())
                .tableSchem(getFunctionSchem())
                .build();
    }

    /**
     * Returns function columns of this function.
     *
     * @return a list of function columns of this function.
     */
    public List<FunctionColumn> getFunctionColumns() {
        if (functionColumns == null) {
            functionColumns = new ArrayList<>();
        }
        return functionColumns;
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

    @ColumnLabel("REMARKS")
    private String remarks;

    @ColumnLabel("FUNCTION_TYPE")
    private short functionType;

    @ColumnLabel("SPECIFIC_NAME")
    private String specificName;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<FunctionColumn> functionColumns;
}
