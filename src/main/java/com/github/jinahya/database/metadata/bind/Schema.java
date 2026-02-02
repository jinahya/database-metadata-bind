package com.github.jinahya.database.metadata.bind;

import org.jspecify.annotations.Nullable;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSchemas(String, String)
 */
@_ChildOf(Catalog.class)
@_ParentOf(Table.class)
public class Schema
        extends AbstractMetadataType {

    private static final long serialVersionUID = 7457236468401244963L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS
    static Comparator<Schema> comparingInSpecifiedOrder(final Context context,
                                                        final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        final var nullSafe = ContextUtils.nullPrecedence(context, comparator);
        return Comparator
                .comparing(Schema::getTableCatalog, nullSafe)
                .thenComparing(Schema::getTableSchem, nullSafe);
    }

    static Comparator<Schema> comparingInSpecifiedOrder(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return comparingInSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER);
    }

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // --------------------------------------------------------------------------------------------------- TABLE_CATALOG

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CATALOG = "TABLE_CATALOG";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS
    static Schema of(final String tableCatalog, final String tableSchem) {
        final var instance = new Schema();
        instance.setTableCatalog(tableCatalog);
        instance.setTableSchem(tableSchem);
        return instance;
    }

    static Schema of(final Catalog tableCatalog, final String tableSchem) {
        return of(
                Optional.ofNullable(tableCatalog)
                        .map(Catalog::getTableCat)
                        .orElse(null),
                tableSchem
        );
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    @SuppressWarnings({
            "java:S2637" // "@NonNull" values should not be set to null
    })
    Schema() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableSchem=" + tableSchem +
               ",tableCatalog=" + tableCatalog +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final var that = (Schema) obj;
        return Objects.equals(tableCatalog, that.tableCatalog) &&
               Objects.equals(tableSchem, that.tableSchem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableCatalog, tableSchem);
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
    public String getTableSchem() {
        return tableSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @param tableSchem the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
    void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ---------------------------------------------------------------------------------------------------- tableCatalog

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_CATALOG} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_CATALOG} column.
     */
    @Nullable
    public String getTableCatalog() {
        return tableCatalog;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_CATALOG} column.
     *
     * @param tableCatalog the value of {@value #COLUMN_LABEL_TABLE_CATALOG} column.
     */
    void setTableCatalog(final String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CATALOG)
    private String tableCatalog;
}
