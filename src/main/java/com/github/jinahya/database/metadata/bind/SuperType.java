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

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getSuperTypes(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTypes(String, String, String, Collection)
 */
@XmlRootElement
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class SuperType
        implements MetadataType,
                   ChildOf<Schema> {

    private static final long serialVersionUID = 4603878785941565029L;

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    @Override
    public Schema extractParent() {
        return Schema.builder()
                .tableCatalog(getTypeCat())
                .tableSchem(getTypeSchem())
                .build();
    }

    public UDT extractType() {
        return UDT.builder()
                .typeCat(getTypeCat())
                .typeSchem(getTypeSchem())
                .typeName(getTypeName())
                .build();
    }

    public UDT extractSuperType() {
        return UDT.builder()
                .typeCat(getSupertypeCat())
                .typeSchem(getSupertypeSchem())
                .typeName(getSupertypeName())
                .build();
    }

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TYPE_CAT")
    private String typeCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TYPE_SCHEM")
    private String typeSchem;

    @XmlElement(nillable = false, required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SUPERTYPE_CAT")
    private String supertypeCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SUPERTYPE_SCHEM")
    private String supertypeSchem;

    @XmlElement(nillable = false, required = true)
    @Label("SUPERTYPE_NAME")
    private String supertypeName;
}
