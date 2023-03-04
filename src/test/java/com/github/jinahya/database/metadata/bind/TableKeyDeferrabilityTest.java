package com.github.jinahya.database.metadata.bind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.github.jinahya.database.metadata.bind.TableKeyDeferrability.valueOfDeferrability;
import static org.assertj.core.api.Assertions.assertThat;

class TableKeyDeferrabilityTest extends _IntFieldEnumTest<TableKeyDeferrability> {

    TableKeyDeferrabilityTest() {
        super(TableKeyDeferrability.class);
    }

    @DisplayName("valueOfDeferrability(deferrability)")
    @EnumSource(TableKeyDeferrability.class)
    @ParameterizedTest
    void valueOfDeferrability__(final TableKeyDeferrability tableKeyDeferrability) {
        assertThat(valueOfDeferrability(tableKeyDeferrability.fieldValueAsInt()))
                .isSameAs(tableKeyDeferrability);
    }
}
