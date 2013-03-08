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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.activities.transform.TupleToByteArrays;
import uk.org.ogsadai.client.toolkit.presentation.jersey.JerseyServer;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestStatus;

/**
 * Simple test for OGSA-DAI queries.
 *
 *
 */
@Slf4j
public class SimpleQueryTestBase
    {

    /**
     * Simple test for OGSA-DAI queries.
     *
     */
    public void execute(String endpoint, String dataset, String query)
    throws Exception
        {
        //
        // Create our server client.
        SimpleClient client = new SimpleClient(
            endpoint
            );
        //
        // Get ResultSet.
        ResultSet results = client.execute(
            dataset,
            query
            );
        if (results != null)
            {
            // Get the result set metadata.
            ResultSetMetaData md = results.getMetaData();
            // Get column names and initial column widths.
            int numColumns = md.getColumnCount();
            log.info("");
            // Get ResultSet rows.
            while (results.next()) 
                {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < numColumns; i++)
                    {
                    Object field = results.getObject(i + 1);
                    if (field == null)
                        {
                        builder.append(
                            pad(
                                "null"
                                )
                            );
                        }
                    else {
                        builder.append(
                            pad(
                                field.toString()
                                )
                            );
                        }

                    builder.append(" ");
                    }
                log.info(
                    builder.toString()
                    );
                }
            results.close();
            }
        }

    /**
     * Pad a string out to a given width with space characters.
     *
     */
    public static String pad(String base)
        {
        return pad(
            base,
            22
            );
        }

    /**
     * Pad a string out to a given width with space characters.
     *
     */
    public static String pad(String base, int width)
        {
        int count = width - base.length();
        if (count == 0)
            {
            return base ;
            }
        else if (count < 0)
            {
            return base.substring(
                0,
                width
                );
            }
        else {
            StringBuffer buffer = new StringBuffer(
                base
                );
            for (int i = 0; i < count; i++)
                {
                buffer.append(" ");
                }
            return buffer.toString();
            }
        }
    }

