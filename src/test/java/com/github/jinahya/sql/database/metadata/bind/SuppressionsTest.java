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


package com.github.jinahya.sql.database.metadata.bind;


import com.github.jinahya.sql.database.metadata.bind.Suppressions;
import com.github.jinahya.sql.database.metadata.bind.SuppressionKey;
import java.io.IOException;
import java.io.PrintStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.testng.annotations.Test;


/**
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public class SuppressionsTest {


    public static final SuppressionKey TEST_KEY;


    static {
        TEST_KEY = new SuppressionKey();
        TEST_KEY.setDatabaseMajorVersion(0);
        TEST_KEY.setDatabaseMinorVersion(0);
        TEST_KEY.setDatabaseProductName("test");
        TEST_KEY.setDatabaseProductVersion("test");
        TEST_KEY.setDriverMajorVersion(0);
        TEST_KEY.setDriverMinorVersion(0);
        TEST_KEY.setDriverName("test");
        TEST_KEY.setDriverVersion("test");
    }


    public static void print(final Suppressions suppressions) 
        throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(Suppressions.class);

        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(suppressions, System.out);
    }


    @Test
    public static void generateSchema_resolver()
        throws JAXBException, IOException {

        final SchemaOutputResolver resolver = new SchemaOutputResolver() {


            @Override
            public Result createOutput(final String namespaceUri,
                                       final String suggestedFileName)
                throws IOException {

                return new StreamResult(System.out) {


                    @Override
                    public String getSystemId() {
                        return suggestedFileName;
                    }


                };
            }


        };

        Suppressions.generateSchema(resolver);
    }


    @Test
    public static void generateSchema_result()
        throws JAXBException, IOException {

        final Result result = new StreamResult(System.out) {


            @Override
            public String getSystemId() {
                return "noSystemId";
            }


        };

        Suppressions.generateSchema(result);
    }


    @Test
    public static void loadInstance_() throws JAXBException, IOException {

        final Suppressions suppression = Suppressions.loadInstance();

        suppression.print(System.out);
    }


}

