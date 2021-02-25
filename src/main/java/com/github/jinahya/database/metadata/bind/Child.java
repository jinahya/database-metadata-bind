package com.github.jinahya.database.metadata.bind;

interface Child<P extends MetadataValue> extends MetadataValue {

    P getParent();

    void setParent(P parent);
}
