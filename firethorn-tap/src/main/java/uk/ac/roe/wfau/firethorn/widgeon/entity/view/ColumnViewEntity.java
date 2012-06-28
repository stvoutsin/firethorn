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
package uk.ac.roe.wfau.firethorn.widgeon.entity.view ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
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

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonStatus;
import uk.ac.roe.wfau.firethorn.widgeon.entity.WidgeonStatusEntity;
import uk.ac.roe.wfau.firethorn.widgeon.entity.base.ColumnBaseEntity;

/**
 * Column View implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = ColumnViewEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                ColumnViewEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.view.column-select-parent",
            query = "FROM ColumnViewEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.column-select-parent.name",
            query = "FROM ColumnViewEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class ColumnViewEntity
extends WidgeonStatusEntity
implements Widgeon.View.Schema.Catalog.Table.Column
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_view_column" ;

    /**
     * The persistence column name for our parent Table.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * The persistence column name for our base Column.
     * 
     */
    public static final String DB_BASE_COL = "base" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Widgeon.View.Schema.Catalog.Table.Column>
    implements Widgeon.View.Schema.Catalog.Table.Column.Factory
        {

        @Override
        public Class etype()
            {
            return ColumnViewEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public Widgeon.View.Schema.Catalog.Table.Column create(final Widgeon.View.Schema.Catalog.Table parent, final Widgeon.Base.Schema.Catalog.Table.Column base, final String name)
            {
            return super.insert(
                new ColumnViewEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.View.Schema.Catalog.Table.Column> select(final Widgeon.View.Schema.Catalog.Table parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.view.table-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.View.Schema.Catalog.Table.Column select(final Widgeon.View.Schema.Catalog.Table parent, final String name)
        throws NameNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "widgeon.view.table-select-parent.name"
                        ).setEntity(
                            "parent",
                            parent
                        ).setString(
                            "name",
                            name
                        )
                    );
                }
            catch(EntityNotFoundException ouch)
                {
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }
        }

    /**
     * Check a view name, using the base name if the given name is null or empty.
     *
     */
    private static String name(final Widgeon.Base.Schema.Catalog.Table.Column base, final String name)
        {
        if ((name == null) || (name.trim().length() == 0))
            {
            return base.name();
            }
        else {
            return name.trim() ;
            }
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected ColumnViewEntity()
        {
        super();
        }

    /**
     * Create a new view.
     *
     */
    protected ColumnViewEntity(final Widgeon.View.Schema.Catalog.Table parent, final Widgeon.Base.Schema.Catalog.Table.Column base)
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
    protected ColumnViewEntity(final Widgeon.View.Schema.Catalog.Table parent, final Widgeon.Base.Schema.Catalog.Table.Column base, final String name)
        {
        super(
            name(
                base,
                name
                )
            );
        this.base   = base   ;
        this.parent = parent ;
        }

    /**
     * Our parent Table.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TableViewEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.View.Schema.Catalog.Table parent ;

    @Override
    public Widgeon.View.Schema.Catalog.Table parent()
        {
        return this.parent ;
        }

    /**
     * Our underlying Column.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = ColumnBaseEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.Base.Schema.Catalog.Table.Column base ;

    @Override
    public Widgeon.Base.Schema.Catalog.Table.Column base()
        {
        return this.base ;
        }

    @Override
    public Widgeon.Status status()
        {
        if (this.parent().status() == Widgeon.Status.ENABLED)
            {
            if (this.base().status() == Widgeon.Status.ENABLED)
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
    }

