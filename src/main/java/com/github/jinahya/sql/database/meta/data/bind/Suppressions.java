/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
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


package com.github.jinahya.sql.database.meta.data.bind;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Result;


/**
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
@XmlRootElement
public class Suppressions {


    private static final String RESOURCE_NAME = "suppressions.xml";


    public static void generateSchema(final SchemaOutputResolver resolver)
        throws JAXBException, IOException {

        final JAXBContext context
            = JAXBContext.newInstance(Suppressions.class);

        context.generateSchema(resolver);
    }


    public static void generateSchema(final Result output)
        throws JAXBException, IOException {

        generateSchema(new SchemaOutputResolver() {


            @Override
            public Result createOutput(final String namespaceUri,
                                       final String suggestedFileName)
                throws IOException {

                return output;
            }


        });
    }


    public static Suppressions loadInstance(final InputStream in)
        throws JAXBException, IOException {

        if (in == null) {
            throw new NullPointerException("in");
        }

        final JAXBContext context
            = JAXBContext.newInstance(Suppressions.class);

        final Unmarshaller unmarshaller = context.createUnmarshaller();

        return (Suppressions) unmarshaller.unmarshal(in);
    }


    public static Suppressions loadInstance()
        throws JAXBException, IOException {

        final InputStream in
            = Suppressions.class.getResourceAsStream(RESOURCE_NAME);
        if (in == null) {
            throw new IOException("failed to load resource: " + RESOURCE_NAME);
        }
        try {
            return loadInstance(in);
        } finally {
            in.close();
        }
    }


    public void print(final PrintStream out) throws JAXBException {

        if (out == null) {
            throw new NullPointerException("null out");
        }

        assert Suppressions.class.getAnnotation(XmlRootElement.class) != null;

        final JAXBContext context = JAXBContext.newInstance(Suppressions.class);

        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(this, out);
    }


    public List<Suppression> getSuppressions() {

        if (suppressions == null) {
            suppressions = new ArrayList<Suppression>();
        }

        return suppressions;
    }


    public Suppression getSuppression(final SuppressionKey suppressionKey) {

        if (suppressionKey == null) {
            throw new NullPointerException("null suppressionKey");
        }

        for (final Suppression suppression : getSuppressions()) {
            if (suppression.getKey().equals(suppressionKey)) {
                return suppression;
            }
        }

        return null;
    }


    @XmlElement(name = "suppression")
    private List<Suppression> suppressions;


}

