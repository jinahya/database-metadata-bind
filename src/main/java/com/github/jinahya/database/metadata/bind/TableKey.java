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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * An abstract class for binding results of the
 * {@link java.sql.DatabaseMetaData#getExportedKeys(String, String, String)} method or the
 * {@link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @see ExportedKey
 * @see ImportedKey
 */
@_ChildOf(Table.class)
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
abstract class TableKey<T extends TableKey<T>> extends AbstractMetadataType {

    private static final long serialVersionUID = 6713872409315471232L;

    static <T extends TableKey<T>> Comparator<T> comparingPktableCaseInsensitive() {
        return Comparator.<T, TableId>comparing(TableKey::getPktableId, TableId.CASE_INSENSITIVE_ORDER)
                .thenComparingInt(TableKey::getKeySeq);
    }

    static <T extends TableKey<T>> Comparator<T> comparingPktableLexicographic() {
        return Comparator.<T, TableId>comparing(TableKey::getPktableId, TableId.LEXICOGRAPHIC_ORDER)
                .thenComparingInt(TableKey::getKeySeq);
    }

    static <T extends TableKey<T>> Comparator<T> comparingFktableCaseInsensitive() {
        return Comparator.<T, TableId>comparing(TableKey::getFktableId, TableId.CASE_INSENSITIVE_ORDER)
                .thenComparingInt(TableKey::getKeySeq);
    }

    static <T extends TableKey<T>> Comparator<T> comparingFktableLexicographic() {
        return Comparator.<T, TableId>comparing(TableKey::getFktableId, TableId.LEXICOGRAPHIC_ORDER)
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
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TableKey)) return false;
        return equals_((TableKey<?>) obj);
    }

    boolean equals_(final TableKey<?> that) {
        assert that != null;
        return Objects.equals(getPkcolumnId(), that.getPkcolumnId()) &&
               Objects.equals(getFkcolumnId(), that.getFkcolumnId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPkcolumnId(), getFkcolumnId());
    }

    public void setPktableCat(final String pktableCat) {
        this.pktableCat = pktableCat;
        pkcolumnid = null;
    }

    public void setPktableSchem(final String pktableSchem) {
        this.pktableSchem = pktableSchem;
        pkcolumnid = null;
    }

    public void setPktableName(final String pktableName) {
        this.pktableName = pktableName;
        pkcolumnid = null;
    }

    public void setPkcolumnName(final String pkcolumnName) {
        this.pkcolumnName = pkcolumnName;
        pkcolumnid = null;
    }

    public void setFktableCat(final String fktableCat) {
        this.fktableCat = fktableCat;
        fkcolumnId = null;
    }

    public void setFktableSchem(final String fktableSchem) {
        this.fktableSchem = fktableSchem;
        fkcolumnId = null;
    }

    public void setFktableName(final String fktableName) {
        this.fktableName = fktableName;
        fkcolumnId = null;
    }

    public void setFkcolumnName(final String fkcolumnName) {
        this.fkcolumnName = fkcolumnName;
        fkcolumnId = null;
    }

    public Integer getUpdateRule() {
        return updateRule;
    }

    public void setUpdateRule(final Integer updateRule) {
        this.updateRule = updateRule;
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

    public Integer getDeleteRule() {
        return deleteRule;
    }

    public void setDeleteRule(final Integer deleteRule) {
        this.deleteRule = deleteRule;
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

    TableKeyDeferrability getDeferrabilityAsEnum() {
        return Optional.ofNullable(getDeferrability())
                .map(v -> TableKeyDeferrability.valueOfDeferrability(getDeferrability()))
                .orElse(null);
    }

    void setDeferrabilityAsEnum(final TableKeyDeferrability deferrabilityAsEnum) {
        setDeferrability(
                Optional.ofNullable(deferrabilityAsEnum)
                        .map(v -> v.fieldValueAsInt())
                        .orElse(null)
        );
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

    @_ColumnLabel(COLUMN_NAME_KEY_SEQ)
    private int keySeq;

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

    String getPktableCatNonNull() {
        final String pktableCat = getPktableCat();
        return pktableCat == null ? Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY : pktableCat;
    }

    String getPktableSchemNonNull() {
        final String pktableSchem = getPktableSchem();
        return pktableSchem == null ? Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY : pktableSchem;
    }

    ColumnId getPkcolumnId() {
        if (pkcolumnid == null) {
            pkcolumnid = ColumnId.of(
                    TableId.of(
                            getPktableCatNonNull(),
                            getPktableSchemNonNull(),
                            getPktableName()
                    ),
                    getPkcolumnName()
            );
        }
        return pkcolumnid;
    }

    private TableId getPktableId() {
        return getPkcolumnId().getTableId();
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient ColumnId pkcolumnid;

    String getFktableCatNonNull() {
        final String fktableCat = getFktableCat();
        return fktableCat == null ? Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY : fktableCat;
    }

    String getFktableSchemNonNull() {
        final String fktableSchem = getFktableSchem();
        return fktableSchem == null ? Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY : fktableSchem;
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

    private TableId getFktableId() {
        return getFkcolumnId().getTableId();
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient ColumnId fkcolumnId;
}
