package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlTransient;

interface Child<P extends MetadataType> extends MetadataType {

    @XmlTransient
    P getParent_();

    void setParent_(P parent);
}
