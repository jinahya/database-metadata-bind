package com.github.jinahya.database.metadata.bind;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class Catalog_Json_Test extends AbstractCatalogTest {

    @Test
    void toJson() {
        final Catalog value = new Catalog();
        value.setTableCat("tableCat");
        final String json = JsonbTests.toJson(value);
        log.debug("json: {}", json);
    }

    @Test
    void toJsonFromJsonAndCompare() {
        final Catalog expected = new Catalog();
        expected.setTableCat("tableCat");
        final String json = JsonbTests.toJson(expected);
        final Catalog actual = JsonbTests.fromJson(json, Catalog.class);
        assertThat(actual).isEqualTo(expected);
    }
}
