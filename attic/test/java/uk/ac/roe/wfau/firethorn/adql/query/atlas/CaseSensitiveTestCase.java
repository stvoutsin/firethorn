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
package uk.ac.roe.wfau.firethorn.adql.query.atlas;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InternalServerErrorException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InvalidRequestException;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
public class CaseSensitiveTestCase
    extends AtlasQueryTestBase
    {

    /**
     * Camel case table, lower case column.
     *
     */
    @Test
    public void test001()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    {ATLAS_VERSION}.dbo.atlasSource.ra AS ra," +
            "    {ATLAS_VERSION}.dbo.atlasSource.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource",

            new ExpectedField[] {
                new ExpectedField("ra", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }

    /**
     * Lower case table, lower case column.
     * Table lookup fails when using PostgreSQL metadata database (case sensitive LIKE).
     *
     */
    @Test
    public void test002()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    atlassource",

            " SELECT TOP 5" +
            "    {ATLAS_VERSION}.dbo.atlasSource.ra AS ra," +
            "    {ATLAS_VERSION}.dbo.atlasSource.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource",

            new ExpectedField[] {
                new ExpectedField("ra", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }

    /**
     * Upper case table, lower case column.
     * Table lookup fails when using PostgreSQL metadata database (case sensitive LIKE).
     *
     */
    @Test
    public void test003()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    ATLASSOURCE",

            " SELECT TOP 5" +
            "    {ATLAS_VERSION}.dbo.atlasSource.ra AS ra," +
            "    {ATLAS_VERSION}.dbo.atlasSource.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource",

            new ExpectedField[] {
                new ExpectedField("ra", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }

    /**
     * Camel case table, upper case column.
     *
     */
    @Test
    public void test004()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    RA," +
            "    DEC" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    {ATLAS_VERSION}.dbo.atlasSource.ra AS ra," +
            "    {ATLAS_VERSION}.dbo.atlasSource.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource",

            new ExpectedField[] {
                new ExpectedField("RA", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("DEC", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }

    /**
     * Lower case table, upper case column.
     * Table lookup fails when using PostgreSQL metadata database (case sensitive LIKE).
     *
     */
    @Test
    public void test005()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    RA," +
            "    DEC" +
            " FROM" +
            "    atlassource",

            " SELECT TOP 5" +
            "    {ATLAS_VERSION}.dbo.atlasSource.ra AS ra," +
            "    {ATLAS_VERSION}.dbo.atlasSource.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource",

            new ExpectedField[] {
                new ExpectedField("RA", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("DEC", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }

    /**
     * Upper case table, upper case column.
     * Table lookup fails when using PostgreSQL metadata database (case sensitive LIKE).
     *
     */
    @Test
    public void test006()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    RA," +
            "    DEC" +
            " FROM" +
            "    ATLASSOURCE",

            " SELECT TOP 5" +
            "    {ATLAS_VERSION}.dbo.atlasSource.ra AS ra," +
            "    {ATLAS_VERSION}.dbo.atlasSource.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource",

            new ExpectedField[] {
                new ExpectedField("RA", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("DEC", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }
    }
