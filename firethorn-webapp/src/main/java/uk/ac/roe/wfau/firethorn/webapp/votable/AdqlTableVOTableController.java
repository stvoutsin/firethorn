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
package uk.ac.roe.wfau.firethorn.webapp.votable;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlTableLinkFactory;

/**
 * Spring MVC controller to format an {@link AdqlTable} as a <a href='http://www.ivoa.net/documents/VOTable/'>VOTable</a>.
 * <br/>Controller path : [{@value AdqlTableLinkFactory#VOTABLE_PATH}]
 * 
 */
@Controller
@RequestMapping(AdqlTableLinkFactory.VOTABLE_PATH)
public class AdqlTableVOTableController
extends BaseTableVOTableController
    {
    /**
     * Public constructor.
     *
     */
    public AdqlTableVOTableController()
        {
        super();
        }

    @Override
    public Path path()
        {
        return path(
            AdqlTableLinkFactory.VOTABLE_PATH
            );
        }

    /**
     * HTTP GET request for a VOTable representation of an {@link AdqlTable}.
     * <br/>Request path : [{@value AdqlTableLinkFactory#VOTABLE_PATH}]
     * @param ident The {@link AdqlTable} identifier from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @param response The {@link HttpServletResponse} to write the VOTable to.
     * @throws EntityNotFoundException If the {@link AdqlTable} could not be found.
     * @throws IOException If there is an error writing to the {@link HttpServletResponse}.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=TEXT_XML_MIME)
    public void votable(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident,
        final HttpServletResponse response
        )
    throws EntityNotFoundException, IOException, IdentifierFormatException, ProtectionException
        {
        response.setContentType(
            TEXT_XML_MIME
            );
        response.setCharacterEncoding(
            "UTF-8"
            );
		write(
		    response.getWriter(),
	        factories().adql().tables().entities().select(
	            factories().adql().tables().idents().ident(
	                ident
	                )
	            )
		    );
        }
    }
