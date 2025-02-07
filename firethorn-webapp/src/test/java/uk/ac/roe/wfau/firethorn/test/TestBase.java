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
package uk.ac.roe.wfau.firethorn.test ;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 * Base class for tests.
 * The test is run using SpringJUnit4ClassRunner in order to support the @Autowired annotation.
 *
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    locations = {
        "classpath:testdata-config.xml",
        "classpath:hibernate-config.xml",
        "classpath:scheduler-config.xml",
        "classpath:component-config.xml"
        }
    )
@Rollback(
    false
    )    
// https://jira.spring.io/browse/SPR-13472
@Transactional(
    readOnly=false,
    propagation=Propagation.REQUIRES_NEW,
    transactionManager="FireThornTransactionManager"
    )
public abstract class TestBase
    {

    /**
     * Our component Factories.
     *
     */
    @Autowired
    private ComponentFactories factories;

    /**
     * Our component Factories.
     *
     */
    public ComponentFactories factories()
        {
        return this.factories;
        }

    @Before
    public void before()
    throws Exception
        {
        log.debug("before()");
        }

    @After
    public void after()
    throws Exception
        {
        log.debug("after()");
        }

    public void flush()
        {
        log.debug("flush()");
        factories().hibernate().flush();
        }

    /**
     * The test class load time.
     *
     */
    protected static long start = System.currentTimeMillis() ;

    /**
     * A shared counter for unique names.
     *
     */
    protected static long uniques = 0 ;

    /**
     * Glue for generated names.
     *
     */
    public static final String UNIQUE_NAME_GLUE = "." ;

    /**
     * Generate a unique string.
     *
     */
    public String unique()
        {
        final StringBuilder builder = new StringBuilder();
        builder.append(
            String.valueOf(
                start
                )
            );
        builder.append(
            UNIQUE_NAME_GLUE
            );
        builder.append(
            String.valueOf(
                uniques++
                )
            );
        return builder.toString();
        }

    /**
     * Generate a unique string.
     *
     */
    public String unique(final String prefix)
        {
        final StringBuilder builder = new StringBuilder();
        builder.append(
            prefix
            );
        builder.append(
            UNIQUE_NAME_GLUE
            );
        builder.append(
            unique()
            );
        return builder.toString();
        }

    /**
     * Generate a unique URI.
     *
     */
    public URI unique(final URI base)
        {
        return base.resolve(
            URI.create(
                unique()
                )
            );
        }

    /**
     * Count the members in a Iterable set.
     *
     */
    public long count(final Iterable<?> iterable)
        {
        long count = 0 ;
        for (final Object object : iterable)
            {
            count++ ;
            }
        return count ;
        }

    /**
     * Empty test to prevent Eclipse from throwing an initializationError when it runs this as a test.
     * @throws Exception
     *
     */
    @Test
    public void empty()
    throws Exception
        {
        }
    }

