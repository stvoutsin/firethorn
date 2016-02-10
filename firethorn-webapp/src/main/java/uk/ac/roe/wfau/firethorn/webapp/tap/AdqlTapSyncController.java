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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.webapp.votable.*;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.tap.TapError;
import uk.ac.roe.wfau.firethorn.webapp.tap.TapJobParams;
import uk.ac.roe.wfau.firethorn.webapp.tap.CommonParams;
import uk.ac.roe.wfau.firethorn.webapp.tap.TapValidator;
import uk.ac.roe.wfau.firethorn.webapp.tap.CapabilitiesGenerator;
import uk.ac.roe.wfau.firethorn.blue.*;
import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;

@Slf4j
@Controller
@RequestMapping("/tap/{ident}/")
public class AdqlTapSyncController extends AbstractController {

	/**
	 * Timeout for query job in miliseconds
	 */
	static final Integer TIMEOUT = 600000;

	/**
	 * Param to start a job
	 */
	static final Status STARTJOB = Status.RUNNING;

	@Autowired
	private CapabilitiesGenerator capgenerator;

	@Override
	public Path path() {
		// TODO Auto-generated method stub
		return path("/tap/{ident}/");
	}

	/**
	 * Get the target workspace based on the ident in the path.
	 *
	 */
	@ModelAttribute("urn:adql.resource.entity")
	public AdqlResource entity(@PathVariable("ident") final String ident) throws IdentifierNotFoundException {
		log.debug("entity() [{}]", ident);
		return factories().adql().resources().entities().select(factories().adql().resources().idents().ident(ident));
	}

	/**
	 * Web service method Create a Synchronous query job
	 * 
	 */
	@RequestMapping(value = "sync", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void sync(@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "QUERY", required = false) String QUERY,
			@RequestParam(value = "LANG", required = false) String LANG,
			@RequestParam(value = "REQUEST", required = false) String REQUEST, 
			@RequestParam(value = "FORMAT", required = false) String FORMAT, 
			@RequestParam(value = "VERSION", required = false) String VERSION, 
			@RequestParam(value = "MAXREC", required = false) String MAXREC
            )
					throws IdentifierNotFoundException, IOException {

		
		response.setContentType(CommonParams.TEXT_XML_MIME);
		
		String tap_query = QUERY;
		String lang = LANG;
		String req = REQUEST;
		String format = FORMAT;
		String version = VERSION;
		String maxrec = MAXREC;

		String results = "";
		PrintWriter writer = response.getWriter();
		response.setCharacterEncoding("UTF-8");

        //
        // Let's obtains parameters name here! 
        //
		log.debug("**********");
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String parameterName = (String) enumeration.nextElement();
			
			if (parameterName.toLowerCase().equals("query")){
				tap_query = request.getParameter(parameterName);
			}
			
			if (parameterName.toLowerCase().equals("lang")){
				lang = request.getParameter(parameterName);
			}
			
			if (parameterName.toLowerCase().equals("request")){
				req = request.getParameter(parameterName);
			}
			
			if (parameterName.toLowerCase().equals("format")){
				format = request.getParameter(parameterName);
			}
			
			if (parameterName.toLowerCase().equals("version")){
				version = request.getParameter(parameterName);
			}
			
			if (parameterName.toLowerCase().equals("maxrec")){
				maxrec = request.getParameter(parameterName);
			}
			
			log.debug(parameterName);
			log.debug(request.getParameter(parameterName));
			
			
		}
		
		log.debug(LANG);

		

		// Check input parameters and return VOTable with appropriate message if
		// any errors found
		TapValidator validator = new TapValidator(req, lang, tap_query, format, version, maxrec);
		boolean valid = validator.checkParams();

		if (valid) {

			if (REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)) {
				writer.append(capgenerator.generateCapabilities(resource,request));
				return;
			} else if (REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_DO_QUERY)) {

				try {
					BlueQuery query;
					if (MAXREC!=null){
						query = resource.blues().create(QUERY, 
								factories().blues().limits().create(
										Long.parseLong(MAXREC.trim()),
										null,
										null
				                        ),
								TaskState.COMPLETED,
								factories().blues().limits().absolute().time());
						;
					} else {
						query = resource.blues().create(QUERY, TaskState.COMPLETED,
								factories().blues().limits().absolute().time());
					}


					// Write results to VOTable using AdqlQueryVOTableController
					if (query != null) {
						
						if (query.state() == TaskState.EDITING || 
								query.state() == TaskState.QUEUED || 
										query.state() == TaskState.READY || 
												query.state() == TaskState.RUNNING || 
														query.state() == TaskState.CANCELLED
								) {
							
							response.setContentType("text/xml");
						    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							writer.append(TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR));
							return;
							
						} else if (query.state() == TaskState.FAILED || query.state() == TaskState.ERROR) {
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							response.setContentType("text/xml");
							
							if (query.syntax()!=null){
								writer.append(TapError.writeErrorToVotable(StringEscapeUtils.escapeXml(query.syntax().friendly())));
							} else {
								writer.append(TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR));
							}
				
							return;
							
						} else {
							results = query.results().adql().link() + "/votable";
							response.setStatus(HttpServletResponse.SC_SEE_OTHER);
						    response.setHeader("Location", results);
						    writer.append("Location: " + results);
						    return;
						    
						}
					

					} 

				} catch (final Exception ouch) {
					log.error("Exception caught [{}]", ouch);
					TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR);
					return;
				}

			}
		} else {
			writer.append(validator.getErrorMessage());
			return;
		}
		
		
		writer.append(TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR));
		return;
	}

	


}
