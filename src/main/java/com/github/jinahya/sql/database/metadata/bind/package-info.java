/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
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
@XmlSchema(
        attributeFormDefault = XmlNsForm.UNQUALIFIED,
        elementFormDefault = XmlNsForm.QUALIFIED,
        namespace = XmlConstants.DATABASE_METADATA_NS_URI,
        xmlns = {
            @XmlNs(prefix = XMLConstants.DEFAULT_NS_PREFIX,
                   namespaceURI = XmlConstants.DATABASE_METADATA_NS_URI),
            @XmlNs(prefix = "xsi",
                   namespaceURI = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI)
        }
)
@XmlAccessorType(XmlAccessType.NONE)
package com.github.jinahya.sql.database.metadata.bind;

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
