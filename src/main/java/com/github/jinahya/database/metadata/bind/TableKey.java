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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Optional;

/**
 * An abstract class for binding results of {@link java.sql.DatabaseMetaData#getExportedKeys(String, String, String)}
 * method or {@link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @see ExportedKey
 * @see ImportedKey
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public abstract class TableKey<T extends TableKey<T>> extends AbstractMetadataType {

    private static final long serialVersionUID = 6713872409315471232L;

    static <T extends TableKey> Comparator<T> comparingPktableKeySeq() {
        return Comparator.<T, TableId>comparing(TableKey::getPktableId)
                .thenComparingInt(TableKey::getKeySeq);
    }

    static <T extends TableKey> Comparator<T> comparingFktableKeySeq() {
        return Comparator.<T, TableId>comparing(TableKey::getFktableId)
                .thenComparingInt(TableKey::getKeySeq);
    }

    public ColumnId getPkcolumnId() {
        return ColumnId.of(getPktableCatNonNull(), getPktableSchemNonNull(), getPktableName(), getPkcolumnName(), 0);
    }

    public TableId getPktableId() {
        return getPkcolumnId().getTableId();
    }

    public ColumnId getFkcolumnId() {
        return ColumnId.of(getFktableCatNonNull(), getFktableSchemNonNull(), getFktableName(), getFkcolumnName(), 0);
    }

    public TableId getFktableId() {
        return getFkcolumnId().getTableId();
    }

    String getPktableCatNonNull() {
        return Optional.ofNullable(getPktableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public String getPktableSchemNonNull() {
        return Optional.ofNullable(getPktableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    String getFktableCatNonNull() {
        return Optional.ofNullable(getFktableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public String getFktableSchemNonNull() {
        return Optional.ofNullable(getFktableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

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

    @SuppressWarnings({"unchecked"})
    T table(final Table table) {
        this.table = table;
        return (T) this;
    }

    @Accessors(fluent = true)
    @Getter(AccessLevel.PACKAGE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Table table;
}
