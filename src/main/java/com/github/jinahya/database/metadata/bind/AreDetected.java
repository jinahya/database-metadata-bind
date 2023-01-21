package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

import java.sql.ResultSet;
import java.util.stream.IntStream;

/**
 * An abstract class for binding the result of {@code DatabaseMetaData#(delete|insert|update)sAreDetected(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DeletesAreDetected
 * @see InsertsAreDetected
 * @see UpdatesAreDetected
 * @see ResultSet#TYPE_FORWARD_ONLY
 * @see ResultSet#TYPE_SCROLL_INSENSITIVE
 * @see ResultSet#TYPE_SCROLL_SENSITIVE
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public abstract class AreDetected
        extends AbstractMetadataType {

    private static final long serialVersionUID = -5726735588783597670L;

    /**
     * Returns a stream of {@link ResultSet#TYPE_FORWARD_ONLY}, {@link ResultSet#TYPE_SCROLL_INSENSITIVE}, and
     * {@link ResultSet#TYPE_SCROLL_SENSITIVE}.
     *
     * @return a stream of valid values for {@code type} proprty.
     */
    static IntStream typeStream() {
        return IntStream.of(
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.TYPE_SCROLL_SENSITIVE
        );
    }

    private int type;

    private boolean value;
}
