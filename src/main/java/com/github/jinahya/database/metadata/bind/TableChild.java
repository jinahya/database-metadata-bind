package com.github.jinahya.database.metadata.bind;

abstract class TableChild extends AbstractChild<Table> {

    public Table getTable_() {
        return getParent_();
    }

    public void setTable_(final Table table) {
        setParent_(table);
    }
}
