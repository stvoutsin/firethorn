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

import uk.ac.roe.wfau.firethorn.access.Protected;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.access.EntityProtector;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * Common interface for a persistent Entity.
 *
 */
public interface Entity
extends Protected
    {

    /**
     * Access to our service instances.
     *
     */
    public interface EntityServices<EntityType extends Entity>
        {
        /**
         * Our {@link Entity.IdentFactory} instance.
         *
         */
        public Entity.IdentFactory<EntityType> idents();

        /**
         * Our {@link Entity.LinkFactory} instance.
         *
         */
        public LinkFactory<EntityType> links();

        /**
         * Our {@link Entity.EntityFactory} instance.
         *
         */
        public EntityFactory<EntityType> entities();

        }

    /**
     * Common interface for an alias factory.
     *
     */
    public interface AliasFactory<EntityType extends Entity>
        {
        /**
         * Generate an alias.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public String alias(final EntityType entity)
        throws ProtectionException;

        /**
         * Check if this {@link AliasFactory} can parse a {@link String}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public boolean matches(final String alias)
        throws ProtectionException;
        
        /**
         * Resolve an alias into an entity.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public EntityType resolve(final String alias)
        throws ProtectionException, EntityNotFoundException;
        
        }

    /**
     * Common interface for a link factory.
     *
     */
    public interface LinkFactory<EntityType extends Entity>
        {
        /**
         * The name for an identifier field.
         * TODO Move this to a webapp model class.
         * 
         */
        public static final String IDENT_FIELD = "ident" ;

        /**
         * The token to match an identifier field.
         * TODO Move this to a webapp model class.
         * 
         */
        public static final String IDENT_TOKEN = "{ident}" ;

        /**
         * The regular expression pattern to match an identifier field.
         * TODO Move this to a webapp model class.
         * 
        public static final String IDENT_REGEX = "\\{ident\\}" ;
         */
        
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
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public EntityType resolve(final String link)
        throws ProtectionException, IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException;

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
    extends Protected
        {
        /**
         * Select a specific Entity by Identifier.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public EntityType select(final Identifier ident)
        throws ProtectionException, IdentifierNotFoundException;

        /**
         * Search for a specific Entity by Identifier.
         * @return The matching Entity, or null if not found.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public EntityType search(final Identifier ident)
        throws ProtectionException;
        
        }

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
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public Identity owner();

    /**
     * The date/time when the Entity was created.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public DateTime created();

    /**
     * The date/time when the Entity was last modified.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public DateTime modified();

    /**
     * The Entity Protector.
     * 
     */
    public EntityProtector protector();

    }

