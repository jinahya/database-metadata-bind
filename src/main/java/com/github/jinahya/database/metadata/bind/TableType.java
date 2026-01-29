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

import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getTableTypes()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTableTypes()
 */
public class TableType
        extends AbstractMetadataType {

    private static final long serialVersionUID = -7630634982776331078L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS
    static Comparator<TableType> comparing(final Context context, final Comparator<? super String> comparator) {
        return Comparator.comparing(TableType::getTableType, comparator);
    }

    // ------------------------------------------------------------------------------------------------------ TABLE_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_TYPE = "TABLE_TYPE";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    TableType() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableType=" + tableType +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final var that = (TableType) obj;
        return Objects.equals(tableType, that.tableType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableType);
    }
    // ------------------------------------------------------------------------------------------------------- tableType

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_TYPE} column.
     */
    public String getTableType() {
        return tableType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_TYPE} column.
     *
     * @param tableType the value of {@value #COLUMN_LABEL_TABLE_TYPE} column.
     */
    void setTableType(final String tableType) {
        this.tableType = tableType;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_TABLE_TYPE)
    @SuppressWarnings({
            "java:S1700"
    })
    private String tableType;
}
