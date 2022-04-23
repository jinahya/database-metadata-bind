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
import javax.xml.bind.annotation.XmlTransient;

/**
 * An abstract class for binding results of {@link java.sql.DatabaseMetaData#getExportedKeys(String, String, String)}
 * method and {@link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @see ImportedKey
 * @see ExportedKey
 */
@XmlTransient
abstract class TableKey
        implements MetadataType {

    private static final long serialVersionUID = 6713872409315471232L;

    /**
     * Creates a new instance.
     */
    protected TableKey() {
        super();
    }

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

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("PKTABLE_CAT")
    private String pktableCat;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("PKTABLE_SCHEM")
    private String pktableSchem;

    @XmlElement(required = true)
    @Label("PKTABLE_NAME")
    private String pktableName;

    @XmlElement(required = true)
    @Label("PKCOLUMN_NAME")
    private String pkcolumnName;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("FKTABLE_CAT")
    private String fktableCat;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("FKTABLE_NAME")
    private String fktableSchem;

    @XmlElement(required = true)
    @Label("FKTABLE_NAME")
    private String fktableName;

    @XmlElement(required = true)
    @Label("FKCOLUMN_NAME")
    private String fkcolumnName;

    @XmlElement(required = true)
    @Label("FKCOLUMN_NAME")
    private short keySeq;

    @XmlElement(required = true)
    @Label("UPDATE_RULE")
    private short updateRule;

    @XmlElement(required = true)
    @Label("DELETE_RULE")
    private short deleteRule;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("FK_NAME")
    private String fkName;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("PK_NAME")
    private String pkName;

    @XmlElement(required = true)
    @Label("DEFERRABILITY")
    private short deferrability;
}
