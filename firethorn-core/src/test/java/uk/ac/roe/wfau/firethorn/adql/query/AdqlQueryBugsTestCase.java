/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.query;

import static org.junit.Assert.assertEquals;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.QueryParam;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.QuerySelectFieldTestBase.ExpectedField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
@Slf4j
public class AdqlQueryBugsTestCase
    extends AtlasQueryTestBase
    {

    /**
     * Distance is a reserved word in ADQL.
     * "SELECT neighbours.distanceMins AS distance"
     * 
     */
    @Test
    public void test002S()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 10\n" + 
            "    distanceMins AS distance\n" + 
            "FROM\n" + 
            "    ATLASv20130426.atlasSourceXtwomass_psc\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }

    /**
     * Distance is a reserved word in ADQL.
     * "SELECT neighbours.distanceMins AS distance"
     * 
     */
    @Test
    public void test002L()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" + 
            "    distanceMins AS distance\n" + 
            "FROM\n" + 
            "    ATLASv20130426.atlasSourceXtwomass_psc\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        assertEquals(
            "DISTANCE is an ADQL reserved word",
            query.syntax().warnings().iterator().next()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("dist", AdqlColumn.Type.FLOAT, 0),
                }
            );
        compare(
            query,
            "select top 10 atlasv20130426.dbo.atlassourcextwomass_psc.distancemins as dist from atlasv20130426.dbo.atlassourcextwomass_psc"
            );
        }

    /**
     * SQLServer '..' syntax to access default schema.
     * "FROM TWOMASS..twomass_psc"
     * 
     */
    @Test
    public void test003S()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 10\n" + 
            "    ra,\n" + 
            "    dec\n" + 
            "FROM\n" + 
            "    TWOMASS..twomass_psc\n" + 
            ""
            );

        assertEquals(
            AdqlQuery.Syntax.Level.STRICT,
            query.syntax().level()
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );

        // TODO Explicitly state LEGACY not available in warning.
        // TODO Change from a string match to a token in the parser grammar.

        }
    
    /**
     * SQLServer '..' syntax to access default schema.
     * "FROM TWOMASS..twomass_psc"
     * 
     */
    @Test
    public void test003L()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" + 
            "    ra,\n" + 
            "    dec\n" + 
            "FROM\n" + 
            "    TWOMASS..twomass_psc\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        assertEquals(
            "SQLServer '..' syntax is not required",
            query.syntax().warnings().iterator().next()
            );
        validate(
            query,
            new ExpectedField[] {
            new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
            new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        compare(
            query,
            "select top 10 twomass.dbo.twomass_psc.ra as ra, twomass.dbo.twomass_psc.dec as dec from twomass.dbo.twomass_psc"
            );
        }

    /**
     * Duplicate column names throw Hibernate ConstraintViolationException.
     * "SELECT atlas.ra, rosat.ra"
     * 
     */
    @Test
    public void test004S()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 10\n" + 
            "    atlas.ra,\n" + 
            "    atlas.dec,\n" + 
            "    rosat.ra,\n" + 
            "    rosat.dec\n" + 
            "FROM\n" + 
            "    atlasSource AS atlas,\n" + 
            "    ROSAT.rosat_fsc AS rosat,\n" + 
            "    atlasSourceXrosat_fsc AS neighbours\n" + 
            "WHERE\n" + 
            "    neighbours.masterObjID=atlas.sourceID\n" + 
            "AND\n" + 
            "    neighbours.slaveObjID=rosat.seqNo\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        // TODO
        compare(
            query,
            "SELECT stuff"
            );
        }

    /**
     * Duplicate column names throw Hibernate ConstraintViolationException.
     * "SELECT atlas.ra, rosat.ra"
     * 
     */
    @Test
    public void test004L()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" + 
            "    atlas.ra,\n" + 
            "    atlas.dec,\n" + 
            "    rosat.ra,\n" + 
            "    rosat.dec\n" + 
            "FROM\n" + 
            "    atlasSource AS atlas,\n" + 
            "    ROSAT.rosat_fsc AS rosat,\n" + 
            "    atlasSourceXrosat_fsc AS neighbours\n" + 
            "WHERE\n" + 
            "    neighbours.masterObjID=atlas.sourceID\n" + 
            "AND\n" + 
            "    neighbours.slaveObjID=rosat.seqNo\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        // TODO
        compare(
            query,
            "SELECT stuff"
            );
        }
    
    /**
     * WHERE clause contains '%' modulus operator.
     * "atlas.sourceID % 100 = 0"
     * 
     */
    @Test
    public void test005S()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 10\n" + 
            "    atlas.ra,\n" + 
            "    atlas.dec\n" + 
            "FROM\n" + 
            "    atlasSource AS atlas\n" + 
            "WHERE\n" + 
            "    atlas.sourceID % 100 = 0\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        // TODO Check the error message.
        }

    /**
     * WHERE clause contains '%' modulus operator.
     * "atlas.sourceID % 100 = 0"
     * 
     */
    @Test
    public void test005L()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" + 
            "    atlas.ra,\n" + 
            "    atlas.dec\n" + 
            "FROM\n" + 
            "    atlasSource AS atlas\n" + 
            "WHERE\n" + 
            "    atlas.sourceID % 100 = 0\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        compare(
            query,
            "select top 10 atlas.ra as ra, atlas.dec as dec from atlasv20130426.dbo.atlassource as atlas where atlas.sourceid%100 = 0"
            );
        }
    
    
    /**
     * Inner WHERE clause refers to table defined in outer FROM clause.
     * "WHERE masterObjID = neighbours.masterObjID"
     * 
     */
    @Test
    public void test006L()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" + 
            "    atlas.ra,\n" + 
            "    atlas.dec\n" + 
            "FROM\n" + 
            "    atlasSource AS atlas,\n" + 
            "    TWOMASS.twomass_psc AS twomass_psc,\n"+ 
            "    atlasSourceXDR8PhotoObj AS neighbours\n" + 
            "WHERE\n" + 
            "   masterObjID = atlas.sourceID\n" + 
            "AND\n" + 
            "    slaveObjID = twomass_psc.ra\n" + 
            "AND\n" + 
            "    distanceMins IN (\n" + 
            "        SELECT\n" + 
            "            MIN(distanceMins)\n" + 
            "        FROM\n" + 
            "            atlasSourceXDR8PhotoObj\n" + 
            "        WHERE\n" + 
            "            masterObjID = neighbours.masterObjID\n" + 
            "        )\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        assertEquals(
        			"SELECT TOP 10 atlas.ra AS ra , atlas.dec AS dec FROM ATLASv20130426.dbo.atlasSource AS atlas , TWOMASS.dbo.twomass_psc AS twomass_psc  , ATLASv20130426.dbo.atlasSourceXDR8PhotoObj AS neighbours  WHERE neighbours.masterObjID = atlas.sourceID AND neighbours.slaveObjID = twomass_psc.ra AND neighbours.distanceMins IN (SELECT MIN(ATLASv20130426.dbo.atlasSourceXDR8PhotoObj.distanceMins) AS MIN FROM ATLASv20130426.dbo.atlasSourceXDR8PhotoObj WHERE ATLASv20130426.dbo.atlasSourceXDR8PhotoObj.masterObjID = neighbours.masterObjID)",
        		query.osql().replace('\n', ' ')
                );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("MIN", AdqlColumn.Type.FLOAT, 0),

                }
            );
    
        }

    /**
     * DATETIME column.
     * 
     */
    @Test
    public void test007L()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" + 
            "    utDate,\n" + 
            "    dateObs\n" + 
            "FROM\n" + 
            "    multiframe\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("creationDate", AdqlColumn.Type.DATETIME, 0),
                }
            );
        // TODO - verify the JDBC column is DATE TIME, not TIMESTAMP.
        // Firethorn create table fails if we have two DATETIME columns.
        // OGSA-DAI insert into TIMESTAMP column fails.

        // TODO
        compare(
            query,
            "SELECT stuff"
            );
        }

    /**
     * Alias for nested query in SELECT.
     * SELECT ... FROM (SELECT .... AS ident ...) AS inner WHERE inner.ident = outer.field
     * 
     */
    @Test
    public void test008L()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT\n" + 
            "    neighbours.distanceMins\n" + 
            "FROM\n" + 
            "    atlassourcexDR8photoobj AS neighbours,\n" + 
            "    (\n" + 
            "    SELECT TOP 10\n" + 
            "        sourceId AS ident\n" + 
            "    FROM\n" + 
            "        atlasSource\n" + 
            "    ) AS sources\n" + 
            "WHERE\n" + 
            "    neighbours.masterObjID = sources.ident\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("dist", AdqlColumn.Type.FLOAT, 0),
                }
            );
        compare(
            query,
            "select neighbours.distancemins as dist from atlasv20130426.dbo.atlassourcexdr8photoobj as neighbours, (select top 10 atlasv20130426.dbo.atlassource.sourceid as ident from atlasv20130426.dbo.atlassource) as sources where neighbours.masterobjid = sources.ident"
            );
        }
    }
