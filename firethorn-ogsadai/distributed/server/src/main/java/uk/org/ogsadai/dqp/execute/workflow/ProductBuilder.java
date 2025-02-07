// Copyright (c) The University of Edinburgh, 2008.
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


package uk.org.ogsadai.dqp.execute.workflow;

import java.util.List;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;

/**
 * Builds activities for operator PRODUCT.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ProductBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2012";
    
    /** 
     * ID of target resource or <code>null</code> if the activity isn't 
     * targeted at a resource. 
     */
    private String mResourceID;
    
    @Override
    public SingleActivityOutput build(
            Operator op, 
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException
    {
        SingleActivityOutput outputLeft = outputs.get(0);
        SingleActivityOutput outputRight = outputs.get(1);
        
        GenericActivity product = 
            new GenericActivity("uk.org.ogsadai.TupleProduct");
        if (mResourceID != null) product.setResourceID(mResourceID);
        product.createInput("data1");
        product.createInput("data2");
        product.createOutput("result", GenericActivity.LIMITED_VALIDATION);
        
        product.connectInput("data1", outputLeft);
        product.connectInput("data2", outputRight);
        builder.add(product);
        
        return product.getOutput("result");
    }
    
    /**
     * Sets the resource ID of the data resource for storing temporary tables.
     * 
     * @param resourceID
     *            data resource ID
     */
    public void setResourceID(String resourceID)
    {
        mResourceID = resourceID;
    }

}
