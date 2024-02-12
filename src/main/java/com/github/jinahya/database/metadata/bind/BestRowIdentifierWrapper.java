package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElementRef;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class BestRowIdentifierWrapper implements Serializable {

    private static final long serialVersionUID = 2581394162146660186L;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute
    private Integer scope;

    @XmlAttribute
    private boolean nullable;

    @XmlElementRef
    private final List<BestRowIdentifier> wrapped = new ArrayList<>();
}
