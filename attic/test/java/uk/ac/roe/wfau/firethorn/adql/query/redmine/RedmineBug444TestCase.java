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
package uk.ac.roe.wfau.firethorn.adql.query.redmine;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase;

/**
 * JUnit test case for RedMine issue #444
 * http://redmine.roe.ac.uk/issues/444
 *
 */
public class RedmineBug444TestCase
    extends AtlasQueryTestBase
    {

    /**
     * Try using nested query to separate ROUND() calculation from the GROUP BY clause.
     * Should work, fails with internal parser error.
     * [AdqlParserImpl] Error parsing query [ADQLColumn with unknown DBLink class [adql.db.DefaultDBColumn]]
     * Known to fail.
     * http://redmine.roe.ac.uk/issues/444
     *
     */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            " SELECT" +
            "     nested.lon      AS lon," +
            "     nested.lat      AS lat," +
            "     COUNT(*) AS num" +
            " FROM" +
            "     (SELECT" +
            "         ROUND(l*6.0,0)/6.0 AS lon," +
            "         ROUND(b*6.0,0)/6.0 AS lat" +
            "     FROM" +
            "         ATLASDR1.atlasSource" +
            "     WHERE" +
            "         priOrSec = 0 OR priOrSec = frameSetID" +
            "     ) AS nested" +
            " GROUP BY" +
            "     nested.lon," +
            "     nested.lat"
            );
        }
    }
