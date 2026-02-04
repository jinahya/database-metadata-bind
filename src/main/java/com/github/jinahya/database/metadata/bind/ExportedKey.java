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
import java.util.function.UnaryOperator;

/**
 * A class for binding results of the {@link DatabaseMetaData#getExportedKeys(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getExportedKeys(String, String, String)
 * @see ImportedKey
 */
public class ExportedKey
        extends PortedKey {

    private static final long serialVersionUID = -6561660015694928357L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator comparing values in the specified order.
     * <blockquote>
     * They are ordered by <code>FKTABLE_CAT</code>, <code>FKTABLE_SCHEM</code>, <code>FKTABLE_NAME</code>, and
     * <code>KEY_SEQ</code>.
     * </blockquote>
     *
     * @param operator   a null-safe unary operator for adjusting string values.
     * @param comparator a null-safe string comparator for comparing values.
     * @return a comparator comparing values in the specified order.
     * @see PortedKey#comparingFk(UnaryOperator, Comparator)
     */
    static Comparator<ExportedKey> comparingInSpecifiedOrder(final UnaryOperator<String> operator,
                                                             final Comparator<? super String> comparator) {
        return PortedKey.comparingFk(operator, comparator);
    }

    static Comparator<ExportedKey> comparingInSpecifiedOrder(final Context context,
                                                             final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return PortedKey.comparingFktable(context, comparator);
    }

    static Comparator<ExportedKey> comparingInSpecifiedOrder(final Context context)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return comparingInSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER);
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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ExportedKey)) {
            return false;
        }
        final var that = (ExportedKey) obj;
        return Objects.equals(fktableCat, that.fktableCat) &&
               Objects.equals(fktableSchem, that.fktableSchem) &&
               Objects.equals(fktableName, that.fktableName) &&
               Objects.equals(fkcolumnName, that.fkcolumnName) &&
               Objects.equals(pktableCat, that.pktableCat) &&
               Objects.equals(pktableSchem, that.pktableSchem) &&
               Objects.equals(pktableName, that.pktableName) &&
               Objects.equals(pkcolumnName, that.pkcolumnName) &&
               Objects.equals(keySeq, that.keySeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                fktableCat,
                fktableSchem,
                fktableName,
                fkcolumnName,
                pktableCat,
                pktableSchem,
                pktableName,
                pkcolumnName,
                keySeq
        );
    }
}
