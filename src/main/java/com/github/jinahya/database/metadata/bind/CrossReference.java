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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Setter
@Getter
public class CrossReference extends AbstractMetadataType {

    private static final long serialVersionUID = -5343386346721125961L;

    static final Comparator<CrossReference> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(CrossReference::getFktableId, TableId.CASE_INSENSITIVE_ORDER)
                    .thenComparingInt(CrossReference::getKeySeq);

    static final Comparator<CrossReference> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(CrossReference::getFktableId, TableId.LEXICOGRAPHIC_ORDER)
                    .thenComparingInt(CrossReference::getKeySeq);

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_UPDATE_RULE = "UPDATE_RULE";

    @Override
    public String toString() {
        return super.toString() + '{' +
               "pktableCat=" + pktableCat +
               ",pktableSchem=" + pktableSchem +
               ",pktableName=" + pktableName +
               ",pkcolumnName=" + pkcolumnName +
               ",fktableCat=" + fktableCat +
               ",fktableSchem=" + fktableSchem +
               ",fktableName=" + fktableName +
               ",fkcolumnName=" + fkcolumnName +
               ",keySeq=" + keySeq +
               ",updateRule=" + updateRule +
               ",deleteRule=" + deleteRule +
               ",fkName=" + fkName +
               ",pkName=" + pkName +
               ",deferrability=" + deferrability +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrossReference)) return false;
        final CrossReference that = (CrossReference) o;
        return Objects.equals(pktableCat, that.pktableCat) &&
               Objects.equals(pktableSchem, that.pktableSchem) &&
               Objects.equals(pktableName, that.pktableName) &&
               Objects.equals(pkcolumnName, that.pkcolumnName) &&
               Objects.equals(fktableCat, that.fktableCat) &&
               Objects.equals(fktableSchem, that.fktableSchem) &&
               Objects.equals(fktableName, that.fktableName) &&
               Objects.equals(fkcolumnName, that.fkcolumnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                pktableCat, pktableSchem, pktableName, pkcolumnName,
                fktableCat, fktableSchem, fktableName, fkcolumnName
        );
    }

    public String getPktableCat() {
        return pktableCat;
    }

    public void setPktableCat(final String pktableCat) {
        this.pktableCat = pktableCat;
        pkcolumnId = null;
    }

    public String getPktableSchem() {
        return pktableSchem;
    }

    public void setPktableSchem(final String pktableSchem) {
        this.pktableSchem = pktableSchem;
        pkcolumnId = null;
    }

    public String getPktableName() {
        return pktableName;
    }

    public void setPktableName(final String pktableName) {
        this.pktableName = pktableName;
        pkcolumnId = null;
    }

    public String getPkcolumnName() {
        return pkcolumnName;
    }

    public void setPkcolumnName(final String pkcolumnName) {
        this.pkcolumnName = pkcolumnName;
        pkcolumnId = null;
    }

    public String getFktableCat() {
        return fktableCat;
    }

    public void setFktableCat(final String fktableCat) {
        this.fktableCat = fktableCat;
        fkcolumnId = null;
    }

    public String getFktableSchem() {
        return fktableSchem;
    }

    public void setFktableSchem(String fktableSchem) {
        this.fktableSchem = fktableSchem;
        fkcolumnId = null;
    }

    public String getFktableName() {
        return fktableName;
    }

    public void setFktableName(String fktableName) {
        this.fktableName = fktableName;
        fkcolumnId = null;
    }

    public String getFkcolumnName() {
        return fkcolumnName;
    }

    public void setFkcolumnName(String fkcolumnName) {
        this.fkcolumnName = fkcolumnName;
        fkcolumnId = null;
    }

    @_NullableBySpecification
    @_ColumnLabel("PKTABLE_CAT")
    private String pktableCat;

    @_NullableBySpecification
    @_ColumnLabel("PKTABLE_SCHEM")
    private String pktableSchem;

    @_ColumnLabel("PKTABLE_NAME")
    private String pktableName;

    @_ColumnLabel("PKCOLUMN_NAME")
    private String pkcolumnName;

    @_NullableBySpecification
    @_ColumnLabel("FKTABLE_CAT")
    private String fktableCat;

    @_NullableBySpecification
    @_ColumnLabel("FKTABLE_SCHEM")
    private String fktableSchem;

    @_ColumnLabel("FKTABLE_NAME")
    private String fktableName;

    @_ColumnLabel("FKCOLUMN_NAME")
    private String fkcolumnName;

    @_ColumnLabel("KEY_SEQ")
    private int keySeq;

    @_ColumnLabel("UPDATE_RULE")
    private int updateRule;

    @_ColumnLabel("DELETE_RULE")
    private int deleteRule;

    @_NullableBySpecification
    @_ColumnLabel("FK_NAME")
    private String fkName;

    @_NullableBySpecification
    @_ColumnLabel("PK_NAME")
    private String pkName;

    @_ColumnLabel("DEFERRABILITY")
    private int deferrability;

    String getPktableCatNonNull() {
        return Optional.ofNullable(getPktableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getPktableSchemNonNull() {
        return Optional.ofNullable(getPktableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    ColumnId getPkcolumnId() {
        if (pkcolumnId == null) {
            pkcolumnId = ColumnId.of(
                    TableId.of(
                            getPktableCatNonNull(),
                            getPktableSchemNonNull(),
                            getPktableName()
                    ),
                    getPkcolumnName()
            );
        }
        return pkcolumnId;
    }

    TableId getPktableId() {
        return getPkcolumnId().getTableId();
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient ColumnId pkcolumnId;

    String getFktableCatNonNull() {
        return Optional.ofNullable(getFktableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getFktableSchemNonNull() {
        return Optional.ofNullable(getFktableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    ColumnId getFkcolumnId() {
        if (fkcolumnId == null) {
            fkcolumnId = ColumnId.of(
                    TableId.of(
                            getFktableCatNonNull(),
                            getFktableSchemNonNull(),
                            getFktableName()
                    ),
                    getFkcolumnName()
            );
        }
        return fkcolumnId;
    }

    TableId getFktableId() {
        return getFkcolumnId().getTableId();
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient ColumnId fkcolumnId;
}
