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

import javax.validation.constraints.AssertTrue;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Objects;

/**
 * Represents best row identifiers of tables.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)
 */
@XmlRootElement
public class BestRowIdentifier implements MetadataType {

    private static final long serialVersionUID = -6733770602373723371L;

    /**
     * Constants for the value of {@code PSEUDO_COLUMN} of best row identifies of a table.
     *
     * @see DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)
     */
    public enum PseudoColumn implements IntFieldEnum<PseudoColumn> {

        /**
         * Constant for {@link DatabaseMetaData#bestRowUnknown}.
         */
        BEST_ROW_UNKNOWN(DatabaseMetaData.bestRowUnknown),

        /**
         * Constant for {@link DatabaseMetaData#bestRowNotPseudo}.
         */
        BEST_ROW_NOT_PSEUDO(DatabaseMetaData.bestRowNotPseudo),

        /**
         * Constant for {@link DatabaseMetaData#bestRowPseudo}.
         */
        BEST_ROW_PSEUDO(DatabaseMetaData.bestRowPseudo);

        /**
         * Returns the constant whose raw value equals to given.
         *
         * @param rawValue the raw value
         * @return the constant whose raw value equals to given.
         */
        public static PseudoColumn valueOf(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(PseudoColumn.class, rawValue);
        }

        PseudoColumn(final int rawValue) {
            this.rawValue = rawValue;
        }

        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    /**
     * Constants for best row identifiers' scope.
     *
     * @see DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)
     */
    public enum Scope implements IntFieldEnum<Scope> {

        /**
         * Constant for {@link DatabaseMetaData#bestRowTemporary}.
         */
        BEST_ROW_TEMPORARY(DatabaseMetaData.bestRowTemporary),

        /**
         * Constant for {@link DatabaseMetaData#bestRowTransaction}.
         */
        BEST_ROW_TRANSACTION(DatabaseMetaData.bestRowTransaction),

        /**
         * Constant for {@link DatabaseMetaData#bestRowSession}.
         */
        BEST_ROW_SESSION(DatabaseMetaData.bestRowSession);

        /**
         * Returns the constant whose raw value equals to given.
         *
         * @param rawValue the raw value to compare
         * @return the constant whose raw value equals to given.
         */
        public static Scope valueOf(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(Scope.class, rawValue);
        }

        Scope(final int value) {
            this.rawValue = value;
        }

        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    /**
     * Creates a new instance.
     */
    public BestRowIdentifier() {
        super();
    }

    @Override
    public String toString() {
        return super.toString() + '{'
               + "scope=" + scope
               + ",columnName=" + columnName
               + ",dataType=" + dataType
               + ",typeName=" + typeName
               + ",columnSize=" + columnSize
               + ",bufferLength=" + bufferLength
               + ",decimalDigits=" + decimalDigits
               + ",pseudoColumn=" + pseudoColumn
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final BestRowIdentifier that = (BestRowIdentifier) obj;
        return scope == that.scope
               && dataType == that.dataType
               && columnSize == that.columnSize
               && pseudoColumn == that.pseudoColumn
               && Objects.equals(columnName, that.columnName)
               && Objects.equals(typeName, that.typeName)
               && Objects.equals(bufferLength, that.bufferLength)
               && Objects.equals(decimalDigits, that.decimalDigits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                scope,
                columnName,
                dataType,
                typeName,
                columnSize,
                bufferLength,
                decimalDigits,
                pseudoColumn);
    }

    // ------------------------------------------------------------------------------------------------- Bean-Validation
    @AssertTrue
    private boolean isScopeValid() {
        return scope == DatabaseMetaData.bestRowTemporary
               || scope == DatabaseMetaData.bestRowTransaction
               || scope == DatabaseMetaData.bestRowSession;
    }

    // ----------------------------------------------------------------------------------------------------------- scope
    public short getScope() {
        return scope;
    }

    public void setScope(final short scope) {
        this.scope = scope;
    }

    public void setScope(final int scope) {
        setScope((short) scope);
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // -------------------------------------------------------------------------------------------------------- dataType
    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------ columnSize
    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(final int columnSize) {
        this.columnSize = columnSize;
    }

    // ---------------------------------------------------------------------------------------------------- bufferLength
    public Integer getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(final Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    // --------------------------------------------------------------------------------------------------- decimalDigits
    public Short getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(final Short decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ---------------------------------------------------------------------------------------------------- pseudoColumn
    public short getPseudoColumn() {
        return pseudoColumn;
    }

    public void setPseudoColumn(final short pseudoColumn) {
        this.pseudoColumn = pseudoColumn;
    }

    public void setPseudoColumn(final int pseudoColumn) {
        setPseudoColumn((short) pseudoColumn);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("SCOPE")
    private short scope;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(required = true)
    @Label("COLUMN_SIZE")
    private int columnSize;

    @XmlElement(required = true, nillable = true)
    @Unused
    @Label("BUFFER_LENGTH")
    private Integer bufferLength;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("DECIMAL_DIGITS")
    private Short decimalDigits;

    @XmlElement(required = true)
    @Label("PSEUDO_COLUMN")
    private short pseudoColumn;
}
