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


import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
@XmlRootElement
@XmlType(
    propOrder = {"key", "paths"}
)
public class Suppression {


    /**
     * logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(Suppression.class);


    /**
     * Creates a new instance.
     */
    public Suppression() {

        super();
    }


    public void print(final PrintStream out) throws JAXBException {

        if (out == null) {
            throw new NullPointerException("null out");
        }

        assert Suppression.class.getAnnotation(XmlRootElement.class) != null;

        final JAXBContext context = JAXBContext.newInstance(Suppression.class);

        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(this, out);
    }


    public boolean isSuppressed(final String path) {

        if (path == null) {
            throw new NullPointerException("null path");
        }

        if (!isEnabled()) {
            return false;
        }

        return getPaths().contains(path);
    }


    // ----------------------------------------------------------------- enabled
    public boolean isEnabled() {

        return enabled;
    }


    public void setEnabled(final boolean enabled) {

        this.enabled = enabled;
    }


    // --------------------------------------------------------------------- key
    public SuppressionKey getKey() {

        return key;
    }


    public void setKey(final SuppressionKey key) {

        this.key = key;
    }


    // ------------------------------------------------------------------- paths
    public Set<String> getPaths() {

        if (paths == null) {
            paths = new HashSet<String>();
        }

        return paths;
    }


    /**
     * enabled.
     */
    @XmlAttribute(required = true)
    private boolean enabled = true;


    /**
     * key.
     */
    @XmlElement(required = true)
    private SuppressionKey key;


    /**
     * paths.
     */
    @XmlElement(name = "path")
    @XmlElementWrapper(required = true)
    private Set<String> paths;


}

