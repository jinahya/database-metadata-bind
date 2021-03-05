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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Objects;

/**
 * An entity class for primary keys.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getPrimaryKeys(String, String, String, Collection)
 */
@XmlRootElement
public class PrimaryKey extends TableChild {

    private static final long serialVersionUID = 3159826510060898330L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public PrimaryKey() {
        super();
    }

    @Override
    public String toString() {
        return super.toString() + '{'
               + "tableCat=" + tableCat
               + ",tableSchem=" + tableSchem
               + ",tableName=" + tableName
               + ",columnName=" + columnName
               + ",keySeq=" + keySeq
               + ",pkName=" + pkName
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final PrimaryKey that = (PrimaryKey) obj;
        return keySeq == that.keySeq
               && Objects.equals(tableCat, that.tableCat)
               && Objects.equals(tableSchem, that.tableSchem)
               && Objects.equals(tableName, that.tableName)
               && Objects.equals(columnName, that.columnName)
               && Objects.equals(pkName, that.pkName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCat,
                            tableSchem,
                            tableName,
                            columnName,
                            keySeq,
                            pkName);
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // ---------------------------------------------------------------------------------------------------------- keySeq
    public short getKeySeq() {
        return keySeq;
    }

    public void setKeySeq(final short keySeq) {
        this.keySeq = keySeq;
    }

    // ---------------------------------------------------------------------------------------------------------- pkName
    public String getPkName() {
        return pkName;
    }

    public void setPkName(final String pkName) {
        this.pkName = pkName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true, required = true)
    @MayBeNull
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(nillable = true, required = true)
    @MayBeNull
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(required = true)
    @Label("TABLE_NAME")
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(required = true)
    @Label("KEY_SEQ")
    private short keySeq;

    @XmlElement(nillable = true, required = true)
    @MayBeNull
    @Label("PK_NAME")
    private String pkName;
}
