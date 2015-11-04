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
@XmlType(propOrder = {"pktableCat", "pktableSchem", "pktableName",
                      "pkcolumnName", "fkcolumnName", "keySeq", "updateRule",
                      "deleteRule", "fkName", "pkName", "deferrability"})
public class ImportedKey {


    public String getPktableCat() {
        return pktableCat;
    }


    public void setPktableCat(String pktableCat) {
        this.pktableCat = pktableCat;
    }


    public String getPktableSchem() {
        return pktableSchem;
    }


    public void setPktableSchem(String pktableSchem) {
        this.pktableSchem = pktableSchem;
    }


    public String getPktableName() {
        return pktableName;
    }


    public void setPktableName(String pktableName) {
        this.pktableName = pktableName;
    }


    // ------------------------------------------------------------ pkColumnName
    public String getPkcolumnName() {

        return pkcolumnName;
    }


    public void setPkcolumnName(final String pkcolumnName) {

        this.pkcolumnName = pkcolumnName;
    }


    // ------------------------------------------------------------ fkcolumnName
    public Column getFkcolumnName() {

        return fkcolumnName;
    }


    public void setFkcolumnName(final Column fkcolumnName) {

        this.fkcolumnName = fkcolumnName;
    }


    // ------------------------------------------------------------------ keySeq
    public short getKeySeq() {

        return keySeq;
    }


    public void setKeySeq(final short keySeq) {

        this.keySeq = keySeq;
    }


    // -------------------------------------------------------------- updateRule
    public short getUpdateRule() {

        return updateRule;
    }


    public void setUpdateRule(short updateRule) {

        this.updateRule = updateRule;
    }


    // -------------------------------------------------------------- deleteRule
    public short getDeleteRule() {

        return deleteRule;
    }


    public void setDeleteRule(final short deleteRule) {

        this.deleteRule = deleteRule;
    }


    // ------------------------------------------------------------------ fnname
    public String getFkName() {

        return fkName;
    }


    public void setFkName(final String fkName) {

        this.fkName = fkName;
    }


    // ------------------------------------------------------------------ pkName
    public String getPkName() {

        return pkName;
    }


    public void setPkName(final String pkName) {

        this.pkName = pkName;
    }


    // --------------------------------------------------------- deferrerability
    /**
     * Returns current value of {@link #deferrability}.
     *
     * @return current value of {@link #deferrability}.
     */
    public short getDeferrability() {

        return deferrability;
    }


    /**
     * Replaces value of {@link #deferrability} with given.
     *
     * @param deferrability new value for {@link #deferrability}
     */
    public void setDeferrability(final short deferrability) {

        this.deferrability = deferrability;
    }


    // ------------------------------------------------------------------- table
    public Table getTable() {

        return table;
    }


    public void setTable(final Table table) {

        this.table = table;
    }


    @ColumnLabel("PKTABLE_CAT")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String pktableCat;


    /**
     * primary key table schema being imported (may be {@code null}).
     */
    @ColumnLabel("PKTABLE_SCHEM")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String pktableSchem;


    /**
     * primary key table name being imported.
     */
    @ColumnLabel("PKTABLE_NAME")
    @XmlElement(required = true)
    private String pktableName;


    @ColumnLabel("PKCOLUMN_NAME")
    @XmlElement(required = true)
    private String pkcolumnName;


    @ColumnLabel("FKTABLE_CAT")
    @XmlAttribute
    private String fktableCat;


    @ColumnLabel("FKTABLE_NAME")
    @XmlAttribute
    private String fktableSchem;


    @ColumnLabel("FKTABLE_NAME")
    @XmlAttribute
    private String fktableName;


    @ColumnLabel("FKCOLUMN_NAME")
    @XmlElement(required = true)
    private Column fkcolumnName;


    @ColumnLabel("FKCOLUMN_NAME")
    @XmlElement(required = true)
    private short keySeq;


    @ColumnLabel("UPDATE_RULE")
    @XmlElement(required = true)
    private short updateRule;


    @ColumnLabel("DELETE_RULE")
    @XmlElement(required = true)
    private short deleteRule;


    @ColumnLabel("FK_NAME")
    @XmlElement(required = true)
    private String fkName;


    @ColumnLabel("PK_NAME")
    @XmlElement(required = true)
    private String pkName;


    @ColumnLabel("DEFERRABILITY")
    @XmlElement(required = true)
    private short deferrability;


    @XmlTransient
    private Table table;


}

