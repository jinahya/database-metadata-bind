package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getBestRowIdentifier(String, String, String, int, boolean)
 */
@ChildOf(Table.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class BestRowIdentifier extends AbstractMetadataType {

    private static final long serialVersionUID = -1512051574198028399L;

    /**
     * A comparator compares objects with their value of {@link #getScope()}.
     */
    public static final Comparator<BestRowIdentifier> COMPARING_SCOPE =
            Comparator.comparingInt(BestRowIdentifier::getScope);

    public static final String COLUMN_LABEL_SCOPE = "SCOPE";

    public static List<Integer> scopes() {
        return Arrays.asList(
                DatabaseMetaData.bestRowTemporary,
                DatabaseMetaData.bestRowTransaction,
                DatabaseMetaData.bestRowSession
        );
    }

    public BestRowIdentifierScope getScopeAsEnum() {
        return BestRowIdentifierScope.valueOfScope(getScope());
    }

    public void setScopeAsEnum(final BestRowIdentifierScope scopeAsEnum) {
        Objects.requireNonNull(scopeAsEnum, "scopeAsEnum is null");
        setScope(scopeAsEnum.fieldValueAsInt());
    }

    @ColumnLabel(COLUMN_LABEL_SCOPE)
    private int scope;

    @ColumnLabel("COLUMN_NAME")
    private String columnName;

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @NullableBySpecification
    @ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @NotUsedBySpecification
    @ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @NullableBySpecification
    @ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @ColumnLabel("PSEUDO_COLUMN")
    private int pseudoColumn;
}
