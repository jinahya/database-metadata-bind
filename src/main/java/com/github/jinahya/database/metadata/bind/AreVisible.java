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
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

/**
 * An abstract class for binding {@code ...AreVisible} values.
 *
 * @see OthersDeletesAreVisible
 * @see OthersInsertsAreVisible
 * @see OthersUpdatesAreVisible
 * @see OwnDeletesAreVisible
 * @see OwnInsertsAreVisible
 * @see OwnUpdatesAreVisible
 */
@XmlTransient
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AreVisible
        implements MetadataType {

    private static final long serialVersionUID = 8635936632512182596L;

    @XmlAttribute(required = true)
    private int type;

    @XmlValue
    private Boolean value;
}
