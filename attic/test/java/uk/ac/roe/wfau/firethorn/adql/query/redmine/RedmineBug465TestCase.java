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
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 * JUnit test case for RedMine issue.
 * http://redmine.roe.ac.uk/issues/439
 *
 */
public class RedmineBug465TestCase
    extends AtlasQueryTestBase
    {

	/**
     * Fails table prefix on '*'.
     * Known to fail.
     * http://redmine.roe.ac.uk/issues/465
     * Result columns out of order? Comparison failure: Expected [sourceID] but was [aR] 
  
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT" + 
            "    ATLASsource.*," + 
            "    2 * ASIN(SQRT(POWER(-0.997825033922517 - cx, 2) + POWER(-0.052293794140904105 - cy, 2) + POWER(-0.040131792532559725 - cz, 2)) / 2) * 60 AS dist" + 
            " FROM" + 
            "    ATLASsource" + 
            " WHERE" + 
            "    dec > -2.3166666666666664 AND dec < -2.283333333333333" + 
            " AND" + 
            "    ra >= 182.98331970017435 AND ra <= 183.01668029982565" + 
            " AND" + 
            "    (cx * -0.997825033922517 + cy * -0.052293794140904105 +cz * -0.040131792532559725 >= 0.9999999576920253)" + 
            "",

            "select" + 
            " {ATLAS_VERSION}.dbo.atlassource.sourceID as sourceID," +
            " {ATLAS_VERSION}.dbo.atlassource.cuEventID as cuEventID," +
            " {ATLAS_VERSION}.dbo.atlassource.frameSetID as frameSetID," +
            " {ATLAS_VERSION}.dbo.atlassource.ra as ra," +
            " {ATLAS_VERSION}.dbo.atlassource.dec as dec," +
            " {ATLAS_VERSION}.dbo.atlassource.cx as cx," +
            " {ATLAS_VERSION}.dbo.atlassource.cy as cy," +
            " {ATLAS_VERSION}.dbo.atlassource.cz as cz," +
            " {ATLAS_VERSION}.dbo.atlassource.htmID as htmID," +
            " {ATLAS_VERSION}.dbo.atlassource.l as l," +
            " {ATLAS_VERSION}.dbo.atlassource.b as b," +
            " {ATLAS_VERSION}.dbo.atlassource.lambda as lambda," +
            " {ATLAS_VERSION}.dbo.atlassource.eta as eta," +
            " {ATLAS_VERSION}.dbo.atlassource.priOrSec as priOrSec," +
            " {ATLAS_VERSION}.dbo.atlassource.umgPnt as umgPnt," +
            " {ATLAS_VERSION}.dbo.atlassource.umgPntErr as umgPntErr," +
            " {ATLAS_VERSION}.dbo.atlassource.gmrPnt as gmrPnt," +
            " {ATLAS_VERSION}.dbo.atlassource.gmrPntErr as gmrPntErr," +
            " {ATLAS_VERSION}.dbo.atlassource.rmiPnt as rmiPnt," +
            " {ATLAS_VERSION}.dbo.atlassource.rmiPntErr as rmiPntErr," +
            " {ATLAS_VERSION}.dbo.atlassource.imzPnt as imzPnt," +
            " {ATLAS_VERSION}.dbo.atlassource.imzPntErr as imzPntErr," +
            " {ATLAS_VERSION}.dbo.atlassource.umgExt as umgExt," +
            " {ATLAS_VERSION}.dbo.atlassource.umgExtErr as umgExtErr," +
            " {ATLAS_VERSION}.dbo.atlassource.gmrExt as gmrExt," +
            " {ATLAS_VERSION}.dbo.atlassource.gmrExtErr as gmrExtErr," +
            " {ATLAS_VERSION}.dbo.atlassource.rmiExt as rmiExt," +
            " {ATLAS_VERSION}.dbo.atlassource.rmiExtErr as rmiExtErr," +
            " {ATLAS_VERSION}.dbo.atlassource.imzExt as imzExt," +
            " {ATLAS_VERSION}.dbo.atlassource.imzExtErr as imzExtErr," +
            " {ATLAS_VERSION}.dbo.atlassource.mergedClassStat as mergedClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.mergedClass as mergedClass," +
            " {ATLAS_VERSION}.dbo.atlassource.pStar as pStar," +
            " {ATLAS_VERSION}.dbo.atlassource.pGalaxy as pGalaxy," +
            " {ATLAS_VERSION}.dbo.atlassource.pNoise as pNoise," +
            " {ATLAS_VERSION}.dbo.atlassource.pSaturated as pSaturated," +
            " {ATLAS_VERSION}.dbo.atlassource.eBV as eBV," +
            " {ATLAS_VERSION}.dbo.atlassource.aU as aU," +
            " {ATLAS_VERSION}.dbo.atlassource.aG as aG," +
            " {ATLAS_VERSION}.dbo.atlassource.aR as aR," +
            " {ATLAS_VERSION}.dbo.atlassource.aI as aI," +
            " {ATLAS_VERSION}.dbo.atlassource.aZ as aZ," +
            " {ATLAS_VERSION}.dbo.atlassource.uPetroMag as uPetroMag," +
            " {ATLAS_VERSION}.dbo.atlassource.uPetroMagErr as uPetroMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.uPsfMag as uPsfMag," +
            " {ATLAS_VERSION}.dbo.atlassource.uPsfMagErr as uPsfMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.uSerMag2D as uSerMag2D," +
            " {ATLAS_VERSION}.dbo.atlassource.uSerMag2DErr as uSerMag2DErr," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag3 as uAperMag3," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag3Err as uAperMag3Err," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag4 as uAperMag4," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag4Err as uAperMag4Err," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag6 as uAperMag6," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag6Err as uAperMag6Err," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMagNoAperCorr3 as uAperMagNoAperCorr3," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMagNoAperCorr4 as uAperMagNoAperCorr4," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMagNoAperCorr6 as uAperMagNoAperCorr6," +
            " {ATLAS_VERSION}.dbo.atlassource.uHlCorSMjRadAs as uHlCorSMjRadAs," +
            " {ATLAS_VERSION}.dbo.atlassource.uGausig as uGausig," +
            " {ATLAS_VERSION}.dbo.atlassource.uEll as uEll," +
            " {ATLAS_VERSION}.dbo.atlassource.uPA as uPA," +
            " {ATLAS_VERSION}.dbo.atlassource.uErrBits as uErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.uAverageConf as uAverageConf," +
            " {ATLAS_VERSION}.dbo.atlassource.uClass as uClass," +
            " {ATLAS_VERSION}.dbo.atlassource.uClassStat as uClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.uppErrBits as uppErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.uSeqNum as uSeqNum," +
            " {ATLAS_VERSION}.dbo.atlassource.uXi as uXi," +
            " {ATLAS_VERSION}.dbo.atlassource.uEta as uEta," +
            " {ATLAS_VERSION}.dbo.atlassource.gPetroMag as gPetroMag," +
            " {ATLAS_VERSION}.dbo.atlassource.gPetroMagErr as gPetroMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.gPsfMag as gPsfMag," +
            " {ATLAS_VERSION}.dbo.atlassource.gPsfMagErr as gPsfMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.gSerMag2D as gSerMag2D," +
            " {ATLAS_VERSION}.dbo.atlassource.gSerMag2DErr as gSerMag2DErr," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag3 as gAperMag3," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag3Err as gAperMag3Err," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag4 as gAperMag4," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag4Err as gAperMag4Err," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag6 as gAperMag6," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag6Err as gAperMag6Err," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMagNoAperCorr3 as gAperMagNoAperCorr3," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMagNoAperCorr4 as gAperMagNoAperCorr4," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMagNoAperCorr6 as gAperMagNoAperCorr6," +
            " {ATLAS_VERSION}.dbo.atlassource.gHlCorSMjRadAs as gHlCorSMjRadAs," +
            " {ATLAS_VERSION}.dbo.atlassource.gGausig as gGausig," +
            " {ATLAS_VERSION}.dbo.atlassource.gEll as gEll," +
            " {ATLAS_VERSION}.dbo.atlassource.gPA as gPA," +
            " {ATLAS_VERSION}.dbo.atlassource.gErrBits as gErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.gAverageConf as gAverageConf," +
            " {ATLAS_VERSION}.dbo.atlassource.gClass as gClass," +
            " {ATLAS_VERSION}.dbo.atlassource.gClassStat as gClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.gppErrBits as gppErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.gSeqNum as gSeqNum," +
            " {ATLAS_VERSION}.dbo.atlassource.gXi as gXi," +
            " {ATLAS_VERSION}.dbo.atlassource.gEta as gEta," +
            " {ATLAS_VERSION}.dbo.atlassource.rPetroMag as rPetroMag," +
            " {ATLAS_VERSION}.dbo.atlassource.rPetroMagErr as rPetroMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.rPsfMag as rPsfMag," +
            " {ATLAS_VERSION}.dbo.atlassource.rPsfMagErr as rPsfMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.rSerMag2D as rSerMag2D," +
            " {ATLAS_VERSION}.dbo.atlassource.rSerMag2DErr as rSerMag2DErr," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag3 as rAperMag3," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag3Err as rAperMag3Err," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag4 as rAperMag4," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag4Err as rAperMag4Err," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag6 as rAperMag6," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag6Err as rAperMag6Err," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMagNoAperCorr3 as rAperMagNoAperCorr3," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMagNoAperCorr4 as rAperMagNoAperCorr4," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMagNoAperCorr6 as rAperMagNoAperCorr6," +
            " {ATLAS_VERSION}.dbo.atlassource.rHlCorSMjRadAs as rHlCorSMjRadAs," +
            " {ATLAS_VERSION}.dbo.atlassource.rGausig as rGausig," +
            " {ATLAS_VERSION}.dbo.atlassource.rEll as rEll," +
            " {ATLAS_VERSION}.dbo.atlassource.rPA as rPA," +
            " {ATLAS_VERSION}.dbo.atlassource.rErrBits as rErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.rAverageConf as rAverageConf," +
            " {ATLAS_VERSION}.dbo.atlassource.rClass as rClass," +
            " {ATLAS_VERSION}.dbo.atlassource.rClassStat as rClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.rppErrBits as rppErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.rSeqNum as rSeqNum," +
            " {ATLAS_VERSION}.dbo.atlassource.rXi as rXi," +
            " {ATLAS_VERSION}.dbo.atlassource.rEta as rEta," +
            " {ATLAS_VERSION}.dbo.atlassource.iPetroMag as iPetroMag," +
            " {ATLAS_VERSION}.dbo.atlassource.iPetroMagErr as iPetroMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.iPsfMag as iPsfMag," +
            " {ATLAS_VERSION}.dbo.atlassource.iPsfMagErr as iPsfMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.iSerMag2D as iSerMag2D," +
            " {ATLAS_VERSION}.dbo.atlassource.iSerMag2DErr as iSerMag2DErr," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag3 as iAperMag3," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag3Err as iAperMag3Err," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag4 as iAperMag4," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag4Err as iAperMag4Err," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag6 as iAperMag6," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag6Err as iAperMag6Err," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMagNoAperCorr3 as iAperMagNoAperCorr3," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMagNoAperCorr4 as iAperMagNoAperCorr4," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMagNoAperCorr6 as iAperMagNoAperCorr6," +
            " {ATLAS_VERSION}.dbo.atlassource.iHlCorSMjRadAs as iHlCorSMjRadAs," +
            " {ATLAS_VERSION}.dbo.atlassource.iGausig as iGausig," +
            " {ATLAS_VERSION}.dbo.atlassource.iEll as iEll," +
            " {ATLAS_VERSION}.dbo.atlassource.iPA as iPA," +
            " {ATLAS_VERSION}.dbo.atlassource.iErrBits as iErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.iAverageConf as iAverageConf," +
            " {ATLAS_VERSION}.dbo.atlassource.iClass as iClass," +
            " {ATLAS_VERSION}.dbo.atlassource.iClassStat as iClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.ippErrBits as ippErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.iSeqNum as iSeqNum," +
            " {ATLAS_VERSION}.dbo.atlassource.iXi as iXi," +
            " {ATLAS_VERSION}.dbo.atlassource.iEta as iEta," +
            " {ATLAS_VERSION}.dbo.atlassource.zPetroMag as zPetroMag," +
            " {ATLAS_VERSION}.dbo.atlassource.zPetroMagErr as zPetroMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.zPsfMag as zPsfMag," +
            " {ATLAS_VERSION}.dbo.atlassource.zPsfMagErr as zPsfMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.zSerMag2D as zSerMag2D," +
            " {ATLAS_VERSION}.dbo.atlassource.zSerMag2DErr as zSerMag2DErr," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag3 as zAperMag3," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag3Err as zAperMag3Err," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag4 as zAperMag4," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag4Err as zAperMag4Err," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag6 as zAperMag6," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag6Err as zAperMag6Err," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMagNoAperCorr3 as zAperMagNoAperCorr3," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMagNoAperCorr4 as zAperMagNoAperCorr4," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMagNoAperCorr6 as zAperMagNoAperCorr6," +
            " {ATLAS_VERSION}.dbo.atlassource.zHlCorSMjRadAs as zHlCorSMjRadAs," +
            " {ATLAS_VERSION}.dbo.atlassource.zGausig as zGausig," +
            " {ATLAS_VERSION}.dbo.atlassource.zEll as zEll," +
            " {ATLAS_VERSION}.dbo.atlassource.zPA as zPA," +
            " {ATLAS_VERSION}.dbo.atlassource.zErrBits as zErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.zAverageConf as zAverageConf," +
            " {ATLAS_VERSION}.dbo.atlassource.zClass as zClass," +
            " {ATLAS_VERSION}.dbo.atlassource.zClassStat as zClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.zppErrBits as zppErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.zSeqNum as zSeqNum," +
            " {ATLAS_VERSION}.dbo.atlassource.zXi as zXi," +
            " {ATLAS_VERSION}.dbo.atlassource.zEta as zEta," +
            " 2 * asin(sqrt(power(-0.997825033922517 - {ATLAS_VERSION}.dbo.atlassource.cx, 2) + power(-0.052293794140904105 - {ATLAS_VERSION}.dbo.atlassource.cy, 2) + power(-0.040131792532559725 - {ATLAS_VERSION}.dbo.atlassource.cz, 2)) / 2) * 60 as dist" + 
            " from" + 
            " {ATLAS_VERSION}.dbo.atlassource" + 
            " where" + 
            " {ATLAS_VERSION}.dbo.atlassource.dec > -2.3166666666666664 and {ATLAS_VERSION}.dbo.atlassource.dec < -2.283333333333333" + 
            " and" + 
            " {ATLAS_VERSION}.dbo.atlassource.ra >= 182.98331970017435 and {ATLAS_VERSION}.dbo.atlassource.ra <= 183.01668029982565" + 
            " and" + 
            " ({ATLAS_VERSION}.dbo.atlassource.cx * -0.997825033922517 + {ATLAS_VERSION}.dbo.atlassource.cy * -0.052293794140904105 + {ATLAS_VERSION}.dbo.atlassource.cz * -0.040131792532559725 >= 0.9999999576920253)" + 
            "",

            new ExpectedField[] {
                new ExpectedField("sourceID", AdqlColumn.AdqlType.LONG, 0),
                new ExpectedField("cuEventID", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("frameSetID", AdqlColumn.AdqlType.LONG, 0),
                new ExpectedField("ra", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("cx", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("cy", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("cz", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("htmID", AdqlColumn.AdqlType.LONG, 0),
                new ExpectedField("l", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("b", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("lambda", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("eta", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("priOrSec", AdqlColumn.AdqlType.LONG, 0),
                new ExpectedField("umgPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("umgPntErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrPntErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rmiPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rmiPntErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("imzPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("imzPntErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("umgExt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("umgExtErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrExt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrExtErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rmiExt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rmiExtErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("imzExt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("imzExtErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("mergedClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("mergedClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("pStar", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("pGalaxy", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("pNoise", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("pSaturated", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("eBV", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("aU", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("aG", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("aR", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("aI", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("aZ", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uPetroMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uPetroMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uPsfMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uPsfMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uSerMag2D", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uSerMag2DErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag3Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag4Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag6Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMagNoAperCorr3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMagNoAperCorr4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMagNoAperCorr6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uHlCorSMjRadAs", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uGausig", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uEll", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uPA", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("uAverageConf", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("uClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uppErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("uSeqNum", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("uXi", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uEta", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gPetroMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gPetroMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gPsfMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gPsfMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gSerMag2D", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gSerMag2DErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag3Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag4Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag6Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMagNoAperCorr3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMagNoAperCorr4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMagNoAperCorr6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gHlCorSMjRadAs", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gGausig", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gEll", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gPA", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("gAverageConf", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("gClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gppErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("gSeqNum", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("gXi", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gEta", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rPetroMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rPetroMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rPsfMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rPsfMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rSerMag2D", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rSerMag2DErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag3Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag4Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag6Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMagNoAperCorr3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMagNoAperCorr4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMagNoAperCorr6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rHlCorSMjRadAs", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rGausig", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rEll", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rPA", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("rAverageConf", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("rClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rppErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("rSeqNum", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("rXi", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rEta", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iPetroMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iPetroMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iPsfMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iPsfMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iSerMag2D", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iSerMag2DErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag3Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag4Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag6Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMagNoAperCorr3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMagNoAperCorr4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMagNoAperCorr6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iHlCorSMjRadAs", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iGausig", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iEll", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iPA", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("iAverageConf", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("iClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("ippErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("iSeqNum", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("iXi", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iEta", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zPetroMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zPetroMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zPsfMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zPsfMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zSerMag2D", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zSerMag2DErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag3Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag4Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag6Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMagNoAperCorr3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMagNoAperCorr4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMagNoAperCorr6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zHlCorSMjRadAs", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zGausig", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zEll", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zPA", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("zAverageConf", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("zClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zppErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("zSeqNum", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("zXi", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zEta", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("dist", AdqlColumn.AdqlType.DOUBLE, 0),
                }
            );
        }

    */
	
	
    /**
     * Works without table prefix on '*'.
     *  
  	 * Known to fail. Result columns out of order? Comparison failure: Expected [sourceID] but was [aR] 
    
    @Test
    public void test002()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT" + 
            "    *," + 
            "    2 * ASIN(SQRT(POWER(-0.997825033922517 - cx, 2) + POWER(-0.052293794140904105 - cy, 2) + POWER(-0.040131792532559725 - cz, 2)) / 2) * 60 AS dist" + 
            " FROM" + 
            "    ATLASsource" + 
            " WHERE" + 
            "    dec > -2.3166666666666664 AND dec < -2.283333333333333" + 
            " AND" + 
            "    ra >= 182.98331970017435 AND ra <= 183.01668029982565" + 
            " AND" + 
            "    (cx * -0.997825033922517 + cy * -0.052293794140904105 +cz * -0.040131792532559725 >= 0.9999999576920253)" + 
            "",

            "select" + 
            " {ATLAS_VERSION}.dbo.atlassource.sourceID as sourceID," +
            " {ATLAS_VERSION}.dbo.atlassource.cuEventID as cuEventID," +
            " {ATLAS_VERSION}.dbo.atlassource.frameSetID as frameSetID," +
            " {ATLAS_VERSION}.dbo.atlassource.ra as ra," +
            " {ATLAS_VERSION}.dbo.atlassource.dec as dec," +
            " {ATLAS_VERSION}.dbo.atlassource.cx as cx," +
            " {ATLAS_VERSION}.dbo.atlassource.cy as cy," +
            " {ATLAS_VERSION}.dbo.atlassource.cz as cz," +
            " {ATLAS_VERSION}.dbo.atlassource.htmID as htmID," +
            " {ATLAS_VERSION}.dbo.atlassource.l as l," +
            " {ATLAS_VERSION}.dbo.atlassource.b as b," +
            " {ATLAS_VERSION}.dbo.atlassource.lambda as lambda," +
            " {ATLAS_VERSION}.dbo.atlassource.eta as eta," +
            " {ATLAS_VERSION}.dbo.atlassource.priOrSec as priOrSec," +
            " {ATLAS_VERSION}.dbo.atlassource.umgPnt as umgPnt," +
            " {ATLAS_VERSION}.dbo.atlassource.umgPntErr as umgPntErr," +
            " {ATLAS_VERSION}.dbo.atlassource.gmrPnt as gmrPnt," +
            " {ATLAS_VERSION}.dbo.atlassource.gmrPntErr as gmrPntErr," +
            " {ATLAS_VERSION}.dbo.atlassource.rmiPnt as rmiPnt," +
            " {ATLAS_VERSION}.dbo.atlassource.rmiPntErr as rmiPntErr," +
            " {ATLAS_VERSION}.dbo.atlassource.imzPnt as imzPnt," +
            " {ATLAS_VERSION}.dbo.atlassource.imzPntErr as imzPntErr," +
            " {ATLAS_VERSION}.dbo.atlassource.umgExt as umgExt," +
            " {ATLAS_VERSION}.dbo.atlassource.umgExtErr as umgExtErr," +
            " {ATLAS_VERSION}.dbo.atlassource.gmrExt as gmrExt," +
            " {ATLAS_VERSION}.dbo.atlassource.gmrExtErr as gmrExtErr," +
            " {ATLAS_VERSION}.dbo.atlassource.rmiExt as rmiExt," +
            " {ATLAS_VERSION}.dbo.atlassource.rmiExtErr as rmiExtErr," +
            " {ATLAS_VERSION}.dbo.atlassource.imzExt as imzExt," +
            " {ATLAS_VERSION}.dbo.atlassource.imzExtErr as imzExtErr," +
            " {ATLAS_VERSION}.dbo.atlassource.mergedClassStat as mergedClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.mergedClass as mergedClass," +
            " {ATLAS_VERSION}.dbo.atlassource.pStar as pStar," +
            " {ATLAS_VERSION}.dbo.atlassource.pGalaxy as pGalaxy," +
            " {ATLAS_VERSION}.dbo.atlassource.pNoise as pNoise," +
            " {ATLAS_VERSION}.dbo.atlassource.pSaturated as pSaturated," +
            " {ATLAS_VERSION}.dbo.atlassource.eBV as eBV," +
            " {ATLAS_VERSION}.dbo.atlassource.aU as aU," +
            " {ATLAS_VERSION}.dbo.atlassource.aG as aG," +
            " {ATLAS_VERSION}.dbo.atlassource.aR as aR," +
            " {ATLAS_VERSION}.dbo.atlassource.aI as aI," +
            " {ATLAS_VERSION}.dbo.atlassource.aZ as aZ," +
            " {ATLAS_VERSION}.dbo.atlassource.uPetroMag as uPetroMag," +
            " {ATLAS_VERSION}.dbo.atlassource.uPetroMagErr as uPetroMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.uPsfMag as uPsfMag," +
            " {ATLAS_VERSION}.dbo.atlassource.uPsfMagErr as uPsfMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.uSerMag2D as uSerMag2D," +
            " {ATLAS_VERSION}.dbo.atlassource.uSerMag2DErr as uSerMag2DErr," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag3 as uAperMag3," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag3Err as uAperMag3Err," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag4 as uAperMag4," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag4Err as uAperMag4Err," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag6 as uAperMag6," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMag6Err as uAperMag6Err," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMagNoAperCorr3 as uAperMagNoAperCorr3," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMagNoAperCorr4 as uAperMagNoAperCorr4," +
            " {ATLAS_VERSION}.dbo.atlassource.uAperMagNoAperCorr6 as uAperMagNoAperCorr6," +
            " {ATLAS_VERSION}.dbo.atlassource.uHlCorSMjRadAs as uHlCorSMjRadAs," +
            " {ATLAS_VERSION}.dbo.atlassource.uGausig as uGausig," +
            " {ATLAS_VERSION}.dbo.atlassource.uEll as uEll," +
            " {ATLAS_VERSION}.dbo.atlassource.uPA as uPA," +
            " {ATLAS_VERSION}.dbo.atlassource.uErrBits as uErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.uAverageConf as uAverageConf," +
            " {ATLAS_VERSION}.dbo.atlassource.uClass as uClass," +
            " {ATLAS_VERSION}.dbo.atlassource.uClassStat as uClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.uppErrBits as uppErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.uSeqNum as uSeqNum," +
            " {ATLAS_VERSION}.dbo.atlassource.uXi as uXi," +
            " {ATLAS_VERSION}.dbo.atlassource.uEta as uEta," +
            " {ATLAS_VERSION}.dbo.atlassource.gPetroMag as gPetroMag," +
            " {ATLAS_VERSION}.dbo.atlassource.gPetroMagErr as gPetroMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.gPsfMag as gPsfMag," +
            " {ATLAS_VERSION}.dbo.atlassource.gPsfMagErr as gPsfMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.gSerMag2D as gSerMag2D," +
            " {ATLAS_VERSION}.dbo.atlassource.gSerMag2DErr as gSerMag2DErr," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag3 as gAperMag3," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag3Err as gAperMag3Err," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag4 as gAperMag4," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag4Err as gAperMag4Err," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag6 as gAperMag6," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMag6Err as gAperMag6Err," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMagNoAperCorr3 as gAperMagNoAperCorr3," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMagNoAperCorr4 as gAperMagNoAperCorr4," +
            " {ATLAS_VERSION}.dbo.atlassource.gAperMagNoAperCorr6 as gAperMagNoAperCorr6," +
            " {ATLAS_VERSION}.dbo.atlassource.gHlCorSMjRadAs as gHlCorSMjRadAs," +
            " {ATLAS_VERSION}.dbo.atlassource.gGausig as gGausig," +
            " {ATLAS_VERSION}.dbo.atlassource.gEll as gEll," +
            " {ATLAS_VERSION}.dbo.atlassource.gPA as gPA," +
            " {ATLAS_VERSION}.dbo.atlassource.gErrBits as gErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.gAverageConf as gAverageConf," +
            " {ATLAS_VERSION}.dbo.atlassource.gClass as gClass," +
            " {ATLAS_VERSION}.dbo.atlassource.gClassStat as gClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.gppErrBits as gppErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.gSeqNum as gSeqNum," +
            " {ATLAS_VERSION}.dbo.atlassource.gXi as gXi," +
            " {ATLAS_VERSION}.dbo.atlassource.gEta as gEta," +
            " {ATLAS_VERSION}.dbo.atlassource.rPetroMag as rPetroMag," +
            " {ATLAS_VERSION}.dbo.atlassource.rPetroMagErr as rPetroMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.rPsfMag as rPsfMag," +
            " {ATLAS_VERSION}.dbo.atlassource.rPsfMagErr as rPsfMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.rSerMag2D as rSerMag2D," +
            " {ATLAS_VERSION}.dbo.atlassource.rSerMag2DErr as rSerMag2DErr," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag3 as rAperMag3," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag3Err as rAperMag3Err," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag4 as rAperMag4," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag4Err as rAperMag4Err," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag6 as rAperMag6," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMag6Err as rAperMag6Err," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMagNoAperCorr3 as rAperMagNoAperCorr3," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMagNoAperCorr4 as rAperMagNoAperCorr4," +
            " {ATLAS_VERSION}.dbo.atlassource.rAperMagNoAperCorr6 as rAperMagNoAperCorr6," +
            " {ATLAS_VERSION}.dbo.atlassource.rHlCorSMjRadAs as rHlCorSMjRadAs," +
            " {ATLAS_VERSION}.dbo.atlassource.rGausig as rGausig," +
            " {ATLAS_VERSION}.dbo.atlassource.rEll as rEll," +
            " {ATLAS_VERSION}.dbo.atlassource.rPA as rPA," +
            " {ATLAS_VERSION}.dbo.atlassource.rErrBits as rErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.rAverageConf as rAverageConf," +
            " {ATLAS_VERSION}.dbo.atlassource.rClass as rClass," +
            " {ATLAS_VERSION}.dbo.atlassource.rClassStat as rClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.rppErrBits as rppErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.rSeqNum as rSeqNum," +
            " {ATLAS_VERSION}.dbo.atlassource.rXi as rXi," +
            " {ATLAS_VERSION}.dbo.atlassource.rEta as rEta," +
            " {ATLAS_VERSION}.dbo.atlassource.iPetroMag as iPetroMag," +
            " {ATLAS_VERSION}.dbo.atlassource.iPetroMagErr as iPetroMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.iPsfMag as iPsfMag," +
            " {ATLAS_VERSION}.dbo.atlassource.iPsfMagErr as iPsfMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.iSerMag2D as iSerMag2D," +
            " {ATLAS_VERSION}.dbo.atlassource.iSerMag2DErr as iSerMag2DErr," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag3 as iAperMag3," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag3Err as iAperMag3Err," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag4 as iAperMag4," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag4Err as iAperMag4Err," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag6 as iAperMag6," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMag6Err as iAperMag6Err," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMagNoAperCorr3 as iAperMagNoAperCorr3," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMagNoAperCorr4 as iAperMagNoAperCorr4," +
            " {ATLAS_VERSION}.dbo.atlassource.iAperMagNoAperCorr6 as iAperMagNoAperCorr6," +
            " {ATLAS_VERSION}.dbo.atlassource.iHlCorSMjRadAs as iHlCorSMjRadAs," +
            " {ATLAS_VERSION}.dbo.atlassource.iGausig as iGausig," +
            " {ATLAS_VERSION}.dbo.atlassource.iEll as iEll," +
            " {ATLAS_VERSION}.dbo.atlassource.iPA as iPA," +
            " {ATLAS_VERSION}.dbo.atlassource.iErrBits as iErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.iAverageConf as iAverageConf," +
            " {ATLAS_VERSION}.dbo.atlassource.iClass as iClass," +
            " {ATLAS_VERSION}.dbo.atlassource.iClassStat as iClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.ippErrBits as ippErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.iSeqNum as iSeqNum," +
            " {ATLAS_VERSION}.dbo.atlassource.iXi as iXi," +
            " {ATLAS_VERSION}.dbo.atlassource.iEta as iEta," +
            " {ATLAS_VERSION}.dbo.atlassource.zPetroMag as zPetroMag," +
            " {ATLAS_VERSION}.dbo.atlassource.zPetroMagErr as zPetroMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.zPsfMag as zPsfMag," +
            " {ATLAS_VERSION}.dbo.atlassource.zPsfMagErr as zPsfMagErr," +
            " {ATLAS_VERSION}.dbo.atlassource.zSerMag2D as zSerMag2D," +
            " {ATLAS_VERSION}.dbo.atlassource.zSerMag2DErr as zSerMag2DErr," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag3 as zAperMag3," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag3Err as zAperMag3Err," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag4 as zAperMag4," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag4Err as zAperMag4Err," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag6 as zAperMag6," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMag6Err as zAperMag6Err," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMagNoAperCorr3 as zAperMagNoAperCorr3," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMagNoAperCorr4 as zAperMagNoAperCorr4," +
            " {ATLAS_VERSION}.dbo.atlassource.zAperMagNoAperCorr6 as zAperMagNoAperCorr6," +
            " {ATLAS_VERSION}.dbo.atlassource.zHlCorSMjRadAs as zHlCorSMjRadAs," +
            " {ATLAS_VERSION}.dbo.atlassource.zGausig as zGausig," +
            " {ATLAS_VERSION}.dbo.atlassource.zEll as zEll," +
            " {ATLAS_VERSION}.dbo.atlassource.zPA as zPA," +
            " {ATLAS_VERSION}.dbo.atlassource.zErrBits as zErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.zAverageConf as zAverageConf," +
            " {ATLAS_VERSION}.dbo.atlassource.zClass as zClass," +
            " {ATLAS_VERSION}.dbo.atlassource.zClassStat as zClassStat," +
            " {ATLAS_VERSION}.dbo.atlassource.zppErrBits as zppErrBits," +
            " {ATLAS_VERSION}.dbo.atlassource.zSeqNum as zSeqNum," +
            " {ATLAS_VERSION}.dbo.atlassource.zXi as zXi," +
            " {ATLAS_VERSION}.dbo.atlassource.zEta as zEta," +
            " 2 * asin(sqrt(power(-0.997825033922517 - {ATLAS_VERSION}.dbo.atlassource.cx, 2) + power(-0.052293794140904105 - {ATLAS_VERSION}.dbo.atlassource.cy, 2) + power(-0.040131792532559725 - {ATLAS_VERSION}.dbo.atlassource.cz, 2)) / 2) * 60 as dist" + 
            " from" + 
            " {ATLAS_VERSION}.dbo.atlassource" + 
            " where" + 
            " {ATLAS_VERSION}.dbo.atlassource.dec > -2.3166666666666664 and {ATLAS_VERSION}.dbo.atlassource.dec < -2.283333333333333" + 
            " and" + 
            " {ATLAS_VERSION}.dbo.atlassource.ra >= 182.98331970017435 and {ATLAS_VERSION}.dbo.atlassource.ra <= 183.01668029982565" + 
            " and" + 
            " ({ATLAS_VERSION}.dbo.atlassource.cx * -0.997825033922517 + {ATLAS_VERSION}.dbo.atlassource.cy * -0.052293794140904105 + {ATLAS_VERSION}.dbo.atlassource.cz * -0.040131792532559725 >= 0.9999999576920253)" + 
            "",

            new ExpectedField[] {
                new ExpectedField("sourceID", AdqlColumn.AdqlType.LONG, 0),
                new ExpectedField("cuEventID", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("frameSetID", AdqlColumn.AdqlType.LONG, 0),
                new ExpectedField("ra", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("cx", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("cy", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("cz", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("htmID", AdqlColumn.AdqlType.LONG, 0),
                new ExpectedField("l", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("b", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("lambda", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("eta", AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("priOrSec", AdqlColumn.AdqlType.LONG, 0),
                new ExpectedField("umgPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("umgPntErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrPntErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rmiPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rmiPntErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("imzPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("imzPntErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("umgExt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("umgExtErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrExt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrExtErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rmiExt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rmiExtErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("imzExt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("imzExtErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("mergedClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("mergedClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("pStar", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("pGalaxy", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("pNoise", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("pSaturated", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("eBV", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("aU", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("aG", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("aR", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("aI", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("aZ", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uPetroMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uPetroMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uPsfMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uPsfMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uSerMag2D", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uSerMag2DErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag3Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag4Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMag6Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMagNoAperCorr3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMagNoAperCorr4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uAperMagNoAperCorr6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uHlCorSMjRadAs", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uGausig", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uEll", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uPA", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("uAverageConf", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("uClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uppErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("uSeqNum", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("uXi", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("uEta", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gPetroMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gPetroMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gPsfMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gPsfMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gSerMag2D", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gSerMag2DErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag3Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag4Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMag6Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMagNoAperCorr3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMagNoAperCorr4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gAperMagNoAperCorr6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gHlCorSMjRadAs", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gGausig", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gEll", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gPA", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("gAverageConf", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("gClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gppErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("gSeqNum", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("gXi", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gEta", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rPetroMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rPetroMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rPsfMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rPsfMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rSerMag2D", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rSerMag2DErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag3Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag4Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMag6Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMagNoAperCorr3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMagNoAperCorr4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rAperMagNoAperCorr6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rHlCorSMjRadAs", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rGausig", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rEll", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rPA", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("rAverageConf", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("rClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rppErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("rSeqNum", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("rXi", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rEta", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iPetroMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iPetroMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iPsfMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iPsfMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iSerMag2D", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iSerMag2DErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag3Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag4Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMag6Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMagNoAperCorr3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMagNoAperCorr4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iAperMagNoAperCorr6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iHlCorSMjRadAs", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iGausig", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iEll", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iPA", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("iAverageConf", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("iClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("ippErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("iSeqNum", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("iXi", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iEta", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zPetroMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zPetroMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zPsfMag", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zPsfMagErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zSerMag2D", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zSerMag2DErr", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag3Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag4Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMag6Err", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMagNoAperCorr3", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMagNoAperCorr4", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zAperMagNoAperCorr6", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zHlCorSMjRadAs", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zGausig", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zEll", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zPA", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("zAverageConf", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zClass", AdqlColumn.AdqlType.SHORT, 0),
                new ExpectedField("zClassStat", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zppErrBits", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("zSeqNum", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("zXi", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zEta", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("dist", AdqlColumn.AdqlType.DOUBLE, 0),
                }
            );
        }
    */    
    
    /**
     * Simpler example.
     * Known to fail.
     * http://redmine.roe.ac.uk/issues/465
     *  
     */
    @Test
    public void test003()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT" + 
            "    ATLASSourceXtwomass_xsc.*," + 
            "    distanceMins / 2 AS half" + 
            " FROM" + 
            "    ATLASSourceXtwomass_xsc" + 
            " WHERE" + 
            "    (masterObjID & 0xFF) = 0xAA" + 
            "",

            "SELECT" + 
            " atlasSourceXtwomass_xsc.*,"+ 
            " {ATLAS_VERSION}.dbo.atlasSourceXtwomass_xsc.distanceMins / 2 AS half" + 
            " FROM" + 
            " {ATLAS_VERSION}.dbo.atlasSourceXtwomass_xsc" + 
            " WHERE" + 
            " ({ATLAS_VERSION}.dbo.atlasSourceXtwomass_xsc.masterObjID & 0xFF) = 0xAA" + 
            "",

            new ExpectedField[] {
                new ExpectedField("distanceMins", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("masterObjID",  AdqlColumn.AdqlType.LONG,  0),
                new ExpectedField("slaveObjID",   AdqlColumn.AdqlType.LONG,  0),
                new ExpectedField("half",         AdqlColumn.AdqlType.FLOAT, 0),
                }
            );
        }

    /**
     * Simpler example
     *  
     */
    @Test
    public void test004()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT" + 
            "    *," + 
            "    distanceMins / 2 AS half" + 
            " FROM" + 
            "    ATLASSourceXtwomass_xsc" + 
            " WHERE" + 
            "    (masterObjID & 0xFF) = 0xAA" + 
            "",

            "SELECT" + 
           " {ATLAS_VERSION}.dbo.atlasSourceXtwomass_xsc.distanceMins as distanceMins," + 
            " {ATLAS_VERSION}.dbo.atlasSourceXtwomass_xsc.masterObjID as masterObjID," + 
            " {ATLAS_VERSION}.dbo.atlasSourceXtwomass_xsc.slaveObjID as slaveObjID," + 
            " {ATLAS_VERSION}.dbo.atlasSourceXtwomass_xsc.distanceMins / 2 AS half" + 
            " FROM" + 
            " {ATLAS_VERSION}.dbo.atlasSourceXtwomass_xsc" + 
            " WHERE" + 
            " ({ATLAS_VERSION}.dbo.atlasSourceXtwomass_xsc.masterObjID & 0xFF) = 0xAA" + 
            "",

            new ExpectedField[] {
            	new ExpectedField("distanceMins", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("masterObjID",  AdqlColumn.AdqlType.LONG,  0),
                new ExpectedField("slaveObjID",   AdqlColumn.AdqlType.LONG,  0),
            	new ExpectedField("half",         AdqlColumn.AdqlType.FLOAT, 0),

                }
            );
        }
    }
