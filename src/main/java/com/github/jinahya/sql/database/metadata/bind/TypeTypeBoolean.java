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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class TypeTypeBoolean {


    static TypeTypeBoolean valueOf(final Object[] args,
                                   final Object value) {

        final TypeTypeBoolean instance = new TypeTypeBoolean();

        instance.setFromType((Integer) args[0]);
        instance.setToType((Integer) args[0]);
        instance.setValue((Boolean) value);

        return instance;
    }


    // ---------------------------------------------------------------- fromType
    public Integer getFromType() {

        return fromType;
    }


    public void setFromType(final Integer fromType) {

        this.fromType = fromType;
    }


    TypeTypeBoolean fromType(final Integer fromType) {

        setFromType(fromType);

        return this;
    }


    // ------------------------------------------------------------------ toType
    public Integer getToType() {

        return toType;
    }


    public void setToType(final Integer toType) {

        this.toType = toType;
    }


    TypeTypeBoolean toType(final Integer toType) {

        setToType(toType);

        return this;
    }


    // ------------------------------------------------------------------- value
    public boolean getValue() {

        return value;
    }


    public void setValue(final boolean value) {

        this.value = value;
    }


    TypeTypeBoolean value(final boolean value) {

        setValue(value);

        return this;
    }


    // -------------------------------------------------------------------------
    @XmlAttribute
    private Integer fromType;


    @XmlAttribute
    private Integer toType;


    @XmlValue
    private boolean value;


}

