package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.annotation.XmlElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter(AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
class BestRowIdentifierXmlValue {

    @XmlElement(name = "bestRowIdentifierWrapper")
    private final List<BestRowIdentifierWrapper> bestRowIdentifierWrappers = new ArrayList<>();
}
