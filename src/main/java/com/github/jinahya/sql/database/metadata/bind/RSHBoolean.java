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
 * Constants for Holdabilities of ResultSets.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class RSHBoolean {

    private static final Logger logger = getLogger(RSHBoolean.class.getName());

    static RSHBoolean valueOf(final Object[] args, final Object value) {
        final RSHBoolean instance = new RSHBoolean();
        instance.setHoldability((Integer) args[0]);
        instance.setValue((Boolean) value);
        return instance;
    }

    // ------------------------------------------------------------- holdability
    public int getHoldability() {
        return holdability;
    }

    public void setHoldability(final int holdability) {
        this.holdability = holdability;
    }

    RSHBoolean holdability(final int holdability) {
        setHoldability(holdability);
        return this;
    }

    @XmlAttribute
    public String getHoldabilityName() {
        try {
            return RSH.valueOf(holdability).name();
        } catch (final IllegalArgumentException iae) {
            logger.log(Level.WARNING, "unknown result set holdability: {0}",
                       new Object[]{holdability});
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

    RSHBoolean value(final boolean value) {
        setValue(value);
        return this;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    private int holdability;

    @XmlValue
    private boolean value;
}
