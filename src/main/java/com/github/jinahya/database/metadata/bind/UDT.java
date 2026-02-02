package com.github.jinahya.database.metadata.bind;

import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Comparator;
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
public class UDT
        extends AbstractMetadataType {

    private static final long serialVersionUID = 8665246093405057553L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS
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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
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

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

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

    // ----------------------------------------------------------------------------------------------------- COMPARATORS
    @_ColumnLabel(COLUMN_LABEL_CLASS_NAME)
    private String className;

    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_BASE_TYPE)
    private Integer baseType;
}
