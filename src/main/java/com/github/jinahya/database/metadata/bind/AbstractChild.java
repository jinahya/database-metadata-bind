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

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlTransient;

abstract class AbstractChild<P extends MetadataType> implements Child<P> {

    AbstractChild() {
        super();
    }

    @Override
    public P getParent_() {
        return parent_;
    }

    @Override
    public void setParent_(final P parent) {
        this.parent_ = parent;
    }

    @XmlTransient
    @Valid
    private P parent_;
}
