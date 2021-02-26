package com.github.jinahya.database.metadata.bind;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import javax.xml.bind.annotation.XmlTransient;

abstract class AbstractChild<P extends MetadataType> implements Child<P> {

    AbstractChild() {
        super();
    }

    @Override
    public P getParent_() {
        return parent_;
    }

    @Override
    public void setParent_(final P parent) {
        this.parent_ = parent;
    }

    @XmlTransient
    @Valid
    @NotNull
    private P parent_;
}
