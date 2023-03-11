package com.github.jinahya.database.metadata.bind;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class TableKeyUpdateRuleTest extends _IntFieldEnumTest<TableKeyUpdateRule> {

    TableKeyUpdateRuleTest() {
        super(TableKeyUpdateRule.class);
    }

    @EnumSource(TableKeyUpdateRule.class)
    @ParameterizedTest
    void valueOfUpdateRule__(final TableKeyUpdateRule tableKeyUpdateRule) {
        assertThat(TableKeyUpdateRule.valueOfUpdateRule(tableKeyUpdateRule.fieldValueAsInt()))
                .isSameAs(tableKeyUpdateRule);
    }
}
