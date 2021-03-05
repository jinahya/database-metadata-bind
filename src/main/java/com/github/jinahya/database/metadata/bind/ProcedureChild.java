package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract class ProcedureChild extends AbstractChild<Procedure> {

    @XmlTransient
    Procedure getProcedure() {
        return getParent();
    }

    void setProcedure(final Procedure procedure) {
        setParent(procedure);
    }
}
