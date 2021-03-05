package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract class FunctionChild extends AbstractChild<Function> {

    @XmlTransient
    Function getFunction() {
        return getParent();
    }

    void setFunction(final Function function) {
        setParent(function);
    }
}
