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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlValue;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.java.Log;

import java.io.Serializable;

/**
 * An abstract class for binding the result of {@code DatabaseMetaData#...DeletesAreVisible(int)} method.
 *
 * @see OthersDeletesAreVisible
 * @see OthersInsertsAreVisible
 * @see OthersUpdatesAreVisible
 * @see OwnDeletesAreVisible
 * @see OwnInsertsAreVisible
 * @see OwnUpdatesAreVisible
 */
@XmlSeeAlso({
        OthersDeletesAreVisible.class,
        OthersInsertsAreVisible.class,
        OthersUpdatesAreVisible.class,
        OwnDeletesAreVisible.class,
        OwnInsertsAreVisible.class,
        OwnUpdatesAreVisible.class
})
@Setter
@Getter
@EqualsAndHashCode
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Log
public abstract class AreVisible
        implements Serializable {

    private static final long serialVersionUID = -4539635096087360299L;

    @XmlAttribute(required = true)
    private int type;

    @XmlValue
    private Boolean value;
}
