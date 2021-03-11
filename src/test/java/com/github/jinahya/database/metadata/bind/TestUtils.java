package com.github.jinahya.database.metadata.bind;

import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

final class TestUtils {

    static String getFilenamePrefix(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        return context.databaseMetaData.getDatabaseProductName() + " - "
               + context.databaseMetaData.getDatabaseProductVersion();
    }

    private TestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
