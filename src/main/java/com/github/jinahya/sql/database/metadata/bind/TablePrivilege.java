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
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlRootElement
@XmlType(propOrder = {"grantor", "grantee", "privilege", "isGrantable"})
public class TablePrivilege {


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


    @ColumnLabel("TABLE_CAT")
    @XmlAttribute
    private String tableCat;


    @ColumnLabel("TABLE_SCHEM")
    @XmlAttribute
    private String tableSchem;


    @ColumnLabel("TABLE_NAME")
    @XmlAttribute
    private String tableName;


    @ColumnLabel("GRANTOR")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String grantor;


    @ColumnLabel("GRANTEE")
    @XmlElement(nillable = false, required = true)
    private String grantee;


    @ColumnLabel("PRIVILEGE")
    @XmlElement(nillable = false, required = true)
    private String privilege;


    @ColumnLabel("IS_GRANTABLE")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String isGrantable;


    @XmlTransient
    private Table table;


}

