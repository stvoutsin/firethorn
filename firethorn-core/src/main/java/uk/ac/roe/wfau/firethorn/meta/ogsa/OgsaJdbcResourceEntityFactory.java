/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.ogsa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 * {@link OgsaJdbcResource.EntityFactory} implementation.
 *
 */
@Repository
public class OgsaJdbcResourceEntityFactory
extends AbstractEntityFactory<OgsaJdbcResource>
implements OgsaJdbcResource.EntityFactory
    {

    @Override
    public Class<?> etype()
        {
        return OgsaJdbcResourceEntity.class;
        }

    @Autowired
    private OgsaJdbcResource.NameFactory names;
    public OgsaJdbcResource.NameFactory names()
        {
        return this.names;
        }

    @Autowired
    private OgsaJdbcResource.IdentFactory idents;
    @Override
    public OgsaJdbcResource.IdentFactory idents()
        {
        return this.idents;
        }

    private OgsaJdbcResource.LinkFactory links;
    @Override
    public OgsaJdbcResource.LinkFactory links()
        {
        return this.links;
        }
    
    @Override
    @SelectMethod
    public Iterable<OgsaJdbcResource> select()
        {
        return super.iterable(
            super.query(
                "OgsaJdbcResource-select-all"
                )
            );
        }
    
    @Override
    @SelectMethod
    public Iterable<OgsaJdbcResource> select(final OgsaService service)
        {
        return super.iterable(
            super.query(
                "OgsaJdbcResource-select-service"
                )
            );
        }

    @Override
    @SelectMethod
    public Iterable<OgsaJdbcResource> select(final OgsaService service, final JdbcResource source)
        {
        return super.iterable(
            super.query(
                "OgsaJdbcResource-select-service-source"
                )
            );
        }

    @Override
    @CreateMethod
    public OgsaJdbcResource create(final OgsaService service, final JdbcResource source)
        {
        return super.insert(
            new OgsaJdbcResourceEntity(
                service,
                source,
                names.name()
                )
            );
        }

    @Override
    @CreateMethod
    public OgsaJdbcResource create(final OgsaService service, final JdbcResource source, final String name)
        {
        return super.insert(
            new OgsaJdbcResourceEntity(
                service,
                source,
                name
                )
            );
        }
    }
