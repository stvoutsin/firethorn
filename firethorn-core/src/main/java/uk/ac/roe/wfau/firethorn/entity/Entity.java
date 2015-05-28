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
package uk.ac.roe.wfau.firethorn.entity ;

import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.entity.access.EntityProtector;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent;

/**
 * Common interface for a persistent Entity.
 *
 */
public interface Entity
    {

    /**
     * Common interface for a name factory.
     *
    public interface NameFactory<EntityType extends Entity>
        {
        }
     */

    /**
     * Common interface for an alias factory.
     *
     */
    public interface AliasFactory<EntityType extends Entity>
        {
        /**
         * Generate an alias.
         *
         */
        public String alias(final EntityType entity);

        /**
         * Check if this {@link AliasFactory} can parse a {@link String}.
         *
         */
        public boolean matches(final String alias);
        
        /**
         * Resolve an alias into an entity.
         *
         */
        public EntityType resolve(final String alias)
        throws EntityNotFoundException;
        
        }

    /**
     * Common interface for a link factory.
     *
     */
    public interface LinkFactory<EntityType extends Entity>
        {
        /**
         * Create an link (as a string).
         *
         */
        public String link(final EntityType entity);

        /**
         * Check if this {@link LinkFactory} can parse a {@link String}.
         * 
         */
        public boolean matches(final String link);

        /**
         * Resolve a link into an {@link Identifier}.
         *
         */
        public Identifier ident(final String link);

        /**
         * Resolve a link into an entity.
         *
         */
        public EntityType resolve(final String link)
        throws IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException;

        }

    /**
     * Common interface for an Identifier factory.
     *
     */
    public interface IdentFactory<EntityType extends Entity>
        {
        /**
         * Create an Identifier from a String.
         *
         */
        public Identifier ident(final String string)
        throws IdentifierFormatException;

        /**
         * Resolve an {@link Identifier} into an entity.
         *
        public EntityType resolve(final Identifier  ident)
        throws IdentifierFormatException, IdentifierNotFoundException;
         */

        /**
         * Resolve a {@link String} into an entity.
         *
        public EntityType resolve(final String ident)
        throws IdentifierFormatException, IdentifierNotFoundException;
         */
        }

    /**
     * Common interface for an Entity factory.
     *
     */
    public interface EntityFactory<EntityType extends Entity>
        {
        /**
         * Select a specific Entity by Identifier.
         *
         */
        public EntityType select(final Identifier ident)
        throws IdentifierNotFoundException;

        /**
         * Search for a specific Entity by Identifier.
         * @return The matching Entity, or null if not found.
         *
         */
        public EntityType search(final Identifier ident);

        /**
         * Our local Identifier factory.
         *
         */
        public Entity.IdentFactory<EntityType> idents();

        /**
         * Our local link factory.
         *
         */
        public LinkFactory<EntityType> links();

        }

    /**
     * Our parent {@link Entity.EntityFactory}.
     *
     */
    public Entity.EntityFactory<?> factory();

    /**
     * The Entity Identifier.
     *
     */
    public Identifier ident();

    /**
     * The Entity URI (as a string).
     *
     */
    public String link();

    /**
     * Get the Entity owner.
     *
     */
    public Identity owner();

    /**
     * The date/time when the Entity was created.
     *
     */
    public DateTime created();

    /**
     * The date/time when the Entity was last modified.
     *
     */
    public DateTime modified();

    /**
     * The Entity Protector.
     * 
     */
    public EntityProtector protector();

    }

