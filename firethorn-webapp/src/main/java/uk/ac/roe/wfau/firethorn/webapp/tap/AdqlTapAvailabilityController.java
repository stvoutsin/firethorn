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
package uk.ac.roe.wfau.firethorn.webapp.tap;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

@Slf4j
@Controller
@RequestMapping("/tap/{ident}/")
public class AdqlTapAvailabilityController extends AbstractController {

	
    public static final String TARGET_ENTITY = "urn:adql.resource.entity" ;

	@Override
	public Path path() {
		// TODO Auto-generated method stub
		return path("/tap/{ident}/") ;
	}
	 /**
     * Get the target workspace based on the ident in the path. 
	 * @throws ProtectionException 
	 * @throws IdentifierFormatException 
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public AdqlResource entity(
        @PathVariable("ident")
        final String ident
        ) throws IdentifierNotFoundException, IdentifierFormatException, ProtectionException  {
        log.debug("entity() [{}]", ident);
        return factories().adql().resources().entities().select(
            factories().adql().resources().idents().ident(
                ident
                )
            );
        }

    /**
     * Write out the availability XML for this resource
     * @param writer
     */
    private void generateAvailability(PrintWriter writer){
    	writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
    			"<vosi:availability xmlns:vosi=\"http://www.ivoa.net/xml/VOSIAvailability/v1.0\">" +
    			"<vosi:available>true</vosi:available>" +
    			"<vosi:note>service is accepting queries</vosi:note></vosi:availability>");
    }
    
    /**
     * Web service method
     * Get the capabilities of a resource
     * 
     */
	@RequestMapping(value="availability", method = RequestMethod.GET, produces=CommonParams.TEXT_XML_MIME)
	public void availability(
        @ModelAttribute(TARGET_ENTITY)
        AdqlResource resource,
        final HttpServletResponse response
        ) throws  IdentifierNotFoundException, IOException {
		
			PrintWriter writer = response.getWriter();

	        response.setContentType(
	        		CommonParams.TEXT_XML_MIME
	            );
	        response.setCharacterEncoding(
	            "UTF-8"
	            );
		
	        generateAvailability(writer);
	}



	 
}
