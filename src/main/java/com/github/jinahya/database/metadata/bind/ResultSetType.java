package com.github.jinahya.database.metadata.bind;

import java.sql.ResultSet;

enum ResultSetType implements IntFieldEnum<ResultSetType> {

    TYPE_FORWARD_ONLY(ResultSet.TYPE_FORWARD_ONLY),

    TYPE_SCROLL_INSENSITIVE(ResultSet.TYPE_SCROLL_INSENSITIVE),

    TYPE_SCROLL_SENSITIVE(ResultSet.TYPE_SCROLL_SENSITIVE);

    ResultSetType(final int rawValue) {
        this.rawValue = rawValue;
    }

    @Override
    public int getRawValue() {
        return rawValue;
    }

    public final int rawValue;
}
