/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
 *
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
 */


package com.github.jinahya.sql.database.metadata.bind;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getClientInfoProperties()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "name", "maxLen", "defaultValue", "description"
    }
)
public class ClientInfoProperty extends AbstractChild<Metadata> {


    @Override
    public String toString() {

        return super.toString() + "{"
               + "name=" + name
               + ", maxLen=" + maxLen
               + ", defaultValue=" + defaultValue
               + ", description=" + description
               + "}";
    }


    // -------------------------------------------------------------------- name
    public String getName() {

        return name;
    }


    public void setName(final String name) {

        this.name = name;
    }


    // ------------------------------------------------------------------ maxLen
    public int getMaxLen() {

        return maxLen;
    }


    public void setMaxLen(final int maxLen) {

        this.maxLen = maxLen;
    }


    // ------------------------------------------------------------ defaultValue
    public String getDefaultValue() {

        return defaultValue;
    }


    public void setDefaultValue(final String defaultValue) {

        this.defaultValue = defaultValue;
    }


    // ------------------------------------------------------------- description
    public String getDescription() {

        return description;
    }


    public void setDescription(final String description) {

        this.description = description;
    }


    // ---------------------------------------------------------------- metadata
    public Metadata getMetadata() {

        return getParent();
    }


    public void setMetadata(final Metadata metadata) {

        setParent(metadata);
    }


    // -------------------------------------------------------------------------
    @Label("NAME")
    @XmlElement(required = true)
    private String name;


    @Label("MAX_LEN")
    @XmlElement(required = true)
    private int maxLen;


    @Label("DEFAULT_VALUE")
    @XmlElement(required = true)
    private String defaultValue;


    @Label("DESCRIPTION")
    @XmlElement(required = true)
    private String description;


}

