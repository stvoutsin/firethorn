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
package uk.ac.roe.wfau.firethorn.meta.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
@Component
public class JdbcFactoriesImpl
    implements JdbcFactories
    {

    @Autowired
    private JdbcResource.EntityFactory resources;
    @Override
    public JdbcResource.EntityFactory resources()
        {
        return this.resources;
        }

    @Autowired
    private JdbcSchema.EntityFactory schemas;
    @Override
    public JdbcSchema.EntityFactory schemas()
        {
        return this.schemas;
        }

    @Autowired
    private JdbcTable.EntityFactory tables;
    @Override
    public JdbcTable.EntityFactory tables()
        {
        return this.tables;
        }

    @Autowired
    private JdbcColumn.EntityFactory columns;
    @Override
    public JdbcColumn.EntityFactory columns()
        {
        return this.columns;
        }
    }
