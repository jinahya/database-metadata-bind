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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class for binding results of {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String,
 * java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctions(String, String, String, Collection)
 * @see FunctionColumn
 */
@XmlRootElement
@Data
@NoArgsConstructor
public class Function
        implements MetadataType,
                   ChildOf<Schema> {

    private static final long serialVersionUID = -3318947900237453301L;

    // -------------------------------------------------------------------------------------- FUNCTION_CAT / functionCat
    public static final String COLUMN_NAME_FUNCTION_CAT = "FUNCTION_CAT";

    public static final String ATTRIBUTE_NAME_FUNCTION_CAT = "functionCat";

    // ---------------------------------------------------------------------------------- FUNCTION_SCHEM / functionSchem
    public static final String COLUMN_NAME_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    public static final String ATTRIBUTE_NAME_FUNCTION_SCHEM = "functionSchem";

    // ------------------------------------------------------------------------------------------------- functionColumns

    /**
     * Returns function columns of this function.
     *
     * @return a list of function columns of this function.
     */
    public List<FunctionColumn> getFunctionColumns() {
        if (functionColumns == null) {
            functionColumns = new ArrayList<>();
        }
        return functionColumns;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label(COLUMN_NAME_FUNCTION_CAT)
    private String functionCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label(COLUMN_NAME_FUNCTION_SCHEM)
    private String functionSchem;

    @XmlElement(required = true)
    @Label("FUNCTION_NAME")
    @EqualsAndHashCode.Exclude
    private String functionName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true)
    @Label("FUNCTION_TYPE")
    private short functionType;

    @XmlElement(required = true)
    @Label("SPECIFIC_NAME")
    private String specificName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<FunctionColumn> functionColumns;
}
