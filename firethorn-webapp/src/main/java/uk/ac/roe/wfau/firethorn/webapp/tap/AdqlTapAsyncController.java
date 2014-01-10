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
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import adql.query.ADQLQuery;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryVOTableController;

@Slf4j
@Controller
@RequestMapping("/tap/{ident}/async")
public class AdqlTapAsyncController extends AbstractController {
	
	
   /**
    * VOTable MIME type.
    *
    */
   public static final String VOTABLE_MIME = "application/x-votable+xml" ;
   
   public static final String FIRETHORN_TAP_BASE ="http://localhost:8080/firethorn/tap/";
   /**
    * TextXml MIME type.
    *
    */
   public static final String TEXT_XML_MIME = "text/xml" ;

	@Override
	public Path path() {
		// TODO Auto-generated method stub
		return path("/tap/{ident}/async") ;
	}
	
	/**
     * Get the target workspace based on the ident in the path. 
     *
     */
    @ModelAttribute("urn:adql.resource.entity")
    public AdqlResource entity(
        @PathVariable("ident")
        final String ident
        ) throws IdentifierNotFoundException  {
        log.debug("entity() [{}]", ident);
      
        return factories().adql().resources().select(
            factories().adql().resources().idents().ident(
                ident
                )
            );
        }
   

	/**
     * Get the target workspace based on the query in the path. 
     *
     */
    @ModelAttribute("urn:adql.resource.query")
    public AdqlQuery queryentity(
        @PathVariable("jobid")
        final String jobid
        ) throws IdentifierNotFoundException  {
        log.debug("query() [{}]", jobid);
     
		return factories().adql().queries().select(
		            factories().adql().queries().idents().ident(
		            		jobid
		                )
		            );
    }
		
      @ResponseBody
      @RequestMapping(method = RequestMethod.POST, produces="text/plain")
      public String initAsyncJob (
    		  @ModelAttribute("urn:adql.resource.entity")
    		  AdqlResource resource,
    		  @RequestParam(value="LANG", required = false) String LANG,
    		  @RequestParam(value="REQUEST", required = false) String REQUEST,
    		  @RequestParam(value="QUERY", required = false) String QUERY
    		  ) throws Exception {
 
				UWSJob uwsjob;
	    		//PrintWriter writer = response.getWriter();
	    		String queryid = "";
	    		
				//if ( resource!=null){
				//	uwsjob = new UWSJob(resource);
				//} else {
				//	uwsjob = new UWSJob();
				//}
				
				//	if (REQUEST!=null) uwsjob.setRequest(REQUEST);
				//	if (LANG!=null) uwsjob.setLang(LANG);
				//	if (QUERY!=null) uwsjob.setQuery(QUERY);
				//	queryid = uwsjob.getQueryId();
	    		return "Go here: ";
	    	//	writer.append(FIRETHORN_TAP_BASE + "async/");
	    	//	writer.append(uwsjob.getQueryId());
    }
				 

    

    @RequestMapping(value="/{jobid}/parameters", method = RequestMethod.POST)
  	@ResponseBody
    public void setparameters (
    		  @PathVariable String jobid,
    		  @ModelAttribute("urn:adql.resource.entity")
    	        AdqlResource resource,
    	      @ModelAttribute("urn:adql.resource.query")
    	        AdqlQuery queryentity,
    	      @RequestParam(value="LANG", required = false) String LANG,
    		  @RequestParam(value="REQUEST", required = false) String REQUEST,
    		  @RequestParam(value="QUERY", required = false) String QUERY,
    		  final HttpServletResponse response
    		  ) throws  IdentifierNotFoundException, Exception {
    		
    		PrintWriter writer = response.getWriter();
    		UWSJob uwsjob;
    		String queryid = "";
    		
    		if (queryentity!=null && resource!=null){
    			uwsjob = new UWSJob(queryentity, resource);
    		} else {
    			uwsjob = new UWSJob();
    		}
    		
    		
    		if (REQUEST!=null) uwsjob.setRequest(REQUEST);
    		if (LANG!=null) uwsjob.setLang(LANG);
    		if (QUERY!=null) uwsjob.setQuery(QUERY);
    		
    		queryid = uwsjob.getQueryId();
    		writer.append(queryid);
      }
    
    
    @RequestMapping(value="/{jobid}/phase", method = RequestMethod.POST)
	@ResponseBody
    public void phase(@PathVariable String jobid,
    		@ModelAttribute("urn:adql.resource.entity")
    			AdqlResource resource,
    		@ModelAttribute("urn:adql.resource.query")
    	        AdqlQuery queryentity,
    		final HttpServletResponse response,
    		@RequestParam(value="PHASE", required = true) String PHASE) throws  IdentifierNotFoundException, Exception {
    	
    	PrintWriter writer = response.getWriter();
    	UWSJob uwsjob;
    	String queryid = "";

    	try{
			
			if (queryentity!=null && resource!=null){
				uwsjob = new UWSJob(queryentity, resource);
			} else {
				uwsjob = new UWSJob();
			}
			//queryid = uwsjob.getResults();

			if (PHASE==null){
				TapError.writeErrorToVotable(TapJobErrors.PARAM_PHASE_MISSING, writer);
			} else {
				uwsjob.setPhase(PHASE);
			}
			
			writer.append(uwsjob.getQueryId());
			
  		} catch (Exception e) {
  			writer.append("ERROR");
  		}
		
	
		//writer.append(uwsjob.getQueryId());
	 	
    }
    
