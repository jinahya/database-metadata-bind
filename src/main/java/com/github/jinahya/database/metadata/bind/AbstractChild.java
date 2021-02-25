package com.github.jinahya.database.metadata.bind;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

abstract class AbstractChild<P extends MetadataValue> implements Child<P> {

    AbstractChild() {
        super();
    }

    @Override
    public P getParent() {
        return parent;
    }

    @Override
    public void setParent(final P parent) {
        this.parent = parent;
    }

    @Valid
    @NotNull
    private P parent;
}
