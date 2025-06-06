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
import java.util.Objects;

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

    @Override
    public boolean equals(final Object obj) {
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
               Objects.equals(supertypeCat, that.supertypeCat) &&
               Objects.equals(supertypeSchem, that.supertypeSchem) &&
               Objects.equals(supertypeName, that.supertypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeCat, typeSchem, typeName, supertypeCat, supertypeSchem,
                            supertypeName);
    }

    // --------------------------------------------------------------------------------------------------------- typeCat
    public String getTypeCat() {
        return typeCat;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem
    public String getTypeSchem() {
        return typeSchem;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getTypeName() {
        return typeName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getSupertypeCat() {
        return supertypeCat;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getSupertypeSchem() {
        return supertypeSchem;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getSupertypeName() {
        return supertypeName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @_ColumnLabel("TYPE_CAT")
    private String typeCat;

    @_NullableBySpecification
    @_ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @_ColumnLabel("SUPERTYPE_CAT")
    private String supertypeCat;

    @_NullableBySpecification
    @_ColumnLabel("SUPERTYPE_SCHEM")
    private String supertypeSchem;

    @_ColumnLabel("SUPERTYPE_NAME")
    private String supertypeName;
}
