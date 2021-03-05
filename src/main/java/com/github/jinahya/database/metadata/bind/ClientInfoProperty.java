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
import java.util.Objects;

/**
 * An entity class for client info properties.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class ClientInfoProperty implements MetadataType {

    private static final long serialVersionUID = -2913230435651853254L;

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "name=" + name
               + ",maxLen=" + maxLen
               + ",defaultValue=" + defaultValue
               + ",description=" + description
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final ClientInfoProperty that = (ClientInfoProperty) obj;
        return maxLen == that.maxLen
               && Objects.equals(name, that.name)
               && Objects.equals(defaultValue, that.defaultValue)
               && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,
                            maxLen,
                            defaultValue,
                            description);
    }

    // ------------------------------------------------------------------------------------------------------------ name
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    // ---------------------------------------------------------------------------------------------------------- maxLen
    public int getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(final int maxLen) {
        this.maxLen = maxLen;
    }

    // ---------------------------------------------------------------------------------------------------- defaultValue
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    // ----------------------------------------------------------------------------------------------------- description
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("NAME")
    private String name;

    @XmlElement(required = true)
    @Label("MAX_LEN")
    private int maxLen;

    @XmlElement(required = true)
    @Label("DEFAULT_VALUE")
    private String defaultValue;

    @XmlElement(required = true)
    @Label("DESCRIPTION")
    private String description;
}
