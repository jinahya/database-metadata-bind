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


import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class TILBoolean {


    private static final Logger logger = getLogger(TILBoolean.class.getName());


    static TILBoolean valueOf(final Object[] args, final Object value) {

        final TILBoolean instance = new TILBoolean();

        instance.setLevel((Integer) args[0]);
        instance.setValue((Boolean) value);

        return instance;
    }


    // ------------------------------------------------------------------- level
    public int getLevel() {

        return level;
    }


    public void setLevel(final int level) {

        this.level = level;
    }


    TILBoolean level(final int level) {

        setLevel(level);

        return this;
    }


    // ------------------------------------------------------------------- value
    public boolean getValue() {

        return value;
    }


    public void setValue(final boolean value) {

        this.value = value;
    }


    TILBoolean value(final boolean value) {

        setValue(value);

        return this;
    }


    // -------------------------------------------------------------------- name
    @XmlAttribute
    public String getName() {

        try {
            return TIL.valueOf(level).name();
        } catch (final IllegalArgumentException iae) {
            logger.log(Level.WARNING, "unknown transacion isolation level: {0}",
                       new Object[]{level});
        }

        return null;
    }


    // -------------------------------------------------------------------------
    @XmlAttribute
    private int level;


    @XmlValue
    private boolean value;

}

