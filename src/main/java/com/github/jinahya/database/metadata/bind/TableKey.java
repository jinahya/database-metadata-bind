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
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;

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
public abstract class TableKey
        extends AbstractMetadataType {

    private static final long serialVersionUID = 6713872409315471232L;

    public static final Comparator<TableKey> COMPARING_FKTABLE_CAT_FKTABLE_SCHEM_FKTABLE_NAME_KEY_SEQ =
            Comparator.comparing(TableKey::getFktableCat, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(TableKey::getFktableSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(TableKey::getFktableName)
                    .thenComparingInt(TableKey::getKeySeq);

    public TableId getPktableId() {
        return TableId.of(getPktableCat(), getPktableSchem(), getPktableName());
    }

    public TableId getFktableId() {
        return TableId.of(getFktableCat(), getFktableSchem(), getFktableName());
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
}
