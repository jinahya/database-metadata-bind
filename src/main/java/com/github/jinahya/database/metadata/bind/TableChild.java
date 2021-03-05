package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract class TableChild extends AbstractChild<Table> {

    @XmlTransient
    Table getTable() {
        return getParent();
    }

    void setTable(final Table table) {
        setParent(table);
    }
}
