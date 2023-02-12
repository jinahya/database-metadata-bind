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
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#getVersionColumns(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getVersionColumns(String, String, String)
 */
@ChildOf(Table.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class VersionColumn extends AbstractMetadataType {

    private static final long serialVersionUID = 3587959398829593292L;

    public static final String COLUMN_LABEL_PSEUDO_COLUMN = "PSEUDO_COLUMN";

    public ColumnId getColumnId(final Table table) {
        Objects.requireNonNull(table, "table is null");
        return ColumnId.of(
                table.getTableId(),
                getColumnName()
        );
    }

    public VersionColumnPseudoColumn getPseudoColumnAsEnum() {
        return VersionColumnPseudoColumn.valueOfPseudoColumn(getPseudoColumn());
    }

    public void setPseudoColumnAsEnum(final VersionColumnPseudoColumn pseudoColumnAsEnum) {
        Objects.requireNonNull(pseudoColumnAsEnum, "pseudoColumnAsEnum is null");
        setPseudoColumn(pseudoColumnAsEnum.fieldValueAsInt());
    }

    @NotUsedBySpecification
    @ColumnLabel("SCOPE")
    private Integer scope;

    @ColumnLabel("COLUMN_NAME")
    private String columnName;

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @NullableBySpecification
    @ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @ColumnLabel("BUFFER_LENGTH")
    private int bufferLength;

    @NullableBySpecification
    @ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @ColumnLabel(COLUMN_LABEL_PSEUDO_COLUMN)
    private int pseudoColumn;

    @Accessors(fluent = true)
    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Table table;
}
