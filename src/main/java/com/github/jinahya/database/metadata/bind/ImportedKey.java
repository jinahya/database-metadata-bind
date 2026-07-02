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

import java.io.Serial;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * A class for binding results of the {@link DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getImportedKeys(String, String, String)
 * @see ExportedKey
 */
@_ChildOf(Table.class)
public class ImportedKey
        extends PortedKey {

    @Serial
    private static final long serialVersionUID = -1900794151555506751L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator comparing values in the specified order.
     * <blockquote>
     * They are ordered by <code>PKTABLE_CAT</code>, <code>PKTABLE_SCHEM</code>, <code>PKTABLE_NAME</code>, and
     * <code>KEY_SEQ</code>.
     * </blockquote>
     *
     * @param comparator a null-safe string comparator for comparing values.
     * @return a comparator comparing values in the specified order.
     * @see PortedKey#comparingPk(Comparator)
     */
    static Comparator<ImportedKey> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
        return PortedKey.comparingPk(comparator);
    }

    /**
     * Returns a comparator comparing values in the specified order, placing {@code null} values (of all keys) as the
     * specified context's database sorts them.
     *
     * @param context    a context whose metadata determines the {@code null} ordering.
     * @param comparator a comparator for comparing (non-{@code null}) string values.
     * @return a comparator comparing values in the specified order.
     * @throws SQLException if a database access error occurs.
     * @see PortedKey#comparingPk(Context, Comparator)
     */
    static Comparator<ImportedKey> comparingInSpecifiedOrder(final Context context,
                                                             final Comparator<? super String> comparator)
            throws SQLException {
        return PortedKey.comparingPk(context, comparator);
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    ImportedKey() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object
}
