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

import jakarta.annotation.Nullable;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * A class for binding results of the {@link DatabaseMetaData#getVersionColumns(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getVersionColumns(String, String, String)
 */
@XmlRootElement
@_ChildOf(Table.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class VersionColumn extends AbstractMetadataType {

    private static final long serialVersionUID = 3587959398829593292L;

    // -----------------------------------------------------------------------------------------------------------------
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
         * @param fieldValue the value of {@link VersionColumn#COLUMN_LABEL_PSEUDO_COLUMN} column to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static PseudoColumn valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(PseudoColumn.class, fieldValue);
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

    // -----------------------------------------------------------------------------------------------------------------
    static final BiPredicate<VersionColumn, Table> IS_OF = (c, t) -> {
        return Objects.equals(c.parent, t);
    };

    // -----------------------------------------------------------------------------------------------------------------
    PseudoColumn getPseudoColumnAsEnum() {
        return Optional.ofNullable(getPseudoColumn())
                .map(PseudoColumn::valueOfFieldValue)
                .orElse(null);
    }

    void setPseudoColumnAsEnum(final PseudoColumn pseudoColumnAsEnum) {
        setPseudoColumn(
                Optional.ofNullable(pseudoColumnAsEnum)
                        .map(PseudoColumn::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Include
    private Table parent;

    // -----------------------------------------------------------------------------------------------------------------
    @_NotUsedBySpecification
    @_ColumnLabel("SCOPE")
    private Integer scope;

    @_ColumnLabel("COLUMN_NAME")
    @EqualsAndHashCode.Include
    private String columnName;

    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @_ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_PSEUDO_COLUMN)
    private Integer pseudoColumn;
}
