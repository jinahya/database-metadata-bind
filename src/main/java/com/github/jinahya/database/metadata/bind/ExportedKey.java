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
 * A class for binding results of the {@link DatabaseMetaData#getExportedKeys(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getExportedKeys(String, String, String)
 * @see ImportedKey
 */
@_ChildOf(Table.class)
public class ExportedKey
        extends PortedKey {

    @Serial
    private static final long serialVersionUID = -6561660015694928357L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator comparing values in the specified order.
     * <blockquote>
     * They are ordered by <code>FKTABLE_CAT</code>, <code>FKTABLE_SCHEM</code>, <code>FKTABLE_NAME</code>, and
     * <code>KEY_SEQ</code>.
     * </blockquote>
     *
     * @param comparator a null-safe string comparator for comparing values.
     * @return a comparator comparing values in the specified order.
     * @see PortedKey#comparingFk(Comparator)
     */
    static Comparator<ExportedKey> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
        return PortedKey.comparingFk(comparator);
    }

    /**
     * Returns a comparator comparing values in the specified order, placing {@code null} values (of all keys) as the
     * specified context's database sorts them.
     *
     * @param context    a context whose metadata determines the {@code null} ordering.
     * @param comparator a comparator for comparing (non-{@code null}) string values.
     * @return a comparator comparing values in the specified order.
     * @throws SQLException if a database access error occurs.
     * @see PortedKey#comparingFk(Context, Comparator)
     */
    static Comparator<ExportedKey> comparingInSpecifiedOrder(final Context context,
                                                             final Comparator<? super String> comparator)
            throws SQLException {
        return PortedKey.comparingFk(context, comparator);
    }

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    ExportedKey() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object
    @Override
    public String toString() {
        return super.toString() + '{' +
               "pktableCat=" + getPktableCat() +
               ",pktableSchem=" + getPktableSchem() +
               ",pktableName=" + getPktableName() +
               ",pkcolumnName=" + getPkcolumnName() +
               ",fktableCat=" + getFktableCat() +
               ",fktableSchem=" + getFktableSchem() +
               ",fktableName=" + getFktableName() +
               ",fkcolumnName=" + getFkcolumnName() +
               ",keySeq=" + keySeq +
               ",updateRule=" + getUpdateRule() +
               ",deleteRule=" + getDeleteRule() +
               ",fkName=" + getFkName() +
               ",pkName=" + getPkName() +
               ",deferrability=" + getDeferrability() +
               '}';
    }
}
