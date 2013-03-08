/**
 * 
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLBulkLoadTuple;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.presentation.jersey.JerseyServer;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;

/**
 *
 *
 */
@Slf4j
public class StoredResultPipeline
    {
    public static final String DRER_IDENT = "DataRequestExecutionResource" ; 
    
    private URL endpoint ;

    private static final boolean flag = true ;
    
    public StoredResultPipeline(final URL endpoint)
        {
        this.endpoint = endpoint ;
        }
    
    public PipelineResult execute(final String source, final String store, final String table, final String query)
        {
        //
        // Create our ogsadai client.
        Server server = new JerseyServer();        
        server.setDefaultBaseServicesURL(
            this.endpoint
            );
        //
        // Create our DRER.
        DataRequestExecutionResource drer = server.getDataRequestExecutionResource(
            new ResourceID(
                DRER_IDENT
                )
            );
        //
        // Create our SQL query.
        SQLQuery sqlquery = new SQLQuery();
        sqlquery.setResourceID(
            source
            );
        sqlquery.addExpression(
            query
            );
        //
        // Create our table.
        CreateTable creater = new CreateTable();
        if (flag)
            {
            creater.setResourceID(
                new ResourceID(
                    store
                    )
                );
            creater.setTableName(
                table
                );
            creater.connectTuples(
                sqlquery.getDataOutput()
                );
            }
        //
        // Create our results writer.
        SQLBulkLoadTuple writer = new SQLBulkLoadTuple();
        writer.setResourceID(
            new ResourceID(
                store
                )
            );
        writer.addTableName(
            table
            );

        if (flag)
            {
            writer.connectDataInput(
                creater.getDataOutput()
                );
            }
        else {
            writer.connectDataInput(
                sqlquery.getDataOutput()
                );
            }
        //
        // Create our delivery handler.
        DeliverToRequestStatus delivery = new DeliverToRequestStatus();
        delivery.connectInput(
            writer.getDataOutput()
            );
        //
        // Create our pipeline.
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(
            sqlquery
            );
        if (flag)
            {
            pipeline.add(
                creater
                );
            }
        pipeline.add(
            writer
            );
        pipeline.add(
            delivery
            );
        //
        // Execute our pipeline.
        try {
            RequestResource request = drer.execute(
                pipeline,
                RequestExecutionType.SYNCHRONOUS
                );
            result(
                request.getRequestExecutionStatus()
                );
            }
        catch (Exception ouch)
            {
            log.debug("Exception during request processing [{}]", ouch);
            result(
                ouch
                );
            }
        return this.result;
        }

    private PipelineResult result ;
    public PipelineResult result()
        {
        return this.result;
        }

    private void result(final RequestExecutionStatus status)
        {
        if (RequestExecutionStatus.COMPLETED.equals(status))
            {
            this.result = new PipelineResultImpl(
                PipelineResult.Result.COMPLETED
                );
            }
        else if (RequestExecutionStatus.COMPLETED_WITH_ERROR.equals(status))
            {
            this.result = new PipelineResultImpl(
                PipelineResult.Result.FAILED
                );
            }
        else if (RequestExecutionStatus.TERMINATED.equals(status))
            {
            this.result = new PipelineResultImpl(
                PipelineResult.Result.CANCELLED
                );
            }
        else if (RequestExecutionStatus.ERROR.equals(status))
            {
            this.result = new PipelineResultImpl(
                PipelineResult.Result.FAILED
                );
            }
        else {
            this.result = new PipelineResultImpl(
                PipelineResult.Result.FAILED,
                "Unknown RequestExecutionStatus [" + status + "]"
                );
            }
        }

    private void result(final Throwable ouch)
        {
        this.result = new PipelineResultImpl(
            ouch
            );
        }

    private static class PipelineResultImpl
    implements PipelineResult
        {
        
        protected PipelineResultImpl(final Result result)
            {
            this(
                result,
                null,
                null
                );
            }

        protected PipelineResultImpl(final Result result, final String message)
            {
            this(
                result,
                message,
                null
                );
            }

        protected PipelineResultImpl(final Throwable cause)
            {
            this(
                Result.FAILED,
                cause.getMessage(),
                cause
                );
            }

        protected PipelineResultImpl(final Result result, final String message, final Throwable cause)
            {
            this.cause   = cause   ;
            this.result  = result  ;
            this.message = message ;
            }

        private String message;
        @Override
        public String message()
            {
            return this.message;
            }

        private Throwable cause;
        @Override
        public Throwable cause()
            {
            return this.cause;
            }

        private Result result;
        @Override
        public Result result()
            {
            return this.result;
            }
        }
    }
