package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

class TablePrivilegeTest
        extends AbstractMetadataType_Test<TablePrivilege> {

    TablePrivilegeTest() {
        super(TablePrivilege.class);
    }

    @Override
    TablePrivilege newTypeInstance() {
        final var instance = super.newTypeInstance();
        instance.setTableName("TABLE_NAME");
        instance.setGrantee("GRANTEE");
        instance.setPrivilege("SELECT");
        return instance;
    }
}
