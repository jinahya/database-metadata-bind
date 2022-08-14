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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class for binding the result of
 * {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctions(String, String, String, Collection)
 * @see FunctionColumn
 */
@XmlRootElement
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

    /**
     * Constants for {@value #COLUMN_NAME_FUNCTION_TYPE} column values.
     */
    @XmlEnum
    public enum FunctionType implements IntFieldEnum<FunctionType> {

        /**
         * Constant for
         * {@link DatabaseMetaData#functionResultUnknown}({@value java.sql.DatabaseMetaData#functionResultUnknown}).
         */
        FUNCTION_RESULTS_UNKNOWN(DatabaseMetaData.functionResultUnknown), // 0

        /**
         * Constant for {@link DatabaseMetaData#functionNoTable}({@value java.sql.DatabaseMetaData#functionNoTable}).
         */
        FUNCTION_NO_TABLE(DatabaseMetaData.functionNoTable), // 1

        /**
         * Constant for
         * {@link DatabaseMetaData#functionReturnsTable}({@value java.sql.DatabaseMetaData#functionReturnsTable}).
         */
        FUNCTION_RETURNS_UNKNOWN(DatabaseMetaData.functionReturnsTable); // 2

        /**
         * Returns the constant whose raw value matches to specified value.
         *
         * @param rawValue the raw value.
         * @return the constant whose raw value matches to {@code rawValue}.
         * @throws IllegalArgumentException when no constant found for the {@code rawValue}.
         */
        public static FunctionType valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(FunctionType.class, rawValue);
        }

        FunctionType(final int rawValue) {
            this.rawValue = rawValue;
        }

        @Override
        public int rawValueAsInt() {
            return rawValue;
        }

        private final int rawValue;
    }

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        {
            context.getFunctionColumns(
                    getFunctionCat(),
                    getFunctionSchem(),
                    getFunctionName(),
                    "%",
                    getFunctionColumns()
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

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label(COLUMN_NAME_FUNCTION_CAT)
    private String functionCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label(COLUMN_NAME_FUNCTION_SCHEM)
    private String functionSchem;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label("FUNCTION_NAME")
    @EqualsAndHashCode.Exclude
    private String functionName;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true)
    @Label("FUNCTION_TYPE")
    private short functionType;

    @XmlElement(required = true)
    @Label("SPECIFIC_NAME")
    private String specificName;

    @XmlTransient
    @Valid
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Schema schema;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull FunctionColumn> functionColumns;
}
