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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.Action;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlTranslator;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnectionEntity.MetadataException;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaJdbcResource;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 * {@link JdbcResource} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = JdbcResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "JdbcResource-select-all",
            query = "FROM JdbcResourceEntity ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "JdbcResource-select-name",
            query = "FROM JdbcResourceEntity WHERE (name = :name) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "JdbcResource-select-uid",
            query = "FROM JdbcResourceEntity WHERE (uid = :uid) ORDER BY ident desc"
            )
        }
    )
public class JdbcResourceEntity
    extends BaseResourceEntity<JdbcResource, JdbcSchema>
    implements JdbcResource
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "JdbcResourceEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_UID_COL = "jdbcuid";

    /**
     * {@link JdbcResource.EntityFactory} implementation.
     *
     */
    @Component
    @Repository
    public static class EntityFactory
    extends BaseResourceEntity.EntityFactory<JdbcResource>
    implements JdbcResource.EntityFactory
        {
        @Override
        public Protector protector()
            {
            return new FactoryAdminCreateProtector();
            }
        
        @Override
        public Class<?> etype()
            {
            return JdbcResourceEntity.class ;
            }

        @Override
        @SelectMethod
        public Iterable<JdbcResource> select()
        throws ProtectionException
            {
            protector().affirm(Action.select);
            return super.iterable(
                super.query(
                    "JdbcResource-select-all"
                    )
                );
            }

        @Override
        @SelectMethod
        public JdbcResource select(final String name)
        throws ProtectionException, NameNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "JdbcResource-select-name"
                        ).setString(
                            "name",
                            name
                        )
                    );
                }
            catch (final EntityNotFoundException ouch)
                {
                log.debug("Unable to locate resource [{}]", name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

		@Override
	    @CreateMethod
	    public JdbcResource create(final String name, final JdbcProductType type, final String database, final String catalog, final String host, final Integer port, final String user, final String pass)
        throws ProtectionException
		    {
            protector().affirm(Action.create);
		    return super.insert(
		        new JdbcResourceEntity(
	                null,
		            name,
		            type,
		            database,
                    catalog,
		            host,
		            port,
		            user,
		            pass
		            )
                );
            }

        /**
         * The 'userdata' database identifier).
         *
         */
        @Value("${firethorn.user.uid:userdata}")
        public String uduid;

        /**
         * The 'userdata' JDBC type (pgsql|mysql|mssql etc).
         *
         */
        @Value("${firethorn.user.type:#{null}}")
        public String udtype ;

        /**
         * The 'userdata' JDBC database name.
         *
         */
        @Value("${firethorn.user.data:#{null}}")
        public String uddata;

        /**
         * The 'userdata' JDBC host name.
         *
         */
        @Value("${firethorn.user.host:#{null}}")
        public String udhost;

        /**
         * The 'userdata' JDBC port number.
         *
         */
        @Value("${firethorn.user.port:#{null}}")
        public String udport;
        
        /**
         * The 'userdata' JDBC user name.
         *
         */
        @Value("${firethorn.user.user:#{null}}")
        public String uduser ;

        /**
         * The 'userdata' JDBC password.
         *
         */
        @Value("${firethorn.user.pass:#{null}}")
        public String udpass ;

        /**
         * Select (or create) the default 'userdata' JdbcResource.
         *
         */
        @Override
        @CreateMethod
        public JdbcResource userdata()
        throws ProtectionException
            {
            // Uses local calls to super.first() and super.insert() to avoid protectors.
            log.debug("userdata()");
            JdbcResource userdata = super.first(
                super.query(
                    "JdbcResource-select-uid"
                    ).setString(
                        "uid",
                        uduid
                        )
                );
            
            if (userdata == null)
                {
                log.debug("Userdata resource is null, creating a new one");

                log.debug(" host [{}]", udhost);
                log.debug(" port [{}]", udport);
                log.debug(" data [{}]", uddata);
                log.debug(" user [{}]", uduser);
                log.debug(" pass [{}]", udpass);
                
                userdata = super.insert(
                    new JdbcResourceEntity(
                        uduid,
                        "Userdata resource",
                        JdbcProductType.valueOf(
                            udtype
                            ),
                        uddata,
                        uddata,
                        udhost,
                        udport,
                        uduser,
                        udpass
                        )
                    );
                }

            log.debug("Userdata resource [{}][{}]", userdata.ident(), userdata.name());
            return userdata ;
            }
        }

    /**
     * {@link Entity.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements JdbcResource.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static JdbcResourceEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return JdbcResourceEntity.EntityServices.instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected EntityServices()
            {
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            if (JdbcResourceEntity.EntityServices.instance == null)
                {
                JdbcResourceEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private JdbcResource.IdentFactory idents;
        @Override
        public JdbcResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private JdbcResource.LinkFactory links;
        @Override
        public JdbcResource.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private JdbcResource.NameFactory names;
        @Override
        public JdbcResource.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private JdbcResource.EntityFactory entities;
        @Override
        public JdbcResource.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
        protected JdbcSchema.EntityFactory schemas;
        @Override
        public JdbcSchema.EntityFactory schemas()
            {
            return this.schemas;
            }
        }

    @Override
    protected JdbcResource.EntityFactory factory()
        {
        return JdbcResourceEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected JdbcResource.EntityServices services()
        {
        return JdbcResourceEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }
    
    /**
     * Protected constructor. 
     *
     */
    protected JdbcResourceEntity()
        {
        super();
        }

    /**
     * Protected constructor. 
     *
     */
    protected JdbcResourceEntity(final String uid, final String name, final JdbcProductType type, final String database, final String catalog, final String host, final String port, final String user, final String pass)
        {
        this(
            uid,
            name,
            type,
            database,
            catalog,
            host,
            ((port != null) ? new Integer(port) : null),
            user,
            pass
            );
        }
    
    
    /**
     * Protected constructor. 
     *
     */
    protected JdbcResourceEntity(final String uid, final String name, final JdbcProductType type, final String database, final String catalog, final String host, final Integer port, final String user, final String pass)
	    {
	    super(
	        name
	        );
        this.uid = uid;
	    this.connection = new JdbcConnectionEntity(
	        this,
	        type,
	        database,
	        catalog,
            host,
	        port,
	        user,
	        pass
	        );
	    }
    
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_UID_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String uid ;
    public String uid()
        {
        return this.uid;
        }
    
    @Override
    public JdbcResource.Schemas schemas()
    throws ProtectionException
        {
        log.debug("schemas() for [{}][{}]", ident(), namebuilder());
        scan();
        return new JdbcResource.Schemas()
            {

            @Override
            public Iterable<JdbcSchema> select()
            throws ProtectionException
                {
                protector().affirm(Action.select);
                return factories().jdbc().schemas().entities().select(
                    JdbcResourceEntity.this
                    );
                }

            @Override
            public JdbcSchema create(final Identity identity)
            throws ProtectionException
                {
                return factories().jdbc().schemas().entities().build(
                    JdbcResourceEntity.this,
                    identity
                    );
                }

            @Override
            public JdbcSchema create(final JdbcSchema.Metadata meta)
            throws ProtectionException
                {
                return factories().jdbc().schemas().entities().create(
                    JdbcResourceEntity.this,
                    meta
                    );
                }
            
            @Override
            @Deprecated
            public JdbcSchema create(final String catalog, final String schema)
            throws ProtectionException
                {
                return factories().jdbc().schemas().entities().create(
                    JdbcResourceEntity.this,
                    catalog,
                    schema
                    );
                }

            @Override
            public JdbcSchema search(final String name)
            throws ProtectionException
                {
                return factories().jdbc().schemas().entities().search(
                    JdbcResourceEntity.this,
                    name
                    );
                }


            @Override
            public JdbcSchema select(Identifier ident)
            throws ProtectionException, IdentifierNotFoundException
                {
                return factories().jdbc().schemas().entities().select(
                    JdbcResourceEntity.this,
                    ident
                    );
                }

            @Override
            public JdbcSchema select(final String name)
            throws ProtectionException, NameNotFoundException
                {
                return factories().jdbc().schemas().entities().select(
                    JdbcResourceEntity.this,
                    name
                    );
                }

            @Override
            public JdbcSchema select(final String catalog, final String schema)
            throws ProtectionException, NameNotFoundException
                {
                return factories().jdbc().schemas().entities().select(
                    JdbcResourceEntity.this,
                        catalog,
                        schema
                    );
                }

            @Override
            public JdbcSchema search(final String catalog, final String schema)
            throws ProtectionException
                {
                return factories().jdbc().schemas().entities().search(
                    JdbcResourceEntity.this,
                    catalog,
                    schema
                    );
                }

            @Override
            public JdbcSchema simple()
            throws ProtectionException, EntityNotFoundException
                {
                return factories().jdbc().schemas().entities().select(
                    JdbcResourceEntity.this,
                    connection().catalog(),
                    connection().type().schema()
                    );
                }

            @Override
            public JdbcSchema.Builder builder()
            throws ProtectionException
                {
                return new JdbcSchemaEntity.Builder(this.select())
                    {
                    @Override
                    protected JdbcSchema create(final JdbcSchema.Metadata meta)
                    throws ProtectionException, DuplicateEntityException
                        {
                        return factories().jdbc().schemas().entities().create(
                            JdbcResourceEntity.this,
                            meta
                            );
                        }
                    };
                }
            };
        }

    @Embedded
    private JdbcConnectionEntity connection;

    /*
     * Declaring this as a protected method enables {@link JdbcConnectionEntity} to access it.
     *  
     */
    protected ComponentFactories factories()
        {
        return super.factories();
        }

    @Override
    public JdbcConnector connection()
        {
        return this.connection;
        }

    private String keyname(final JdbcSchema schema )
    throws ProtectionException
        {
        return keyname(
            schema.catalog(),
            schema.schema()
            );
        }

    private String keyname(final JdbcMetadataScanner.Schema schema)
        {
        return keyname(
            schema.catalog().name(),
            schema.name()
            );
        }

    private String keyname(final String catalog, final String schema)
        {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append(catalog);
        builder.append("}{");
        builder.append(schema);
        builder.append("}");
        return builder.toString();
        }
    
    @Override
    protected void scanimpl()
    throws ProtectionException
        {
        log.debug("scanimpl() for [{}][{}]", this.ident(), this.namebuilder());
        //
        // Create our metadata scanner.
        JdbcMetadataScanner scanner = connection().scanner();
        //
        // Load our Map of known schema.
        Map<String, JdbcSchema> known = new HashMap<String, JdbcSchema>();
        Map<String, JdbcSchema> matching = new HashMap<String, JdbcSchema>();
        for (JdbcSchema schema : factories().jdbc().schemas().entities().select(JdbcResourceEntity.this))
            {
            final String key = keyname(
                schema
                );
            log.debug("Caching known schema [{}]", key);
            known.put(
                key,
                schema
                );
            }
        //
        // Try/finally to close our connection. 
        try {
            //
            // Scan all the catalogs.
            if ((connection().catalog() == null) || ("*".equals(connection().catalog())))
                {
                log.trace("connection().catalog() [{}]", connection().catalog());
                try {
                    for (JdbcMetadataScanner.Catalog catalog : scanner.catalogs().select())
                        {
                        log.trace("catalog [{}]", catalog);
                        scan(
                            known,
                            matching,
                            catalog
                            );
                        }
                    }
                catch (SQLException ouch)
                    {
                    log.warn("Exception while fetching JDBC catalogs [{}][{}]", this.ident(), ouch.getMessage());
                    scanner.handle(ouch);
                    }
                catch (MetadataException ouch)
                    {
                    log.warn("Exception while fetching JDBC catalogs [{}][{}]", this.ident(), ouch.getMessage());
                    }
                }
            //
            // Scan a specific catalog.
            else {
            	log.trace("connection().catalog() [{}]", connection().catalog());
                try {
                    final JdbcMetadataScanner.Catalog catalog = scanner.catalogs().select(
            	        connection().catalog()
                        );
                    log.trace("catalog [{}]", catalog);
                    scan(
                        known,
                        matching,
                        catalog
                        );
                    }
                catch (SQLException ouch)
                    {
                    log.warn("Exception while fetching JDBC catalog [{}][{}][{}]", this.ident(), connection().catalog(), ouch.getMessage());
                    scanner.handle(ouch);
                    }
                catch (MetadataException ouch)
                    {
                    log.warn("Exception while fetching JDBC catalog [{}][{}][{}]", this.ident(), connection().catalog(), ouch.getMessage());
                    }
                }
            }
        finally {
            scanner.connector().close();
            }
        log.debug("schemas() scan done for [{}][{}]", this.ident(), this.namebuilder());
        log.debug("Matching schemas [{}]", matching.size());
        log.debug("Listed but not matched [{}]", known.size());
        }
    
    protected void scan(final Map<String, JdbcSchema> known, final Map<String, JdbcSchema> matching, final JdbcMetadataScanner.Catalog catalog)
    throws ProtectionException
        {
        if (catalog == null)
            {
            log.warn("Null catalog");
            }
        else {
            log.debug("Scanning catalog [{}]", catalog.name());
            try {
                for (JdbcMetadataScanner.Schema schema : catalog.schemas().select())
                    {
                    scan(
                        known,
                        matching,
                        schema
                        );                
                    }
                }
            catch (SQLException ouch)
                {
                if (ouch.getErrorCode() == 916)
                    {
                    log.debug("Access denied to catalog [{}]", catalog.name());
                    }
                else {
                    log.warn("Exception while scanning catalog [{}][{}][{}]", this.ident(), catalog.name(), ouch.getMessage());
                    catalog.scanner().handle(ouch);
                    }
                }
            }
        }

    protected void scan(final Map<String, JdbcSchema> existing, final Map<String, JdbcSchema> matching, final JdbcMetadataScanner.Schema schema)
    throws ProtectionException
        {
        final String key = keyname(
            schema
            );
        log.debug("Scanning for schema [{}][{}]", schema.catalog().name(), schema.name());
        log.trace("Scanning for schema [{}]", key);
        //
        // Check for an existing match.
        if (existing.containsKey(key))
            {
            log.debug("Found matching schema [{}]", key);
            matching.put(
                key,
                existing.remove(
                    key
                    )
                );            
            }
        //
        // No match, so create a new one.
        else {
            log.debug("Creating new schema [{}][{}]", schema.catalog().name(), schema.name());
            log.trace("Cacheing new schema [{}]", key);
            matching.put(
                key,
                factories().jdbc().schemas().entities().create(
                    JdbcResourceEntity.this,
                    schema.catalog().name(),
                    schema.name()
                    )
                );
            }
        }

    /**
     * Generate the JDBC metadata.
     * 
     */
    protected JdbcResource.Metadata.Jdbc jdbcmeta()
    throws ProtectionException
        {
        return new JdbcResource.Metadata.Jdbc()
            {
            };
        }
    
    @Override
    public JdbcResource.Metadata meta()
    throws ProtectionException
        {
        return new JdbcResource.Metadata()
            {
            @Override
            public JdbcResource.Metadata.Jdbc jdbc()
            throws ProtectionException
                {
                return jdbcmeta();
                }
            };
        }

    @Override
    public OgsaJdbcResources ogsa()
    throws ProtectionException
        {
        return new OgsaJdbcResources()
            {
            @Override
            public OgsaJdbcResource primary()
            throws ProtectionException
                {
                return factories().ogsa().jdbc().entities().primary(
                    JdbcResourceEntity.this
                    );
                }

            @Override
            public Iterable<OgsaJdbcResource> select()
            throws ProtectionException
                {
                return factories().ogsa().jdbc().entities().select(
                    JdbcResourceEntity.this
                    );
                }
            };
        }
    
    @Override
    public AdqlTranslator translator()
        {
        return this.connection.translator();
        }
    }
