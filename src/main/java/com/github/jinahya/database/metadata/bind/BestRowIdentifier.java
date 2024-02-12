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
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getBestRowIdentifier(String, String, String, int, boolean)
 * @see PseudoColumn
 * @see Scope
 */
@XmlRootElement
@Setter
@Getter
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class BestRowIdentifier
        extends AbstractMetadataType
        implements Comparable<BestRowIdentifier> {

    private static final long serialVersionUID = -1512051574198028399L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A comparator compares objects with their value of {@link #getScope()}.
     */
    static final Comparator<BestRowIdentifier> COMPARING_SCOPE = Comparator.comparingInt(BestRowIdentifier::getScope);

    // ----------------------------------------------------------------------------------------------------------- SCOPE
    public static final String COLUMN_LABEL_SCOPE = "SCOPE";

    /**
     * Constants for {@value #COLUMN_LABEL_SCOPE} column values.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum Scope implements _IntFieldEnum<Scope> {

        /**
         * A value for {@link DatabaseMetaData#bestRowTemporary}({@value DatabaseMetaData#bestRowTemporary}).
         */
        BEST_ROW_TEMPORARY(DatabaseMetaData.bestRowTemporary),// 0

        /**
         * A value for {@link DatabaseMetaData#bestRowTransaction}({@value DatabaseMetaData#bestRowTransaction}).
         */
        BEST_ROW_TRANSACTION(DatabaseMetaData.bestRowTransaction), // 1

        /**
         * A value for {@link DatabaseMetaData#bestRowSession}({@value DatabaseMetaData#bestRowSession}).
         */
        BEST_ROW_SESSION(DatabaseMetaData.bestRowSession) // 2
        ;

        /**
         * Finds the value for specified {@link BestRowIdentifier#COLUMN_LABEL_SCOPE} column value.
         *
         * @param fieldValue the value of {@link BestRowIdentifier#COLUMN_LABEL_SCOPE} column to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static Scope valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(Scope.class, fieldValue);
        }

        Scope(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // ----------------------------------------------------------------------------------------------------- COLUMN_NAME
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // ------------------------------------------------------------------------------------------------------- DATA_TYPE
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // --------------------------------------------------------------------------------------------------- PSEUDO_COLUMN
    public static final String COLUMN_LABEL_PSEUDO_COLUMN = "PSEUDO_COLUMN";

    /**
     * Constants for {@value #COLUMN_LABEL_PSEUDO_COLUMN} column values.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum PseudoColumn implements _IntFieldEnum<PseudoColumn> {

        /**
         * A value for {@link DatabaseMetaData#bestRowUnknown}({@value DatabaseMetaData#bestRowUnknown}).
         */
        BEST_ROW_UNKNOWN(DatabaseMetaData.bestRowUnknown),// 0

        /**
         * A value for {@link DatabaseMetaData#bestRowNotPseudo}({@value DatabaseMetaData#bestRowNotPseudo}).
         */
        BEST_ROW_NOT_PSEUDO(DatabaseMetaData.bestRowNotPseudo), // 1

        /**
         * A value for {@link DatabaseMetaData#bestRowPseudo}({@value DatabaseMetaData#bestRowPseudo}).
         */
        BEST_ROW_PSEUDO(DatabaseMetaData.bestRowPseudo) // 2
        ;

        /**
         * Finds the value for specified {@link BestRowIdentifier#COLUMN_LABEL_PSEUDO_COLUMN} column value.
         *
         * @param fieldValue the {@link BestRowIdentifier#COLUMN_LABEL_PSEUDO_COLUMN} column value to match.
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
    static final BiPredicate<BestRowIdentifier, Table> IS_OF = (i, t) -> {
        return Objects.equals(i.parent, t);
    };

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        final BestRowIdentifier that = (BestRowIdentifier) obj;
        return Objects.equals(parent, that.parent) &&
               Objects.equals(scope, that.scope) &&
               Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parent, scope, columnName);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int compareTo(final BestRowIdentifier o) {
        if (o == null) {
            throw new NullPointerException("o is null");
        }
        return COMPARING_SCOPE.compare(this, o);
    }

    // ----------------------------------------------------------------------------------------------------------- scope
    Scope getScopeAsEnum() {
        return Optional.ofNullable(getScope())
                .map(Scope::valueOfFieldValue)
                .orElse(null);
    }

    void setScopeAsEnum(final Scope scopeAsEnum) {
        setScope(
                Optional.ofNullable(scopeAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // ---------------------------------------------------------------------------------------------------- pseudoColumn
    PseudoColumn getPseudoColumnAsEnum() {
        return Optional.ofNullable(getPseudoColumn())
                .map(PseudoColumn::valueOfFieldValue)
                .orElse(null);
    }

    void setPseudoColumnAsEnum(final PseudoColumn pseudoColumnAsEnum) {
        setPseudoColumn(
                Optional.ofNullable(pseudoColumnAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonbTransient
    @XmlTransient
    @Setter(AccessLevel.PACKAGE)
    @EqualsAndHashCode.Include
    private Table parent;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_SCOPE)
    @EqualsAndHashCode.Include
    private Integer scope;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    @EqualsAndHashCode.Include
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @_NotUsedBySpecification
    @_ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_PSEUDO_COLUMN)
    private Integer pseudoColumn;
}
