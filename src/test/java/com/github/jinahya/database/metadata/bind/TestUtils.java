package com.github.jinahya.database.metadata.bind;

import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

final class TestUtils {

    static String getFilenamePrefix(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        return context.databaseMetaData.getDatabaseProductName() + " - "
               + context.databaseMetaData.getDatabaseProductVersion();
    }

    static <T extends MetadataType> void testEquals(final List<? extends T> list) {
        requireNonNull(list, "list is null");
        for (int i = 0; i < list.size();i++) {
            for (int j = 0; j < list.size();j++) {
                if (j == i) {
                    assertThat(list.get(i)).isEqualTo(list.get(j));
                    assertThat(list.get(j)).isEqualTo(list.get(i));
                } else {
                    assertThat(list.get(i)).isNotEqualTo(list.get(j));
                    assertThat(list.get(j)).isNotEqualTo(list.get(i));
                }
            }
        }
    }

    private TestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
