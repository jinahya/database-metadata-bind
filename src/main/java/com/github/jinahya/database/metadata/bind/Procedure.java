package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ParentOf(ProcedureColumn.class)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Procedure
        implements MetadataType,
                   ChildOf<Schema> {

    private static final long serialVersionUID = -6262056388403934829L;

    @NullableBySpecification
    @ColumnLabel("PROCEDURE_CAT")
    private String procedureCat;

    @NullableBySpecification
    @ColumnLabel("PROCEDURE_SCHEM")
    private String procedureSchem;

    @ColumnLabel("PROCEDURE_NAME")
    private String procedureName;

    @NullableByVendor("HSQL")
    @ColumnLabel("REMARKS")
    private String remarks;

    @ColumnLabel("PROCEDURE_TYPE")
    private short procedureType;

    @ColumnLabel("SPECIFIC_NAME")
    private String specificName;
}
