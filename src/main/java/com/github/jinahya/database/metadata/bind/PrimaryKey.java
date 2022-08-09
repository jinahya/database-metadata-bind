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

import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;

/**
 * A class for binding results of {@link DatabaseMetaData#getPrimaryKeys(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getPrimaryKeys(String, String, String, Collection)
 */
@XmlRootElement
@ChildOf(Table.class)
@Setter
@Getter
@EqualsAndHashCode
@ToString(callSuper = true)
@NoArgsConstructor
public class PrimaryKey
        implements MetadataType {

    private static final long serialVersionUID = 3159826510060898330L;

    public static final Comparator<PrimaryKey> COMPARATOR = Comparator.comparing(PrimaryKey::getColumnName);

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

    @XmlElement(required = true)
    @Label("TABLE_NAME")
    private String tableName;

    @XmlElement(required = true)
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(required = true)
    @Label("KEY_SEQ")
    private short keySeq;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PK_NAME")
    private String pkName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlTransient
    @Valid
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Table table;
}
