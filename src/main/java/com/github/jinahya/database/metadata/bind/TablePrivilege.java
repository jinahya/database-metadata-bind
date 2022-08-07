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

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@ChildOf(Table.class)
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class TablePrivilege
        implements MetadataType {

    private static final long serialVersionUID = -1799954363648972203L;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label("TABLE_NAME")
    private String tableName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("GRANTOR")
    private String grantor;

    @XmlElement(nillable = true, required = true)
    @Label("GRANTEE")
    private String grantee;

    @NotBlank
    @XmlElement(nillable = false, required = true)
    @Label("PRIVILEGE")
    private String privilege;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("IS_GRANTABLE")
    private String isGrantable;
}
