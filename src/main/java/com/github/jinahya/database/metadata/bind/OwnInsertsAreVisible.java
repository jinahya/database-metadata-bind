package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Level;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#ownInsertsAreVisible(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class OwnInsertsAreVisible extends AreVisible {

    static List<OwnInsertsAreVisible> list(final DatabaseMetaData databaseMetaData) throws SQLException {
        requireNonNull(databaseMetaData, "databaseMetaData is null");
        final List<OwnInsertsAreVisible> list = list(OwnInsertsAreVisible.class);
        for (final OwnInsertsAreVisible v : list) {
            try {
                v.value = databaseMetaData.ownInsertsAreVisible(v.type);
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
            }
        }
        return list;
    }
}
