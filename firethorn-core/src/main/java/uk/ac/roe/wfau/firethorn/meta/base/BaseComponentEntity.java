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
package uk.ac.roe.wfau.firethorn.meta.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.Period;

import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.exception.FirethornUncheckedException;

/**
 *
 *
 */
@Slf4j
@Access(
    AccessType.FIELD
    )
@MappedSuperclass
public abstract class BaseComponentEntity<ComponentType extends BaseComponent>
extends AbstractNamedEntity
implements BaseComponent
    {
    /**
     * Hibernate column mapping.
     *
     */
    public static final String DB_BASE_COL   = "base";
    public static final String DB_PARENT_COL = "parent";
    public static final String DB_STATUS_COL = "status";
    public static final String DB_ALIAS_COL  = "alias";

    protected static final String DB_NAME_IDX        = "IndexByName";
    protected static final String DB_PARENT_IDX      = "IndexByParent";
    protected static final String DB_PARENT_NAME_IDX = "IndexByParentAndName";

    protected static final String DB_RESOURCE_COL = "resource";
    protected static final String DB_SCHEMA_COL   = "schema";
    protected static final String DB_TABLE_COL    = "table";
    protected static final String DB_COLUMN_COL   = "column";

    protected static final String DB_COPY_DEPTH_COL = "copydepth" ;

    protected static final String DB_SCAN_TIME_COL   = "scantime";
    protected static final String DB_SCAN_PERIOD_COL = "scanperiod";

    
    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected BaseComponentEntity()
        {
        super();
        }

    /**
     * Protected constructor, owner defaults to the current actor.
     *
     */
    protected BaseComponentEntity(final String name)
        {
        this(
            CopyDepth.FULL,
            name
            );
        }

    /**
     * Protected constructor, owner defaults to the current actor.
     *
     */
    protected BaseComponentEntity(final CopyDepth depth, final String name)
        {
        super(
            name
            );
        this.depth = depth;
        log.debug("BaseComponentEntity(CopyDepth, String)");
        log.debug("  Name  [{}]", name);
        log.debug("  Depth [{}]", depth);
        }

    /**
     * The component status.
     *
     */
    @Column(
        name = DB_STATUS_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Status status = Status.CREATED ;

    @Override
    public Status status()
        {
        return this.status;
        }

    @Override
    public void status(final Status status)
        {
        this.status = status;
        }

    /**
     * The polling interval for waiting scans.
     *
     */
    private static final long POLL_WAIT = 1000 ;

    /**
     * Synchronised set of Identifiers for active scans.
     *
     */
    private static Set<String> scanset = Collections.synchronizedSet(
        new HashSet<String>()
        );

    /**
     * Synchronise our metadata scans.
     *
    public void scansync()
        {
        log.debug("scansync() for [{}]", ident());
        boolean doscan = false ;
        final String ident = this.ident().toString();
        //
        // Sync on the set and check for active scans.
        synchronized (scanset)
            {
            //
            // If we are already scanning, wait for notification.
            log.debug("Checking for existing scan [{}]", ident);
            if (scanset.contains(ident))
                {
                do {
                    try {
                        log.debug("Scan wait start [{}]", ident);
                        scanset.wait(POLL_WAIT);
                        log.debug("Scan wait woken [{}]", ident);
                        }
                    catch (final Exception ouch)
                        {
                        log.warn("Scan wait interrupted [{}][{}]", ident, ouch.getMessage());
                        }
                    }
                while (scanset.contains(ident));
                log.debug("Scan wait done [{}]", ident);
                }
            //
            // If not already scanning, add our Identifier to the Set and run a scan.
            else {
                log.debug("Adding new scan [{}]", ident);
                doscan = true ;
                scanset.add(
                    ident
                    );
                }
            }
        //
        // If we are due to run a scan.
        if (doscan)
            {
            //
            // Run our scan ...
            try {
                log.debug("Running scan [{}]", ident);
                scanimpl();
                scantime(
                    System.currentTimeMillis()
                    );
                }
            //
            // Remove ourselves from the Set and notify anyone else waiting.
            finally {
                log.debug("Completed scan [{}]", ident);
                synchronized (scanset)
                    {
                    scanset.remove(
                        ident
                        );
                    scanset.notifyAll();
                    }
                }
            }
        }
     */

    /**
     * Metadata scan implementation.
     * @todo Not null when we re-build DB.
     *
     */
    protected abstract void scanimpl();

    @Column(
        name = DB_COPY_DEPTH_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    protected CopyDepth depth = CopyDepth.FULL ;

    @Override
    public CopyDepth depth()
        {
        return this.depth;
        }

    @Override
    public void depth(final CopyDepth type)
        {
        this.depth = type;
        }
    
    /**
     * Exception thrown if loading self() fails.
     * 
     */
    public static class SelfException
    extends FirethornUncheckedException
        {
        /**
         * Generated serial version UID.
         *
         */
        private static final long serialVersionUID = -8936523546194517863L;

        /**
         * Protected constructor.
         * 
         */
        protected SelfException(final Class<?> clazz, final Identifier ident)
            {
            this.clazz = clazz;
            this.ident = ident;
            }

        /**
         * The Entity class.
         * 
         */
        private Class<?> clazz ;

        /**
         * The Entity class.
         * 
         */
        public Class<?> clazz()
            {
            return this.clazz;
            }

        /**
         * The Entity Identifier.
         * 
         */
        private Identifier ident ;

        /**
         * The Entity Identifier.
         * 
         */
        public Identifier ident()
            {
            return this.ident;
            }
        }
    
    /**
     * Load a persistent reference for this Entity.
     * @return The persistent instance or proxy for the entity.
     * @see Session#load(Class, java.io.Serializable)
     * @todo Delegate to each entity class.
     * @todo Check for recursion.
     *
     */
    protected ComponentType self()
        {
        log.debug("Loading proxy for self");
        @SuppressWarnings("unchecked")
        final ComponentType entity = (ComponentType) factories().hibernate().session().load(
            this.getClass(),
            this.ident().value()
            );
        if (entity != null)
            {
            return entity ;
            }
        else {
            throw new SelfException(
                this.getClass(),
                this.ident()
                );
            }
        }

    /**
     * Synchronised Map of scan blocks.
     *
     */
    private static Map<Identifier, Object> blocks = new HashMap<Identifier, Object>();

    /**
     * Check for an existing block, or create a new one.
     *
     */
    protected Object block()
        {
        log.debug("Checking for existing block [{}]", this.ident());
        synchronized (blocks)
            {
            Object block = blocks.get(
                this.ident()
                );
            if (block != null)
                {
                log.debug("Found existing block [{}][{}]", this.ident(), block);
                return block;
                }
            else {
                block = new DateTime();
                log.debug("Adding new block [{}][{}]", this.ident(), block);
                blocks.put(
                    this.ident(),
                    block
                    );
                return null ;
                }
            }
        }

    /**
     * Release waiting blocks.
     * 
     */
    protected void release()
        {
        log.debug("Releasing blocks [{}]", this.ident());
        synchronized (blocks)
            {
            Object block = blocks.get(
                this.ident()
                );
            if (block != null)
                {
                log.debug("Found existing block [{}][{}]", this.ident(), block);
                log.debug("Removing ....");
                blocks.remove(this.ident());
                log.debug("Notifying ....");
                synchronized (block)
                    {
                    block.notifyAll();
                    }
                }
            else {
                log.debug("No blocks found [{}]", this.ident());
                }
            }
        }

    /**
     * The default re-scan interval.
     * 
     */
    protected static final Period DEFAULT_SCAN_PERIOD = new Period(0, 10, 0, 0);

    /**
     * The blocking timeout.
     * 
     */
    protected static final long BLOCKING_TIMEOUT = 500 ;
    
    /**
     * The data/time of the last scan.
     * 
     */
    @Column(
        name = DB_SCAN_TIME_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime scantime ;
    protected DateTime scantime()
        {
        return this.scantime;
        }
    protected void scandate(final DateTime time)
        {
        this.scantime = time;
        }
    
    /**
     * The scan refresh period.
     * 
     */
    @Column(
        name = DB_SCAN_PERIOD_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private Period scanperiod = DEFAULT_SCAN_PERIOD ;
    protected Period scanperiod()
        {
        return this.scanperiod;
        }
    protected void scanperiod(final Period period)
        {
        this.scanperiod = period;
        }
    
    /**
     * Check the scan criteria and scan.
     *
     */
    protected void scantest()
        {
        log.debug("scantest for [{}][{}]", this.ident(), this.name());
        log.debug("scandate [{}]", scantime);
        if ((scantime == null) || (scantime.plus(scanperiod).isBeforeNow()))
            {
            log.debug("scandate is in the past");
            Object block = block();
            if (block == null)
                {
                try {
                    log.debug("Running scan [{}][{}]", this.ident(), this.name());
                    scanimpl();
                    }
                finally {
                    scantime = new DateTime();
                    release();
                    }
                }
            else {
                log.debug("Found block [{}][{}][{}]", this.ident(), this.name(), block);
                while (block != null)
                    {
                    try {
                        log.debug("Waiting on block [{}][{}][{}]", this.ident(), this.name(), block);
                        block.wait(BLOCKING_TIMEOUT);
                        }
                    catch (Exception ouch)
                        {
                        log.debug("Interrupted [{}][{}][{}][{}]", this.ident(), this.name(), block, ouch.getMessage());
                        }
                    block = block();
                    }
                log.debug("No more blocks [{}][{}]", this.ident(), this.name());
                }
            }
        else {
            log.debug("scandate is recent [{}][{}]", this.ident(), this.name());
            }
        }
    }
