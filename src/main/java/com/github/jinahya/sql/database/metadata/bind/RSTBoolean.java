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
class RSTBoolean {

    private static final Logger logger = getLogger(RSTBoolean.class.getName());

    static RSTBoolean valueOf(final Object[] args, final Object value) {
        final RSTBoolean instance = new RSTBoolean();
        instance.setType((Integer) args[0]);
        instance.setValue((Boolean) value);
        return instance;
    }

    // -------------------------------------------------------------------- type
    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    RSTBoolean type(final int type) {
        setType(type);
        return this;
    }

    @XmlAttribute
    public String getTypeName() {
        try {
            return RST.valueOf(type).name();
        } catch (final IllegalArgumentException iae) {
            logger.log(Level.WARNING, "unknown result set type: {0}",
                       new Object[]{type});
        }
        return null;
    }

    // ------------------------------------------------------------------- value
    public boolean getValue() {
        return value;
    }

    public void setValue(final boolean value) {
        this.value = value;
    }

    RSTBoolean value(final boolean value) {
        setValue(value);
        return this;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    private int type;

    @XmlValue
    private boolean value;
}
