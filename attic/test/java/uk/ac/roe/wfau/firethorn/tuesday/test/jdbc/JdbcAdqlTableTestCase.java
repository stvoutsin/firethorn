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
package uk.ac.roe.wfau.firethorn.tuesday.test.jdbc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlParserTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 * TODO experiment with DatabaseBuilder
 * http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseBuilder.html
 *
 */
@Slf4j
public class JdbcAdqlTableTestCase
    extends TestBase
    {

	public JdbcResource resource()
		{
        return factories().jdbc().resources().entities().create(
            "twomass",
            "test:twomass",
            "spring:RoeTWOMASS"
            );
		}

	public AdqlResource workspace()
		{
        return factories().adql().resources().entities().create(
            "test-workspace"
            );
		}

    public void display(final AdqlResource resource)
	    {
	    log.debug("---");
	    log.debug("- ADQL resource [{}]", resource.name());

	    for (final AdqlSchema schema : resource.schemas().select())
	        {
	        log.debug("--- Schema [{}][{}]", new Object[] {resource.name(), schema.name()});
	        for (final AdqlTable table : schema.tables().select())
	            {
	            log.debug("---- Table [{}][{}][{}]", new Object[] {table.resource().name(), table.alias(), table.namebuilder()});
	            log.debug("---- Base  [{}][{}][{}]", new Object[] {table.base().resource().name(), table.base().alias(), table.base().namebuilder()});
	            for (final AdqlColumn column : table.columns().select())
	                {
	                log.debug("----- Column [{}][{}][{}]", new Object[] {column.resource().name(),        column.alias(),        column.namebuilder()});
	                log.debug("----- Base   [{}][{}][{}]", new Object[] {column.base().resource().name(), column.base().alias(), column.base().namebuilder()});
	                }
	            }
	        }
	    }

	@Test
    public void test001()
    throws Exception
        {
        final JdbcResource resource  = resource();
        final AdqlResource workspace = workspace();
        //
        // Import the JdbcSchema metadata.
        //resource.inport();
        //final Object a = resource.schemas();
        //final Object b = resource.schemas().select("TWOMASS","dbo");
        //final Object c = resource.schemas().select("TWOMASS","dbo").tables();
        //final Object d = resource.schemas().select("TWOMASS","dbo").tables().select("twomass_psc");
        //
        // Import a JdbcTable into an AdqlSchema
        workspace.schemas().create(
            "test-schema"
            ).tables().create(
	    		resource.schemas().select("TWOMASS","dbo").tables().select("twomass_psc"),
	            "test-table"
	            );
        display(
    		workspace
    		);

        }

	@Test
    public void test002()
    throws Exception
        {
        final JdbcResource resource  = resource();
        final AdqlResource workspace = workspace();
        //
        // Import a JdbcSchema into our workspace.
        workspace.schemas().create(
            "test-schema",
    		resource.schemas().select(
    		    "TWOMASS",
    		    "dbo"
    		    )
            );
        display(
    		workspace
    		);
        }

    /**
     * Our autowired AdqlParserTable factory.
     *
     */
    @Autowired
    private AdqlParserTable.Factory tables;
    public AdqlParserTable.Factory tables()
    	{
    	return this.tables ;
    	}
    }
