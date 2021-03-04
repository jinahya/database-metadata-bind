package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link java.sql.DatabaseMetaData#othersInsertsAreVisible(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class OthersInsertsAreVisible extends AreVisible {

    static List<OthersInsertsAreVisible> list(final DatabaseMetaData databaseMetaData) throws SQLException {
        requireNonNull(databaseMetaData, "databaseMetaData is null");
        final List<OthersInsertsAreVisible> list = list(OthersInsertsAreVisible.class);
        for (final OthersInsertsAreVisible v : list) {
            try {
                v.value = databaseMetaData.othersDeletesAreVisible(v.type);
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                Utils.logSqlFeatureNotSupportedException(logger, sqlfnse);
            }
        }
        return list;
    }
}
