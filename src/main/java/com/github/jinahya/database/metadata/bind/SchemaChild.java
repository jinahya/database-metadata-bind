package com.github.jinahya.database.metadata.bind;

abstract class SchemaChild extends AbstractChild<Schema> {

    Schema getSchema_() {
        return getParent_();
    }

    void setSchema_(final Schema schema) {
        setParent_(schema);
    }
}