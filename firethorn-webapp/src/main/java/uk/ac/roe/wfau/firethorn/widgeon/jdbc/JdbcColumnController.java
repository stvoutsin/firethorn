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
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.JdbcColumnLinkFactory;

/**
 * Spring MVC controller for <code>JdbcColumn</code>.
 *
 */
@Controller
@RequestMapping(JdbcColumnLinkFactory.COLUMN_PATH)
public class JdbcColumnController
extends AbstractEntityController<JdbcColumn, JdbcColumnBean>
implements JdbcColumnModel
    {

    @Override
    public Path path()
        {
        return path(
            JdbcColumnLinkFactory.COLUMN_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public JdbcColumnController()
        {
        super();
        }

    @Override
    public Iterable<JdbcColumnBean> bean(final Iterable<JdbcColumn> iter)
        {
        return new JdbcColumnBean.Iter(
            iter
            );
        }

    @Override
    public JdbcColumnBean bean(final JdbcColumn entity)
        {
        return new JdbcColumnBean(
            entity
            );
        }

    /**
     * Get the target column based on the identifier in the request.
     * @throws EntityNotFoundException
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public JdbcColumn entity(
        @PathVariable("ident")
        final String ident
        )
    throws EntityNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().jdbc().columns().entities().select(
            factories().jdbc().columns().idents().ident(
                ident
                )
            );
        }

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<JdbcColumnBean> select(
        @ModelAttribute(TARGET_ENTITY)
        final JdbcColumn entity
        ){
        return selected(
            entity
            ) ;
        }
    }