    @RequestMapping(value="/{jobid}/quote", method = RequestMethod.GET)
	@ResponseBody
    public String quote(
    	  @PathVariable String jobid,
  		  @ModelAttribute("urn:adql.resource.entity")
  	        AdqlResource resource,
  	      @ModelAttribute("urn:adql.resource.query")
  	        AdqlQuery queryentity) {
        return "quote";
    }
    
    @RequestMapping(value="/{jobid}/executionduration", method = RequestMethod.GET	)
	@ResponseBody
    public String executionduration(
    	  @PathVariable String jobid,
  		  @ModelAttribute("urn:adql.resource.query")
  	        AdqlQuery queryentity,
  	      @ModelAttribute("urn:adql.resource.entity")
    		AdqlResource resource) {
        return "executionduration";
    }
 
    
    @RequestMapping(value="/{jobid}/destruction", method = RequestMethod.GET)
   	@ResponseBody
    public String destruction(
    		@PathVariable String jobid,
  		  	@ModelAttribute("urn:adql.resource.entity")
  	        	AdqlResource resource,
  	        @ModelAttribute("urn:adql.resource.query")
  	        	AdqlQuery queryentity) {
           return "destruction";
       }
    
    @RequestMapping(value="/{jobid}/error", method = RequestMethod.GET)
    @ResponseBody
    public String error(
    		@PathVariable String jobid,
  		  	@ModelAttribute("urn:adql.resource.entity")
  	        	AdqlResource resource,
  	        @ModelAttribute("urn:adql.resource.query")
    			AdqlQuery queryentity) {
        return "error";
    }
    
    
    @RequestMapping(value="/{jobid}/results", method = RequestMethod.POST)
	@ResponseBody
    public String results(
    		@PathVariable String jobid,
  		  	@ModelAttribute("urn:adql.resource.entity")
  	        	AdqlResource resource,
  	        @ModelAttribute("urn:adql.resource.query")
  	        	AdqlQuery queryentity) {
    	
	    	String queryid = "";
	    	UWSJob uwsjob;
	  		try{
				
				if (queryentity!=null && resource!=null){
					uwsjob = new UWSJob(queryentity, resource);
				} else {
					uwsjob = new UWSJob();
				}
				queryid = uwsjob.getResults();
	  		} catch (Exception e) {
	  			
	  		}
				
				
			
			
		 	return queryid;
    }
  
    
	private boolean checkParams(PrintWriter writer, String REQUEST,String LANG,String QUERY){

		String error_message;
		boolean valid = true;
		
		// Check for errors and return appropriate VOTable error messages		
		if (REQUEST==null){
			TapError.writeErrorToVotable(TapJobErrors.PARAM_REQUEST_MISSING, writer);
			valid = false;
			return valid;
		} else if (!REQUEST.equalsIgnoreCase("doQuery")){
			error_message = "Invalid REQUEST: " + REQUEST;
			TapError.writeErrorToVotable(error_message, writer);
			valid = false;
			return valid;
		}
		
		if (LANG==null){
			TapError.writeErrorToVotable(TapJobErrors.PARAM_LANGUAGE_MISSING, writer);
			valid = false;
			return valid;
		}  else if (!LANG.equalsIgnoreCase("ADQL") && !LANG.equalsIgnoreCase("PQL")){
			error_message = "Invalid LANGUAGE: " + LANG;
			TapError.writeErrorToVotable(error_message, writer);
			valid = false;
		} 
		
		if (QUERY==null){
			TapError.writeErrorToVotable(TapJobErrors.PARAM_QUERY_MISSING, writer);
			valid = false;
			return valid;
		}
		
		return valid;
		
	}
}
