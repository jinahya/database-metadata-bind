/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.jinahya.sql.database.metadata.bind;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class ResultSets {


    static Set<String> getColumnLabels(final ResultSet resultSet)
        throws SQLException {

        final ResultSetMetaData rsmd = resultSet.getMetaData();
        final int columnCount = rsmd.getColumnCount();
        final Set<String> columnLabels = new HashSet<String>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            columnLabels.add(rsmd.getColumnLabel(i));
        }

        return columnLabels;
    }


    private ResultSets() {

        super();
    }


}

