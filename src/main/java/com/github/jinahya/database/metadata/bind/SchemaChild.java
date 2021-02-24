package com.github.jinahya.database.metadata.bind;

abstract class SchemaChild extends AbstractChildValue<Schema> {

    public Schema getSchema() {
        return getParent();
    }

    public void setSchema(final Schema schema) {
        setParent(schema);
    }
}
