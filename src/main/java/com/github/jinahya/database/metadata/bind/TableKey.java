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

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * An abstract class for binding results of the
 * {@link java.sql.DatabaseMetaData#getExportedKeys(String, String, String)} method or the
 * {@link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @see ExportedKey
 * @see ImportedKey
 */
@Setter
@Getter
public abstract class TableKey<T extends TableKey<T>> extends AbstractMetadataType {

    private static final long serialVersionUID = 6713872409315471232L;

    static <T extends TableKey<T>> Comparator<T> comparingPktableCaseInsensitive() {
        return Comparator.<T, String>comparing(TableKey::getPktableCat, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                .thenComparing(TableKey::getPktableSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                .thenComparing(TableKey::getPktableName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                .thenComparingInt(TableKey::getKeySeq);
    }

    static <T extends TableKey<T>> Comparator<T> comparingPktableLexicographic() {
        return Comparator.<T, String>comparing(TableKey::getPktableCat, nullsFirst(naturalOrder()))
                .thenComparing(TableKey::getPktableSchem, nullsFirst(naturalOrder()))
                .thenComparing(TableKey::getPktableName, nullsFirst(naturalOrder()))
                .thenComparingInt(TableKey::getKeySeq);
    }

    static <T extends TableKey<T>> Comparator<T> comparingFktableCaseInsensitive() {
        return Comparator.<T, String>comparing(TableKey::getFktableCat, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                .thenComparing(TableKey::getFktableSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                .thenComparing(TableKey::getFktableName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                .thenComparingInt(TableKey::getKeySeq);
    }

    static <T extends TableKey<T>> Comparator<T> comparingFktableLexicographic() {
        return Comparator.<T, String>comparing(TableKey::getFktableCat, nullsFirst(naturalOrder()))
                .thenComparing(TableKey::getFktableSchem, nullsFirst(naturalOrder()))
                .thenComparing(TableKey::getFktableName, nullsFirst(naturalOrder()))
                .thenComparingInt(TableKey::getKeySeq);
    }

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_PKTABLE_CAT = "PKTABLE_CAT";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_PKTABLE_SCHEM = "PKTABLE_SCHEM";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_PKTABLE_NAME = "PKTABLE_NAME";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_PKCOLUMN_NAME = "PKCOLUMN_NAME";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_FKTABLE_CAT = "FKTABLE_CAT";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_FKTABLE_SCHEM = "FKTABLE_SCHEM";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_FKTABLE_NAME = "FKTABLE_NAME";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_FKCOLUMN_NAME = "FKCOLUMN_NAME";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_KEY_SEQ = "KEY_SEQ";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_UPDATE_RULE = "UPDATE_RULE";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_DELETE_RULE = "DELETE_RULE";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_FK_NAME = "FK_NAME";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_PK_NAME = "PK_NAME";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_DEFERRABILITY = "DEFERRABILITY";

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
        if (!(obj instanceof TableKey)) return false;
        final TableKey<?> that = (TableKey<?>) obj;
        return equals_(that);
    }

    @SuppressWarnings({"java:S100"})
    boolean equals_(final TableKey<?> that) {
        assert that != null;
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

    public Integer getDeleteRule() {
        return deleteRule;
    }

    public void setDeleteRule(final Integer deleteRule) {
        this.deleteRule = deleteRule;
    }

    /**
     * Returns current value of {@link #COLUMN_NAME_DEFERRABILITY} column value.
     *
     * @return current value of {@link #COLUMN_NAME_DEFERRABILITY} column value.
     */
    public Integer getDeferrability() {
        return deferrability;
    }

    /**
     * Replaces current value of {@link #COLUMN_NAME_DEFERRABILITY} column value with specified value.
     *
     * @param deferrability new value for the {@link #COLUMN_NAME_DEFERRABILITY} column value.
     */
    public void setDeferrability(final Integer deferrability) {
        this.deferrability = deferrability;
    }

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PKTABLE_CAT)
    private String pktableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PKTABLE_SCHEM)
    private String pktableSchem;

    @_ColumnLabel(COLUMN_NAME_PKTABLE_NAME)
    private String pktableName;

    @_ColumnLabel(COLUMN_NAME_PKCOLUMN_NAME)
    private String pkcolumnName;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FKTABLE_CAT)
    private String fktableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FKTABLE_SCHEM)
    private String fktableSchem;

    @_ColumnLabel(COLUMN_NAME_FKTABLE_NAME)
    private String fktableName;

    @_ColumnLabel(COLUMN_NAME_FKCOLUMN_NAME)
    private String fkcolumnName;

    @_NotNull
    @_ColumnLabel(COLUMN_NAME_KEY_SEQ)
    private Integer keySeq;

    @_NotNull
    @_ColumnLabel(COLUMN_NAME_UPDATE_RULE)
    private Integer updateRule;

    @_NotNull
    @_ColumnLabel(COLUMN_NAME_DELETE_RULE)
    private Integer deleteRule;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FK_NAME)
    private String fkName;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PK_NAME)
    private String pkName;

    @_NotNull
    @_ColumnLabel(COLUMN_NAME_DEFERRABILITY)
    private Integer deferrability;

    String pktableCatNonNull() {
        final String pktableCat_ = getPktableCat();
        return pktableCat_ == null ? Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY : pktableCat_;
    }

    String pktableSchemNonNull() {
        final String pktableSchem_ = getPktableSchem();
        return pktableSchem_ == null ? Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY : pktableSchem_;
    }

    String fktableCatNonNull() {
        final String fktableCat_ = getFktableCat();
        return fktableCat_ == null ? Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY : fktableCat_;
    }

    String fktableSchemNonNull() {
        final String fktableSchem_ = getFktableSchem();
        return fktableSchem_ == null ? Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY : fktableSchem_;
    }

    TableKeyUpdateRule getUpdateRuleAsEnum() {
        return Optional.ofNullable(getUpdateRule())
                .map(TableKeyUpdateRule::valueOfUpdateRule)
                .orElse(null);
    }

    void setUpdateRuleAsEnum(final TableKeyUpdateRule updateRuleAsEnum) {
        setUpdateRule(
                Optional.ofNullable(updateRuleAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    TableKeyDeleteRule getDeleteRuleAsEnum() {
        return Optional.ofNullable(getDeleteRule())
                .map(TableKeyDeleteRule::valueOfDeleteRule)
                .orElse(null);
    }

    void setDeleteRuleAsEnum(final TableKeyDeleteRule deleteRuleAsEnum) {
        setDeleteRule(
                Optional.ofNullable(deleteRuleAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    TableKeyDeferrability getDeferrabilityAsEnum() {
        return Optional.ofNullable(getDeferrability())
                .map(v -> TableKeyDeferrability.valueOfDeferrability(getDeferrability()))
                .orElse(null);
    }

    void setDeferrabilityAsEnum(final TableKeyDeferrability deferrabilityAsEnum) {
        setDeferrability(
                Optional.ofNullable(deferrabilityAsEnum)
                        .map(TableKeyDeferrability::fieldValueAsInt)
                        .orElse(null)
        );
    }
}
