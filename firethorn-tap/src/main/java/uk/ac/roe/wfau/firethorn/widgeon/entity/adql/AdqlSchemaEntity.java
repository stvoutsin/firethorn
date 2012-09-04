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
package uk.ac.roe.wfau.firethorn.widgeon.entity.adql ;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CascadeEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.widgeon.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.BaseResource;
import uk.ac.roe.wfau.firethorn.widgeon.ResourceStatusEntity;
import uk.ac.roe.wfau.firethorn.widgeon.entity.jdbc.JdbcSchemaEntity;

/**
 * AdqlResource.AdqlSchema implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlSchemaEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                AdqlSchemaEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "adql.schema-select-parent",
            query = "FROM AdqlSchemaEntity WHERE (parent = :parent) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.schema-select-parent.name",
            query = "FROM AdqlSchemaEntity WHERE ((parent = :parent) AND (((name IS NOT null) AND (name = :name)) OR ((name IS null) AND (base.name = :name)))) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.schema-select-base",
            query = "FROM AdqlSchemaEntity WHERE (base = :base) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.schema-select-parent.base",
            query = "FROM AdqlSchemaEntity WHERE ((parent = :parent) AND (base = :base)) ORDER BY ident desc"
            )
        }
    )
public class AdqlSchemaEntity
extends ResourceStatusEntity
implements AdqlResource.AdqlSchema
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "adql_schema" ;

    /**
     * The persistence column name for our parent catalog.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * The persistence column name for our base schema.
     * 
     */
    public static final String DB_BASE_COL = "base" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<AdqlResource.AdqlSchema>
    implements AdqlResource.AdqlSchema.Factory
        {

        @Override
        public Class<?> etype()
            {
            return AdqlSchemaEntity.class ;
            }

        /**
         * Insert a View into the database and create views for each child.
         *
         */
        @CascadeEntityMethod
        protected AdqlResource.AdqlSchema insert(AdqlSchemaEntity entity)
            {
            super.insert(
                entity
                );
            for (BaseResource.BaseTable<?> baseTable : entity.base().tables().select())
                {
                this.tables().cascade(
                    entity,
                    baseTable
                    );
                }
            return entity ;
            }

        /**
         * Create a default view of a schema.
         *
         */
        @CreateEntityMethod
        protected AdqlResource.AdqlSchema create(final AdqlResource.AdqlCatalog parent, final BaseResource.BaseSchema<?> base)
            {
            return this.insert(
                new AdqlSchemaEntity(
                    parent,
                    base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public AdqlResource.AdqlSchema search(final AdqlResource.AdqlCatalog parent, final BaseResource.BaseSchema<?> base)
            {
            return super.first(
                super.query(
                    "adql.schema-select-parent.base"
                    ).setEntity(
                        "parent",
                        parent
                    ).setEntity(
                        "base",
                        base
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlResource.AdqlSchema cascade(final AdqlResource.AdqlCatalog parent, final BaseResource.BaseSchema<?> base)
            {
            AdqlResource.AdqlSchema result = this.search(
                parent,
                base
                );
            if (result == null)
                {
                result = this.create(
                    parent,
                    base
                    );
                }
            return result ;
            }

        @Override
        @CreateEntityMethod
        public AdqlResource.AdqlSchema create(final AdqlResource.AdqlCatalog parent, final BaseResource.BaseSchema<?> base, final String name)
            {
            return this.insert(
                new AdqlSchemaEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlResource.AdqlSchema> select(final AdqlResource.AdqlCatalog parent)
            {
            return super.iterable(
                super.query(
                    "adql.schema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public AdqlResource.AdqlSchema select(final AdqlResource.AdqlCatalog parent, final String name)
        throws NameNotFoundException
            {
            AdqlResource.AdqlSchema result = this.search(
                parent,
                name
                );
            if (result != null)
                {
                return result ;
                }
            else {
                throw new NameNotFoundException(
                    name
                    );
                }
            }

        @Override
        @SelectEntityMethod
        public AdqlResource.AdqlSchema search(final AdqlResource.AdqlCatalog parent, final String name)
            {
            return super.first(
                super.query(
                    "adql.schema-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlResource.AdqlSchema> select(final BaseResource.BaseSchema<?> base)
            {
            return super.iterable(
                super.query(
                    "adql.schema-select-base"
                    ).setEntity(
                        "base",
                        base
                        )
                );
            }

        /**
         * Our Autowired table factory.
         * 
         */
        @Autowired
        protected AdqlResource.AdqlTable.Factory tables ;

        @Override
        public AdqlResource.AdqlTable.Factory tables()
            {
            return this.tables ;
            }
        }

    @Override
    public AdqlResource.AdqlSchema.Tables tables()
        {
        return new AdqlResource.AdqlSchema.Tables()
            {

            @Override
            public Iterable<AdqlResource.AdqlTable> select()
                {
                return womble().resources().base().views().catalogs().schemas().tables().select(
                    AdqlSchemaEntity.this
                    ) ;
                }

            @Override
            public AdqlResource.AdqlTable select(String name)
            throws NameNotFoundException
                {
                return womble().resources().base().views().catalogs().schemas().tables().select(
                    AdqlSchemaEntity.this,
                    name
                    ) ;
                }

            @Override
            public AdqlResource.AdqlTable search(String name)
                {
                return womble().resources().base().views().catalogs().schemas().tables().search(
                    AdqlSchemaEntity.this,
                    name
                    ) ;
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected AdqlSchemaEntity()
        {
        super();
        }

    /**
     * Create a new view.
     *
     */
    protected AdqlSchemaEntity(final AdqlResource.AdqlCatalog parent, final BaseResource.BaseSchema<?> base)
        {
        this(
            parent,
            base,
            null
            );
        }

    /**
     * Create a new view.
     *
     */
    protected AdqlSchemaEntity(final AdqlResource.AdqlCatalog parent, final BaseResource.BaseSchema<?> base, final String name)
        {
        super(
            name
            );
        log.debug("new([{}]", name);
        this.base   = base   ;
        this.parent = parent ;
        }

    /**
     * Our parent catalog.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = AdqlCatalogEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private AdqlResource.AdqlCatalog parent ;

    @Override
    public AdqlResource.AdqlCatalog parent()
        {
        return this.parent ;
        }

    /**
     * Our underlying schema.
     * @todo BaseSchemaEntity.class
     * 
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcSchemaEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private BaseResource.BaseSchema<?> base ;

    @Override
    public BaseResource.BaseSchema<?> base()
        {
        return this.base ;
        }

    @Override
    public String name()
        {
        if (this.name != null)
            {
            return this.name ;
            }
        else {
            return base.name() ;
            }
        }

    @Override
    public Status status()
        {
        if (this.parent().status() == Status.ENABLED)
            {
            if (this.base().status() == Status.ENABLED)
                {
                return super.status() ;
                }
            else {
                return this.base().status();
                }
            }
        else {
            return this.parent().status();
            }
        }

    @Override
    public AdqlResource resource()
        {
        return this.parent.resource();
        }

    @Override
    public AdqlResource.AdqlCatalog catalog()
        {
        return this.parent;
        }
    }

