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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class SDTSDTBoolean {

    private static final Logger logger
            = getLogger(SDTSDTBoolean.class.getName());

    static SDTSDTBoolean valueOf(final Object[] args, final Object value) {
        final SDTSDTBoolean instance = new SDTSDTBoolean();
        instance.setFromType((Integer) args[0]);
        instance.setToType((Integer) args[1]);
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

    SDTSDTBoolean fromType(final Integer fromType) {
        setFromType(fromType);
        return this;
    }

    @XmlAttribute
    public String getFromTypeName() {
        if (fromType == null) {
            return null;
        }
        try {
            return Utils.sqlTypeName(fromType);
        } catch (final IllegalAccessException iae) {
            logger.log(Level.WARNING, "failed to get sql data type name: {0}",
                       new Object[]{fromType});
        }
        return null;
    }

    // ------------------------------------------------------------------ toType
    public Integer getToType() {
        return toType;
    }

    public void setToType(final Integer toType) {
        this.toType = toType;
    }

    SDTSDTBoolean toType(final Integer toType) {
        setToType(toType);
        return this;
    }

    @XmlAttribute
    public String getToTypeName() {
        if (toType == null) {
            return null;
        }
        try {
            return Utils.sqlTypeName(toType);
        } catch (final IllegalAccessException iae) {
            logger.log(Level.WARNING, "failed to get sql data type name: {0}",
                       new Object[]{toType});
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

    SDTSDTBoolean value(final boolean value) {
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
