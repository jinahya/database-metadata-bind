package com.github.jinahya.database.metadata.bind;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class TableAssert
        extends AbstractMetadataTypeAssert<TableAssert, Table> {

    TableAssert(final Table actual) {
        super(actual, TableAssert.class);
    }

    TableAssert hasTableCat(final String tableCat) {
        return isNotNull().satisfies(a -> {
            assertThat(a.getTableCat()).isEqualTo(tableCat);
        });
    }

    TableAssert hasTableSchem(final String tableSchem) {
        return isNotNull().satisfies(a -> {
            assertThat(a.getTableSchem()).isEqualTo(tableSchem);
        });
    }

    TableAssert isOf(final Catalog catalog) {
        Objects.requireNonNull(catalog, "catalog is null");
        return hasTableCat(catalog.getTableCat());
    }

    TableAssert isOf(final Schema schema) {
        Objects.requireNonNull(schema, "schema is null");
        return hasTableCat(schema.getTableCatalog())
                .hasTableSchem(schema.getTableSchem());
    }
}