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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * An abstract class for binding results of {@code DatabaseMetaData#...AreDetected(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DeletesAreDetected
 * @see InsertsAreDetected
 * @see UpdatesAreDetected
 */
@XmlSeeAlso({
        DeletesAreDetected.class,
        InsertsAreDetected.class,
        UpdatesAreDetected.class
})
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@SuperBuilder(toBuilder = true)
public abstract class AreDetected
        implements Serializable,
                   MetadataType {

    private static final long serialVersionUID = 7505598364855010122L;

    @Override
    public void retrieveChildren(Context context) throws SQLException {
        // no children.
    }

    @XmlAttribute(required = false)
    public ResultSetType getTypeAsEnum() {
        return ResultSetType.valueOfRawValue(getType());
    }

    @XmlAttribute(required = true)
    private int type;

    @XmlValue
    private Boolean value;
}
