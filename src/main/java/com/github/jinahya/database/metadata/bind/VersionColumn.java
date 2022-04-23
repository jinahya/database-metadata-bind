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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Collection;

/**
 * A class for binding results of {@link DatabaseMetaData#getVersionColumns(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getVersionColumns(String, String, String, Collection)
 */
@XmlRootElement
@ChildOf(Table.class)
public class VersionColumn
        implements MetadataType {

    private static final long serialVersionUID = 3587959398829593292L;

    /**
     * Constants for pseudo column values of version columns.
     *
     * @see Context#getVersionColumns(String, String, String, Collection)
     */
    public enum PseudoColumn
            implements IntFieldEnum<PseudoColumn> {

        /**
         * Constant for {@link DatabaseMetaData#versionColumnUnknown} whose value is
         * {@value DatabaseMetaData#versionColumnUnknown}.
         */
        VERSION_COLUMN_NO_NULLS(DatabaseMetaData.versionColumnUnknown),

        /**
         * Constant for {@link DatabaseMetaData#versionColumnNotPseudo} whose value is
         * {@value DatabaseMetaData#versionColumnNotPseudo}.
         */
        VERSION_COLUMN_NULLABLE(DatabaseMetaData.versionColumnNotPseudo),

        /**
         * Constant for {@link DatabaseMetaData#versionColumnPseudo} whose value is
         * {@value DatabaseMetaData#versionColumnPseudo}.
         */
        VERSION_COLUMN_NULLABLE_UNKNOWN(DatabaseMetaData.versionColumnPseudo);

        /**
         * Returns the constant whose raw value equals to given. An instance of {@link IllegalArgumentException} will be
         * throw if no constants matches.
         *
         * @param rawValue the value value
         * @return the constant whose raw value equals to given.
         */
        public static PseudoColumn valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(PseudoColumn.class, rawValue);
        }

        /**
         * Creates a new instance with specified raw value.
         *
         * @param rawValue the raw value.
         */
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
     * Creates a new instance.
     */
    public VersionColumn() {
        super();
    }

    @Override
    public String toString() {
        return super.toString() + '{'
               + "scope=" + scope
               + ",columnName='" + columnName
               + ",dataType=" + dataType
               + ",typeName='" + typeName
               + ",columnSize=" + columnSize
               + ",bufferLength=" + bufferLength
               + ",decimalDigits=" + decimalDigits
               + ",pseudoColumn=" + pseudoColumn
               + '}';
    }

    public Short getScope() {
        return scope;
    }

    public void setScope(final Short scope) {
        this.scope = scope;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(final int columnSize) {
        this.columnSize = columnSize;
    }

    public int getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(final int bufferLength) {
        this.bufferLength = bufferLength;
    }

    public Short getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(final Short decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public short getPseudoColumn() {
        return pseudoColumn;
    }

    public void setPseudoColumn(final short pseudoColumn) {
        this.pseudoColumn = pseudoColumn;
    }

    @XmlElement(required = true, nillable = true)
    @Unused
    @Label("SCOPE")
    private Short scope;

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

    @XmlElement(required = true)
    @Label("BUFFER_LENGTH")
    private int bufferLength;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("DECIMAL_DIGITS")
    private Short decimalDigits;

    @XmlElement(required = true)
    @Label("PSEUDO_COLUMN")
    private short pseudoColumn;
}
