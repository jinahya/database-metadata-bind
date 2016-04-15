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
class RSCBoolean {

    static RSCBoolean valueOf(final Object[] args, final Object value) {
        final RSCBoolean instance = new RSCBoolean();
        instance.setConcurrency((Integer) args[0]);
        instance.setValue((Boolean) value);
        return instance;
    }

    // ------------------------------------------------------------- concurrency
    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(final int concurrency) {
        this.concurrency = concurrency;
    }

    RSCBoolean concurrency(final int concurrency) {
        setConcurrency(concurrency);
        return this;
    }

    // ------------------------------------------------------------------- value
    public boolean getValue() {
        return value;
    }

    public void setValue(final boolean value) {
        this.value = value;
    }

    RSCBoolean value(final boolean value) {
        setValue(value);
        return this;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    private int concurrency;

    @XmlValue
    private boolean value;
}
