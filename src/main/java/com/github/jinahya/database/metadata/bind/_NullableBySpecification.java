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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A marker for a column the JDBC specification documents as possibly {@code null}.
 * <p>
 * Applied to a binding field when, and only when, the corresponding {@link java.sql.DatabaseMetaData} method documents
 * that column with a "(may be {@code null})" qualifier. The specification is uneven across API families - the same
 * column name may be nullable in one method and not in another - so this marker records the per-method contract rather
 * than a guess from the column's name.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@_Nullable
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@interface _NullableBySpecification {

}
