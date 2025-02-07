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

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.BaseProtector;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.access.Action;
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * Generic base class for a persistent Entity Factory.
 *
 */
@Slf4j
@Repository
public abstract class AbstractEntityFactory<EntityType extends Entity>
extends AbstractComponent
implements Entity.EntityFactory<EntityType>
    {

    /**
     * Get the class of Entity we manage.
     * Required because we can't get this from the generics at runtime, because ....
     * http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
     * http://stackoverflow.com/questions/2225979/getting-t-class-despite-javas-type-erasure
     *
     */
    public abstract Class<?> etype();

    @Override
    @SelectMethod
    @SuppressWarnings("unchecked")
    public EntityType search(final Identifier ident)
    throws ProtectionException
        {
        log.trace("search() [{}]", (ident != null) ? ident.value() : null);
        return (EntityType) factories().hibernate().select(
            etype(),
            ident
            );
        }
    
    @Override
    @SelectMethod
    public EntityType select(final Identifier ident)
    throws IdentifierNotFoundException, ProtectionException
        {
        log.trace("select() [{}]", (ident != null) ? ident.value() : null);
        final EntityType result = search(
            ident
            );
        if (result != null)
            {
            return result ;
            }
        else {
            throw new IdentifierNotFoundException(
                ident
                );
            }
        }

    /**
     * Get a named query.
     *
     */
    protected Query query(final String name)
        {
        return factories().hibernate().query(
            name
            );
        }

    /**
     * Insert a new Entity into the database.
     *
     */
    @SuppressWarnings("unchecked")
    protected EntityType insert(final EntityType entity)
        {
        log.trace("insert [{}]", entity);
        return (EntityType) factories().hibernate().insert(
            entity
            );
        }

    /**
     * Flush changes to the database.
     *
     */
    protected void flush()
        {
        factories().hibernate().flush();
        }

    /**
     * Clear the session state, discarding unsaved changes.
     *
     */
    protected void clear()
        {
        factories().hibernate().clear();
        }

    /**
     * Select a single object from the results of a query, or throws NotFoundException if the results are empty.
     *
     */
    @SelectMethod
    protected EntityType single(final Query query)
    throws EntityNotFoundException
        {
        @SuppressWarnings("unchecked")
        final EntityType result = (EntityType) factories().hibernate().single(
            query
            );
        if (result != null)
            {
            return result ;
            }
        else {
            throw new EntityNotFoundException();
            }
        }

    /**
     * Return the first result of a query, or null if the results are empty.
     *
     */
    @SelectMethod
    @SuppressWarnings("unchecked")
    protected EntityType first(final Query query)
        {
        return (EntityType) factories().hibernate().first(
            query
            );
        }

    /**
     * Select an Iterable set of objects.
     *
     */
    @SelectMethod
    protected Iterable<EntityType> iterable(final Query query)
        {
        return new Iterable<EntityType>()
            {
            @Override
            @SelectMethod
            @SuppressWarnings("unchecked")
            public Iterator<EntityType> iterator()
                {
                try {
                    return query.iterate();
                    }
                catch (final HibernateException ouch)
                    {
                    throw factories().hibernate().convert(
                        ouch
                        );
                    }
                }
            };
        }

    /**
     * Select an Iterable set of objects.
     *
     */
    @SelectMethod
    protected Iterable<EntityType> iterable(final int limit, final Query query)
        {
        return new Iterable<EntityType>()
            {
            @Override
            @SelectMethod
            @SuppressWarnings("unchecked")
            public Iterator<EntityType> iterator()
                {
                try {
                    query.setMaxResults(limit);
                    return query.iterate();
                    }
                catch (final HibernateException ouch)
                    {
                    throw factories().hibernate().convert(
                        ouch
                        );
                    }
                }
            };
        }

    /**
     * Select an Iterable set of objects.
     *
     */
    @SelectMethod
    protected Iterable<EntityType> iterable(final int first, final int limit, final Query query)
        {
        return new Iterable<EntityType>()
            {
            @Override
            @SelectMethod
            @SuppressWarnings("unchecked")
            public Iterator<EntityType> iterator()
                {
                try {
                    query.setFirstResult(first);
                    query.setMaxResults(limit);
                    return query.iterate();
                    }
                catch (final HibernateException ouch)
                    {
                    throw factories().hibernate().convert(
                        ouch
                        );
                    }
                }
            };
        }

    /**
     * Select a List of objects.
     *
     */
    @SelectMethod
    @SuppressWarnings("unchecked")
    protected List<EntityType> list(final Query query)
        {
        try {
            return query.list();
            }
        catch (final HibernateException ouch)
            {
            throw factories().hibernate().convert(
                ouch
                );
            }
        }

    /**
     * Select a List of objects.
     *
     */
    @SelectMethod
    @SuppressWarnings("unchecked")
    protected List<EntityType> list(final int limit, final Query query)
        {
        try {
            query.setMaxResults(limit);
            return query.list();
            }
        catch (final HibernateException ouch)
            {
            throw factories().hibernate().convert(
                ouch
                );
            }
        }

    /**
     * Select a List of objects.
     *
     */
    @SelectMethod
    @SuppressWarnings("unchecked")
    protected List<EntityType> list(final int first, final int limit, final Query query)
        {
        try {
            query.setFirstResult(first);
            query.setMaxResults(limit);
            return query.list();
            }
        catch (final HibernateException ouch)
            {
            throw factories().hibernate().convert(
                ouch
                );
            }
        }

    /**
     * Create a text search string.
     * TODO .. lots !!
     *
     */
    protected String searchParam(final String text)
        {
        //
        // Using wildcards in a HQL query with named parameters.
        // http://www.stpe.se/2008/07/hibernate-hql-like-query-named-parameters/
        return new StringBuilder(text).append("%").toString();
        }

    /**
     * Base class for an {@link EntityFactory} {@link Protector}.
     *
     */
    public abstract class FactoryProtector
    extends BaseProtector
    implements Protector
        {
        public FactoryProtector()
            {
            super(AbstractEntityFactory.this);
            }
        }

    public class FactoryAdminCreateProtector
    extends FactoryProtector
    implements Protector
        {
        @Override
        public boolean check(Identity identity, Action action)
            {
            log.trace("check(Identity, Action)");
            log.trace("  Identity [{}]", identity);
            log.trace("  Action   [{}]", action);
            switch (action.type())
                {
                case CREATE:
                case UPDATE:
                    return isAdmin(
                        identity
                        );

                case SELECT:
                    return true ;
                    
                default :
                    return false ;
                }
            }
        }

    public class FactoryAllowCreateProtector
    extends FactoryProtector
    implements Protector
        {
        @Override
        public boolean check(Identity identity, Action action)
            {
            log.trace("check(Identity, Action)");
            log.trace("  Identity [{}]", identity);
            log.trace("  Action   [{}]", action);
            switch (action.type())
                {
                case CREATE:
                case UPDATE:
                case SELECT:
                    return true ;
                    
                default :
                    return false ;
                }
            }
        }
    }


