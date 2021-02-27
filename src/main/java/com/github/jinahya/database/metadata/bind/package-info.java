@XmlSchema(
        attributeFormDefault = XmlNsForm.UNQUALIFIED,
        elementFormDefault = XmlNsForm.QUALIFIED,
        namespace = XmlConstants.NS_URI_DATABASE_METADATA_BIND,
        xmlns = {
                @XmlNs(prefix = XMLConstants.DEFAULT_NS_PREFIX,
                       namespaceURI = XmlConstants.NS_URI_DATABASE_METADATA_BIND),
                @XmlNs(prefix = "xsi", namespaceURI = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI)
        }
)
@XmlAccessorType(XmlAccessType.NONE)
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

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.*;
