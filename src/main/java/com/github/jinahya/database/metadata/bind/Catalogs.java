package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A wrapper class for {@link Catalog}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class Catalogs extends Wrapper<Catalog> {

    public Catalogs() {
        super(Catalog.class);
    }
}
