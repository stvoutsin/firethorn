// Copyright (c) The University of Edinburgh, 2010.
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

package uk.org.ogsadai.dqp.lqp.udf.scalar;

import java.math.BigDecimal;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeConversionException;
import uk.org.ogsadai.tuple.TypeConverter;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Scalar function to calculate cosine.
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class Cos extends LogicalExecutableFunctionBase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2011.";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(Mod.class);

    /** The function's name. */
    private static final String FUNCTION_NAME = "COS";
    
    /** The data type of the input parameter. */
    private int mType = -1;

    /** The data type of the evaluation result. */
    private int mResultType = -1;
    
    /** The evaluation result. */
    private Object mResult;
    
    /**
     * Constructor.
     */
    public Cos() {
        super(1);
    }
    
    /**
     * Constructor. The constructor object copies the state of the object 
     * passed to it, i.e. the data type of the inputs and output.
     * 
     * @param mod
     */
    public Cos(Cos cos) {
        this();
        mType = cos.getParameterType();
        mResultType = cos.getOutputType();
    }
    
    /**
     * {@inheritDoc}
     */
    public String getName() {
        return FUNCTION_NAME;
    }

    /**
     * {@inheritDoc}
     */
    public FunctionType getType() {
        return FunctionType.UDF_SCALAR;
    }
    
    /**
     * Configures the function with the given data types.
     */
    public void configure(int... types) throws TypeMismatchException {
        switch(types[0]) {
        case TupleTypes._SHORT:
        case TupleTypes._LONG:
        case TupleTypes._INT:
        case TupleTypes._DOUBLE:
        case TupleTypes._FLOAT:
        case TupleTypes._BIGDECIMAL:
            mResultType = TupleTypes._DOUBLE;
            break;
        default:  
            throw new TypeMismatchException(types[0]);
        }
        mType = types[0];
    }
    
    /**
     * Returns the parameter type.
     * 
     * @return type.
     */
    protected int getParameterType() {
        return mType;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getOutputType() {
        return mResultType;
    }

    /**
     * {@inheritDoc}
     */
    public Object getResult() {
        return mResult;
    }
    
    /**
     * Puts the input parameter X and computes cos(X).
     * 
     */
    public void put(Object... parameters) {
        if (parameters[0] == Null.VALUE)
        {
            mResult = Null.VALUE;
        }
        else
        {
            Object x = null;
            try 
            {
                x = TypeConverter.convertObject(mType, mResultType, parameters[0]);
            } 
            catch (TypeConversionException e) 
            {
                LOG.error(e, true);
                throw new RuntimeException(e);
            }
            switch(mResultType) {
                case TupleTypes._DOUBLE:
                    mResult = Math.cos((Double)x);
                    break;
                case TupleTypes._BIGDECIMAL:
                    BigDecimal xb = (BigDecimal)x;
                    mResult = Math.cos(xb.doubleValue());
                    break;
            }
        }
    }
}
