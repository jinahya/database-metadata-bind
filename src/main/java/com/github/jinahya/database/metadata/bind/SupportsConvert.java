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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.lang.invoke.MethodHandles;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#supportsConvert(int, int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class SupportsConvert {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    static List<SupportsConvert> list(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        final List<SupportsConvert> list = new ArrayList<>();
        final List<JDBCType> types = Arrays.stream(JDBCType.values())
                .filter(v -> "java.sql".equals(v.getVendor()))
                .collect(Collectors.toList());
        for (final JDBCType fromType : types) {
            for (final JDBCType toType : types) {
                final SupportsConvert v = new SupportsConvert();
                v.fromType = fromType.getVendorTypeNumber();
                v.fromTypeName = fromType.getName();
                v.toType = toType.getVendorTypeNumber();
                v.toTypeName = toType.getName();
                try {
                    v.value = context.databaseMetaData.supportsConvert(v.fromType, v.toType);
                } catch (final SQLException sqle) {
                    logger.log(Level.WARNING, sqle,
                               () -> String.format("failed to invoke supportsConvert(%1$d, %2$d)", v.fromType,
                                                   v.toType));
                    if (!context.isSuppressed(sqle)) {
                        throw sqle;
                    }
                }
                list.add(v);
            }
        }
        return list;
    }

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

    public Boolean getValue() {
        return value;
    }

    public void setValue(final Boolean value) {
        this.value = value;
    }

    @XmlAttribute(required = true)
    private int fromType;

    @XmlAttribute(required = true)
    private String fromTypeName;

    @XmlAttribute(required = true)
    private int toType;

    @XmlAttribute(required = true)
    private String toTypeName;

    @XmlValue
    private Boolean value;
}
