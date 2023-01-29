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

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class ColumnId implements Serializable {

    private static final long serialVersionUID = -4452694121211962289L;

    public static ColumnId of(final TableId tableId, final String columnName) {
        return builder()
                .tableId(tableId)
                .columnName(columnName)
                .build();
    }

    public static ColumnId of(final String tableCat, final String tableSchem, final String tableName,
                              final String columnName) {
        return of(TableId.of(tableCat, tableSchem, tableName), columnName);
    }

    private TableId tableId;

    private String columnName;
}
