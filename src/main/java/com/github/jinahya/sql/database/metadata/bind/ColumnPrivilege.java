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
package com.github.jinahya.sql.database.metadata.bind;

import java.io.Serializable;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "grantor", "grantee", "privilege", "isGrantable"
})
public class ColumnPrivilege implements Serializable {

    private static final long serialVersionUID = 4384654744147773380L;

    // -------------------------------------------------------------------------
    private static final Logger logger
            = getLogger(ColumnPrivilege.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableCat=" + tableCat
               + ",tableSchem=" + tableSchem
               + ",tableName=" + tableName
               + ",columnName=" + columnName
               + ",grantor=" + grantor
               + ",grantee=" + grantee
               + ",privilege=" + privilege
               + ",isGrantable=" + isGrantable
               + "}";
    }

    // ---------------------------------------------------------------- tableCat
//    public String getTableCat() {
//        return tableCat;
//    }
//
//    public void setTableCat(final String tableCat) {
//        this.tableCat = tableCat;
//    }
    // -------------------------------------------------------------- tableSchem
//    public String getTableSchem() {
//        return tableSchem;
//    }
//
//    public void setTableSchem(final String tableSchem) {
//        this.tableSchem = tableSchem;
//    }
    // --------------------------------------------------------------- tableName
//    public String getTableName() {
//        return tableName;
//    }
//
//    public void setTableName(final String tableName) {
//        this.tableName = tableName;
//    }
    // -------------------------------------------------------------- columnName
//    public String getColumnName() {
//        return columnName;
//    }
//
//    public void setColumnName(final String columnName) {
//        this.columnName = columnName;
//    }
    // ----------------------------------------------------------------- grantor
//    public String getGrantor() {
//        return grantor;
//    }
//
//    public void setGrantor(final String grantor) {
//        this.grantor = grantor;
//    }
    // ----------------------------------------------------------------- grantee
//    public String getGrantee() {
//        return grantee;
//    }
//
//    public void setGrantee(final String grantee) {
//        this.grantee = grantee;
//    }
    // --------------------------------------------------------------- privilege
//    public String getPrivilege() {
//        return privilege;
//    }
//
//    public void setPrivilege(final String privilege) {
//        this.privilege = privilege;
//    }
    // ------------------------------------------------------------- isGrantable
//    public String getIsGrantable() {
//        return isGrantable;
//    }
//
//    public void setIsGrantable(final String isGrantable) {
//        this.isGrantable = isGrantable;
//    }
    // -------------------------------------------------------------------------
    @XmlAttribute
    @Labeled("TABLE_CAT")
    @Nillable
    @Getter
    @Setter
    private String tableCat;

    @XmlAttribute
    @Labeled("TABLE_SCHEM")
    @Nillable
    @Getter
    @Setter
    private String tableSchem;

    @XmlAttribute
    @Labeled("TABLE_NAME")
    @Getter
    @Setter
    private String tableName;

    @XmlAttribute
    @Labeled("COLUMN_NAME")
    @Getter
    @Setter
    private String columnName;

    // -------------------------------------------------------------------------
    @XmlElement(nillable = true, required = true)
    @Labeled("GRANTOR")
    @Nillable
    @Getter
    @Setter
    private String grantor;

    @XmlElement(required = true)
    @Labeled("GRANTEE")
    @Getter
    @Setter
    private String grantee;

    @XmlElement(required = true)
    @Labeled("PRIVILEGE")
    @Getter
    @Setter
    private String privilege;

    @XmlElement(nillable = true, required = true)
    @Labeled("IS_GRANTABLE")
    @Nillable
    @Getter
    @Setter
    private String isGrantable;
}
