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

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.JdbcResourceLinkFactory;

/**
 * Spring MVC controller for <code>JdbcResource</code> schema.
 *
 */
@Controller
@RequestMapping(JdbcResourceLinkFactory.RESOURCE_SCHEMA_PATH)
public class JdbcResourceSchemaController
extends AbstractEntityController<JdbcSchema, JdbcSchemaBean>
implements JdbcResourceModel, JdbcSchemaModel
    {
    @Override
    public Path path()
        {
        return path(
            JdbcResourceLinkFactory.RESOURCE_SCHEMA_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcResourceSchemaController()
        {
        super();
        }


    @Override
    public JdbcSchemaBean bean(final JdbcSchema entity)
        {
        return new JdbcSchemaBean(
            entity
            );
        }

    @Override
    public Iterable<JdbcSchemaBean> bean(final Iterable<JdbcSchema> iter)
        {
        return new JdbcSchemaBean.Iter(
            iter
            );
        }

    /**
     * Get the parent resource based on the identifier in the request.
     * @throws EntityNotFoundException
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(JdbcResourceController.TARGET_ENTITY)
    public JdbcResource parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws EntityNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().jdbc().resources().entities().select(
            factories().jdbc().resources().idents().ident(
                ident
                )
            );
        }

    /**
     * GET request to select all the {@link JdbcSchema}.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<Iterable<JdbcSchemaBean>> select(
        @ModelAttribute(JdbcResourceController.TARGET_ENTITY)
        final JdbcResource resource
        )
    throws ProtectionException
        {
        return selected(
            resource.schemas().select()
            );
        }

    /**
     * POST request to select a {@link JdbcSchema} by {@link Identifier}.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=SCHEMA_IDENT_PARAM, produces=JSON_MIME)
    public ResponseEntity<JdbcSchemaBean> select_by_ident(
        @ModelAttribute(JdbcResourceController.TARGET_ENTITY)
        final JdbcResource resource,
        @RequestParam(SCHEMA_IDENT_PARAM)
        final String ident
        )
    throws EntityNotFoundException, ProtectionException
        {
        return selected(
            resource.schemas().select(
                factories().jdbc().schemas().idents().ident(
                    ident
                    )
                )
            );
        }

    /**
     * POST request to select a {@link JdbcSchema} by name.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.POST, params=SCHEMA_NAME_PARAM, produces=JSON_MIME)
    public ResponseEntity<JdbcSchemaBean> select_by_name(
        @ModelAttribute(JdbcResourceController.TARGET_ENTITY)
        final JdbcResource resource,
        @RequestParam(SCHEMA_NAME_PARAM)
        final String name
        )
    throws EntityNotFoundException, ProtectionException
        {
        return selected(
            resource.schemas().select(
                name
                )
            );
        }

    /**
     * JSON request to select by schema and catalog name.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params={CATALOG_NAME_PARAM, SCHEMA_NAME_PARAM}, produces=JSON_MIME)
    public ResponseEntity<JdbcSchemaBean> select(
        @ModelAttribute(JdbcResourceController.TARGET_ENTITY)
        final JdbcResource resource,
        @RequestParam(CATALOG_NAME_PARAM)
        final String catalog,
        @RequestParam(SCHEMA_NAME_PARAM)
        final String schema
        )
    throws EntityNotFoundException, ProtectionException
        {
        return selected(
            resource.schemas().select(
                catalog,
                schema
                )
            );
        }

    
    /**
     * JSON request to create a new schema.
     * @throws ProtectionException 
     *
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, params={CATALOG_NAME_PARAM, SCHEMA_NAME_PARAM}, produces=JSON_MIME)
    public ResponseEntity<JdbcSchemaBean> create(
        @ModelAttribute(JdbcResourceController.TARGET_ENTITY)
        final JdbcResource resource,
        @RequestParam(CATALOG_NAME_PARAM)
        final String catalog,
        @RequestParam(SCHEMA_NAME_PARAM)
        final String schema
        )
    throws EntityNotFoundException, ProtectionException
        {
        return created(
            resource.schemas().create(
                catalog,
                schema
                )
            );
        }
    }
