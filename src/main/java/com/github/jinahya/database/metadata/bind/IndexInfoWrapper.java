package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter(AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
class IndexInfoWrapper {

    @XmlElementWrapper
    @XmlElement(name = "category")
    private final List<IndexInfoCategory> categories = new ArrayList<>();
}
