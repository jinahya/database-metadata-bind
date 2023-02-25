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
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Optional;

/**
 * A class for binding the results of
 * {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class CrossReference
        extends AbstractMetadataType {

    private static final long serialVersionUID = -5343386346721125961L;

    public static final Comparator<CrossReference> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(CrossReference::getFktableId, TableId.CASE_INSENSITIVE_ORDER)
                    .thenComparingInt(CrossReference::getKeySeq);

    public static final Comparator<CrossReference> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(CrossReference::getFktableId, TableId.LEXICOGRAPHIC_ORDER)
                    .thenComparingInt(CrossReference::getKeySeq);

    public static final String COLUMN_LABEL_UPDATE_RULE = "UPDATE_RULE";

    public static final String PROPERTY_NAME_UPDATE_RULE = "updateRule";

    public ColumnId getPkcolumnId() {
        return ColumnId.of(
                TableId.of(
                        getPktableCatNonNull(),
                        getPktableSchemNonNull(),
                        getPktableName()
                ),
                getPkcolumnName()
        );
    }

    public ColumnId getFkcolumnId() {
        return ColumnId.of(
                TableId.of(
                        getFktableCatNonNull(),
                        getFktableSchemNonNull(),
                        getFktableName()
                ),
                getFkcolumnName()
        );
    }

    TableId getPktableId() {
        return getPkcolumnId().getTableId();
    }

    TableId getFktableId() {
        return getFkcolumnId().getTableId();
    }

    String getPktableCatNonNull() {
        return Optional.ofNullable(getPktableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getPktableSchemNonNull() {
        return Optional.ofNullable(getPktableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    String getFktableCatNonNull() {
        return Optional.ofNullable(getFktableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getFktableSchemNonNull() {
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
}
