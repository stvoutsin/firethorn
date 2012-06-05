/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.CacheMode;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.HibernateException;

import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;
import uk.ac.roe.wfau.firethorn.common.ident.LongIdent;

/**
 * Generic base class for a persistent Entity Factory.
 *
 */
@Repository
public abstract class AbstractFactory<InterfaceType extends GenericEntity, EntityType extends AbstractEntity>
implements GenericEntity.Factory
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        AbstractFactory.class
        );

    /**
     * Get the class of Entity we manage.
     * Required because we can't get this from the generics at runtime, because ....
     * http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
     * http://stackoverflow.com/questions/2225979/getting-t-class-despite-javas-type-erasure
     *
     */
    public abstract Class etype();

    /**
     * Create an Identifier from a String.
     *
     */
    @Override
    public Identifier ident(String string)
        {
        return new LongIdent(
            string
            );
        }

    /**
     * Our autowired reference to the global Womble.
     *
     */
    @Autowired
    private Womble womble ;
    public Womble womble()
        {
        return womble;
        }

    public HibernateTools<InterfaceType, EntityType> tools()
        {
        return new HibernateTools<InterfaceType, EntityType>();
        }

    /**
     * Inner class to wrap common Hibernate functions.
     *
     */
    public class HibernateTools<InterfaceType extends GenericEntity, EntityType extends AbstractEntity>
        {

        /**
         * Get a named query.
         *
         */
        public Query query(String name)
            {
            return womble.hibernate().query(
                name
                );
            }

        /**
         * Insert a new Entity into the database.
         *
         */
        public EntityType insert(EntityType entity)
            {
            logger.debug("insert(EntityType)");
            logger.debug("  entity [{}]", entity);
            return (EntityType) womble.hibernate().insert(
                entity
                );
            }

        /**
         * Select a specific Entity by Identifier.
         *
         */
        public EntityType select(Identifier ident)
            {
            logger.debug("select(Class, Identifier)");
            logger.debug("  ident [{}]", (ident != null) ? null : ident.value());
            return (EntityType) womble.hibernate().select(
                etype(),
                ident
                );
            }

        /**
         * Update an Entity.
         *
         */
        public EntityType update(InterfaceType entity)
            {
            logger.debug("update(InterfaceType)");
            logger.debug("  entity [{}]", entity);
            if (etype().isInstance(entity))
                {
                return (EntityType) womble.hibernate().update(
                    (EntityType) entity
                    );
                }
            else {
                logger.error(
                    "Update not supported for [" + entity.getClass().getName() + "]"
                    );
                throw new IllegalArgumentException(
                    "Update not supported for [" + entity.getClass().getName() + "]"
                    );
                }
            }

        /**
         * Delete an Entity.
         *
         */
        public void delete(InterfaceType entity)
            {
            logger.debug("delete(InterfaceType)");
            logger.debug("  entity [{}]", entity);
            if (etype().isInstance(entity))
                {
                womble.hibernate().delete(
                    (EntityType) entity
                    );
                }
            else {
                logger.error(
                    "Delete not supported for [" + entity.getClass().getName() + "]"
                    );
                throw new IllegalArgumentException(
                    "Delete not supported for [" + entity.getClass().getName() + "]"
                    );
                }
            }

        /**
         * Flush changes to the database.
         *
         */
        protected void flush()
            {
            womble.hibernate().flush();
            }

        /**
         * Clear the session state, discarding unsaved changes.
         *
         */
        protected void clear()
            {
            womble.hibernate().clear();
            }

        /**
         * Select a single object.
         *
         */
        public InterfaceType single(Query query)
            {
            return (InterfaceType) womble.hibernate().single(
                query
                );
            }

        /**
         * Return the first result of a query.
         *
         */
        public InterfaceType first(Query query)
            {
            return (InterfaceType) womble.hibernate().first(
                query
                );
            }

        /**
         * Select an Iterable set of objects.
         *
         */
        public Iterable<InterfaceType> iterable(final Query query)
            {
            return new Iterable<InterfaceType>()
                {
                public Iterator<InterfaceType> iterator()
                    {
                    try {
                        return (Iterator<InterfaceType>) query.iterate();
                        }
                    catch (HibernateException ouch)
                        {
                        throw womble.hibernate().convert(
                            ouch
                            );
                        }
                    }
                };
            }
        }


    }


