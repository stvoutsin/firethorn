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
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Controller
@RequestMapping("/tap/{ident}/")
public class AdqlTapSchemaController extends AbstractController {

	public static final String TARGET_ENTITY = "urn:adql.resource.entity";

	@Autowired
	private ServletContext servletContext;
	
    /**
     * Property for updating the connection URL.
     *
     */
    public static final String CONN_URL = "url" ;

    /**
     * Property for updating the connection user name.
     *
     */
    public static final String CONN_USER = "user" ;

    /**
     * Property for updating the connection password.
     *
     */
    public static final String CONN_PASS = "pass" ;
    

    /**
     * Property for updating the connection driver.
     *
     */
    public static final String CONN_DRIVER = "driver" ;

    /**
     * Property for updating the connection catalog.
     *
     */
    public static final String CONN_CATALOG = "catalog" ;

    /**
     * Property for updating the connection database.
     *
     */
    public static final String CONN_DATABASE = "database" ;

    /**
     * Property for updating the connection host.
     *
     */
    public static final String CONN_HOST= "host" ;
    
    /**
     * Property for updating the connection type.
     *
     */
    public static final String CONN_TYPE = "type" ;
    
    /**
     * Property for updating the connection port.
     *
     */
    public static final String CONN_PORT = "port" ;
    

	@Value("${firethorn.webapp.baseurl:null}")
	private String baseurl;

	@Value("${firethorn.tapschema.resource.name}")
	private String jdbcname;

	@Value("${firethorn.tapschema.database.user}")
	private String username;

	@Value("${firethorn.tapschema.database.pass}")
	private String password;

	@Value("${firethorn.tapschema.database.name}")
	private String catalog;

	@Value("${firethorn.tapschema.database.name}")
	private String database;

	@Value("${firethorn.tapschema.database.host}")
	private String host;

	@Value("${firethorn.tapschema.database.type:pgsql}")
	private String type;

	@Value("${firethorn.tapschema.database.driver:org.postgresql.Driver}")
	private String driver;

	@Value("${firethorn.tapschema.database.port:5432}")
	private String port;

	@Override
	public Path path() {
		// TODO Auto-generated method stub
		return path("/tap/{ident}/");
	}

	/**
	 * Get the target workspace based on the ident in the path.
	 * @throws ProtectionException 
	 * @throws IdentifierFormatException 
	 * 
	 */
	@ModelAttribute(TARGET_ENTITY)
	public AdqlResource entity(@PathVariable("ident") final String ident)
			throws IdentifierNotFoundException, IdentifierFormatException, ProtectionException {
		log.debug("entity() [{}]", ident);
		return factories().adql().resources().entities()
				.select(factories().adql().resources().idents().ident(ident));
	}

	/**
	 * Web service method Generate the TAP_SCHEMA for this resource
	 * 
	 * @param resource
	 * @param response
	 * @throws IdentifierNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws ProtectionException 
	 * @throws NameNotFoundException 
	 */
	@RequestMapping(value = "generateTapSchema", method = {  RequestMethod.POST, RequestMethod.GET })
	public void generateTapSchema(
			@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response,
			HttpServletRequest request)
			throws IdentifierNotFoundException, IOException, SQLException,
			ClassNotFoundException, ProtectionException, NameNotFoundException {

        TapSchemaProperties properties = new TapSchemaProperties(username, password, catalog, database, host, type, driver, port, jdbcname);
		TapSchemaGeneratorImpl generator = new TapSchemaGeneratorImpl(servletContext, factories(), resource, properties);
		generator.getProperties().setBaseurl(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());

		log.debug("Before JDBC");
        component.jdbc(generator);
        log.debug("After JDBC");

        log.debug("Before ADQL");
        component.adql(generator);
        log.debug("After ADQL");

	}

	@Autowired
	private AdqlTapSchemaComponent component ;
    
}
