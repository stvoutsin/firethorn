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
package uk.ac.roe.wfau.firethorn.meta.ogsa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
@Component
public class OgsaFactoriesImpl
implements OgsaFactories
    {
    @Autowired
    private OgsaTableResolver tables;
    @Override
    public OgsaTableResolver tables()
        {
        return this.tables;
        }

    @Autowired
    private OgsaColumnResolver columns;
    @Override
    public OgsaColumnResolver columns()
        {
        return this.columns;
        }

    @Autowired
    private OgsaService.EntityServices services;
    @Override
    public OgsaService.EntityServices services()
        {
        return services;
        }

    @Autowired
    private OgsaJdbcResource.EntityServices jdbc;
    @Override
    public OgsaJdbcResource.EntityServices jdbc()
        {
        return jdbc;
        }

    @Autowired
    private OgsaIvoaResource.EntityServices ivoa;
    @Override
    public OgsaIvoaResource.EntityServices ivoa()
        {
        return ivoa;
        }

    @Autowired
    private OgsaExecResource.EntityServices exec;
    @Override
    public OgsaExecResource.EntityServices exec()
        {
        return exec;
        }

    @Autowired
    private OgsaDQPResource.EntityServices dqp;
    @Override
    public OgsaDQPResource.EntityServices dqp()
        {
        return dqp;
        }
    }