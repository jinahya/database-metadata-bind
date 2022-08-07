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

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
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
        VERSION_COLUMN_NO_NULLS(DatabaseMetaData.versionColumnUnknown), // 0

        /**
         * Constant for {@link DatabaseMetaData#versionColumnNotPseudo} whose value is
         * {@value DatabaseMetaData#versionColumnNotPseudo}.
         */
        VERSION_COLUMN_NULLABLE(DatabaseMetaData.versionColumnNotPseudo), // 1

        /**
         * Constant for {@link DatabaseMetaData#versionColumnPseudo} whose value is
         * {@value DatabaseMetaData#versionColumnPseudo}.
         */
        VERSION_COLUMN_NULLABLE_UNKNOWN(DatabaseMetaData.versionColumnPseudo); // 2

        /**
         * Returns the constant whose raw value equals to specified raw value. An instance of
         * {@link IllegalArgumentException} will be thrown if no constants matches.
         *
         * @param rawValue the raw value.
         * @return the constant whose raw value equals to {@code rawValue}.
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

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @Label("SCOPE")
    private Integer scope;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(nillable = false, required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(nillable = false, required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("COLUMN_SIZE")
    private Integer columnSize;

    @XmlElement(nillable = false, required = true)
    @Label("BUFFER_LENGTH")
    private int bufferLength;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement(nillable = false, required = true)
    @Label("PSEUDO_COLUMN")
    private int pseudoColumn;
}
