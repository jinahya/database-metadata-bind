package com.github.jinahya.database.metadata.bind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class TableKeyDeleteRuleTest extends _IntFieldEnumTest<TableKeyDeleteRule> {

    TableKeyDeleteRuleTest() {
        super(TableKeyDeleteRule.class);
    }

    @DisplayName("valueOfDeleteRule")
    @EnumSource(TableKeyDeleteRule.class)
    @ParameterizedTest
    void valueOfDeleteRule__(final TableKeyDeleteRule deleteRule) {
        assertThat(TableKeyDeleteRule.valueOfDeleteRule(deleteRule.fieldValueAsInt()))
                .isSameAs(deleteRule);
    }
}
