package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2024 Jinahya, Inc.
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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

final class __JakartaXmlBinding_Test_Utils {

    static JAXBContext newContext() throws JAXBException {
        return JAXBContext.newInstance(AbstractMetadataType.class);
    }

    static Marshaller createMarshaller() throws JAXBException {
        return newContext().createMarshaller();
    }

    static Unmarshaller createUnmarshaller() throws JAXBException {
        return newContext().createUnmarshaller();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private __JakartaXmlBinding_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
