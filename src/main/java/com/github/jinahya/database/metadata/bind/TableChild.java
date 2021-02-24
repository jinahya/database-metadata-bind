package com.github.jinahya.database.metadata.bind;

abstract class TableChild extends AbstractChildValue<Table> {

    public Table getTable() {
        return getParent();
    }

    public void setTable(final Table table) {
        setParent(table);
    }
}
