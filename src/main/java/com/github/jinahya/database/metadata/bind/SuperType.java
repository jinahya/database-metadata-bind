package com.github.jinahya.database.metadata.bind;

import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.util.Objects;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getSuperTypes(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTypes(String, String, String)
 */
@_ChildOf(UDT.class)
public class SuperType
        extends AbstractMetadataType {

    private static final long serialVersionUID = 4603878785941565029L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

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

    // ---------------------------------------------------------------------------------------------------- SUPERTYPE_CAT

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SUPERTYPE_CAT = "SUPERTYPE_CAT";

    // -------------------------------------------------------------------------------------------------- SUPERTYPE_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SUPERTYPE_SCHEM = "SUPERTYPE_SCHEM";

    // --------------------------------------------------------------------------------------------------- SUPERTYPE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SUPERTYPE_NAME = "SUPERTYPE_NAME";

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    SuperType() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object
    @Override
    public String toString() {
        return super.toString() + '{' +
               "typeCat=" + typeCat +
               ",typeSchem=" + typeSchem +
               ",typeName=" + typeName +
               ",supertypeCat=" + supertypeCat +
               ",supertypeSchem=" + supertypeSchem +
               ",supertypeName=" + supertypeName +
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
        final var that = (SuperType) obj;
        return Objects.equals(typeCat, that.typeCat) &&
               Objects.equals(typeSchem, that.typeSchem) &&
               Objects.equals(typeName, that.typeName) &&
               Objects.equals(supertypeName, that.supertypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeCat, typeSchem, typeName, supertypeName);
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

    // ---------------------------------------------------------------------------------------------------- supertypeCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_SUPERTYPE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SUPERTYPE_CAT} column.
     */
    @Nullable
    public String getSupertypeCat() {
        return supertypeCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SUPERTYPE_CAT} column.
     *
     * @param supertypeCat the value of {@value #COLUMN_LABEL_SUPERTYPE_CAT} column.
     */
    void setSupertypeCat(final String supertypeCat) {
        this.supertypeCat = supertypeCat;
    }

    // -------------------------------------------------------------------------------------------------- supertypeSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_SUPERTYPE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SUPERTYPE_SCHEM} column.
     */
    @Nullable
    public String getSupertypeSchem() {
        return supertypeSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SUPERTYPE_SCHEM} column.
     *
     * @param supertypeSchem the value of {@value #COLUMN_LABEL_SUPERTYPE_SCHEM} column.
     */
    void setSupertypeSchem(final String supertypeSchem) {
        this.supertypeSchem = supertypeSchem;
    }

    // --------------------------------------------------------------------------------------------------- supertypeName

    /**
     * Returns the value of {@value #COLUMN_LABEL_SUPERTYPE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SUPERTYPE_NAME} column.
     */
    public String getSupertypeName() {
        return supertypeName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SUPERTYPE_NAME} column.
     *
     * @param supertypeName the value of {@value #COLUMN_LABEL_SUPERTYPE_NAME} column.
     */
    void setSupertypeName(final String supertypeName) {
        this.supertypeName = supertypeName;
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

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SUPERTYPE_CAT)
    private String supertypeCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SUPERTYPE_SCHEM)
    private String supertypeSchem;

    @_ColumnLabel(COLUMN_LABEL_SUPERTYPE_NAME)
    private String supertypeName;
}
