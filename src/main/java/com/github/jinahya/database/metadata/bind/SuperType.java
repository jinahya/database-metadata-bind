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
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getSuperTypes(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTypes(String, String, String)
 */
@XmlRootElement
@_ChildOf(UDT.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SuperType extends AbstractMetadataType {

    private static final long serialVersionUID = 4603878785941565029L;

    // -----------------------------------------------------------------------------------------------------------------
    static final BiPredicate<SuperType, UDT> IS_OF = (c, p) -> {
        return Objects.equals(c.supertypeCat, p.getTypeCat()) &&
               Objects.equals(c.supertypeSchem, p.getTypeSchem()) &&
               Objects.equals(c.supertypeName, p.getTypeName());
    };

    // --------------------------------------------------------------------------------------------------------- typeCat

    // ------------------------------------------------------------------------------------------------------- typeSchem

    // ---------------------------------------------------------------------------------------------------- superTypeCat

    // -------------------------------------------------------------------------------------------------- superTypeSchem

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
}
