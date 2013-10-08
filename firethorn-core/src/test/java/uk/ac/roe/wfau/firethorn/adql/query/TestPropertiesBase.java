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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
@Slf4j
public class TestPropertiesBase
    extends TestBase
    {

    private static final String PROPERTIES_FILE = "target/junit-test.properties" ;

    protected Properties testprops = new Properties();
    protected Properties testprops()
        {
        return testprops;
        }

    @Before
    public void loadProperties()
        {
        log.debug("Loading test properties");
        try {
            testprops.load(
                new FileReader(
                    PROPERTIES_FILE
                    )
                );
            }
        catch (FileNotFoundException ouch)
            {
            //log.debug("FileNotFoundException loading test properties [{}]", PROPERTIES_FILE);
            }
        catch (IOException ouch)
            {
            log.error("IOException loading test properties [{}][{}]",PROPERTIES_FILE , ouch.getMessage());
            }
        }
    
    @After
    public void saveProperties()
        {
        log.debug("Saving test properties");
        try {
            testprops.store(
                new FileWriter(
                    PROPERTIES_FILE
                    ),
                "Auto save after test"
                );
            }
        catch (IOException ouch)
            {
            log.error("IOException saving test properties [{}][{}]",PROPERTIES_FILE , ouch.getMessage());
            }
        }
    
    /**
     * Test our tests
     * 
    @Test
    public void test000()
    throws Exception
        {
        log.debug("Properties [{}]", testprops);
        testprops().setProperty("date", new DateTime().toString());
        }
     */
    }
