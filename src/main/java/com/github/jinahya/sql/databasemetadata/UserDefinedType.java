/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
 *
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
 */


package com.github.jinahya.sql.databasemetadata;


import com.github.jinahya.sql.databasemetadata.ColumnLabel;
import com.github.jinahya.sql.databasemetadata.ColumnRetriever;
import com.github.jinahya.sql.databasemetadata.Suppression;
import com.github.jinahya.sql.databasemetadata.SuppressionPath;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlType(
    propOrder = {
        "typeName", "className", "dataType", "remarks", "baseType", "attributes"
    }
)
public class UserDefinedType {


    public static final String SUPPRESSION_PATH_ATTRIBUTES
        = "userDefinedType/attributes";


    public static UserDefinedType retrieve(
        final Suppression suppression, final ResultSet resultSet)
        throws SQLException {

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(resultSet, "null resultSte");

        final UserDefinedType instance = new UserDefinedType();

        ColumnRetriever.retrieve(
            UserDefinedType.class, instance, suppression, resultSet);

        return instance;
    }


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schemaPattern
     * @param typeNamePattern
     * @param types
     * @param userDefinedTypes
     *
     * @throws SQLException
     *
     * @see DatabaseMetaData#getUDTs(java.lang.String, java.lang.String,
     * java.lang.String, int[])
     */
    public static void retrieve(
        final DatabaseMetaData database, final Suppression suppression,
        final String catalog, final String schemaPattern,
        final String typeNamePattern, final int[] types,
        final Collection<? super UserDefinedType> userDefinedTypes)
        throws SQLException {

        Objects.requireNonNull(database, "null database");

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(userDefinedTypes, "null userDefinedTypes");

        if (suppression.isSuppressed(
            Schema.SUPPRESSION_PATH_USER_DEFINED_TYPES)) {
            return;
        }

        final ResultSet resultSet = database.getUDTs(
            catalog, schemaPattern, typeNamePattern, types);
        try {
            while (resultSet.next()) {
                userDefinedTypes.add(retrieve(suppression, resultSet));
            }
        } finally {
            resultSet.close();
        }
    }


    /**
     *
     * @param database
     * @param suppression
     * @param schema
     *
     * @throws SQLException
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Schema schema)
        throws SQLException {

        Objects.requireNonNull(database, "null database");

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(schema, "null schema");

        retrieve(database, suppression,
                 schema.getCatalog().getTableCat(), schema.getTableSchem(),
                 null, null,
                 schema.getUserDefinedTypes());

        for (final UserDefinedType userDefinedType
             : schema.getUserDefinedTypes()) {
            userDefinedType.setSchema(schema);
        }

        for (final UserDefinedType uesrDefinedType
             : schema.getUserDefinedTypes()) {
            Attribute.retrieve(database, suppression, uesrDefinedType);
        }
    }


    /**
     * Creates a new instance.
     */
    public UserDefinedType() {

        super();
    }


    // ------------------------------------------------------------------ schema
    public Schema getSchema() {

        return schema;
    }


    public void setSchema(final Schema schema) {

        this.schema = schema;
    }


    // ---------------------------------------------------------------- typeName
    public String getTypeName() {

        return typeName;
    }


    public void setTypeName(final String typeName) {

        this.typeName = typeName;
    }


    // --------------------------------------------------------------- className
    public String getClassName() {

        return className;
    }


    public void setClassName(final String className) {

        this.className = className;
    }


    // ---------------------------------------------------------------- dataType
    public int getDataType() {

        return dataType;
    }


    public void setDataType(final int dataType) {

        this.dataType = dataType;
    }


    // ----------------------------------------------------------------- remarks
    public String getRemarks() {

        return remarks;
    }


    public void setRemarks(final String remarks) {

        this.remarks = remarks;
    }


    // ---------------------------------------------------------------- baseType
    public Short getBaseType() {

        return baseType;
    }


    public void setBaseType(final Short baseType) {

        this.baseType = baseType;
    }


    // -------------------------------------------------------------- attributes
    public List<Attribute> getAttributes() {

        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }

        return attributes;
    }


    /**
     * the type's catalog (may be {@code null}).
     */
    @ColumnLabel("TYPE_CAT")
    @SuppressionPath("userDefinedType/typeCat")
    @XmlAttribute
    private String typeCat;


    /**
     * type's schema (may be {@code null}).
     */
    @ColumnLabel("TYPE_CAT")
    @SuppressionPath("userDefinedType/typeSchem")
    @XmlAttribute
    private String typeSchem;


    /**
     * schema.
     */
    @XmlTransient
    private Schema schema;


    /**
     * type name.
     */
    @ColumnLabel("TYPE_NAME")
    @XmlElement(required = true)
    protected String typeName;


    /**
     * Java class name.
     */
    @ColumnLabel("CLASS_NAME")
    @XmlElement(required = true)
    protected String className;


    /**
     * type value defined in {@link java.sql.Types}. One of
     * {@link java.sql.Types#JAVA_OBJECT}, {@link java.sql.Types#STRUCT}, or
     * {@link java.sql.Types#DISTINCT}.
     */
    @ColumnLabel("DATA_TYPE")
    @XmlElement(required = true)
    protected int dataType;


    /**
     * explanatory comment on the type.
     */
    @ColumnLabel("REMARKS")
    @XmlElement(required = true)
    protected String remarks;


    /**
     * type code of the source type of a {@link java.sql.Types#DISTINCT} type or
     * the type that implements the user-generated reference type of the
     * SELF_REFERENCING_COLUMN of a structured type as defined in
     * {@link java.sql.Types} ({@code null} if {@link #dataType DATA_TYPE} is
     * not {@link java.sql.Types#DISTINCT} or not {@link java.sql.Types#STRUCT}
     * with REFERENCE_GENERATION = USER_DEFINED)
     */
    @ColumnLabel("BASE_TYPE")
    //@SuppressionPath("userDefinedType/baseType")
    @XmlElement(nillable = true, required = true)
    @XmlElementNillableBySpecification
    protected Short baseType;


    /**
     * attributes.
     */
    @SuppressionPath(SUPPRESSION_PATH_ATTRIBUTES)
    @XmlElement(name = "attribute")
    protected List<Attribute> attributes;


}

