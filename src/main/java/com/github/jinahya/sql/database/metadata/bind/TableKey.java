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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlTransient
@XmlType(propOrder = {
    "pkcolumnName", "fktableCat", "fktableSchem",
    "fktableName", "fkcolumnName", "keySeq", "updateRule",
    "deleteRule", "fkName", "pkName", "deferrability"
})
abstract class TableKey implements Serializable {

    private static final long serialVersionUID = 6713872409315471232L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(TableKey.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "pktableCat=" + pktableCat
               + ",pktableSchem=" + pktableSchem
               + ",pktableName=" + pktableName
               + ",pkcolumnName=" + pkcolumnName
               + ",fktableCat=" + fktableCat
               + ",fktableSchem=" + fktableSchem
               + ",fktableName=" + fktableName
               + ",fkcolumnName=" + fkcolumnName
               + ",keySeq=" + keySeq
               + ",updateRule=" + updateRule
               + ",deleteRule=" + deleteRule
               + ",fkName=" + fkName
               + ",pkName=" + pkName
               + ",deferrability=" + deferrability
               + "}";
    }

    // -------------------------------------------------------------- pktableCat
//    public String getPktableCat() {
//        return pktableCat;
//    }
//
//    public void setPktableCat(final String pktableCat) {
//        this.pktableCat = pktableCat;
//    }
    // ------------------------------------------------------------ pktableSchem
//    public String getPktableSchem() {
//        return pktableSchem;
//    }
//
//    public void setPktableSchem(final String pktableSchem) {
//        this.pktableSchem = pktableSchem;
//    }
    // ------------------------------------------------------------- pktableName
//    public String getPktableName() {
//        return pktableName;
//    }
//
//    public void setPktableName(final String pktableName) {
//        this.pktableName = pktableName;
//    }
    // ------------------------------------------------------------ pkColumnName
//    public String getPkcolumnName() {
//        return pkcolumnName;
//    }
//
//    public void setPkcolumnName(final String pkcolumnName) {
//        this.pkcolumnName = pkcolumnName;
//    }
    // -------------------------------------------------------------- fktableCat
//    public String getFktableCat() {
//        return fktableCat;
//    }
//
//    public void setFktableCat(final String fktableCat) {
//        this.fktableCat = fktableCat;
//    }
    // ------------------------------------------------------------ fktableSchem
//    public String getFktableSchem() {
//        return fktableSchem;
//    }
//
//    public void setFktableSchem(final String fktableSchem) {
//        this.fktableSchem = fktableSchem;
//    }
    // ------------------------------------------------------------- fktableName
//    public String getFktableName() {
//        return fktableName;
//    }
//
//    public void setFktableName(final String fktableName) {
//        this.fktableName = fktableName;
//    }
    // ------------------------------------------------------------ fkcolumnName
//    public String getFkcolumnName() {
//        return fkcolumnName;
//    }
//
//    public void setFkcolumnName(final String fkcolumnName) {
//        this.fkcolumnName = fkcolumnName;
//    }
    // ------------------------------------------------------------------ keySeq
//    public short getKeySeq() {
//        return keySeq;
//    }
//
//    public void setKeySeq(final short keySeq) {
//        this.keySeq = keySeq;
//    }
    // -------------------------------------------------------------- updateRule
//    public short getUpdateRule() {
//        return updateRule;
//    }
//
//    public void setUpdateRule(final short updateRule) {
//        this.updateRule = updateRule;
//    }
    // -------------------------------------------------------------- deleteRule
//    public short getDeleteRule() {
//        return deleteRule;
//    }
//
//    public void setDeleteRule(final short deleteRule) {
//        this.deleteRule = deleteRule;
//    }
    // ------------------------------------------------------------------ fnname
//    public String getFkName() {
//        return fkName;
//    }
//
//    public void setFkName(final String fkName) {
//        this.fkName = fkName;
//    }
    // ------------------------------------------------------------------ pkName
//    public String getPkName() {
//        return pkName;
//    }
//
//    public void setPkName(final String pkName) {
//        this.pkName = pkName;
//    }
    // --------------------------------------------------------- deferrerability
//    public short getDeferrability() {
//        return deferrability;
//    }
//
//    public void setDeferrability(final short deferrability) {
//        this.deferrability = deferrability;
//    }
    // -------------------------------------------------------------------------
    @XmlAttribute
    @Labeled("PKTABLE_CAT")
    @Nillable
    @Getter
    @Setter
    private String pktableCat;

    @XmlAttribute
    @Labeled("PKTABLE_SCHEM")
    @Nillable
    @Getter
    @Setter
    private String pktableSchem;

    @XmlAttribute
    @Labeled("PKTABLE_NAME")
    @Getter
    @Setter
    private String pktableName;

    @XmlElement(required = true)
    @Labeled("PKCOLUMN_NAME")
    @Getter
    @Setter
    private String pkcolumnName;

    @XmlElement(nillable = true, required = true)
    @Labeled("FKTABLE_CAT")
    @Nillable
    @Getter
    @Setter
    private String fktableCat;

    @XmlElement(nillable = true, required = true)
    @Labeled("FKTABLE_NAME")
    @Nillable
    @Getter
    @Setter
    private String fktableSchem;

    @XmlElement(required = true)
    @Labeled("FKTABLE_NAME")
    @Getter
    @Setter
    private String fktableName;

    @XmlElement(required = true)
    @Labeled("FKCOLUMN_NAME")
    @Getter
    @Setter
    private String fkcolumnName;

    @XmlElement(required = true)
    @Labeled("FKCOLUMN_NAME")
    @Getter
    @Setter
    private short keySeq;

    @XmlElement(required = true)
    @Labeled("UPDATE_RULE")
    @Getter
    @Setter
    private short updateRule;

    @XmlElement(required = true)
    @Labeled("DELETE_RULE")
    @Getter
    @Setter
    private short deleteRule;

    @XmlElement(required = true)
    @Labeled("FK_NAME")
    @Getter
    @Setter
    private String fkName;

    @XmlElement(required = true)
    @Labeled("PK_NAME")
    @Getter
    @Setter
    private String pkName;

    @XmlElement(required = true)
    @Labeled("DEFERRABILITY")
    @Getter
    @Setter
    private short deferrability;
}
