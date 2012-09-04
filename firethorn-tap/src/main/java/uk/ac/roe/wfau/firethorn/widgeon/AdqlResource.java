/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.widgeon ;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;

/**
 * Public interface for a ADQL view of a DataResource.
 *
 */
public interface AdqlResource
extends DataResource
    {

    /**
     * Factory interface for creating and selecting views.
     *
     */
    public static interface Factory
    extends Entity.Factory<AdqlResource>
        {

        /**
         * Create a view of a resource.
         *
         */
        public AdqlResource create(BaseResource base, String name);

        /**
         * Select all the views of a resource.
         *
         */
        public Iterable<AdqlResource> select(BaseResource base);

        /**
         * Select a view of a resource by name.
         *
         */
        public AdqlResource select(BaseResource base, String name)
        throws NameNotFoundException;

        /**
         * Search for a view of a resource by name.
         *
         */
        public AdqlResource search(BaseResource base, String name);

        /**
         * Access to our catalog factory.
         * 
         */
        public AdqlResource.AdqlCatalog.Factory catalogs();

        }

    /**
     * Access to our base resource.
     *
     */
    public BaseResource base();

    /**
     * Public interface for accessing a resource's catalogs.
     *
     */
    public interface Catalogs
    extends DataResource.Catalogs<AdqlResource.AdqlCatalog>
        {
        }

    /**
     * Access to this resource's catalogs.
     *
     */
    public Catalogs catalogs();

    /**
     * Public interface for a catalog view.
     *
     */
    public interface AdqlCatalog
    extends DataResource.DataCatalog<AdqlResource>
        {

        /**
         * Factory interface for creating and selecting catalog views.
         *
         */
        public static interface Factory
        extends DataResource.DataCatalog.Factory<AdqlResource, AdqlResource.AdqlCatalog>
            {

            /**
             * Find an existing catalog view, or create a new one.
             *
             */
            public AdqlResource.AdqlCatalog cascade(AdqlResource parent, BaseResource.BaseCatalog<?> base);

            /**
             * Create a new view of a catalog.
             *
             */
            public AdqlResource.AdqlCatalog create(AdqlResource parent, BaseResource.BaseCatalog<?> base, String name);

            /**
             * Select all the views of a catalog.
             *
             */
            public Iterable<AdqlResource.AdqlCatalog> select(BaseResource.BaseCatalog<?> base);

            /**
             * Search for a specific view of a catalog.
             *
             */
            public AdqlResource.AdqlCatalog search(AdqlResource parent, BaseResource.BaseCatalog<?> base);

            /**
             * Access to our schema factory.
             * 
             */
            public AdqlResource.AdqlSchema.Factory schemas();

            }

        /**
         * Access to our base catalog.
         *
         */
        public BaseResource.BaseCatalog<?> base();

        /**
         * Public interface for accessing a catalog's schemas.
         *
         */
        public interface Schemas
        extends DataResource.DataCatalog.Schemas<AdqlResource.AdqlSchema>
            {
            }

        /**
         * Access to this catalog's schemas.
         *
         */
        public Schemas schemas();

        /**
         * Access to our parent resource.
         *
         */
        public AdqlResource resource();

        }

    /**
     * Public interface for schema metadata.
     *
     */
    public interface AdqlSchema
    extends DataResource.DataSchema<AdqlResource.AdqlCatalog>
        {

        /**
         * Factory interface for creating and selecting schemas.
         *
         */
        public static interface Factory
        extends DataResource.DataSchema.Factory<AdqlResource.AdqlCatalog, AdqlResource.AdqlSchema>
            {

            /**
             * Find an existing schema view, or create a new one.
             *
             */
            public AdqlResource.AdqlSchema cascade(AdqlResource.AdqlCatalog parent, BaseResource.BaseSchema<?> base);

            /**
             * Create a new view of a schema.
             *
             */
            public AdqlResource.AdqlSchema create(AdqlResource.AdqlCatalog parent, BaseResource.BaseSchema<?> base, String name);

            /**
             * Select all the views of a schema.
             *
             */
            public Iterable<AdqlResource.AdqlSchema> select(BaseResource.BaseSchema<?> base);

            /**
             * Search for a specific view of a schema.
             *
             */
            public AdqlResource.AdqlSchema search(AdqlResource.AdqlCatalog parent, BaseResource.BaseSchema<?> base);

            /**
             * Access to our table factory.
             * 
             */
            public AdqlResource.AdqlTable.Factory tables();
            
            }

        /**
         * Access to our base schema.
         *
         */
        public BaseResource.BaseSchema<?> base();

        /**
         * Public interface for accessing a schema's tables.
         *
         */
        public interface Tables
        extends DataResource.DataSchema.Tables<AdqlResource.AdqlTable>
            {
            }

        /**
         * Access to this schema's tables.
         *
         */
        public Tables tables();

        /**
         * Access to our parent resource.
         *
         */
        public AdqlResource resource();

        /**
         * Access to our parent catalog.
         *
         */
        public AdqlResource.AdqlCatalog catalog();

        }

    /**
     * Public interface for table metadata.
     *
     */
    public interface AdqlTable
    extends DataResource.DataTable<AdqlResource.AdqlSchema>
        {

        /**
         * Factory interface for creating and selecting tables.
         *
         */
        public static interface Factory
        extends DataResource.DataTable.Factory<AdqlResource.AdqlSchema, AdqlResource.AdqlTable>
            {

            /**
             * Find an existing table view, or create a new one.
             *
             */
            public AdqlResource.AdqlTable cascade(AdqlResource.AdqlSchema parent, BaseResource.BaseTable<?> base);

            /**
             * Create a new view of a table.
             *
             */
            public AdqlResource.AdqlTable create(AdqlResource.AdqlSchema parent, BaseResource.BaseTable<?> base, String name);

            /**
             * Select all the views of a table.
             *
             */
            public Iterable<AdqlResource.AdqlTable> select(BaseResource.BaseTable<?> base);

            /**
             * Search for a specific view of a table.
             *
             */
            public AdqlResource.AdqlTable search(AdqlResource.AdqlSchema parent, BaseResource.BaseTable<?> base);

            /**
             * Access to our column factory.
             * 
             */
            public AdqlResource.AdqlColumn.Factory adqlColumns();

            }

        /**
         * Access to our base table.
         *
         */
        public BaseResource.BaseTable<?> base();

        /**
         * Public interface for accessing a table's adqlColumns.
         *
         */
        public interface Columns
        extends DataResource.DataTable.Columns<AdqlResource.AdqlColumn>
            {

            /**
             * Search for a view of a specific column.
             *
             */
            public AdqlResource.AdqlColumn search(BaseResource.BaseColumn<?> base);

            }

        /**
         * Access to this table's adqlColumns.
         *
         */
        public Columns columns();

        /**
         * Access to our parent resource.
         *
         */
        public AdqlResource resource();

        /**
         * Access to our parent catalog.
         *
         */
        public AdqlResource.AdqlCatalog catalog();

        /**
         * Access to our parent schema.
         *
         */
        public AdqlResource.AdqlSchema schema();

        }

    /**
     * Public interface for column metadata.
     *
     */
    public interface AdqlColumn
    extends DataResource.DataColumn<AdqlResource.AdqlTable>
        {

        /**
         * Factory interface for creating and selecting adqlColumns.
         *
         */
        public static interface Factory
        extends DataResource.DataColumn.Factory<AdqlResource.AdqlTable, AdqlResource.AdqlColumn>
            {

            /**
             * Find an existing column view, or create a new one.
             *
             */
            public AdqlResource.AdqlColumn cascade(AdqlResource.AdqlTable parent, BaseResource.BaseColumn<?> base);

            /**
             * Create a new View of a column.
             *
             */
            public AdqlResource.AdqlColumn create(AdqlResource.AdqlTable parent, BaseResource.BaseColumn<?> base, String name);

            /**
             * Search for a specific view of a column.
             *
             */
            public AdqlResource.AdqlColumn search(AdqlResource.AdqlTable parent, BaseResource.BaseColumn<?> base);

            /**
             * Select all the views of a column.
             *
             */
            public Iterable<AdqlResource.AdqlColumn> select(BaseResource.BaseColumn<?> base);

            }

        /**
         * Access to our base column.
         *
         */
        public BaseResource.BaseColumn<?> base();

        /**
         * Access to our parent resource.
         *
         */
        public AdqlResource resource();

        /**
         * Access to our parent catalog.
         *
         */
        public AdqlResource.AdqlCatalog catalog();

        /**
         * Access to our parent schema.
         *
         */
        public AdqlResource.AdqlSchema schema();

        /**
         * Access to our parent table.
         *
         */
        public AdqlResource.AdqlTable table();

        }
    }

