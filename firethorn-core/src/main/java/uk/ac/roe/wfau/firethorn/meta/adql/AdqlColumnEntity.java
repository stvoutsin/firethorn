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
package uk.ac.roe.wfau.firethorn.meta.adql;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumnEntity;

/**
 * {@link AdqlColumn} implementation.
 *
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlColumnEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            name = AdqlColumnEntity.DB_INDEX_NAME + AdqlColumnEntity.DB_PARENT_COL,
            columnList = AdqlColumnEntity.DB_PARENT_COL
            ),
        @Index(
            name = AdqlColumnEntity.DB_INDEX_NAME + AdqlColumnEntity.DB_BASE_COL,
            columnList = AdqlColumnEntity.DB_BASE_COL
            )
        },
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AdqlColumnEntity.DB_NAME_COL,
                AdqlColumnEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "AdqlColumn-select-parent",
            query = "FROM AdqlColumnEntity WHERE parent = :parent ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "AdqlColumn-select-parent.name",
            query = "FROM AdqlColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "AdqlColumn-search-parent.text",
            query = "FROM AdqlColumnEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident asc"
            )
        }
    )
public class AdqlColumnEntity
    extends BaseColumnEntity<AdqlColumn>
    implements AdqlColumn
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "AdqlColumnEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JOIN_NAME  = DB_TABLE_PREFIX + "AdqlColumnJoinTo";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_INDEX_NAME = DB_TABLE_PREFIX + "AdqlColumnIndexBy";

    /**
     * The default name prefix, {@value}.
     * 
     */
    protected static final String NAME_PREFIX = "ADQL_COLUMN";

    /**
     * The default alias prefix, {@value}.
     * 
     */
    protected static final String ALIAS_PREFIX = "ADQL_COLUMN_";

    /**
     * {@link AdqlColumn.AliasFactory} implementation.
     *
     */
    @Component
    public static class AliasFactory
    implements AdqlColumn.AliasFactory
        {
        @Override
        public String alias(final AdqlColumn entity)
            {
            return ALIAS_PREFIX.concat(
                entity.ident().toString()
                );
            }

        @Override
        public boolean matches(String alias)
            {
            return alias.startsWith(
                ALIAS_PREFIX
                );
            }
        
        @Override
        public AdqlColumn resolve(String alias)
            throws EntityNotFoundException
            {
            return entities.select(
                idents.ident(
                    alias.substring(
                        ALIAS_PREFIX.length()
                        )
                    )
                );
            }

        /**
         * Our {@link AdqlColumn.IdentFactory}.
         * 
         */
        @Autowired
        private AdqlColumn.IdentFactory idents ;

        /**
         * Our {@link AdqlColumn.EntityFactory}.
         * 
         */
        @Autowired
        private AdqlColumn.EntityFactory entities;
        
        }

    /**
     * {@link AdqlColumn.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseColumnEntity.EntityFactory<AdqlTable, AdqlColumn>
    implements AdqlColumn.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return AdqlColumnEntity.class ;
            }

        @Autowired
        private AdqlTable.EntityFactory tables ;

        @Override
        @SelectMethod
        public AdqlColumn select(final Identifier ident)
        throws IdentifierNotFoundException
            {
            log.debug("select(Identifier) [{}]", ident);
            if (ident instanceof ProxyIdentifier)
                {
                log.debug("-- proxy identifier");
                final ProxyIdentifier proxy = (ProxyIdentifier) ident;

                log.debug("-- parent table");
                final AdqlTable table = tables.select(
                    proxy.parent()
                    );

                log.debug("-- proxy column");
                final AdqlColumn column = table.columns().select(
                    proxy.base()
                    );

                return column ;
                }
            else {
                return super.select(
                    ident
                    );
                }
            }

        @Override
        @CreateMethod
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base)
            {
            return this.insert(
                new AdqlColumnEntity(
                    this,
                    parent,
                    base
                    )
                );
            }

        @Override
        @CreateMethod
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base, final String name)
            {
            return this.insert(
                new AdqlColumnEntity(
                    this,
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<AdqlColumn> select(final AdqlTable parent)
            {
            return super.list(
                super.query(
                    "AdqlColumn-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectMethod
        public AdqlColumn select(final AdqlTable parent, final String name)
        throws NameNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "AdqlColumn-select-parent.name"
                        ).setEntity(
                            "parent",
                            parent
                        ).setString(
                            "name",
                            name
                        )
                    );
                }
            catch (final EntityNotFoundException ouch)
                {
                log.debug("Unable to locate column [{}][{}]", parent.namebuilder().toString(), name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

        @Override
        @SelectMethod
        public AdqlColumn search(final AdqlTable parent, final String name)
            {
            return super.first(
                super.query(
                    "AdqlColumn-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        @Autowired
        protected AdqlColumn.IdentFactory idents;
        @Override
        public AdqlColumn.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected AdqlColumn.LinkFactory links;
        @Override
        public AdqlColumn.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected AdqlColumn.AliasFactory aliases;
        @Override
        public AdqlColumn.AliasFactory aliases()
            {
            return this.aliases;
            }
        }

    /**
     * Reference to our parent {@link AdqlColumn.EntityFactory}.
     * 
     */
    @Transient
    protected AdqlColumn.EntityFactory factory;

    @Override
    public AdqlColumn.EntityFactory factory()
        {
        return this.factory;
        }

    protected AdqlColumnEntity()
        {
        super();
        }

    protected AdqlColumnEntity(final AdqlColumn.EntityFactory factory, final AdqlTable table, final BaseColumn<?> base)
        {
        this(
            factory,
            table,
            base,
            null
            );
        }

    protected AdqlColumnEntity(final AdqlColumn.EntityFactory factory, final AdqlTable table, final BaseColumn<?> base, final String name)
        {
        super(
            table,
            ((name != null) ? name : base.name())
            );
        this.factory = factory ;
        this.base  = base ;
        this.table = table;
        }

    @Override
    public String text()
        {
        if (super.text() == null)
            {
            return base().text();
            }
        else {
            return super.text();
            }
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private AdqlTable table;
    @Override
    public AdqlTable table()
        {
        return this.table;
        }
    @Override
    public AdqlSchema schema()
        {
        return this.table().schema();
        }
    @Override
    public AdqlResource resource()
        {
        return this.table().resource();
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = BaseColumnEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private BaseColumn<?> base ;
    @Override
    public BaseColumn<?> base()
        {
        return this.base ;
        }
    @Override
    public BaseColumn<?> root()
        {
        return base().root();
        }

    @Override
    public String alias()
        {
        return factories().adql().columns().aliases().alias(
            this
            );
        }

    @Override
    public String link()
        {
        return factories().adql().columns().links().link(
            this
            );
        }

    @Override
    protected void scanimpl()
        {
        // TODO Auto-generated method stub
        }

    protected AdqlColumn.AdqlType adqltype()
        {
        if (super.adqltype() != null)
            {
            return super.adqltype();
            }
        else {
            return base().meta().adql().type();
            }
        }

    protected Integer adqlsize()
        {
        if (super.adqlsize() != null)
            {
            return super.adqlsize() ;
            }
        else {
            return base().meta().adql().arraysize();
            }
        }

    protected String adqlunit()
        {
        if (this.adqlunit != null)
            {
            return this.adqlunit ;
            }
        else {
            return base().meta().adql().units();
            }
        }

    protected String adqlutype()
        {
        if (this.adqlutype != null)
            {
            return this.adqlutype ;
            }
        else {
            return base().meta().adql().utype();
            }
        }

    protected String adqlucd()
        {
        if (this.adqlucd != null)
            {
            return this.adqlucd ;
            }
        else {
            return base().meta().adql().ucd();
            }
        }
    }
