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
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.CompareToBuilder;


/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "pkcolumnName", "fkcolumnName", "keySeq", "updateRule", "deleteRule",
        "fkName", "pkName", "deferrability"
    }
)
public class CrossReference extends AbstractChild<Metadata>
    implements Comparable<CrossReference> {


    // by FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and KEY_SEQ.
    @Override
    public int compareTo(final CrossReference o) {

        return new CompareToBuilder()
            .append(fktableCat, o.getFktableCat())
            .append(fktableSchem, o.getFktableSchem())
            .append(fktableName, o.getFktableName())
            .append(keySeq, o.getKeySeq())
            .build();
    }


    @Override
    public String toString() {

        return super.toString() + "{"
               + "pktableCat=" + pktableCat
               + ", pktableSchem=" + pktableSchem
               + ", pktableName=" + pktableName
               + ", pkcolumnName=" + pkcolumnName
               + ", fktableCat=" + fktableCat
               + ", fktableSchem=" + fktableSchem
               + ", fktableName=" + fktableName
               + ", fkcolumnName=" + fkcolumnName
               + ", keySeq=" + keySeq
               + ", updateRule=" + updateRule
               + ", deleteRule=" + deleteRule
               + ", fkName=" + fkName
               + ", pkName=" + pkName
               + ", deferrability=" + deferrability
               + "}";
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


    // ---------------------------------------------------------------- metadata
    // just for class diagram
    private Metadata getMetadata() {

        return getParent();
    }


//    public void setMetadata(final Metadata metadata) {
//
//        setParent(metadata);
//    }
//
//
//    CrossReference metadata(final Metadata metadata) {
//
//        setMetadata(metadata);
//
//        return this;
//    }
    // -------------------------------------------------------------------------
    @Label("PKTABLE_CAT")
    @NillableBySpecification
    @XmlAttribute
    private String pktableCat;


    @Label("PKTABLE_SCHEM")
    @NillableBySpecification
    @XmlAttribute
    private String pktableSchem;


    @Label("PKTABLE_NAME")
    @XmlAttribute
    private String pktableName;


    @Label("PKCOLUMN_NAME")
    @XmlElement(required = true)
    private String pkcolumnName;


    @Label("FKTABLE_CAT")
    @NillableBySpecification
    @XmlAttribute
    private String fktableCat;


    @Label("FKTABLE_NAME")
    @NillableBySpecification
    @XmlAttribute
    private String fktableSchem;


    @Label("FKTABLE_NAME")
    @XmlAttribute
    private String fktableName;


    @Label("FKCOLUMN_NAME")
    @XmlElement(required = true)
    private String fkcolumnName;


    @Label("FKCOLUMN_NAME")
    @XmlElement(required = true)
    private short keySeq;


    @Label("UPDATE_RULE")
    @XmlElement(required = true)
    private short updateRule;


    @Label("DELETE_RULE")
    @XmlElement(required = true)
    private short deleteRule;


    @Label("FK_NAME")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String fkName;


    @Label("PK_NAME")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String pkName;


    @Label("DEFERRABILITY")
    @XmlElement(required = true)
    private short deferrability;

}

