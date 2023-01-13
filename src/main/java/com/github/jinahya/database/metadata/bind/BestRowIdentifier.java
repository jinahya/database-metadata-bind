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

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
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
    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @SuperBuilder(toBuilder = true)
    static class BestRowIdentifierCategory
            implements Serializable {

        private static final long serialVersionUID = 4793328436607858329L;

        static BestRowIdentifierCategory of(final int scope, final boolean nullable) {
            return builder()
                    .scope(scope)
                    .nullable(nullable)
                    .build();
        }

        private int scope;

        private boolean nullable;
    }

    /**
     * A class for wrapping categorized instances of {@link BestRowIdentifier}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
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

        public int getScope() {
            return Objects.requireNonNull(category, "category is null").getScope();
        }

        public void setScope(final int scope) {
            Optional.ofNullable(category)
                    .orElseGet(() -> (category = new BestRowIdentifierCategory()))
                    .setScope(scope);
        }

        public boolean isNullable() {
            return Objects.requireNonNull(category, "category is null").isNullable();
        }

        public void setNullable(final boolean nullable) {
            Optional.ofNullable(category)
                    .orElseGet(() -> (category = new BestRowIdentifierCategory()))
                    .setNullable(nullable);
        }

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

        @Setter(AccessLevel.PACKAGE)
        @Getter(AccessLevel.PACKAGE)
        private BestRowIdentifierCategory category;

        @Setter(AccessLevel.NONE)
        @Getter(AccessLevel.NONE)
        private List<BestRowIdentifier> bestRowIdentifiers;
    }

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    @ColumnLabel("SCOPE")
    private int scope;

    @ColumnLabel("COLUMN_NAME")
    private String columnName;

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @NullableBySpecification // > Null is returned for data types where the column size is not applicable.
    @ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @NotUsedBySpecification
    @ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @NullableBySpecification
    @ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

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
