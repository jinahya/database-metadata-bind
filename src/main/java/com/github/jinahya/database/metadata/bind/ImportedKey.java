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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding results of the {@link DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getImportedKeys(String, String, String)
 * @see ExportedKey
 */

public class ImportedKey
        extends PortedKey {

    private static final long serialVersionUID = -1900794151555506751L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<ImportedKey> comparingInSpecifiedOrder(final Context context,
                                                             final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return PortedKey.comparingPktable(context, comparator);
    }

    static Comparator<ImportedKey> comparingInSpecifiedOrder(final Context context)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return comparingInSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    ImportedKey() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ImportedKey)) {
            return false;
        }
        final var that = (ImportedKey) obj;
        return Objects.equals(pktableCat, that.pktableCat) &&
               Objects.equals(pktableSchem, that.pktableSchem) &&
               Objects.equals(pktableName, that.pktableName) &&
               Objects.equals(pkcolumnName, that.pkcolumnName) &&
               Objects.equals(fktableCat, that.fktableCat) &&
               Objects.equals(fktableSchem, that.fktableSchem) &&
               Objects.equals(fktableName, that.fktableName) &&
               Objects.equals(fkcolumnName, that.fkcolumnName) &&
               Objects.equals(keySeq, that.keySeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                pktableCat,
                pktableSchem,
                pktableName,
                pkcolumnName,
                fktableCat,
                fktableSchem,
                fktableName,
                fkcolumnName,
                keySeq
        );
    }
}
