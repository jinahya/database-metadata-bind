package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract class UDTChild extends AbstractChild<UDT> {

    @XmlTransient
    UDT getUDT() {
        return getParent();
    }

    void setUDT(final UDT udt) {
        setParent(udt);
    }
}
