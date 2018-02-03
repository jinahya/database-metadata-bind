/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
 *
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
 */
package com.github.jinahya.database.metadata.bind;

import java.io.Serializable;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * An entity class for primary keys.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see MetadataContext#getPrimaryKeys(java.lang.String, java.lang.String,
 * java.lang.String)
 */
@XmlRootElement
@XmlType(propOrder = {
    "columnName", "keySeq", "pkName"
})
public class PrimaryKey implements Serializable {

    private static final long serialVersionUID = 3159826510060898330L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(PrimaryKey.class.getName());

    // ---------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // -------------------------------------------------------------- tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // --------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // -------------------------------------------------------------- columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // ------------------------------------------------------------------ keySeq
    public short getKeySeq() {
        return keySeq;
    }

    public void setKeySeq(final short keySeq) {
        this.keySeq = keySeq;
    }

    // ------------------------------------------------------------------ pkName
    public String getPkName() {
        return pkName;
    }

    public void setPkName(final String pkName) {
        this.pkName = pkName;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Bind(label = "TABLE_CAT", nillable = true)
    private String tableCat;

    @XmlAttribute
    @Bind(label = "TABLE_SCHEM", nillable = true)
    private String tableSchem;

    @XmlAttribute
    @Bind(label = "TABLE_NAME")
    private String tableName;

    // -------------------------------------------------------------------------
    @XmlElement
    @Bind(label = "COLUMN_NAME")
    private String columnName;

    @XmlElement
    @Bind(label = "KEY_SEQ")
    private short keySeq;

    @XmlElement(nillable = true)
    @Bind(label = "PK_NAME", nillable = true)
    private String pkName;
}
