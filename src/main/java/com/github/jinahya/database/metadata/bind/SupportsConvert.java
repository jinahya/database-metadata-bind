package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;
import java.lang.invoke.MethodHandles;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#supportsConvert(int, int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#supportsConvert(int, int)
 */
@XmlRootElement
public class SupportsConvert implements MetadataType {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link Context#supportsConvert(int, int)} method for all types defined in {@link JDBCType} and returns
     * bound values.
     *
     * @param context a context.
     * @return a list of bound values.
     * @throws SQLException if a database access error occurs.
     * @see Context#supportsConvert(int, int)
     */
    public static @NotEmpty List<@Valid @NotNull SupportsConvert> getAllInstances(final @NotNull Context context)
            throws SQLException {
        requireNonNull(context, "context is null");
        final List<SupportsConvert> all = new ArrayList<>();
        for (final JDBCType fromType : JDBCType.values()) {
            for (final JDBCType toType : JDBCType.values()) {
                all.add(context.supportsConvert(fromType, toType));
            }
        }
        return all;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public SupportsConvert() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return super.toString() + '{'
               + "fromType=" + fromType
               + ",fromTypeName=" + fromTypeName
               + ",toType=" + toType
               + ",toTypeName=" + toTypeName
               + ",value=" + value
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final SupportsConvert that = (SupportsConvert) obj;
        return fromType == that.fromType
               && toType == that.toType
               && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromType,
                            toType,
                            value);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Indicates whether {@code toType} is same as {@code fromType}.
     *
     * @return {@code true} if {@code toType} is same as {@code fromType}; {@code false} otherwise;
     */
    @XmlTransient
    public boolean isForSelfConversion() {
        return toType == fromType;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int getFromType() {
        return fromType;
    }

    public void setFromType(final int fromType) {
        this.fromType = fromType;
    }

    public String getFromTypeName() {
        return fromTypeName;
    }

    public void setFromTypeName(final String fromTypeName) {
        this.fromTypeName = fromTypeName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int getToType() {
        return toType;
    }

    public void setToType(final int toType) {
        this.toType = toType;
    }

    public String getToTypeName() {
        return toTypeName;
    }

    public void setToTypeName(final String toTypeName) {
        this.toTypeName = toTypeName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Boolean getValue() {
        return value;
    }

    public void setValue(final Boolean value) {
        this.value = value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute(required = true)
    private int fromType;

    @XmlAttribute
    private String fromTypeName;

    @XmlAttribute(required = true)
    private int toType;

    @XmlAttribute
    private String toTypeName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlValue
    private Boolean value;
}
