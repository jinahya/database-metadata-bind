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
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.java.Log;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getBestRowIdentifier(String, String, String, int, boolean, Collection)
 */
@XmlRootElement
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Log
public class BestRowIdentifier
        implements MetadataType {

    private static final long serialVersionUID = -1512051574198028399L;

    /**
     * Constants for {@code SCOPE} column values of the result of
     * {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)} method.
     */
    @XmlEnum
    public enum Scope
            implements IntFieldEnum<Scope> {

        /**
         * Constant for {@link DatabaseMetaData#bestRowTemporary}({@value java.sql.DatabaseMetaData#bestRowTemporary}).
         */
        BEST_ROW_TEMPORARY(DatabaseMetaData.bestRowTemporary), // 0

        /**
         * Constant for
         * {@link DatabaseMetaData#bestRowTransaction}({@value java.sql.DatabaseMetaData#bestRowTransaction}).
         */
        BEST_ROW_TRANSACTION(DatabaseMetaData.bestRowTransaction), // 1

        /**
         * Constant for {@link DatabaseMetaData#bestRowSession}({@value java.sql.DatabaseMetaData#bestRowSession}).
         */
        BEST_ROW_SESSION(DatabaseMetaData.bestRowSession); // 2

        /**
         * Returns the constant whose raw value equals to given.
         *
         * @param rawValue the raw value to compare
         * @return the constant whose raw value equals to given.
         */
        public static Scope valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(Scope.class, rawValue);
        }

        Scope(final int value) {
            this.rawValue = value;
        }

        @Override
        public int rawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    /**
     * Constants for {@code PSEUDO_COLUMN} column values of a result of
     * {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)} method.
     */
    @XmlEnum
    public enum PseudoColumn
            implements IntFieldEnum<PseudoColumn> {

        /**
         * Constant for {@link DatabaseMetaData#bestRowUnknown}({@value java.sql.DatabaseMetaData#bestRowUnknown}).
         */
        BEST_ROW_UNKNOWN(DatabaseMetaData.bestRowUnknown), // 0

        /**
         * Constant for {@link DatabaseMetaData#bestRowNotPseudo}({@value java.sql.DatabaseMetaData#bestRowNotPseudo}).
         */
        BEST_ROW_NOT_PSEUDO(DatabaseMetaData.bestRowNotPseudo), // 1

        /**
         * Constant for {@link DatabaseMetaData#bestRowPseudo}({@value java.sql.DatabaseMetaData#bestRowPseudo}).
         */
        BEST_ROW_PSEUDO(DatabaseMetaData.bestRowPseudo); // 2

        /**
         * Returns the constant whose raw value equals to specified value.
         *
         * @param rawValue the raw value.
         * @return the constant whose raw value equals to the {@code rawValue}.
         */
        public static PseudoColumn valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(PseudoColumn.class, rawValue);
        }

        PseudoColumn(final int rawValue) {
            this.rawValue = rawValue;
        }

        @Override
        public int rawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    @NotNull
    public Scope getScopeAsEnum() {
        return Scope.valueOfRawValue(getScope());
    }

    public void setScopeAsEnum(@NotNull final Scope scopeAsEnum) {
        Objects.requireNonNull(scopeAsEnum, "scopeAsEnum is null");
        setScope(scopeAsEnum.rawValue());
    }

    @NotNull
    public PseudoColumn getPseudoColumnAsEnum() {
        return PseudoColumn.valueOfRawValue(getPseudoColumn());
    }

    public void setPseudoColumnAsEnum(@NotNull final PseudoColumn pseudoColumnAsEnum) {
        Objects.requireNonNull(pseudoColumnAsEnum, "pseudoColumnAsEnum is null");
        setPseudoColumn(pseudoColumnAsEnum.rawValue());
    }

    @XmlElement(nillable = false, required = true)
    @Label("SCOPE")
    private int scope;

    @NotBlank
    @XmlElement(nillable = false, required = true)
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(nillable = false, required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(nillable = false, required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification // > Null is returned for data types where the column size is not applicable.
    @Label("COLUMN_SIZE")
    private Integer columnSize;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @Label("BUFFER_LENGTH")
    private Integer bufferLength;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement(nillable = false, required = true)
    @Label("PSEUDO_COLUMN")
    private int pseudoColumn;
}
