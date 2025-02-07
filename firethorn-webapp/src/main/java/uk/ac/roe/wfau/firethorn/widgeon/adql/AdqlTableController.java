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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlSchemaLinkFactory;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlTableLinkFactory;

/**
 * Spring MVC controller to handle {@link AdqlTable} entities.
 * <br/>Controller path : [{@value AdqlTableLinkFactory#ENTITY_PATH}]
 *
 */
@Controller
@RequestMapping(AdqlTableLinkFactory.TABLE_PATH)
public class AdqlTableController
extends AbstractEntityController<AdqlTable, AdqlTableBean>
implements AdqlTableModel
    {

    @Override
    public Path path()
        {
        return path(
            AdqlTableLinkFactory.TABLE_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlTableController()
        {
        super();
        }

    @Override
    public Iterable<AdqlTableBean> bean(final Iterable<AdqlTable> iter)
        {
        return new AdqlTableBean.Iter(
            iter
            );
        }

    @Override
    public AdqlTableBean bean(final AdqlTable entity)
        {
        return new AdqlTableBean(
            entity
            );
        }

    /**
     * Get the target {@link AdqlTable} based on the {@Identifier} in the request path.
     * @param ident The {@link AdqlTable} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The target {@link AdqlTable}.
     * @throws IdentifierNotFoundException If the {@link AdqlTable} could not be found.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public AdqlTable entity(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        )
    throws IdentifierNotFoundException, IdentifierFormatException, ProtectionException
        {
        return factories().adql().tables().entities().select(
            factories().adql().tables().idents().ident(
                ident
                )
            );
        }

    /**
     * {@link RequestMethod#GET} request to select a specific {@link AdqlTable}.
     * <br/>Request path : [{@value AdqlTableLinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param entity The {@link AdqlTable} selected using the {@Identifier} in the request path.
     * @return The selected {@link AdqlTable} wrapped in a {@link AdqlTableBean}.
     * 
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public ResponseEntity<AdqlTableBean> select(
        @ModelAttribute(TARGET_ENTITY)
        final AdqlTable table
        ){
        return selected(
            table
            );
        }

    /**
     * {@link RequestMethod#POST} request to update a specific {@link AdqlTable}.
     * <br/>Request path : [{@value AdqlSchemaLinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param entity The {@link AdqlTable} selected using the {@Identifier} in the request path.
     * <br/>Optional {@link AdqlTable} params :
     * @param name   The {@link AdqlTable} name, [{@value #TABLE_NAME_PARAM}].
     * @return The updated {@link AdqlTable} wrapped in a {@link AdqlTableBean}.
     * @throws ProtectionException 
     * @throws NameFormatException 
     * 
     */
    @ResponseBody
    @UpdateAtomicMethod
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<AdqlTableBean> update(
        @ModelAttribute(TARGET_ENTITY)
        final AdqlTable table,
        @RequestParam(value=TABLE_NAME_PARAM, required=false)
        final String name
        )
    throws NameFormatException, ProtectionException
        {
        if (name != null)
            {
            if (name.length() > 0)
                {
                table.name(
                    name
                    );
                }
            }
        return selected(
            table
            );
        }
    }
