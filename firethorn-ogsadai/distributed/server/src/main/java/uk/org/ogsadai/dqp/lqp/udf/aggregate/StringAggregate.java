// Copyright (c) The University of Edinburgh, 2011.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

package uk.org.ogsadai.dqp.lqp.udf.aggregate;

/**
 * Aggregation function STRING_AGGREGATE. The function produces a 
 * comma-separated list of the input values.
 *
 * @author The OGSA-DAI Project Team.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;
import uk.org.ogsadai.tuple.serialise.SerialisableFunction;

/**
 * Aggregation function StringAggregate.
 *
 * @author The OGSA-DAI Project Team.
 */
public class StringAggregate extends SQLAggregateFunction
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011.";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(StringAggregate.class);

    /** The current aggregate string. */
    private StringBuilder mAggregate;
    
    /**
     * Default constructor.
     */
    public StringAggregate()
    {
    }

    /**
     * Copy constructor which initialises STRING_AGGREGATE but does not 
     * copy the value.
     * 
     * @param stringAggregate
     *            function to copy
     */
    public StringAggregate(StringAggregate stringAggregate)
    {
    }
    
    // --------------------------
    // ExecutableFunction methods 
    // --------------------------
    
    /**
     * {@inheritDoc}
     */
    public void configure(int... parameterTypes)
        throws TypeMismatchException
    {
        // accepts any input types
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
    
        Object parameter = parameters[0];
        LOG.debug("ZRQ put class [" + parameter.getClass().getName() + "]");
        LOG.debug("ZRQ put value [" + parameter.toString() + "]");

        if (parameter != Null.VALUE)
        {
            if (mAggregate == null)
                {
                mAggregate = new StringBuilder();
                }
            else
                {
                mAggregate.append(",");
                }
// ZRQ -- Temporary fix.
// ZRQ -- Need to check this doesn't interfere with other types.            
// ZRQ -- WHY DO WE NEED THE TRIM !?
            if (parameter instanceof String)
                {
                mAggregate.append('\'');
                mAggregate.append(parameter.toString().trim());
                mAggregate.append('\'');
                }
            else {
                mAggregate.append(parameter.toString());
                }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if (mAggregate != null)
        {
            return mAggregate.toString();
        }
        else
        {
            return Null.VALUE;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType()
    {
        return TupleTypes._STRING;
    }

    // ----------------------------
    // SerialisableFunction methods 
    // ----------------------------
    
    /**
     * {@inheritDoc}
     */
    public void merge(SerialisableFunction function)
    {
        StringBuilder aggregate = ((StringAggregate)function).mAggregate;
        LOG.debug("ZRQ merge [" + aggregate + "]");

        
        if (aggregate != null)
        {
            if (mAggregate != null)
            {
                mAggregate.append(",");
                mAggregate.append(aggregate);
            }
            else
            {
                mAggregate = aggregate;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public SerialisableFunction deserialise(DataInputStream input) throws IOException
    {
    LOG.debug("deserialise [...]");
    
        StringAggregate stringAggregate = new StringAggregate(this);
        // boolean indicating whether the value is null
        if (!input.readBoolean())
        {
            stringAggregate.mAggregate = new StringBuilder();
//ZRQ
            String string = input.readUTF();
            LOG.debug("ZRQ append [" + string + "]");
            stringAggregate.mAggregate.append(string);
        }
        return stringAggregate;
    }

    /**
     * {@inheritDoc}
     */
    public void serialise(DataOutputStream output) throws IOException
    {
    LOG.debug("serialise [...]");
        output.writeBoolean(mAggregate == null);
        if (mAggregate != null)
        {
            output.writeUTF(mAggregate.toString());
        }
    }

    // -----------------------
    // LogicalFunction methods 
    // -----------------------

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "STRING_AGGREGATE";
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "STRING_AGGREGATE(value=" + mAggregate + ")";
    }

}
