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
import lombok.extern.java.Log;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * A key class for categorizing best row identifiers by their {@code scope} and {@code nullable}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    @XmlRootElement
    @Setter
    @Getter
    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @SuperBuilder(toBuilder = true)
    public static class BestRowIdentifierCategory
            implements Serializable {

        private static final long serialVersionUID = 4793328436607858329L;

        static BestRowIdentifierCategory of(final int scope, final boolean nullable) {
            return builder()
                    .scope(scope)
                    .nullable(nullable)
                    .build();
        }

        static final Set<BestRowIdentifierCategory> _VALUES = Collections.unmodifiableSet(
                Arrays.stream(BestRowIdentifier.Scope.values())
                        .map(IntFieldEnum::rawValue)
                        .flatMap(s -> Stream.of(BestRowIdentifierCategory.of(s, false),
                                                BestRowIdentifierCategory.of(s, true)))
                        .collect(Collectors.toSet())
        );

        /**
         * Returns the value of {@code scope} attribute.
         *
         * @return the value of {@code scope} attribute.
         */
        public int getScope() {
            return scope;
        }

        public void setScope(final int scope) {
            this.scope = scope;
        }

        /**
         * Returns the value of {@code nullable} attribute.
         *
         * @return the value of {@code nullable} attribute.
         */
        public boolean isNullable() {
            return nullable;
        }

        public void setNullable(final boolean nullable) {
            this.nullable = nullable;
        }

        @XmlElement(nillable = false, required = true)
        private int scope;

        @XmlElement(nillable = false, required = true)
        private boolean nullable;
    }

    /**
     * A class for wrapping categorized instances of {@link BestRowIdentifier}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    @XmlRootElement
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @SuperBuilder(toBuilder = true)
    public static class CategorizedBestRowIdentifiers
            implements Serializable {

        private static final long serialVersionUID = -3993338410915583081L;

        static CategorizedBestRowIdentifiers of(BestRowIdentifierCategory category) {
            return builder()
                    .category(category)
                    .build();
        }

        public BestRowIdentifierCategory getCategory() {
            return category;
        }

        public void setCategory(final BestRowIdentifierCategory category) {
            this.category = category;
        }

        @NotNull
        public List<BestRowIdentifier> getBestRowIdentifiers() {
            if (bestRowIdentifiers == null) {
                bestRowIdentifiers = new ArrayList<>();
            }
            return bestRowIdentifiers;
        }

        @Deprecated
        public void setBestRowIdentifiers(final List<BestRowIdentifier> bestRowIdentifiers) {
            this.bestRowIdentifiers = bestRowIdentifiers;
        }

        @XmlElement(nillable = false, required = true)
        @Valid
        @NotNull
        @Setter(AccessLevel.NONE)
        @Getter(AccessLevel.NONE)
        private BestRowIdentifierCategory category;

        @XmlElementRef
        @Setter(AccessLevel.NONE)
        @Getter(AccessLevel.NONE)
        private List<@Valid @NotNull BestRowIdentifier> bestRowIdentifiers;
    }

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

    protected void setScopeAsEnum(@NotNull final Scope scopeAsEnum) {
        Objects.requireNonNull(scopeAsEnum, "scopeAsEnum is null");
        setScope(scopeAsEnum.rawValue());
    }

    @NotNull
    public PseudoColumn getPseudoColumnAsEnum() {
        return PseudoColumn.valueOfRawValue(getPseudoColumn());
    }

    protected void setPseudoColumnAsEnum(@NotNull final PseudoColumn pseudoColumnAsEnum) {
        Objects.requireNonNull(pseudoColumnAsEnum, "pseudoColumnAsEnum is null");
        setPseudoColumn(pseudoColumnAsEnum.rawValue());
    }

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("SCOPE")
    private int scope;

    @NotBlank
    @XmlElement(nillable = false, required = true)
    @ColumnLabel("COLUMN_NAME")
    private String columnName;

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification // > Null is returned for data types where the column size is not applicable.
    @ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("PSEUDO_COLUMN")
    private int pseudoColumn;

    // -----------------------------------------------------------------------------------------------------------------

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public Integer getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public int getPseudoColumn() {
        return pseudoColumn;
    }

    public void setPseudoColumn(int pseudoColumn) {
        this.pseudoColumn = pseudoColumn;
    }
}
