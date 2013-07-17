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
package uk.ac.roe.wfau.firethorn.widgeon.jdbc;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>JdbcSchema</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcSchemaLinkFactory.SCHEMA_PATH)
public class JdbcSchemaController
    extends AbstractEntityController<JdbcSchema>
    {

    @Override
    public Path path()
        {
        return path(
            JdbcSchemaLinkFactory.SCHEMA_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcSchemaController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String TARGET_ENTITY = "urn:jdbc.schema.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "jdbc.schema.update.name" ;

    @Override
    public Iterable<EntityBean<JdbcSchema>> bean(final Iterable<JdbcSchema> iter)
        {
        return new JdbcSchemaBean.Iter(
            iter
            );
        }

    @Override
    public EntityBean<JdbcSchema> bean(final JdbcSchema entity)
        {
        return new JdbcSchemaBean(
            entity
            );
        }

    /**
     * Get the target schema based on the identifier in the request.
     * @throws NotFoundException
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public JdbcSchema entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws NotFoundException {
        log.debug("schema() [{}]", ident);
        return factories().jdbc().schemas().select(
            factories().jdbc().schemas().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public EntityBean<JdbcSchema> select(
        @ModelAttribute(TARGET_ENTITY)
        final JdbcSchema entity
        ){
        log.debug("select()");
        return bean(
            entity
            );
        }

    /**
     * JSON POST update.
     *
     */
    @ResponseBody
    @UpdateAtomicMethod
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MAPPING)
    public EntityBean<JdbcSchema> update(
        @ModelAttribute(TARGET_ENTITY)
        final JdbcSchema entity,
        @RequestParam(value=UPDATE_NAME, required=false)
        final String name
        ){
        log.debug("select()");
        if (name != null)
            {
            if (name.length() > 0)
                {
                entity.name(
                    name
                    );
                }
            }
        return bean(
            entity
            );
        }
    }
