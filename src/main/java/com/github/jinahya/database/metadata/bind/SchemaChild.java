package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Optional;

@XmlTransient
abstract class SchemaChild extends AbstractChild<Schema> {

    @XmlTransient
    Schema getSchema() {
        return getParent();
    }

    void setSchema(final Schema schema) {
        setParent(schema);
    }

    @XmlAttribute
    public String getSchemaTableSchem() {
        return Optional.ofNullable(getSchema())
                .map(Schema::getTableSchem)
                .orElse(null);
    }

    @XmlAttribute
    public String getSchemaTableCatalog() {
        return Optional.ofNullable(getSchema())
                .map(Schema::getTableCatalog)
                .orElse(null);
    }

    @XmlAttribute
    public String getSchemaCatalogTableCat() {
        return Optional.ofNullable(getSchema())
                .map(Schema::getCatalog)
                .map(Catalog::getTableCat)
                .orElse(null);
    }
}
