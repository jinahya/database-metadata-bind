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


import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class DeletesAreDetected {


    static final List<Integer> TYPES = Arrays.asList(
        ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.TYPE_SCROLL_SENSITIVE);


    static DeletesAreDetected valueOf(final Object[] args, final Object value) {

        return new DeletesAreDetected()
            .type((Integer) args[0])
            .value((Boolean) value);
    }


    // -------------------------------------------------------------------- type
    public int getType() {

        return type;
    }


    public void setType(final int type) {

        this.type = type;
    }


    DeletesAreDetected type(final int type) {

        setType(type);

        return this;
    }


    // ------------------------------------------------------------------- value
    public boolean getValue() {

        return value;
    }


    public void setValue(final boolean value) {

        this.value = value;
    }


    DeletesAreDetected value(final boolean value) {

        setValue(value);

        return this;
    }


    // -------------------------------------------------------------------------
    @XmlAttribute
    private int type;


    @XmlValue
    private boolean value;


}

