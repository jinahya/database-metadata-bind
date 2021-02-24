package com.github.jinahya.database.metadata.bind;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

@XmlRootElement
public class Metadata implements Serializable {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    // -----------------------------------------------------------------------------------------------------------------
    public static Metadata newInstance(final MetadataContext context) throws SQLException {
        requireNonNull(context, "context is null");
        // -------------------------------------------------------------------------------------------------------------
        final Metadata instance = new Metadata();
        // -------------------------------------------------------------------------------------------------------------
        instance.allProceduresAreCallable = context.databaseMetaData.allProceduresAreCallable();
        instance.allTablesAreSelectable = context.databaseMetaData.allTablesAreSelectable();
        try {
            instance.autoCommitFailureClosesAllResultSets
                    = context.databaseMetaData.autoCommitFailureClosesAllResultSets();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        try {
            instance.dataDefinitionCausesTransactionCommit
                    = context.databaseMetaData.dataDefinitionCausesTransactionCommit();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        try {
            instance.dataDefinitionIgnoredInTransactions
                    = context.databaseMetaData.dataDefinitionIgnoredInTransactions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        for (final int type : new int[] {ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE}) {
            final DeletesAreDetected v = new DeletesAreDetected();
            v.setType(type);
            instance.deletesAreDetected.add(v);
            try {
                v.setValue(context.databaseMetaData.deletesAreDetected(v.getType()));
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
            }
        }
        try {
            instance.doesMaxRowSizeIncludeBlobs
                    = context.databaseMetaData.doesMaxRowSizeIncludeBlobs();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        try {
            instance.generatedKeyAlwaysReturned
                    = context.databaseMetaData.generatedKeyAlwaysReturned();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        // ---------------------------------------------------------------------------------------------------- catalogs
        context.getCatalogs(instance.getCatalog());
        if (instance.getCatalog().isEmpty()) {
            instance.getCatalog().add(Catalog.newVirtualInstance());
        }
        for (final Catalog catalog : instance.getCatalog()) {
            context.getSchemas(catalog);
        }
        try {
            instance.getCatalogSeparator = context.databaseMetaData.getCatalogSeparator();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        try {
            instance.getCatalogTerm = context.databaseMetaData.getCatalogTerm();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        // ------------------------------------------------------------------------------------------ clientInfoProperty
        context.getClientInfoProperties(instance.getClientInfoProperty());
        // -----------------------------------------------------------------------------------------------------------------
        try {
            instance.databaseMajorVersion = context.databaseMetaData.getDatabaseMajorVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        try {
            instance.databaseMinorVersion = context.databaseMetaData.getDatabaseMinorVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        // -----------------------------------------------------------------------------------------------------------------
        try {
            instance.databaseProductName = context.databaseMetaData.getDatabaseProductName();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        try {
            instance.databaseProductVersion = context.databaseMetaData.getDatabaseProductVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        // -----------------------------------------------------------------------------------------------------------------
        try {
            instance.defaultTransactionIsolation = context.databaseMetaData.getDefaultTransactionIsolation();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        // -----------------------------------------------------------------------------------------------------------------
//        try {
            instance.driverMajorVersion = context.databaseMetaData.getDriverMajorVersion();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
//        }
//        try {
            instance.driverMinorVersion = context.databaseMetaData.getDriverMinorVersion();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
//        }
        // -----------------------------------------------------------------------------------------------------------------
        try {
            instance.driverName = context.databaseMetaData.getDriverName();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        try {
            instance.driverVersion = context.databaseMetaData.getDriverVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        // -----------------------------------------------------------------------------------------------------------------
        try {
            instance.extraNameCharacters = context.databaseMetaData.getExtraNameCharacters();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        // -----------------------------------------------------------------------------------------------------------------
        try {
            instance.identifierQuoteString = context.databaseMetaData.getIdentifierQuoteString();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        // -----------------------------------------------------------------------------------------------------------------
        try {
            instance.JDBCMajorVersion = context.databaseMetaData.getJDBCMajorVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        try {
            instance.JDBCMinorVersion = context.databaseMetaData.getJDBCMinorVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        // --------------------------------------------------------------------------------------------------- typeInfos
        context.getTypeInfo(instance.getTypeInfo());
        // -------------------------------------------------------------------------------------------------------------
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Metadata() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public boolean isAllProceduresAreCallable() {
        return allProceduresAreCallable;
    }

    public boolean isAllTablesAreSelectable() {
        return allTablesAreSelectable;
    }

    public Boolean getAutoCommitFailureClosesAllResultSets() {
        return autoCommitFailureClosesAllResultSets;
    }

    public Boolean getDataDefinitionCausesTransactionCommit() {
        return dataDefinitionCausesTransactionCommit;
    }

    public Boolean getDataDefinitionIgnoredInTransactions() {
        return dataDefinitionIgnoredInTransactions;
    }

    public List<DeletesAreDetected> getDeletesAreDetected() {
        return deletesAreDetected;
    }

    public Boolean getDoesMaxRowSizeIncludeBlobs() {
        return doesMaxRowSizeIncludeBlobs;
    }

    // -------------------------------------------------------------------------------------------------------- catalogs
    public List<Catalog> getCatalog() {
        if (catalog == null) {
            catalog = new ArrayList<>();
        }
        return catalog;
    }

    public String getGetCatalogSeparator() {
        return getCatalogSeparator;
    }

    public String getGetCatalogTerm() {
        return getCatalogTerm;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public List<ClientInfoProperty> getClientInfoProperty() {
        if (clientInfoProperty == null) {
            clientInfoProperty = new ArrayList<>();
        }
        return clientInfoProperty;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Integer getDatabaseMajorVersion() {
        return databaseMajorVersion;
    }

    public Integer getDatabaseMinorVersion() {
        return databaseMinorVersion;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public String getDatabaseProductVersion() {
        return databaseProductVersion;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Integer getDefaultTransactionIsolation() {
        return defaultTransactionIsolation;
    }
    // -----------------------------------------------------------------------------------------------------------------
    public Integer getDriverMajorVersion() {
        return driverMajorVersion;
    }

    public Integer getDriverMinorVersion() {
        return driverMinorVersion;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getDriverName() {
        return driverName;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getExtraNameCharacters() {
        return extraNameCharacters;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getIdentifierQuoteString() {
        return identifierQuoteString;
    }
    // -----------------------------------------------------------------------------------------------------------------
    public Integer getJDBCMajorVersion() {
        return JDBCMajorVersion;
    }

    public Integer getJDBCMinorVersion() {
        return JDBCMinorVersion;
    }

    // ------------------------------------------------------------------------------------------------------- typeInfos
    public List<TypeInfo> getTypeInfo() {
        if (typeInfo == null) {
            typeInfo = new ArrayList<>();
        }
        return typeInfo;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement
    private boolean allProceduresAreCallable;

    @XmlElement
    private boolean allTablesAreSelectable;

    @XmlElement(nillable = true)
    private Boolean autoCommitFailureClosesAllResultSets;

    @XmlElement(nillable = true)
    private Boolean dataDefinitionCausesTransactionCommit;

    @XmlElement(nillable = true)
    private Boolean dataDefinitionIgnoredInTransactions;

    @XmlElement(nillable = true)
    private List<DeletesAreDetected> deletesAreDetected = new ArrayList<>();

    @XmlElement(nillable = true)
    private Boolean doesMaxRowSizeIncludeBlobs;

    @XmlElement(nillable = true)
    private Boolean generatedKeyAlwaysReturned;

    //    @XmlElementWrapper(required = true, nillable = true)
    @XmlElementRef
    private List<@Valid @NotNull Catalog> catalog;

    @XmlElement(nillable = true)
    private String getCatalogSeparator;

    @XmlElement(nillable = true)
    private String getCatalogTerm;

    @XmlElementRef
    private List<ClientInfoProperty> clientInfoProperty;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer databaseMajorVersion;

    @XmlElement(nillable = true)
    private Integer databaseMinorVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private String databaseProductName;

    @XmlElement(nillable = true)
    private String databaseProductVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer defaultTransactionIsolation;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer driverMajorVersion;

    @XmlElement(nillable = true)
    private Integer driverMinorVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private String driverName;

    @XmlElement(nillable = true)
    private String driverVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private String extraNameCharacters;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private String identifierQuoteString;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer JDBCMajorVersion;

    @XmlElement(nillable = true)
    private Integer JDBCMinorVersion;

    // -----------------------------------------------------------------------------------------------------------------
    //    @XmlElementWrapper(required = true, nillable = true)
    @XmlElementRef
    private List<@Valid @NotNull TypeInfo> typeInfo;
}
