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

import java.sql.DatabaseMetaData;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getSuperTypes(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTypes(String, String, String)
 */

@_ChildOf(UDT.class)
@EqualsAndHashCode(callSuper = true)
public class SuperType
        extends AbstractMetadataType {

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
    public String getTypeCat() {
        return typeCat;
    }

    protected void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem
    public String getTypeSchem() {
        return typeSchem;
    }

    protected void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getTypeName() {
        return typeName;
    }

    protected void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getSupertypeCat() {
        return supertypeCat;
    }

    protected void setSupertypeCat(final String supertypeCat) {
        this.supertypeCat = supertypeCat;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getSupertypeSchem() {
        return supertypeSchem;
    }

    protected void setSupertypeSchem(final String supertypeSchem) {
        this.supertypeSchem = supertypeSchem;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getSupertypeName() {
        return supertypeName;
    }

    protected void setSupertypeName(final String supertypeName) {
        this.supertypeName = supertypeName;
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

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SUPERTYPE_CAT)
    private String supertypeCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SUPERTYPE_SCHEM)
    private String supertypeSchem;

    @_ColumnLabel(COLUMN_LABEL_SUPERTYPE_NAME)
    private String supertypeName;
}
