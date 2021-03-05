package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Optional;

@XmlTransient
abstract class CatalogChild extends AbstractChild<Catalog> {

    @XmlTransient
    Catalog getCatalog() {
        return getParent();
    }

    void setCatalog(final Catalog catalog) {
        setParent(catalog);
    }

    @XmlAttribute
    public String getCatalogTableCat() {
        return Optional.ofNullable(getCatalog())
                .map(Catalog::getTableCat)
                .orElse(null);
    }
}
