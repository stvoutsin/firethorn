/*
 *
 */
package uk.ac.roe.wfau.firethorn.widgeon ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional; 

import uk.ac.roe.wfau.firethorn.common.entity.Womble;
import uk.ac.roe.wfau.firethorn.common.entity.Womble.HibernateStuff;

import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 */
public class WidgeonTestCase
extends TestBase
    {
    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        WidgeonTestCase.class
        );

    @Before
    public void before()
        {
        logger.debug("before()");
        super.before();
        }

    @After
    public void after()
        {
        logger.debug("after()");
        super.after();
        }

    @Test
    @Transactional(
        readOnly=false,
        propagation=Propagation.REQUIRES_NEW
        )
    public void something()
        {
        logger.debug("something()");

        assertNotNull(
            womble()
            );

        assertNotNull(
            womble().hibernate()
            );

        assertEquals(
            Womble.HibernateStuff.StateFullNess.STATE_FULL,
            womble().hibernate().stateness()
            );

        assertNotNull(
            womble().hibernate().statefull()
            );

        assertNotNull(
            womble().hibernate().statefull().session()
            );

logger.debug("Session [{}]", womble().hibernate().statefull().session());
logger.debug("Transaction [{}]", womble().hibernate().statefull().session().getTransaction());

        Widgeon one = womble().widgeons().create(
            "albert",
            URI.create("ivo://org.astrogrid.test/0001")
            );

        assertNotNull(
            one
            );
        assertNotNull(
            one.name()
            );

        logger.debug("One [{}][{}]", one.ident(), one.name());

        Widgeon two = womble().widgeons().create(
            "albert",
            URI.create("ivo://org.astrogrid.test/0001")
            );
        assertNotNull(
            two
            );
        assertNotNull(
            two.name()
            );

        logger.debug("Two [{}][{}]", two.ident(), two.name());

logger.debug("Session [{}]", womble().hibernate().statefull().session());
logger.debug("Transaction [{}]", womble().hibernate().statefull().session().getTransaction());

logger.debug("--");
logger.debug("Initiating session().flush()");
        womble().hibernate().statefull().session().flush();
logger.debug("--");

logger.debug("Session [{}]", womble().hibernate().statefull().session());
logger.debug("Transaction [{}]", womble().hibernate().statefull().session().getTransaction());

        logger.debug("One [{}][{}]", one.ident(), one.name());
        logger.debug("Two [{}][{}]", two.ident(), two.name());

        }
    }

