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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "name", "maxLen", "defaultValue", "description"
    }
)
public class ClientInfoProperty {


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

        return metadata;
    }


    public void setMetadata(final Metadata metadata) {

        this.metadata = metadata;
    }


    /**
     * The name of the client info property.
     */
    @Label("NAME")
    @XmlElement(required = true)
    String name;


    /**
     * The maximum length of the value for the property.
     */
    @Label("MAX_LEN")
    @XmlElement(required = true)
    int maxLen;


    /**
     * The default value of the property.
     */
    @Label("DEFAULT_VALUE")
    @XmlElement(required = true)
    String defaultValue;


    /**
     * A description of the property. This will typically contain information as
     * to where this property is stored in the database.
     */
    @Label("DESCRIPTION")
    @XmlElement(required = true)
    String description;


    /**
     * parent metadata.
     */
    @XmlTransient
    private Metadata metadata;


}

