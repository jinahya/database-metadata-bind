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


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {"grantor", "grantee", "privilege", "isGrantable"})
public class TablePrivilege {


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


    // ----------------------------------------------------------------- grantor
    /**
     * Returns {@link #grantor}.
     *
     * @return {@link #grantor}.
     */
    public String getGrantor() {

        return grantor;
    }


    /**
     * Replaces {@link #grantor}.
     *
     * @param grantor {@link #grantor}.
     */
    public void setGrantor(final String grantor) {

        this.grantor = grantor;
    }


    // ----------------------------------------------------------------- grantee
    /**
     * Returns {@link #grantee}.
     *
     * @return {@link #grantee}.
     */
    public String getGrantee() {

        return grantee;
    }


    /**
     * Replaces {@link #grantee}.
     *
     * @param grantee {@link #grantee}.
     */
    public void setGrantee(final String grantee) {

        this.grantee = grantee;
    }


    // --------------------------------------------------------------- privilege
    public String getPrivilege() {

        return privilege;
    }


    public void setPrivilege(final String privilege) {

        this.privilege = privilege;
    }


    // ------------------------------------------------------------- isGrantable
    public String getIsGrantable() {

        return isGrantable;
    }


    public void setIsGrantable(final String isGrantable) {

        this.isGrantable = isGrantable;
    }


    // ------------------------------------------------------------------- table
    /**
     * Returns the parent table of this table privilege.
     *
     * @return the parent table.
     */
    public Table getTable() {

        return table;
    }


    public void setTable(final Table table) {

        this.table = table;
    }


    @Label("TABLE_CAT")
    @NillableBySpecification
    @XmlAttribute
    private String tableCat;


    @Label("TABLE_SCHEM")
    @NillableBySpecification
    @XmlAttribute
    private String tableSchem;


    @Label("TABLE_NAME")
    @XmlAttribute
    private String tableName;


    @Label("GRANTOR")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String grantor;


    @Label("GRANTEE")
    @XmlElement(required = true)
    private String grantee;


    @Label("PRIVILEGE")
    @XmlElement(required = true)
    private String privilege;


    @Label("IS_GRANTABLE")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String isGrantable;


    @XmlTransient
    private Table table;


}

