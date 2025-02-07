/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.spring;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.identity.AuthMethod;
import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.chaos.MonkeyParam;

/**
 * Factory for accessing the current {@link Operation}, {@link AuthMethod} and {@link Identity}.  
 *
 */
@Component
public class ContextFactoryImpl
extends AbstractComponent
implements Context.Factory
    {
    
    @Override
    public Context current()
        {
        return new Context()
            {
            @Override
            public Operation oper()
                {
                return factories().operations().entities().current();
                }

            @Override
            public Authentication auth()
                {
                Operation oper = this.oper();
                if (oper != null)
                    {
                    return oper.authentications().primary();
                    }
                return null ;
                }

            @Override
            public Identity identity()
                {
                Authentication auth = this.auth();
                if (auth != null)
                    {
                    return auth.identity();
                    }
                return null ;
                }

            @Override
            public MonkeyParam monkey()
                {
                return oper().monkey();
                }
            };
        }
    }
