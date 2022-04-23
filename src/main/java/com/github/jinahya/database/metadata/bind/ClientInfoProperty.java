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
 * A class for binding results of {@link DatabaseMetaData#getClientInfoProperties()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getClientInfoProperties(Collection)
 */
@XmlRootElement
public class ClientInfoProperty
        implements MetadataType {

    private static final long serialVersionUID = -2913230435651853254L;

    /**
     * Creates a new instance.
     */
    public ClientInfoProperty() {
        super();
    }

    @Override
    public String toString() {
        return super.toString() + '{'
               + "name=" + name
               + ",maxLen=" + maxLen
               + ",defaultValue=" + defaultValue
               + ",description=" + description
               + '}';
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
