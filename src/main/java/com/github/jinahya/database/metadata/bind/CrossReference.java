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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * An entity class for cross references.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see MetadataContext#getCrossReferences(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
 * java.lang.String, java.lang.String)
 */
@XmlRootElement
@XmlType(propOrder = {
        "pkcolumnName", "fkcolumnName", "keySeq", "updateRule", "deleteRule", "fkName", "pkName", "deferrability"
})
public class CrossReference implements Serializable {

    private static final long serialVersionUID = -5343386346721125961L;

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
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
               + '}';
    }

    // -------------------------------------------------------------- pktableCat
    public String getPktableCat() {
        return pktableCat;
    }

    public void setPktableCat(final String pktableCat) {
        this.pktableCat = pktableCat;
    }

    // ------------------------------------------------------------ pktableSchem
    public String getPktableSchem() {
        return pktableSchem;
    }

    public void setPktableSchem(final String pktableSchem) {
        this.pktableSchem = pktableSchem;
    }

    // ------------------------------------------------------------- pktableName
    public String getPktableName() {
        return pktableName;
    }

    public void setPktableName(final String pktableName) {
        this.pktableName = pktableName;
    }

    // ------------------------------------------------------------ pkColumnName
    public String getPkcolumnName() {
        return pkcolumnName;
    }

    public void setPkcolumnName(final String pkcolumnName) {
        this.pkcolumnName = pkcolumnName;
    }

    // -------------------------------------------------------------- fktableCat
    public String getFktableCat() {
        return fktableCat;
    }

    public void setFktableCat(final String fktableCat) {
        this.fktableCat = fktableCat;
    }

    // ------------------------------------------------------------ fktableSchem
    public String getFktableSchem() {
        return fktableSchem;
    }

    public void setFktableSchem(final String fktableSchem) {
        this.fktableSchem = fktableSchem;
    }

    // ------------------------------------------------------------- fktableName
    public String getFktableName() {
        return fktableName;
    }

    public void setFktableName(final String fktableName) {
        this.fktableName = fktableName;
    }

    // ------------------------------------------------------------ fkcolumnName
    public String getFkcolumnName() {
        return fkcolumnName;
    }

    public void setFkcolumnName(final String fkcolumnName) {
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

    public void setUpdateRule(final short updateRule) {
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
    public short getDeferrability() {
        return deferrability;
    }

    public void setDeferrability(final short deferrability) {
        this.deferrability = deferrability;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Bind(label = "PKTABLE_CAT", nillable = true)
    private String pktableCat;

    @XmlAttribute
    @Bind(label = "PKTABLE_SCHEM", nillable = true)
    private String pktableSchem;

    @XmlAttribute
    @Bind(label = "PKTABLE_NAME")
    private String pktableName;

    // -------------------------------------------------------------------------
    @XmlElement
    @Bind(label = "PKCOLUMN_NAME")
    private String pkcolumnName;

    @XmlAttribute
    @Bind(label = "FKTABLE_CAT", nillable = true)
    private String fktableCat;

    @XmlAttribute
    @Bind(label = "FKTABLE_NAME", nillable = true)
    private String fktableSchem;

    @XmlAttribute
    @Bind(label = "FKTABLE_NAME")
    private String fktableName;

    @XmlElement
    @Bind(label = "FKCOLUMN_NAME")
    private String fkcolumnName;

    @XmlElement
    @Bind(label = "FKCOLUMN_NAME")
    private short keySeq;

    @XmlElement
    @Bind(label = "UPDATE_RULE")
    private short updateRule;

    @XmlElement
    @Bind(label = "DELETE_RULE")
    private short deleteRule;

    @XmlElement(nillable = true)
    @Bind(label = "FK_NAME", nillable = true)
    private String fkName;

    @XmlElement(nillable = true)
    @Bind(label = "PK_NAME", nillable = true)
    private String pkName;

    @XmlElement
    @Bind(label = "DEFERRABILITY")
    private short deferrability;
}
