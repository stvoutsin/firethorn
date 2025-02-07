/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;

/**
 * Test for JdbcCreateResource activity.
 *
 *
 */
@Slf4j
public class JdbcCreateResourceTestCase
extends JdbcResourceTestBase
    {

    @Test
    public void test000()
    throws Exception
        {
        final JdbcCreateResourceWorkflow workflow = new JdbcCreateResourceWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final ResourceWorkflowResult created = workflow.execute(
            config().jdbc().databases().get("atlas")
            );

        log.debug("Status  [{}]", created.status());
        log.debug("Request [{}]", created.request());
        log.debug("Created [{}]", created.result());

        assertNotNull(
            created
            );
        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            created.status()
            );
        assertNotNull(
            created.request()
            );
        assertNotNull(
            created.result()
            );
        }
    }
