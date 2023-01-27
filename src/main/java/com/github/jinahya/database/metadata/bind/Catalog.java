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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A class for binding results of {@link java.sql.DatabaseMetaData#getCatalogs()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getCatalogs(Consumer)
 */
@ParentOf(Schema.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Catalog
        extends AbstractMetadataType {

    private static final long serialVersionUID = 6239185259128825953L;

    /**
     * A comparator compares objects with their {@link #getTableCat()} values.
     */
    public static final Comparator<Catalog> COMPARING_TABLE_CAT =
            Comparator.comparing(Catalog::getTableCat, Comparator.nullsFirst(Comparator.naturalOrder()));

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String ATTRIBUTE_NAME_TABLE_CAT = "tableCat";

    /**
     * A value for {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute for virtual instances. The value is {@value}.
     */
    public static final String COLUMN_VALUE_TABLE_CAT_EMPTY = "";

    public CatalogId getCatalogId() {
        return CatalogId.builder()
                .tableCat(getTableCat())
                .build();
    }

    String getTableCatNonNull() {
        return Optional.ofNullable(getTableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;
}
