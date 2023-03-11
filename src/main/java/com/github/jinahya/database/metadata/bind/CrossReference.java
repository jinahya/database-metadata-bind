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

import lombok.Getter;
import lombok.Setter;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Setter
@Getter
public class CrossReference extends AbstractMetadataType {

    private static final long serialVersionUID = -5343386346721125961L;

    static final Comparator<CrossReference> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(CrossReference::fktableCatNonNull, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(CrossReference::fktableSchemNonNull, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(CrossReference::getFktableName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparingInt(CrossReference::getKeySeq);

    static final Comparator<CrossReference> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(CrossReference::fktableCatNonNull, naturalOrder())
                    .thenComparing(CrossReference::fktableSchemNonNull, naturalOrder())
                    .thenComparing(CrossReference::getFktableName, nullsFirst(naturalOrder()))
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
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CrossReference)) return false;
        final CrossReference that = (CrossReference) obj;
        return Objects.equals(pktableCatNonNull(), that.pktableCatNonNull()) &&
               Objects.equals(pktableSchemNonNull(), that.pktableSchemNonNull()) &&
               Objects.equals(pktableName, that.pktableName) &&
               Objects.equals(pkcolumnName, that.pkcolumnName) &&
               Objects.equals(fktableCatNonNull(), that.fktableCatNonNull()) &&
               Objects.equals(fktableSchemNonNull(), that.fktableSchemNonNull()) &&
               Objects.equals(fktableName, that.fktableName) &&
               Objects.equals(fkcolumnName, that.fkcolumnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                pktableCatNonNull(), pktableSchemNonNull(), pktableName, pkcolumnName,
                fktableCatNonNull(), fktableSchemNonNull(), fktableName, fkcolumnName
        );
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

    @_NotNull
    @_ColumnLabel("KEY_SEQ")
    private Integer keySeq;

    @_NotNull
    @_ColumnLabel("UPDATE_RULE")
    private Integer updateRule;

    @_NotNull
    @_ColumnLabel("DELETE_RULE")
    private Integer deleteRule;

    @_NullableBySpecification
    @_ColumnLabel("FK_NAME")
    private String fkName;

    @_NullableBySpecification
    @_ColumnLabel("PK_NAME")
    private String pkName;

    @_NotNull
    @_ColumnLabel("DEFERRABILITY")
    private Integer deferrability;

    String pktableCatNonNull() {
        final String pktableCat_ = getPktableCat();
        if (pktableCat_ != null) {
            return pktableCat_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }

    String pktableSchemNonNull() {
        final String pktableSchem_ = getPktableSchem();
        if (pktableSchem_ != null) {
            return pktableSchem_;
        }
        return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
    }

    String fktableCatNonNull() {
        final String fktableCat_ = getFktableCat();
        if (fktableCat_ != null) {
            return fktableCat_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }

    String fktableSchemNonNull() {
        final String fktableSchem_ = getFktableSchem();
        if (fktableSchem_ != null) {
            return fktableSchem_;
        }
        return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
    }
}
