/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class UnknownResult {


    @Override
    public String toString() {

        return super.toString() + "{"
               + "label=" + label
               + ", value=" + value
               + "}";
    }


    // ------------------------------------------------------------------- label
    public String getLabel() {

        return label;
    }


    public void setLabel(String label) {

        this.label = label;
    }


    UnknownResult label(final String label) {

        setLabel(label);

        return this;
    }


    // ------------------------------------------------------------------- value
    public Object getValue() {

        return value;
    }


    public void setValue(Object value) {

        this.value = value;
    }


    UnknownResult value(final Object value) {

        setValue(value);

        return this;
    }


    @XmlValue
    public String getValueString() {

        return value == null ? null : value.toString();
    }


    // -------------------------------------------------------------------------
    @XmlAttribute
    private String label;


    private Object value;

}

