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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;

/**
 * A class for binding results of {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getColumnPrivileges(String, String, String, String, Collection)
 */
@XmlRootElement
@ChildOf__(Table.class)
@Data
public class ColumnPrivilege implements MetadataType {

    private static final long serialVersionUID = 4384654744147773380L;

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
    }

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(nillable = false, required = true)
    @Label("TABLE_NAME")
    private String tableName;

    @XmlElement(nillable = false, required = true)
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("GRANTOR")
    private String grantor;

    @XmlElement(nillable = false, required = true)
    @Label("GRANTEE")
    private String grantee;

    @XmlElement(nillable = false, required = true)
    @Label("PRIVILEGE")
    private String privilege;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("IS_GRANTABLE")
    private String isGrantable;
}
