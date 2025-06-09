package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
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

import lombok.EqualsAndHashCode;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * An abstract class for binding results of the {@link DatabaseMetaData#getExportedKeys(String, String, String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getExportedKeys(String, String, String)
 * @see ImportedKey
 */
@EqualsAndHashCode(callSuper = true)
public class ExportedKey
        extends PortedKey {

    private static final long serialVersionUID = -6561660015694928357L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<ExportedKey> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
        return PortedKey.comparingFktable(comparator);
    }

    static Comparator<ExportedKey> comparingInSpecifiedOrder(final Context context,
                                                             final Comparator<? super String> comparator)
            throws SQLException {
        return comparingInSpecifiedOrder(ContextUtils.nullPrecedence(context, comparator));
    }

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    public ExportedKey() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object
    @Override
    public String toString() {
        return super.toString() + '{' +
               '}';
    }
}
