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

import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * A entity class for binding results of the
 * {@link java.sql.DatabaseMetaData#getSuperTables(java.lang.String, java.lang.String, java.lang.String)}
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTables(String, String, String)
 */

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SuperTable
        extends AbstractMetadataType {

    private static final long serialVersionUID = 3579710773784268831L;

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_SUPERTABLE_NAME = "SUPERTABLE_NAME";

    // -----------------------------------------------------------------------------------------------------------------
    List<Table> getTables(final Context context, final String[] types) throws SQLException {
        return context.getTables(tableCat, tableSchem, tableName, types);
    }

    Optional<Table> getTable(final Context context, final String[] types) throws SQLException {
        return getTables(context, types).stream().findFirst();
    }

    List<Table> getSuperTables(final Context context, final String[] types) throws SQLException {
        return context.getTables(tableCat, tableSchem, supertableName, types);
    }

    Optional<Table> getSuperTable(final Context context, final String[] types) throws SQLException {
        return getSuperTables(context, types).stream().findFirst();
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    // ------------------------------------------------------------------------------------------------------ tableSchem

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    @EqualsAndHashCode.Include
    private String tableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    @EqualsAndHashCode.Include
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    @EqualsAndHashCode.Include
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_SUPERTABLE_NAME)
    @EqualsAndHashCode.Include
    private String supertableName;
}
