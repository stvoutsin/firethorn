/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.mock;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMapping;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMappingService;

/**
 *
 *
 */
public class TableMappingServiceMock
    implements TableMappingService
    {
    private static Log log = LogFactory.getLog(TableMappingServiceMock.class);
    private final Map<String, TableMapping> map = new HashMap<String, TableMapping>();

    public class TableMappingMock
    implements TableMapping
        {
        private final String resource;
        private final String alias;
        private final String name;

        public TableMappingMock(final String resource, final String alias, final String name)
            {
            //log.debug("TableMappingMock()");
            //log.debug(" Mapping [" + resource + "][" + name + "][" + alias+ "]");
            this.resource = resource;
            this.alias = alias;
            this.name = name ;
            }

        @Override
        public String resourceIdent()
            {
            return this.resource;
            }

        @Override
        public String tableAlias()
            {
            return this.alias;
            }

        @Override
        public String tableName()
            {
            return this.name;
            }
        }

    public void put(final String resource, final String alias, final String name)
        {

        this.put(
            new TableMappingMock(
                resource,
                alias,
                name
                )
            );
        }

    public void put(final TableMapping mapping)
        {
        log.debug("TableMappingServiceMock.put() [" + mapping.resourceIdent() + "][" + mapping.tableName() + "][" + mapping.tableAlias() + "]");
        this.map.put(
            mapping.tableAlias(),
            mapping
            );
        }

    public TableMapping get(final String source)
        {
        log.debug("TableMappingServiceMock.get(String) [" + source + "]");
        return this.map.get(
            source
            );
        }

    @Override
    public TableMapping getTableMapping(final String source)
        {
        log.debug("TableMappingServiceMock.getTableMapping(String) [" + source + "]");
        return this.map.get(
            source
            );
        }
    }

