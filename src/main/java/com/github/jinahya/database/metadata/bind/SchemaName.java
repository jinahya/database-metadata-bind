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

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import java.sql.DatabaseMetaData;
import java.util.Collection;

/**
 * A class for binding results of {@link DatabaseMetaData#getSchemas()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see DatabaseMetaData#getSchemas()
 * @see Context#getSchemas(Collection)
 */
@XmlRootElement
@Data
@NoArgsConstructor
public class SchemaName implements MetadataType {

    private static final long serialVersionUID = 5784631477568740816L;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "token")
    @MayBeNull
    @Label("TABLE_CATALOG")
    private String tableCatalog;
}
