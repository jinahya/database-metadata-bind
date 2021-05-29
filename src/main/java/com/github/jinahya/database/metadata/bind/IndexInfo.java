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

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Collection;

/**
 * A class for binding results of {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getIndexInfo(String, String, String, boolean, boolean, Collection)
 */
@XmlRootElement
@ChildOf(Table.class)
@Data
@NoArgsConstructor
public class IndexInfo implements MetadataType {

    private static final long serialVersionUID = -768486884376018474L;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(required = true)
    @Label("TABLE_NAME")
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("NON_UNIQUE")
    private boolean nonUnique;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("INDEX_QUALIFIER")
    private String indexQualifier;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("INDEX_NAME")
    private String indexName;

    @XmlElement(required = true)
    @Label("TYPE")
    private short type;

    @XmlElement(required = true)
    @Label("ORDINAL_POSITION")
    private short ordinalPosition;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("ASC_OR_DESC")
    private String ascOrDesc;

    @XmlElement(required = true)
    @Label("CARDINALITY")
    private long cardinality;

    @XmlElement(required = true)
    @Label("PAGES")
    private long pages;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("FILTER_CONDITION")
    private String filterCondition;
}
