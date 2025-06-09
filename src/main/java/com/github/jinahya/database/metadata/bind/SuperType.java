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

import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Optional;

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
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("TYPE_CAT")
    private String typeCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("SUPERTYPE_CAT")
    private String supertypeCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("SUPERTYPE_SCHEM")
    private String supertypeSchem;

    @_ColumnLabel("SUPERTYPE_NAME")
    private String supertypeName;

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Catalog typeCatalog_;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Schema typeSchema_;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Catalog supertypeCatalog_;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Schema supertypeSchema_;

    Catalog getTypeCatalog_() {
        if (typeCatalog_ == null) {
            typeCatalog_ = Catalog.of(typeCat);
        }
        return typeCatalog_;
    }

    void setTypeCatalog_(final Catalog typeCatalog_) {
        this.typeCatalog_ = typeCatalog_;
        setTypeCat(
                Optional.ofNullable(this.typeCatalog_)
                        .map(Catalog::getTableCat)
                        .orElse(null)
        );
    }

    Schema getTypeSchema_() {
        if (typeSchema_ == null) {
            typeSchema_ = Schema.of(getTypeCatalog_(), typeSchem);
        }
        return typeSchema_;
    }

    void setTypeSchema_(final Schema typeSchema_) {
        this.typeSchema_ = typeSchema_;
        setTypeSchem(
                Optional.ofNullable(this.typeSchema_)
                        .map(Schema::getTableSchem)
                        .orElse(null)
        );
    }

    Catalog getSupertypeCatalog_() {
        if (supertypeCatalog_ == null) {
            supertypeCatalog_ = Catalog.of(supertypeCat);
        }
        return supertypeCatalog_;
    }

    void setSupertypeCatalog_(final Catalog supertypeCatalog_) {
        this.supertypeCatalog_ = supertypeCatalog_;
        setSupertypeCat(
                Optional.ofNullable(this.supertypeCatalog_)
                        .map(Catalog::getTableCat)
                        .orElse(null)
        );
    }

    Schema getSupertypeSchema_() {
        if (supertypeSchema_ == null) {
            supertypeSchema_ = Schema.of(getSupertypeCatalog_(), supertypeSchem);
        }
        return supertypeSchema_;
    }

    void setSupertypeSchema_(final Schema supertypeSchema_) {
        this.supertypeSchema_ = supertypeSchema_;
        setSupertypeSchem(
                Optional.ofNullable(this.supertypeSchema_)
                        .map(Schema::getTableSchem)
                        .orElse(null)
        );
    }
}
