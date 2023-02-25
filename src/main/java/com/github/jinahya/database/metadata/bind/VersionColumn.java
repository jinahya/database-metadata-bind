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

import java.sql.DatabaseMetaData;
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#getVersionColumns(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getVersionColumns(String, String, String)
 */
@ChildOf(Table.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class VersionColumn extends AbstractMetadataType {

    private static final long serialVersionUID = 3587959398829593292L;

    public static final String COLUMN_LABEL_PSEUDO_COLUMN = "PSEUDO_COLUMN";

    /**
     * Constants for {@link #COLUMN_LABEL_PSEUDO_COLUMN} column values.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum PseudoColumnEnum implements _IntFieldEnum<PseudoColumnEnum> {

        /**
         * A value for {@link DatabaseMetaData#versionColumnUnknown}({@value DatabaseMetaData#versionColumnUnknown}).
         */
        VERSION_COLUMN_UNKNOWN(DatabaseMetaData.versionColumnUnknown),// 0

        /**
         * A value for
         * {@link DatabaseMetaData#versionColumnNotPseudo}({@value DatabaseMetaData#versionColumnNotPseudo}).
         */
        VERSION_COLUMN_NOT_PSEUDO(DatabaseMetaData.versionColumnNotPseudo), // 1

        /**
         * A value for {@link DatabaseMetaData#versionColumnPseudo}({@value DatabaseMetaData#versionColumnPseudo}).
         */
        VERSION_COLUMN_PSEUDO(DatabaseMetaData.versionColumnPseudo) // 2
        ;

        /**
         * Finds the value for specified {@link VersionColumn#COLUMN_LABEL_PSEUDO_COLUMN} column value.
         *
         * @param pseudoColumn the value of {@link VersionColumn#COLUMN_LABEL_PSEUDO_COLUMN} column to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static PseudoColumnEnum valueOfPseudoColumn(final int pseudoColumn) {
            return _IntFieldEnum.valueOfFieldValue(PseudoColumnEnum.class, pseudoColumn);
        }

        PseudoColumnEnum(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public void setColumnName(final String columnName) {
        this.columnName = columnName;
        columnId = null;
    }

    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
    ColumnId getColumnId(final TableId tableId) {
        Objects.requireNonNull(tableId, "tableId is null");
        if (columnId == null) {
            columnId = ColumnId.of(
                    tableId,
                    columnName
            );
        }
        return columnId;
    }

    ColumnId getColumnId(final Table table) {
        Objects.requireNonNull(table, "table is null");
        return getColumnId(table.getTableId());
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient ColumnId columnId;
}
