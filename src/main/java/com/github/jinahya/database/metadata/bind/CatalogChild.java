package com.github.jinahya.database.metadata.bind;

interface CatalogChild extends MetadataValue {

    Catalog getCatalog();

    void setCatalog(Catalog catalog);
}
