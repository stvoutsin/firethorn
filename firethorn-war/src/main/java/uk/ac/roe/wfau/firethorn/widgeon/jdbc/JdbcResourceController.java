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
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;
import uk.ac.roe.wfau.firethorn.widgeon.ResourceStatus.Status;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;

/**
 * Spring MVC controller for <code>JdbcResource</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcResourceIdentFactory.RESOURCE_PATH)
public class JdbcResourceController
    extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            JdbcResourceIdentFactory.RESOURCE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcResourceController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String RESOURCE_ENTITY = "urn:jdbc.resource.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "jdbc.resource.update.name" ;

    /**
     * MVC property for updating the status.
     *
     */
    public static final String UPDATE_STATUS = "jdbc.resource.update.status" ;

    /**
     * Wrap an entity as a bean.
     *
     */
    public JdbcResourceBean bean(
        final JdbcResource entity
        ){
        log.debug("bean() [{}]", entity);
        return new JdbcResourceBean(
            entity
            );
        }

    /**
     * Get the target entity based on the ident in the path.
     *
     */
    @ModelAttribute(RESOURCE_ENTITY)
    public JdbcResource entity(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException  {
        log.debug("entity(}");
        log.debug("ident [{}]", ident);
        JdbcResource entity = womble().resources().jdbc().resources().select(
            womble().resources().jdbc().resources().ident(
                ident
                )
            );
        return entity ;
        }
    
    /**
     * HTML GET request.
     *
     */
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView htmlSelect(
        final ModelAndView model
        ){
        model.setViewName(
            "jdbc/resource/display"
            );
        return model ;
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcResourceBean jsonSelect(
        @ModelAttribute(RESOURCE_ENTITY)
        final JdbcResource entity
        ){
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
    public JdbcResourceBean jsonUpdate(
        @RequestParam(value=UPDATE_NAME, required=false)
        String name,
        @RequestParam(value=UPDATE_STATUS, required=false)
        String status,
        @ModelAttribute(RESOURCE_ENTITY)
        final JdbcResource entity
        ){

        if (name != null)
            {
            if (name.length() > 0)
                {
                entity.name(
                    name
                    );
                }
            }

        if (status != null)
            {
            if (status.length() > 0)
                {
                entity.status(
                    Status.valueOf(
                        status
                        )
                    );
                }
            }
        
        return bean(
            entity
            );
        }
    }
