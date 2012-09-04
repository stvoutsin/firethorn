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
package uk.ac.roe.wfau.firethorn.widgeon.entity.jdbc ;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.widgeon.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.JdbcResource;
import uk.ac.roe.wfau.firethorn.widgeon.entity.base.BaseResourceEntity;

/**
 * BaseResource implementations.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@DiscriminatorValue(
    value=JdbcResourceEntity.DB_CLASS_TYPE
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "jdbc.resource-select-all",
            query = "FROM JdbcResourceEntity ORDER BY ident desc"
            ),
            @NamedQuery(
                name  = "jdbc.resource-select-name",
                query = "FROM JdbcResourceEntity WHERE (name = :name) ORDER BY ident desc"
                ),
        @NamedQuery(
            name  = "jdbc.resource-search-text",
            query = "FROM JdbcResourceEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
public class JdbcResourceEntity
extends BaseResourceEntity
implements JdbcResource
    {

    /**
     * Our persistence type value.
     * 
     */
    public static final String DB_CLASS_TYPE = "JDBC" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<JdbcResource>
    implements JdbcResource.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcResourceEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource> select()
            {
            return super.iterable(
                super.query(
                    "jdbc.resource-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource> select(final String name)
            {
            return super.iterable(
                super.query(
                    "jdbc.resource-select-name"
                    ).setString(
                        "name",
                        name
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource> search(final String text)
            {
            String match = new StringBuilder(text).append("%").toString();
            return super.iterable(
                super.query(
                    "jdbc.resource-search-text"
                    ).setString(
                        "text",
                        match 
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public JdbcResource create(final String name)
            {
            return super.insert(
                new JdbcResourceEntity(
                    name
                    )
                );
            }
        
        /**
         * Our Autowired view factory.
         * 
         */
        @Autowired
        protected AdqlResource.Factory views ;

        @Override
        public AdqlResource.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired catalog factory.
         * 
         */
        @Autowired
        protected JdbcResource.JdbcCatalog.Factory catalogs ;

        @Override
        public JdbcResource.JdbcCatalog.Factory catalogs()
            {
            return this.catalogs ;
            }
        }

    @Override
    public JdbcResource.Catalogs catalogs()
        {
        return new JdbcResource.Catalogs()
            {
            @Override
            public JdbcCatalog create(String name)
                {
                return womble().resources().jdbc().catalogs().create(
                    JdbcResourceEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<JdbcResource.JdbcCatalog> select()
                {
                return womble().resources().jdbc().catalogs().select(
                    JdbcResourceEntity.this
                    );
                }

            @Override
            public JdbcResource.JdbcCatalog select(final String name)
            throws NameNotFoundException
                {
                return womble().resources().jdbc().catalogs().select(
                    JdbcResourceEntity.this,
                    name
                    );
                }

            @Override
            public JdbcResource.JdbcCatalog search(final String name)
                {
                return womble().resources().jdbc().catalogs().search(
                    JdbcResourceEntity.this,
                    name
                    );
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected JdbcResourceEntity()
        {
        super();
        }

    /**
     * Create a new resource.
     *
     */
    private JdbcResourceEntity(final String name)
        {
        super(name);
        log.debug("new([{}]", name);
        }
    }

