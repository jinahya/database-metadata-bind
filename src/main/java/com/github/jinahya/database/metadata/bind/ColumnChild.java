package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract class ColumnChild extends AbstractChild<Column> {

    @XmlTransient
    Column getColumn() {
        return getParent();
    }

    void setColumn(final Column column) {
        setParent(column);
    }
}
