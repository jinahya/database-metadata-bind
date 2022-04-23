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
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getCrossReference(String, String, String, String, String, String, Collection)
 */
@XmlRootElement
public class CrossReference
        implements MetadataType {

    private static final long serialVersionUID = -5343386346721125961L;

    public static <C extends Collection<? super CrossReference>> C getAllInstance(final Context context,
                                                                                  final C collection)
            throws SQLException {
        requireNonNull(context, "context is null");
        requireNonNull(collection, "collection is null");
        final List<Table> tables = context.getTables(null, null, "%", null, new ArrayList<>());
        for (final Table parentTable : tables) {
            for (final Table foreignTable : tables) {
                context.getCrossReference(parentTable, foreignTable, collection);
            }
        }
        return collection;
    }

    // ---------------------------------------------------------------------------------------- UPDATE_RULE / updateRule
    public static final String COLUMN_NAME_UPDATE_RULE = "UPDATE_RULE";

    public static final String ATTRIBUTE_NAME_UPDATE_RULE = "updateRule";

    /**
     * Creates a new instance.
     */
    public CrossReference() {
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
