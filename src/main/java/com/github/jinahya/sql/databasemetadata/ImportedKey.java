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


package com.github.jinahya.sql.databasemetadata;


import com.github.jinahya.sql.databasemetadata.ColumnLabel;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
public class ImportedKey {


    public String getPktableCat() {

        return pktableCat;
    }


    public void setPktableCat(final String pktableCat) {

        this.pktableCat = pktableCat;
    }


    public String getPktableSchem() {

        return pktableSchem;
    }


    public void setPktableSchem(final String pktableSchem) {

        this.pktableSchem = pktableSchem;
    }


    public String getPktableName() {

        return pktableName;
    }


    public void setPktableName(final String pktableName) {

        this.pktableName = pktableName;
    }


    public String getPkcolumnName() {

        return pkcolumnName;
    }


    public void setPkcolumnName(final String pkcolumnName) {

        this.pkcolumnName = pkcolumnName;
    }


    public String getFktableCat() {

        return fktableCat;
    }


    public void setFktableCat(final String fktableCat) {

        this.fktableCat = fktableCat;
    }


    public String getFktableSchem() {

        return fktableSchem;
    }


    public void setFktableSchem(final String fktableSchem) {

        this.fktableSchem = fktableSchem;
    }


    public String getFktableName() {

        return fktableName;
    }


    public void setFktableName(final String fktableName) {

        this.fktableName = fktableName;
    }


    public String getFkcolumnName() {

        return fkcolumnName;
    }


    public void setFkcolumnName(final String fkcolumnName) {

        this.fkcolumnName = fkcolumnName;
    }


    public short getKeySeq() {

        return keySeq;
    }


    public void setKeySeq(final short keySeq) {

        this.keySeq = keySeq;
    }


    public short getUpdateRule() {

        return updateRule;
    }


    public void setUpdateRule(final short updateRule) {

        this.updateRule = updateRule;
    }


    public short getDeleteRule() {

        return deleteRule;
    }


    public void setDeleteRule(final short deleteRule) {

        this.deleteRule = deleteRule;
    }


    public String getFkName() {

        return fkName;
    }


    public void setFkName(final String fkName) {

        this.fkName = fkName;
    }


    public String getPkName() {

        return pkName;
    }


    public void setPkName(final String pkName) {

        this.pkName = pkName;
    }


    public short getDeferrability() {

        return deferrability;
    }


    public void setDeferrability(final short deferrability) {

        this.deferrability = deferrability;
    }


    @ColumnLabel("PKTABLE_CAT")
    @XmlTransient
    private String pktableCat;


    @ColumnLabel("PKTABLE_SCHEM")
    @XmlTransient
    private String pktableSchem;


    @ColumnLabel("PKTABLE_NAME")
    @XmlTransient
    private String pktableName;


    @ColumnLabel("PKCOLUMN_NAME")
    @XmlTransient
    private String pkcolumnName;


    @ColumnLabel("FKTABLE_CAT")
    @XmlTransient
    private String fktableCat;


    @ColumnLabel("FKTABLE_SCHEM")
    @XmlTransient
    private String fktableSchem;


    @ColumnLabel("FKTABLE_NAME")
    @XmlTransient
    private String fktableName;


    @ColumnLabel("FKCOLUMN_NAME")
    @XmlTransient
    private String fkcolumnName;


    @ColumnLabel("KEY_SEQ")
    @XmlElement(required = true)
    private short keySeq;


    @ColumnLabel("UPDATE_RULE")
    @XmlElement(required = true)
    private short updateRule;


    @ColumnLabel("DELETE_RULE")
    @XmlElement(required = true)
    private short deleteRule;


    @ColumnLabel("FK_NAME")
    @XmlElement(nillable = true, required = true)
    private String fkName;


    @ColumnLabel("PK_NAME")
    @XmlElement(nillable = true, required = true)
    private String pkName;


    @ColumnLabel("DEFERRABILITY")
    @XmlElement(required = true)
    private short deferrability;


}

