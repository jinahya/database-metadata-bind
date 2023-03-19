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
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of the {@link DatabaseMetaData#getVersionColumns(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getVersionColumns(String, String, String)
 */
@Setter
@Getter
public class VersionColumn extends AbstractMetadataType {

    private static final long serialVersionUID = 3587959398829593292L;

    public static final String COLUMN_LABEL_PSEUDO_COLUMN = "PSEUDO_COLUMN";

    /**
     * Constants for {@value #COLUMN_LABEL_PSEUDO_COLUMN} column values.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum PseudoColumn implements _IntFieldEnum<PseudoColumn> {

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
        public static PseudoColumn valueOfPseudoColumn(final int pseudoColumn) {
            return _IntFieldEnum.valueOfFieldValue(PseudoColumn.class, pseudoColumn);
        }

        PseudoColumn(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "scope=" + scope +
               ",columnName=" + columnName +
               ",dataType=" + dataType +
               ",typeName=" + typeName +
               ",columnSize=" + columnSize +
               ",bufferLength=" + bufferLength +
               ",decimalDigits=" + decimalDigits +
               ",pseudoColumn=" + pseudoColumn +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof VersionColumn)) return false;
        final VersionColumn that = (VersionColumn) obj;
        return Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName);
    }

    @_NotUsedBySpecification
    @_ColumnLabel("SCOPE")
    private Integer scope;

    @_ColumnLabel("COLUMN_NAME")
    private String columnName;

    @_NotNull
    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @_NullableBySpecification
    @_ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @_NullableByVendor("PostgreSQL")
    @_NotNull
    @_ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @_NullableBySpecification
    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @_NotNull
    @_ColumnLabel(COLUMN_LABEL_PSEUDO_COLUMN)
    private Integer pseudoColumn;

    PseudoColumn getPseudoColumnAsEnum() {
        return Optional.ofNullable(getPseudoColumn())
                .map(PseudoColumn::valueOfPseudoColumn)
                .orElse(null);
    }

    void setPseudoColumnAsEnum(final PseudoColumn pseudoColumnAsEnum) {
        setPseudoColumn(
                Optional.ofNullable(pseudoColumnAsEnum)
                        .map(PseudoColumn::fieldValueAsInt)
                        .orElse(null)
        );
    }
}
