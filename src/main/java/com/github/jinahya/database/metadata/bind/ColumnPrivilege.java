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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;

/**
 * A class for binding results of the {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getColumnPrivileges(String, String, String, String)
 */

@_ChildOf(Table.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ColumnPrivilege
        extends AbstractMetadataType {

    private static final long serialVersionUID = 4384654744147773380L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<ColumnPrivilege> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator.comparing(ColumnPrivilege::getColumnName, ContextUtils.nulls(context, comparator))
                .thenComparing(ColumnPrivilege::getPrivilege, ContextUtils.nulls(context, comparator));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_IS_GRANTABLE = "IS_GRANTABLE";

    public static final String COLUMN_VALUE_IS_GRANTABLE_YES = YesNoConstants.YES;

    public static final String COLUMN_VALUE_IS_GRANTABLE_NO = YesNoConstants.NO;

    public enum IsGrantable
            implements YesNoEnum<IsGrantable> {

        /**
         * A value for {@link #COLUMN_VALUE_IS_GRANTABLE_YES}.
         */
        YES(COLUMN_VALUE_IS_GRANTABLE_YES), // "YES"

        /**
         * A value for {@link #COLUMN_VALUE_IS_GRANTABLE_NO}.
         */
        NO(COLUMN_VALUE_IS_GRANTABLE_NO);   // "NO"

        public static IsGrantable valueOfFieldValue(final String fieldValue) {
            return YesNoEnum.valueOfFieldValue(IsGrantable.class, fieldValue);
        }

        IsGrantable(final String fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public String fieldValue() {
            return fieldValue;
        }

        private final String fieldValue;
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    // ------------------------------------------------------------------------------------------------------ tableSchem

    // ------------------------------------------------------------------------------------------------------- tableName

    // ------------------------------------------------------------------------------------------------------ columnName

    // ----------------------------------------------------------------------------------------------------- isGrantable

    @Nullable
    IsGrantable getIsGrantableAsEnum() {
        return Optional.ofNullable(getIsGrantable())
                .map(IsGrantable::valueOfFieldValue)
                .orElse(null);
    }

    void setIsGrantableAsEnum(@Nullable final IsGrantable isGrantableAsEnum) {
        setIsGrantable(
                Optional.ofNullable(isGrantableAsEnum)
                        .map(_FieldEnum::fieldValue)
                        .orElse(null)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("GRANTOR")
    private String grantor;

    @_ColumnLabel("GRANTEE")
    private String grantee;

    @_ColumnLabel("PRIVILEGE")
    private String privilege;

    @Nullable
    @Pattern(regexp = YesNoConstants.REGEXP_YES_NO)
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_IS_GRANTABLE)
    private String isGrantable;
}
