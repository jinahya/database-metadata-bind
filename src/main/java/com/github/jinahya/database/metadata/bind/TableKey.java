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
 * An abstract class for binding results of {@link java.sql.DatabaseMetaData#getExportedKeys(String, String, String)}
 * method or {@link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @see ExportedKey
 * @see ImportedKey
 */
@ChildOf(Table.class)
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public abstract class TableKey<T extends TableKey<T>> extends AbstractMetadataType {

    private static final long serialVersionUID = 6713872409315471232L;

    static <T extends TableKey<T>> Comparator<T> comparingPktableKeySeqCaseInsensitive() {
        return Comparator.<T, TableId>comparing(TableKey::getPktableId, TableId.CASE_INSENSITIVE_ORDER)
                .thenComparingInt(TableKey::getKeySeq);
    }

    static <T extends TableKey<T>> Comparator<T> comparingPktableKeySeqLexicographic() {
        return Comparator.<T, TableId>comparing(TableKey::getPktableId, TableId.LEXICOGRAPHIC_ORDER)
                .thenComparingInt(TableKey::getKeySeq);
    }

    static <T extends TableKey<T>> Comparator<T> comparingFktableKeySeqCaseInsensitive() {
        return Comparator.<T, TableId>comparing(TableKey::getFktableId, TableId.CASE_INSENSITIVE_ORDER)
                .thenComparingInt(TableKey::getKeySeq);
    }

    static <T extends TableKey<T>> Comparator<T> comparingFktableKeySeqLexicographic() {
        return Comparator.<T, TableId>comparing(TableKey::getFktableId, TableId.LEXICOGRAPHIC_ORDER)
                .thenComparingInt(TableKey::getKeySeq);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TableKey)) return false;
        return equals((TableKey<?>) obj);
    }

    boolean equals(final TableKey<?> that) {
        return Objects.equals(getPkcolumnId(), that.getPkcolumnId()) &&
               Objects.equals(getFkcolumnId(), that.getFkcolumnId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPkcolumnId(), getFkcolumnId());
    }

    // ------------------------------------------------------------------------------------------------------ pktableCat
    String getPktableCatNonNull() {
        return Optional.ofNullable(getPktableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public void setPktableCat(final String pktableCat) {
        this.pktableCat = pktableCat;
        pkcolumnid = null;
    }

    // ---------------------------------------------------------------------------------------------------- pktableSchem
    public String getPktableSchemNonNull() {
        return Optional.ofNullable(getPktableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    public void setPktableSchem(final String pktableSchem) {
        this.pktableSchem = pktableSchem;
        pkcolumnid = null;
    }

    // ----------------------------------------------------------------------------------------------------- pktableName
    public void setPktableName(final String pktableName) {
        this.pktableName = pktableName;
        pkcolumnid = null;
    }

    // ---------------------------------------------------------------------------------------------------- pkcolumnName
    public void setPkcolumnName(final String pkcolumnName) {
        this.pkcolumnName = pkcolumnName;
        pkcolumnid = null;
    }

    // ------------------------------------------------------------------------------------------------------ fktableCat
    String getFktableCatNonNull() {
        return Optional.ofNullable(getFktableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public void setFktableCat(final String fktableCat) {
        this.fktableCat = fktableCat;
        fkcolumnId = null;
    }

    // ---------------------------------------------------------------------------------------------------- fktableSchem
    public String getFktableSchemNonNull() {
        return Optional.ofNullable(getFktableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    public void setFktableSchem(final String fktableSchem) {
        this.fktableSchem = fktableSchem;
        fkcolumnId = null;
    }

    // ----------------------------------------------------------------------------------------------------- fktableName
    public void setFktableName(final String fktableName) {
        this.fktableName = fktableName;
        fkcolumnId = null;
    }

    // ---------------------------------------------------------------------------------------------------- fkcolumnName
    public void setFkcolumnName(final String fkcolumnName) {
        this.fkcolumnName = fkcolumnName;
        fkcolumnId = null;
    }

    // ------------------------------------------------------------------------------------------------------ pkcolumnId
    public ColumnId getPkcolumnId() {
        if (pkcolumnid == null) {
            pkcolumnid = ColumnId.of(
                    getPktableCatNonNull(),
                    getPktableSchemNonNull(),
                    getPktableName(),
                    getPkcolumnName()
            );
        }
        return pkcolumnid;
    }

    private TableId getPktableId() {
        return getPkcolumnId().getTableId();
    }

    // ------------------------------------------------------------------------------------------------------ fkcolumnId
    public ColumnId getFkcolumnId() {
        if (fkcolumnId == null) {
            fkcolumnId = ColumnId.of(
                    getFktableCatNonNull(),
                    getFktableSchemNonNull(),
                    getFktableName(),
                    getFkcolumnName()
            );
        }
        return fkcolumnId;
    }

    private TableId getFktableId() {
        return getFkcolumnId().getTableId();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @NullableBySpecification
    @ColumnLabel("PKTABLE_CAT")
    private String pktableCat;

    @NullableBySpecification
    @ColumnLabel("PKTABLE_SCHEM")
    private String pktableSchem;

    @ColumnLabel("PKTABLE_NAME")
    private String pktableName;

    @ColumnLabel("PKCOLUMN_NAME")
    private String pkcolumnName;

    @NullableBySpecification
    @ColumnLabel("FKTABLE_CAT")
    private String fktableCat;

    @NullableBySpecification
    @ColumnLabel("FKTABLE_SCHEM")
    private String fktableSchem;

    @ColumnLabel("FKTABLE_NAME")
    private String fktableName;

    @ColumnLabel("FKCOLUMN_NAME")
    private String fkcolumnName;

    @ColumnLabel("KEY_SEQ")
    private int keySeq;

    @ColumnLabel("UPDATE_RULE")
    private int updateRule;

    @ColumnLabel("DELETE_RULE")
    private int deleteRule;

    @NullableBySpecification
    @ColumnLabel("FK_NAME")
    private String fkName;

    @NullableBySpecification
    @ColumnLabel("PK_NAME")
    private String pkName;

    @ColumnLabel("DEFERRABILITY")
    private int deferrability;

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient ColumnId pkcolumnid;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient ColumnId fkcolumnId;
}
