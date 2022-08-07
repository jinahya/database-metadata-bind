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

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getIndexInfo(String, String, String, boolean, boolean, Collection)
 */
@XmlRootElement
@ChildOf(Table.class)
@Data
public class IndexInfo
        implements MetadataType {

    private static final long serialVersionUID = -768486884376018474L;

    public static final Comparator<IndexInfo> COMPARATOR
            = Comparator.comparing(IndexInfo::isNonUnique)
            .thenComparing(IndexInfo::getType)
            .thenComparing(IndexInfo::getIndexName)
            .thenComparing(IndexInfo::getOrdinalPosition);

    public static final String COLUMN_NAME_TYPE = "TYPE";

    /**
     * Constants for {@value #COLUMN_NAME_TYPE} column values.
     */
    @XmlEnum
    public enum Type implements IntFieldEnum<Type> {

        /**
         * Constant for
         * {@link DatabaseMetaData#tableIndexStatistic}({@value java.sql.DatabaseMetaData#tableIndexStatistic}).
         */
        TABLE_INDEX_STATISTICS(DatabaseMetaData.tableIndexStatistic), // 0

        /**
         * Constant for
         * {@link DatabaseMetaData#tableIndexClustered}({@value java.sql.DatabaseMetaData#tableIndexClustered}).
         */
        TABLE_INDEX_CLUSTERED(DatabaseMetaData.tableIndexClustered), // 1

        /**
         * Constant for {@link DatabaseMetaData#tableIndexHashed}({@value java.sql.DatabaseMetaData#tableIndexHashed}).
         */
        TABLE_INDEX_HASHED(DatabaseMetaData.tableIndexHashed), // 2

        /**
         * Constant for {@link DatabaseMetaData#tableIndexOther}({@value java.sql.DatabaseMetaData#tableIndexOther}).
         */
        TABLE_INDEX_OTHER(DatabaseMetaData.tableIndexOther); // 3

        /**
         * Returns the constant whose raw value matches to specified value.
         *
         * @param rawValue the raw value.
         * @return the constant whose raw value matches to {@code rawValue}.
         * @throws IllegalArgumentException when no constant found for the {@code rawValue}.
         */
        public static Type valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(Type.class, rawValue);
        }

        Type(final int rawValue) {
            this.rawValue = rawValue;
        }

        @Override
        public int rawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    @AssertTrue
    private boolean isNonUniqueFalseWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return !isNonUnique();
    }

    @AssertTrue
    private boolean isIndexQualifierNullWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return getIndexQualifier() == null;
    }

    @AssertTrue
    private boolean isIndexNameNullWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return getIndexName() == null;
    }

    @AssertTrue
    private boolean isOrdinalPositionZeroWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return getOrdinalPosition() == 0;
    }

    @AssertTrue
    private boolean isColumnNameNullWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return getColumnName() == null;
    }

    @AssertTrue
    private boolean isAscOrDescNullWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return getAscOrDesc() == null;
    }

    public Type getTypeAsEnum() {
        return Type.valueOfRawValue(getType());
    }

    public void setTypeAsIndex(final Type typeAsIndex) {
        setType(Objects.requireNonNull(typeAsIndex, "typeAsIndex is null").rawValue());
    }

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label("TABLE_NAME")
    private String tableName;

    @XmlElement(nillable = false, required = true)
    @Label("NON_UNIQUE")
    private boolean nonUnique;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("INDEX_QUALIFIER")
    private String indexQualifier;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("INDEX_NAME")
    private String indexName;

    @XmlElement(nillable = false, required = true)
    @Label(COLUMN_NAME_TYPE)
    private int type;

    @XmlElement(nillable = false, required = true)
    @PositiveOrZero
    @Label("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("ASC_OR_DESC")
    private String ascOrDesc;

    @XmlElement(nillable = false, required = true)
    @Label("CARDINALITY")
    private long cardinality;

    @XmlElement(nillable = false, required = true)
    @Label("PAGES")
    private long pages;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("FILTER_CONDITION")
    private String filterCondition;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlTransient
    @Valid
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Table table;
}
