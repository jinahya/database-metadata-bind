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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Comparator;

/**
 * A class for binding results of the {@link DatabaseMetaData#getUDTs(String, String, String, int[])} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getUDTs(String, String, String, int[])
 */
@_ChildOf(Catalog.class)
@_ChildOf(Schema.class)
@_ParentOf(Attribute.class)
@_ParentOf(UDT.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class UDT
        extends AbstractMetadataType {

    private static final long serialVersionUID = 8665246093405057553L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<UDT> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator
                .comparing(UDT::getDataType, ContextUtils.nulls(context, Comparator.naturalOrder()))
                .thenComparing(UDT::getTypeCat, ContextUtils.nulls(context, comparator))
                .thenComparing(UDT::getTypeSchem, ContextUtils.nulls(context, comparator))
                .thenComparing(UDT::getTypeName, ContextUtils.nulls(context, comparator));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_CAT = "TYPE_CAT";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_SCHEM = "TYPE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CLASS_NAME = "CLASS_NAME";

    // ------------------------------------------------------------------------------------------------------- DATA_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    public static final int COLUMN_VALUES_DATA_TYPE_JAVA_OBJECT = Types.JAVA_OBJECT; // 2000

    public static final int COLUMN_VALUES_DATA_TYPE_DISTINCT = Types.DISTINCT; // 2001

    public static final int COLUMN_VALUES_DATA_TYPE_STRUCT = Types.STRUCT; // 2002

    // -------------------------------------------------------------------------------------------------------- dataType
    private boolean isDataTypeValid() {
        if (dataType == null) {
            return true;
        }
        return dataType == COLUMN_VALUES_DATA_TYPE_JAVA_OBJECT ||
               dataType == COLUMN_VALUES_DATA_TYPE_DISTINCT ||
               dataType == COLUMN_VALUES_DATA_TYPE_STRUCT;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_CAT)
    @EqualsAndHashCode.Include
    private String typeCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_SCHEM)
    @EqualsAndHashCode.Include
    private String typeSchem;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    @EqualsAndHashCode.Include
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_CLASS_NAME)
    private String className;

    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel("REMARKS")
    private String remarks;

    @_NullableBySpecification
    @_ColumnLabel("BASE_TYPE")
    private Integer baseType;
}
