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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final var that = (UDT) obj;
        return Objects.equals(typeCat, that.typeCat) &&
               Objects.equals(typeSchem, that.typeSchem) &&
               Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeCat, typeSchem, typeName);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getTypeCat() {
        return typeCat;
    }

    public String getTypeSchem() {
        return typeSchem;
    }

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

    // -----------------------------------------------------------------------------------------------------------------
    List<Attribute> getAttributes(final Context context, final String attributeNamePattern) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getAttributes(this, attributeNamePattern);
    }

    List<SuperType> getSuperTypes(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getSuperTypes(this);
    }

    List<UDT> getSuperUDTs(final Context context, final int[] types) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        final var collection = new ArrayList<UDT>();
        for (final var superType : getSuperTypes(context)) {
            context.addUDTs(
                    superType.getTypeCat(),
                    superType.getTypeSchem(),
                    superType.getTypeName(),
                    types,
                    collection
            );
        }
        return collection;
    }
}
