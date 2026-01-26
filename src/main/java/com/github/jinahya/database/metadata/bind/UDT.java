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
@EqualsAndHashCode(callSuper = true)
public class UDT
        extends AbstractMetadataType {

    private static final long serialVersionUID = 8665246093405057553L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<UDT> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator
                .comparing(UDT::getDataType, ContextUtils.nullPrecedence(context, Comparator.naturalOrder()))
                .thenComparing(UDT::getTypeCat, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(UDT::getTypeSchem, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(UDT::getTypeName, ContextUtils.nullPrecedence(context, comparator));
    }

    // -------------------------------------------------------------------------------------------------------- TYPE_CAT

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_CAT = "TYPE_CAT";

    // ------------------------------------------------------------------------------------------------------ TYPE_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_SCHEM = "TYPE_SCHEM";

    // ------------------------------------------------------------------------------------------------------- TYPE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // ------------------------------------------------------------------------------------------------------ CLASS_NAME

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

    // --------------------------------------------------------------------------------------------------------- REMARKS

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_REMARKS = "REMARKS";

    // ------------------------------------------------------------------------------------------------------- BASE_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_BASE_TYPE = "BASE_TYPE";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "typeCat=" + typeCat +
               ",typeSchem=" + typeSchem +
               ",typeName=" + typeName +
               ",className=" + className +
               ",dataType=" + dataType +
               ",remarks=" + remarks +
               ",baseType=" + baseType +
               '}';
    }

    // --------------------------------------------------------------------------------------------------------- typeCat
    public String getTypeCat() {
        return typeCat;
    }

    public String getTypeSchem() {
        return typeSchem;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public String getClassName() {
        return className;
    }

    // -------------------------------------------------------------------------------------------------------- dataType
    public Integer getDataType() {
        return dataType;
    }

    public String getRemarks() {
        return remarks;
    }

    // --------------------------------------------------------------------------------------------------------- bseType
    public Integer getBaseType() {
        return baseType;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_CAT)

    private String typeCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_SCHEM)

    private String typeSchem;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)

    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_CLASS_NAME)
    private String className;

    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_BASE_TYPE)
    private Integer baseType;
}
