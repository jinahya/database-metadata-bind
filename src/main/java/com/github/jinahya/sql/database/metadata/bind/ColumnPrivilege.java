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
public class ColumnPrivilege {


    // ------------------------------------------------------------------ column
    public Table getTable() {

        return table;
    }


    // ----------------------------------------------------------------- GRANTOR
    public String getGrantor() {

        return grantor;
    }


    public void setGrantor(final String grantor) {

        this.grantor = grantor;
    }


    // ----------------------------------------------------------------- GRANTEE
    public String getGrantee() {

        return grantee;
    }


    public void setGrantee(String grantee) {

        this.grantee = grantee;
    }


    // --------------------------------------------------------------- PRIVILEGE
    public String getPrivilege() {

        return privilege;
    }


    public void setPrivilege(final String privilege) {

        this.privilege = privilege;
    }


    // ------------------------------------------------------------ IS_GRANTABLE
    public String getIsGrantable() {

        return isGrantable;
    }


    public void setIsGrantable(final String isGrantable) {

        this.isGrantable = isGrantable;
    }


    /**
     * table catalog (may be {@code null}).
     */
    @ColumnLabel("TABLE_CAT")
    @XmlAttribute
    private String tableCat;


    /**
     * table schema (may be {@code null}).
     */
    @ColumnLabel("TABLE_SCHEM")
    @XmlAttribute
    private String tableSchem;


    /**
     * table name.
     */
    @ColumnLabel("TABLE_NAME")
    @XmlAttribute
    private String tableName;


    /**
     * column name.
     */
    @ColumnLabel("COLUMN_NAME")
    @XmlAttribute(required = true)
    //@XmlElement(nillable = true, required = true)
    private String columnName;


    /**
     * grantor of access (may be {@code null}).
     */
    @ColumnLabel("GRANTOR")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    String grantor;


    /**
     * grantee of access.
     */
    @ColumnLabel("GRANTEE")
    @XmlElement(required = true)
    String grantee;


    /**
     * name of access (SELECT, INSERT, UPDATE, REFRENCES, ...)
     */
    @ColumnLabel("PRIVILEGE")
    @XmlElement(required = true)
    String privilege;


    /**
     * {@code YES} if grantee is permitted to grant to others; {@code NO} if
     * not; {@code null} if unknown.
     */
    @ColumnLabel("IS_GRANTABLE")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    String isGrantable;


    /**
     * parent table
     */
    @XmlTransient
    private Table table;


}

