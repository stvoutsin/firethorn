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
package uk.ac.roe.wfau.firethorn.widgeon.entity.base ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.exception.*;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.*;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonBase;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonView;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonStatus;
import uk.ac.roe.wfau.firethorn.widgeon.entity.WidgeonStatusEntity;

/**
 * Schema implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = SchemaBaseEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                SchemaBaseEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.base.schema-select-parent",
            query = "FROM SchemaBaseEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.base.schema-select-parent.name",
            query = "FROM SchemaBaseEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class SchemaBaseEntity
extends WidgeonStatusEntity
implements WidgeonBase.Catalog.Schema
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_base_schema" ;

    /**
     * The persistence column name for our parent Catalog.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<WidgeonBase.Catalog.Schema>
    implements WidgeonBase.Catalog.Schema.Factory
        {

        @Override
        public Class etype()
            {
            return SchemaBaseEntity.class ;
            }

        /**
         * Insert a Schema into the database and update all the parent views.
         *
         */
        @CascadeEntityMethod
        protected WidgeonBase.Catalog.Schema insert(final SchemaBaseEntity entity)
            {
            super.insert(
                entity
                );
            for (WidgeonView.Catalog view : entity.parent().views().select())
                {
                this.views().cascade(
                    view,
                    entity
                    );
                }
            return entity ;
            }

        @Override
        @CreateEntityMethod
        public WidgeonBase.Catalog.Schema create(final WidgeonBase.Catalog parent, final String name)
            {
            return this.insert(
                new SchemaBaseEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<WidgeonBase.Catalog.Schema> select(final WidgeonBase.Catalog parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.base.schema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public WidgeonBase.Catalog.Schema select(final WidgeonBase.Catalog parent, final String name)
        throws NameNotFoundException
            {
            WidgeonBase.Catalog.Schema result = this.search(
                parent,
                name
                );
            if (result != null)
                {
                return result;
                }
            else {
                throw new NameNotFoundException(
                    name
                    );
                }
            }

        /**
         * Search for a named Schema in a Catalog.
         *
         */
        @SelectEntityMethod
        protected WidgeonBase.Catalog.Schema search(final WidgeonBase.Catalog parent, final String name)
            {
            return super.first(
                super.query(
                    "widgeon.base.schema-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        /**
         * Our Autowired View factory.
         * 
         */
        @Autowired
        protected WidgeonView.Catalog.Schema.Factory views ;

        @Override
        public WidgeonView.Catalog.Schema.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired Table factory.
         * 
         */
        @Autowired
        protected WidgeonBase.Catalog.Schema.Table.Factory tables ;

        @Override
        public WidgeonBase.Catalog.Schema.Table.Factory tables()
            {
            return this.tables ;
            }
        }

    @Override
    public WidgeonBase.Catalog.Schema.Views views()
        {
        return new WidgeonBase.Catalog.Schema.Views()
            {
            public Iterable<WidgeonView.Catalog.Schema> select()
                {
                return womble().widgeons().views().catalogs().schemas().select(
                    SchemaBaseEntity.this
                    );
                }
            };
        }

    @Override
    public WidgeonBase.Catalog.Schema.Tables tables()
        {
        return new WidgeonBase.Catalog.Schema.Tables()
            {
            @Override
            public WidgeonBase.Catalog.Schema.Table create(String name)
                {
                return womble().widgeons().catalogs().schemas().tables().create(
                    SchemaBaseEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<WidgeonBase.Catalog.Schema.Table> select()
                {
                return womble().widgeons().catalogs().schemas().tables().select(
                    SchemaBaseEntity.this
                    ) ;
                }

            @Override
            public WidgeonBase.Catalog.Schema.Table select(String name)
            throws NameNotFoundException
                {
                return womble().widgeons().catalogs().schemas().tables().select(
                    SchemaBaseEntity.this,
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
    protected SchemaBaseEntity()
        {
        super();
        }

    /**
     * Create a new Catalog.
     *
     */
    protected SchemaBaseEntity(final WidgeonBase.Catalog parent, final String name)
        {
        super(name);
        this.parent = parent ;
        }

    /**
     * Our parent Catalog.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = CatalogBaseEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private WidgeonBase.Catalog parent ;

    @Override
    public WidgeonBase.Catalog parent()
        {
        return this.parent ;
        }

    @Override
    public Widgeon.Status status()
        {
        if (this.parent().status() == Widgeon.Status.ENABLED)
            {
            return super.status();
            }
        else {
            return this.parent().status();
            }
        }
    }

