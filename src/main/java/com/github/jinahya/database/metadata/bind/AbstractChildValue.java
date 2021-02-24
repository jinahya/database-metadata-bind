package com.github.jinahya.database.metadata.bind;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

abstract class AbstractChildValue<P extends MetadataValue> implements ChildValue<P> {

    AbstractChildValue() {
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
