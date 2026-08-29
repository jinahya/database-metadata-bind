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
 * A meta-annotation for the field-level markers whose columns are reserved.
 * <p>
 * This annotation targets {@link java.lang.annotation.ElementType#ANNOTATION_TYPE ANNOTATION_TYPE}, not fields: it is
 * applied to {@link _ReservedBySpecification}, and is never applied to a binding field directly. It is itself
 * {@link _Nullable}, since a reserved column has no defined value yet.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@_Nullable
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@interface _Reserved {

}
