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
package uk.ac.roe.wfau.firethorn.meta.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Enumeration of known database types, indexed by the product name in DatabaseMetaData.getDatabaseProductName()
 * @todo Refactor this as an interface, allowing TAP services t have a ProductType.
 *
 */
@Slf4j
public enum JdbcProductType
    {
    unknown(
        "unknown",
        "unknown",
        null
        ),
    pgsql(
        "PostgreSQL",
        "public",
        new String[]{}
        ),
    mysql(
        "MySQL",
        "public",
        new String[]{}
        ),
    mssql(
        "Microsoft SQL Server",
        "dbo",
        new String[]{
            "sys",
            "guest",
            "INFORMATION_SCHEMA",
            "db_accessadmin",
            "db_backupoperator",
            "db_datareader",
            "db_datawriter",
            "db_ddladmin",
            "db_denydatareader",
            "db_denydatawriter",
            "db_owner",
            "db_securityadmin"
            }
        ),
    hsqldb(
        "HSQL Database Engine",
        "PUBLIC.PUBLIC",
        new String[]{}
        );

    /**
     * Private constructor.
     * @param mname  The metadata name.
     * @param schema The default schema name.
     *
     */
    private JdbcProductType(final String mname, final String schema)
        {
        this(
            mname,
            schema,
            null
            );
        }

    /**
     * Private constructor.
     * @param mname  The metadata name.
     * @param schema The default schema name.
     * @param ignores A list of 'system' schema to ignore.
     *
     */
    private JdbcProductType(final String mname, final String schema, final String[] ignores)
        {
        this.mname  = mname;
        this.schema = schema;
        if (ignores != null)
            {
            for (final String ignore : ignores)
                {
                this.ignore.add(
                    ignore
                    );
                }
            }
        }

    /**
     * The name reported in DatabaseMetaData.
     *
     */
    private final String mname ;

    /**
     * The name reported in DatabaseMetaData.
     *
     */
    public String mname()
        {
        return this.mname;
        }

    /**
     *
     * The default schema name.
     *
     */
    private final String schema ;

    /**
     * The default schema name.
     *
     */
    public String schema()
        {
        return this.schema ;
        }

    /**
     * A list of 'system' schema to ignore.
     *
     */
    private final Collection<String> ignore = new ArrayList<String>();

    /**
     * A list of 'system' schema to ignore.
     *
     */
    public Collection<String> ignore()
        {
        return this.ignore;
        }

    /**
     * A shared {@link Map} of {@link String} to {@link JdbcProductType}.
     *  
     */
    static protected Map<String, JdbcProductType> mapping = new HashMap<String, JdbcProductType>();
    static {
        for (final JdbcProductType type : JdbcProductType.values())
            {
            mapping.put(
                type.mname(),
                type
                );
            }
        }

    /**
     * Search the shared {@link Map} for a {@link JdbcProductType} based on its name. 
     * @param mname The name of the {@link JdbcProductType} to look for.
     * @return The matching {@link JdbcProductType}, or {@link JdbcProductType#unknown} if no match was found. 
     *
     */
    static public JdbcProductType match(final String mname)
        {
        if (mapping.containsKey(mname))
            {
            return mapping.get(
                mname
                );
            }
        else {
            return unknown;
            }
        }

    /**
     * Search the shared {@link Map} for a {@link JdbcProductType} based on the name given by {@link DatabaseMetaData#getDatabaseProductName}. 
     * @param metadata The {@link DatabaseMetaData} to match the {@link JdbcProductType} name with.
     * @return The matching {@link JdbcProductType}, or {@link JdbcProductType#unknown} if no match was found. 
     *
     */
    static public JdbcProductType match(final DatabaseMetaData metadata)
        {
        if (metadata != null)
            {
            try {
                return match(
                    metadata.getDatabaseProductName()
                    );
                }
            catch (final SQLException ouch)
                {
                log.error("SQLException reading database metadata [{}]", ouch.getMessage());
                return unknown;
                }
            }
        else {
            return unknown;
            }
        }
    }
