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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Comparator;

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getTableTypes()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTableTypes()
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TableType extends AbstractMetadataType {

    private static final long serialVersionUID = -7630634982776331078L;

    // -----------------------------------------------------------------------------------------------------------------
    static final Comparator<TableType> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(TableType::getTableType, String.CASE_INSENSITIVE_ORDER);

    static final Comparator<TableType> LEXICOGRAPHIC_ORDER = Comparator.comparing(TableType::getTableType);

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_TABLE_TYPE = "TABLE_TYPE";

    // -----------------------------------------------------------------------------------------------------------------
    @SuppressWarnings({
            "java:S1700"
    })
    @_ColumnLabel(COLUMN_LABEL_TABLE_TYPE)
    private String tableType;
}
