package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A class for binding results of the {@link DatabaseMetaData#getUDTs(String, String, String, int[])} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getUDTs(String, String, String, int[])
 */
@_ChildOf(Schema.class)
@_ChildOf(Catalog.class)
@_ParentOf(SuperType.class)
@_ParentOf(Attribute.class)
public class UDT
        extends AbstractMetadataType {

    @Serial
    private static final long serialVersionUID = 8665246093405057553L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator comparing values in the specified order.
     * <blockquote>
     * They are ordered by <code>DATA_TYPE</code>, <code>TYPE_CAT</code>, <code>TYPE_SCHEM</code> and
     * <code>TYPE_NAME</code>.
     * </blockquote>
     *
     * @param operator   a unary operator for adjusting string values; applied only to non-{@code null} values.
     * @param comparator a null-safe string comparator for comparing values.
     * @return a comparator comparing values in the specified order.
     * @see DatabaseMetaData#getUDTs(String, String, String, int[])
     */
    static Comparator<UDT> comparingInSpecifiedOrder(final UnaryOperator<String> operator,
                                                     final Comparator<? super String> comparator) {
        Objects.requireNonNull(operator, "operator is null");
        Objects.requireNonNull(comparator, "comparator is null");
        final UnaryOperator<String> op = v -> v == null ? null : operator.apply(v);
        return Comparator
                .comparing(UDT::getDataType, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(v -> op.apply(v.getTypeCat()), comparator)
                .thenComparing(v -> op.apply(v.getTypeSchem()), comparator)
                .thenComparing(v -> op.apply(v.getTypeName()), comparator);
    }

    /**
     * Returns a comparator comparing values, using specified context for ordering {@code null} values, in the specified
     * order.
     * <blockquote>
     * They are ordered by <code>DATA_TYPE</code>, <code>TYPE_CAT</code>, <code>TYPE_SCHEM</code> and
     * <code>TYPE_NAME</code>.
     * </blockquote>
     *
     * @param context    a context for ordering {@code null} values.
     * @param comparator a comparator for comparing string values.
     * @return a comparator comparing values in the specified order.
     * @throws SQLException if a database access error occurs.
     * @see ContextUtils#nullOrdered(Context, Comparator)
     */
    static Comparator<UDT> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator
                .comparing(UDT::getDataType, ContextUtils.nullOrdered(context, Comparator.naturalOrder()))
                .thenComparing(UDT::getTypeCat, ContextUtils.nullOrdered(context, comparator))
                .thenComparing(UDT::getTypeSchem, ContextUtils.nullOrdered(context, comparator))
                .thenComparing(UDT::getTypeName, ContextUtils.nullOrdered(context, comparator));
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

    /**
     * A column value of {@link Types#JAVA_OBJECT}({@value Types#JAVA_OBJECT}) for the {@value #COLUMN_LABEL_DATA_TYPE}
     * column.
     */
    public static final int COLUMN_VALUES_DATA_TYPE_JAVA_OBJECT = Types.JAVA_OBJECT; // 2000

    /**
     * A column value of {@link Types#DISTINCT}({@value Types#DISTINCT}) for the {@value #COLUMN_LABEL_DATA_TYPE}
     * column.
     */
    public static final int COLUMN_VALUES_DATA_TYPE_DISTINCT = Types.DISTINCT;       // 2001

    /**
     * A column value of {@link Types#STRUCT}({@value Types#STRUCT}) for the {@value #COLUMN_LABEL_DATA_TYPE} column.
     */
    public static final int COLUMN_VALUES_DATA_TYPE_STRUCT = Types.STRUCT;           // 2002

    static final List<Integer> COLUMN_VALUES_DATA_TYPE = List.of(
            COLUMN_VALUES_DATA_TYPE_JAVA_OBJECT,
            COLUMN_VALUES_DATA_TYPE_DISTINCT,
            COLUMN_VALUES_DATA_TYPE_STRUCT
    );

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

    /**
     * Creates a new instance.
     */
    UDT() {
        super();
    }

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

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     */
    @Nullable
    public String getTypeCat() {
        return typeCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     *
     * @param typeCat the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     */
    void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     */
    @Nullable
    public String getTypeSchem() {
        return typeSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     *
     * @param typeSchem the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     */
    void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    // -------------------------------------------------------------------------------------------------------- typeName

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     *
     * @param typeName the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     */
    void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------- className

    /**
     * Returns the value of {@value #COLUMN_LABEL_CLASS_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_CLASS_NAME} column.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_CLASS_NAME} column.
     *
     * @param className the value of {@value #COLUMN_LABEL_CLASS_NAME} column.
     */
    void setClassName(final String className) {
        this.className = className;
    }

    // -------------------------------------------------------------------------------------------------------- dataType

    /**
     * Returns the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     *
     * @param dataType the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     */
    void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    // --------------------------------------------------------------------------------------------------------- remarks

    /**
     * Returns the value of {@value #COLUMN_LABEL_REMARKS} column.
     *
     * @return the value of {@value #COLUMN_LABEL_REMARKS} column.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_REMARKS} column.
     *
     * @param remarks the value of {@value #COLUMN_LABEL_REMARKS} column.
     */
    void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // -------------------------------------------------------------------------------------------------------- baseType

    /**
     * Returns the value of {@value #COLUMN_LABEL_BASE_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_BASE_TYPE} column.
     */
    @Nullable
    public Integer getBaseType() {
        return baseType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_BASE_TYPE} column.
     *
     * @param baseType the value of {@value #COLUMN_LABEL_BASE_TYPE} column.
     */
    void setBaseType(final Integer baseType) {
        this.baseType = baseType;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_CAT)
    private String typeCat;

    @Nullable
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

    // null if DATA_TYPE is not DISTINCT or not STRUCT with REFERENCE_GENERATION = USER_DEFINED
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_BASE_TYPE)
    private Integer baseType;
}
