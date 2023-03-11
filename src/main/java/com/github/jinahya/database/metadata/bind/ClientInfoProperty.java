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

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link DatabaseMetaData#getClientInfoProperties()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getClientInfoProperties()
 */
public class ClientInfoProperty extends AbstractMetadataType {

    private static final long serialVersionUID = -2913230435651853254L;

    static final Comparator<ClientInfoProperty> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(ClientInfoProperty::getName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<ClientInfoProperty> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(ClientInfoProperty::getName, nullsFirst(naturalOrder()));

    public static final String COLUMN_LABEL_NAME = "NAME";

    public static final String COLUMN_LABEL_MAX_LEN = "MAX_LEN";

    public static final String COLUMN_LABEL_DEFAULT_VALUE = "DEFAULT_VALUE";

    public static final String COLUMN_LABEL_DESCRIPTION = "DESCRIPTION";

    @Override
    public String toString() {
        return super.toString() + '{' +
               "name=" + name +
               ",maxLen=" + maxLen +
               ",defaultValue=" + defaultValue +
               ",description=" + description +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ClientInfoProperty)) return false;
        final ClientInfoProperty that = (ClientInfoProperty) obj;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(final Integer maxLen) {
        this.maxLen = maxLen;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @_ColumnLabel(COLUMN_LABEL_NAME)
    private String name;

    @_NotNull
    @_ColumnLabel(COLUMN_LABEL_MAX_LEN)
    private Integer maxLen;

    @_ColumnLabel(COLUMN_LABEL_DEFAULT_VALUE)
    private String defaultValue;

    @_ColumnLabel(COLUMN_LABEL_DESCRIPTION)
    private String description;
}
