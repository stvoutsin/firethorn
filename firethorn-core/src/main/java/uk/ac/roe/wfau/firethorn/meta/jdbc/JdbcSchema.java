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
package uk.ac.roe.wfau.firethorn.meta.jdbc;

import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;

/**
 *
 *
 */
public interface JdbcSchema
extends BaseSchema<JdbcSchema, JdbcTable>
    {
    /**
     * Name factory interface.
     *
     */
    public static interface NameFactory
    extends Entity.NameFactory<JdbcSchema>
        {
        /**
         * Create a new date based name.
         *
         */
        public String datename();

        /**
         * Create a new date based name.
         *
         */
        public String datename(final Identity identity);

        /**
         * Create a new date based name.
         *
         */
        public String datename(final String prefix, final Identity identity);

        /**
         * Create a physical schema name from logical catalog and schema names.
         *
         */
        public String fullname(final String catalog, final String schema);

        }

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<JdbcSchema>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory
        {
        }

    /**
     * Schema factory interface.
     *
     */
    public static interface EntityFactory
    extends BaseSchema.EntityFactory<JdbcResource, JdbcSchema>
        {
        /**
         * Create a new schema for an identity.
         * This actually creates the schema in the database.
         *
         */
        public JdbcSchema build(final JdbcResource parent, final Identity identity);

        /**
         * Create a new schema.
         *
         */
        public JdbcSchema create(final JdbcResource parent, final String catalog, final String schema);

        /**
         * Select a schema.
         *
         */
        public JdbcSchema select(final JdbcResource parent, final String catalog, final String schema)
        throws EntityNotFoundException;

        /**
         * Search for a schema.
         *
         */
        public JdbcSchema search(final JdbcResource parent, final String catalog, final String schema);

        /**
         * Select the schemas for an Identity.
         *
         */
        public Iterable<JdbcSchema> select(final JdbcResource parent, final Identity identity);

        /**
         * The schema table factory.
         *
         */
        public JdbcTable.EntityFactory tables();

        /**
         * NameFactory implementation.
         *
         */
        public JdbcSchema.NameFactory names();

        /**
         * Builder implementation.
         *
         */
        public JdbcSchema.Builder builder();

        }

    /**
     * Builder interface that manipulates 'real' JDBC schemas.
     *
     */
    public static interface Builder
        {
        /**
         * Create a JDBC schema.
         * @return
         *
         */
        public JdbcSchema create(final JdbcSchema schema);

        /**
         * Delete a JDBC schema.
         *
         */
        public void delete(final JdbcSchema schema);

        }

    @Override
    public JdbcResource resource();

    /**
     * Access to the schema tables.
     *
     */
    public interface Tables extends BaseSchema.Tables<JdbcTable>
        {
        /**
         *  Create a new table.
         *
         */
        public JdbcTable create(final String name);

        /**
         *  Create a new table.
         *
         */
        public JdbcTable create(final String name, final JdbcTable.JdbcType type);

        /**
         *  Create a new table.
         *
         */
        public JdbcTable create(final AdqlQuery query);

        /**
         * Update the tables.
         *
         */
        public void scan();

        /**
         * Get the next set of tables to process. 
         *
         */
        public Iterable<JdbcTable> pending(final DateTime date);

        }
    @Override
    public Tables tables();

    /**
     * The catalog name.
     *
     */
    public String catalog();

    /**
     * The schema name.
     *
     */
    public String schema();

    }
