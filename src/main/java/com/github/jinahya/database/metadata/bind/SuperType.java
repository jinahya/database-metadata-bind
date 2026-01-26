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

    private static final long serialVersionUID = 4603878785941565029L;

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

    // --------------------------------------------------------------------------------------------------------- typeCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     */
    public String getTypeCat() {
        return typeCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     *
     * @param typeCat the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     */
    protected void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     */
    public String getTypeSchem() {
        return typeSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     *
     * @param typeSchem the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     */
    protected void setTypeSchem(final String typeSchem) {
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
    protected void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ----------------------------------------------------------------------------------------------------- supertypeCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_SUPERTYPE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SUPERTYPE_CAT} column.
     */
    public String getSupertypeCat() {
        return supertypeCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SUPERTYPE_CAT} column.
     *
     * @param supertypeCat the value of {@value #COLUMN_LABEL_SUPERTYPE_CAT} column.
     */
    protected void setSupertypeCat(final String supertypeCat) {
        this.supertypeCat = supertypeCat;
    }

    // --------------------------------------------------------------------------------------------------- supertypeSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_SUPERTYPE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SUPERTYPE_SCHEM} column.
     */
    public String getSupertypeSchem() {
        return supertypeSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SUPERTYPE_SCHEM} column.
     *
     * @param supertypeSchem the value of {@value #COLUMN_LABEL_SUPERTYPE_SCHEM} column.
     */
    protected void setSupertypeSchem(final String supertypeSchem) {
        this.supertypeSchem = supertypeSchem;
    }

    // ---------------------------------------------------------------------------------------------------- supertypeName

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
    protected void setSupertypeName(final String supertypeName) {
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
