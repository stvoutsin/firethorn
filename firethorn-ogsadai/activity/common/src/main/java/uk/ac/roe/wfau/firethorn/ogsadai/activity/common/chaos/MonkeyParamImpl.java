/*
 *  Copyright (C) 2018 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.ogsadai.activity.common.chaos;

/**
 * 
 * 
 */
public class MonkeyParamImpl
extends MonkeyParamBase
implements MonkeyParam
    {
    /**
     * Public constructor.
     * 
     */
    public MonkeyParamImpl()
        {
        super();
        }

    /**
     * Public constructor.
     * 
     */
    public MonkeyParamImpl(final Object name, final Object data)
        {
        super(
            name,
            data
            );
        }
    
    private String name ;
    @Override
    public String name()
        {
        return this.name;
        }
    @Override
    public void name(final Object name)
        {
        if (name != null)
            {
            this.name = name.toString();
            }
        else {
            this.name = null ;
            }
        }
    
    private Object data ;
    @Override
    public Object data()
        {
        return this.data;
        }
    @Override
    public void data(final Object data)
        {
        this.data = data ;
        }

    }
