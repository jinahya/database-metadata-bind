package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter(AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
class IndexInfoCategory {

    @XmlAttribute
    private boolean unique;

    @XmlAttribute
    private boolean approximate;

    @XmlElementWrapper
    @XmlElement(name = "indexInfo")
    private final List<IndexInfo> indexInfo_ = new ArrayList<>();
}
