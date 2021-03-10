package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
 * %%
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
 * #L%
 */

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import java.lang.invoke.MethodHandles;
import java.sql.DatabaseMetaData;
import java.util.Objects;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#deletesAreDetected(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DeletesAreDetected
 * @see InsertsAreDetected
 * @see UpdatesAreDetected
 */
abstract class AreDetected<T extends AreDetected<T>> implements Comparable<T> {

    static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    protected AreDetected() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return super.toString() + '{'
               + "type=" + type
               + ",typeName=" + typeName
               + ",value=" + value
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final AreDetected<?> that = (AreDetected<?>) obj;
        return type == that.type
               && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public int compareTo(final T o) {
        requireNonNull(o, "o is null");
        return Integer.compare(type, o.getType());
    }

    // -----------------------------------------------------------------------------------------------------------------
    void setType(final ResultSetType type) {
        requireNonNull(type, "type is null");
        setType(type.getRawValue());
        setTypeName(type.name());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Boolean getValue() {
        return value;
    }

    public void setValue(final Boolean value) {
        this.value = value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute(required = true)
    private int type;

    @XmlAttribute
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlValue
    private Boolean value;
}