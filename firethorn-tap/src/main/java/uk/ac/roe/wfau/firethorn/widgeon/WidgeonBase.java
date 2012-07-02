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

import java.net.URI;
import java.net.URL;

import javax.sql.DataSource;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.exception.*;

/**
 * Public interface for a base Widgeon, describing a real data resource (JDBC database OR TAP service).
 *
 */
public interface WidgeonBase
extends Widgeon
    {

    /**
     * Factory interface for creating and selecting Widgeons.
     *
     */
    public static interface Factory
    extends Entity.Factory<WidgeonBase>
        {

        /**
         * Select all of the Widgeons.
         *
         */
        public Iterable<WidgeonBase> select();

        /**
         * Select a Widgeon by name.
         *
         */
        public WidgeonBase select(String name)
        throws NameNotFoundException;

        /**
         * Create a Widgeon from a registry URI.
         * 
         */
        public WidgeonBase create(String name, URI uri);

        /**
         * Create a Widgeon from a VOSI URL.
         * 
         */
        public WidgeonBase create(String name, URL url);

        /**
         * Create a Widgeon from a JDBC DataSource.
         * 
         */
        public WidgeonBase create(String name, DataSource src);

        /**
         * Access to our View factory.
         * 
         */
        public WidgeonView.Factory views();

        /**
         * Access to our Catalog factory.
         * 
         */
        public WidgeonBase.Catalog.Factory catalogs();

        }

    /**
     * Public interface for accessing a Widgeon's Views.
     *
     */
    public interface Views
        {

        /*
         * Create a new View of the Widgeon.
         *
         */
        public WidgeonView create(String name);

        /*
         * Select all the Views of the Widgeon.
         *
         */
        public Iterable<WidgeonView> select();

        /*
         * Select a named View from the Widgeon.
         *
         */
        public WidgeonView select(String name)
        throws NameNotFoundException;

        }

    /**
     * Access to this Widgeon's Views.
     *
     */
    public WidgeonBase.Views views();

    /**
     * Public interface for accessing a Widgeon's Catalogs.
     *
     */
    public interface Catalogs
    extends Widgeon.Catalogs<WidgeonBase.Catalog>
        {

        /**
         * Create a new Catalog for the Widgeon.
         *
         */
        public WidgeonBase.Catalog create(String name);

        }

    /**
     * Access to this Widgeon's Catalogs.
     *
     */
    public Catalogs catalogs();

    /**
     * Public interface for Catalog metadata.
     *
     */
    public interface Catalog
    extends Widgeon.Catalog<WidgeonBase>
        {

        /**
         * Factory interface for creating and selecting Catalog.
         *
         */
        public static interface Factory
        extends Widgeon.Catalog.Factory<WidgeonBase, WidgeonBase.Catalog>
            {

            /**
             * Create a new Catalog for a Widgeon.
             *
             */
            public WidgeonBase.Catalog create(WidgeonBase parent, String name);

            /**
             * Access to our View factory.
             * 
             */
            public WidgeonView.Catalog.Factory views();

            /**
             * Access to our Schema factory.
             * 
             */
            public WidgeonBase.Catalog.Schema.Factory schemas();

            }

        /**
         * Public interface for accessing a Catalog's Views.
         *
         */
        public interface Views
            {

            /*
             * Select all the Catalog's Views.
             *
             */
            public Iterable<WidgeonView.Catalog> select();

            }

        /**
         * Access to this Catalog's Views.
         *
         */
        public WidgeonBase.Catalog.Views views();

        /**
         * Public interface for accessing a Catalog's Schemas.
         *
         */
        public interface Schemas
        extends Widgeon.Catalog.Schemas<WidgeonBase.Catalog.Schema>
            {

            /**
             * Create a new Schema for the Catalog.
             *
             */
            public WidgeonBase.Catalog.Schema create(String name);

            }

        /**
         * Access to this Catalog's Schemas.
         *
         */
        public Schemas schemas();

        /**
         * Public interface for Schema metadata.
         *
         */
        public interface Schema
        extends Widgeon.Catalog.Schema
            {

            /**
             * Factory interface for creating and selecting Schemas.
             *
             */
            public static interface Factory
            extends Widgeon.Catalog.Schema.Factory<WidgeonBase.Catalog, WidgeonBase.Catalog.Schema>
                {

                /**
                 * Create a new Schema for a Catalog.
                 *
                 */
                public WidgeonBase.Catalog.Schema create(WidgeonBase.Catalog parent, String name);

                /**
                 * Access to our View factory.
                 * 
                 */
                public WidgeonView.Catalog.Schema.Factory views();

                /**
                 * Access to our Table factory.
                 * 
                 */
                public WidgeonBase.Catalog.Schema.Table.Factory tables();

                }

            /**
             * Public interface for accessing a Schema's Views.
             *
             */
            public interface Views
                {

                /*
                 * Select all the Schema's Views.
                 *
                 */
                public Iterable<WidgeonView.Catalog.Schema> select();

                }

            /**
             * Access to this Schema's Views.
             *
             */
            public WidgeonBase.Catalog.Schema.Views views();

            /**
             * Public interface for accessing a Schema's Tables.
             *
             */
            public interface Tables
            extends Widgeon.Catalog.Schema.Tables<WidgeonBase.Catalog.Schema.Table>
                {

                /**
                 * Create a new Table for the Schema.
                 *
                 */
                public WidgeonBase.Catalog.Schema.Table create(String name);

                }

            /**
             * Access to this Schema's Tables.
             *
             */
            public Tables tables();

            /**
             * Public interface for Table metadata.
             *
             */
            public interface Table
            extends Widgeon.Catalog.Schema.Table
                {

                /**
                 * Factory interface for creating and selecting Tables.
                 *
                 */
                public static interface Factory
                extends Widgeon.Catalog.Schema.Table.Factory<WidgeonBase.Catalog.Schema, WidgeonBase.Catalog.Schema.Table>
                    {

                    /**
                     * Create a new Table for a Schema.
                     *
                     */
                    public WidgeonBase.Catalog.Schema.Table create(WidgeonBase.Catalog.Schema parent, String name);

                    /**
                     * Access to our View factory.
                     * 
                     */
                    public WidgeonView.Catalog.Schema.Table.Factory views();

                    /**
                     * Access to our Column factory.
                     * 
                     */
                    public WidgeonBase.Catalog.Schema.Table.Column.Factory columns();

                    }

                /**
                 * Public interface for accessing a Table's Views.
                 *
                 */
                public interface Views
                    {

                    /*
                     * Select all the Table's Views.
                     *
                     */
                    public Iterable<WidgeonView.Catalog.Schema.Table> select();

                    }

                /**
                 * Access to this Table's Views.
                 *
                 */
                public WidgeonBase.Catalog.Schema.Table.Views views();

                /**
                 * Public interface for accessing a Table's Columns.
                 *
                 */
                public interface Columns
                extends Widgeon.Catalog.Schema.Table.Columns<WidgeonBase.Catalog.Schema.Table.Column>
                    {

                    /**
                     * Create a new Column for the Table.
                     *
                     */
                    public WidgeonBase.Catalog.Schema.Table.Column create(String name);

                    }

                /**
                 * Access to this Table's Columns.
                 *
                 */
                public Columns columns();

                /**
                 * Public interface for Column metadata.
                 *
                 */
                public interface Column
                extends Widgeon.Catalog.Schema.Table.Column
                    {

                    /**
                     * Factory interface for creating and selecting Columns.
                     *
                     */
                    public static interface Factory
                    extends Widgeon.Catalog.Schema.Table.Column.Factory<WidgeonBase.Catalog.Schema.Table, WidgeonBase.Catalog.Schema.Table.Column>
                        {

                        /**
                         * Create a new Column for a Table.
                         *
                         */
                        public WidgeonBase.Catalog.Schema.Table.Column create(WidgeonBase.Catalog.Schema.Table parent, String name);

                        /**
                         * Access to our View factory.
                         * 
                         */
                        public WidgeonView.Catalog.Schema.Table.Column.Factory views();

                        }

                    /**
                     * Public interface for accessing a Column's Views.
                     *
                     */
                    public interface Views
                        {

                        /*
                         * Select all the Views of the Column.
                         *
                         */
                        public Iterable<WidgeonView.Catalog.Schema.Table.Column> select();

                        }

                    /**
                     * Access to this Column's Views.
                     *
                     */
                    public WidgeonBase.Catalog.Schema.Table.Column.Views views();

                    }
                }
            }
        }
    }

