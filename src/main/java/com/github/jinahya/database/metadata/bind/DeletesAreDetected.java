package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#insertsAreDetected(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class DeletesAreDetected extends AreDetected {

    static List<DeletesAreDetected> list(final DatabaseMetaData databaseMetaData) throws SQLException {
        requireNonNull(databaseMetaData, "databaseMetaData is null");
        final List<DeletesAreDetected> list = list(DeletesAreDetected.class);
        for (final DeletesAreDetected v : list) {
            try {
                v.value = databaseMetaData.insertsAreDetected(v.type);
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                Utils.logSqlFeatureNotSupportedException(logger, sqlfnse);
            }
        }
        return list;
    }
}
