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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResourceEntity;

/**
 *
 *
 */
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = OgsaIvoaResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "OgsaIvoaResource-select-all",
            query = "FROM OgsaIvoaResourceEntity ORDER BY name asc, ident desc"
            ),
        }
    )
public class OgsaIvoaResourceEntity
    extends AbstractNamedEntity
    implements OgsaIvoaResource
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "OgsaServiceEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESOURCE_OGSAID_COL = "ogsaid";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESOURCE_STATUS_COL = "status";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESOURCE_SERVICE_COL = "service";
    
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESOURCE_SOURCE_COL = "source";
    
    /**
     * Protected constructor. 
     *
     */
    public OgsaIvoaResourceEntity()
        {
        super();
        }

    /**
    *
    * Public constructor.
    * @param name The resource name.
    * @param service The parent {@link OgsaService}
    * @param source  The source {@link IvoaResource}
    * @throws NameFormatException
    *
    */
   public OgsaIvoaResourceEntity(final OgsaService service, final IvoaResource source) throws NameFormatException
       {
       this(
           service,
           source,
           "fred"
           );
       }

   /**
     *
     * Public constructor.
     * @param name The resource name.
     * @param service The parent {@link OgsaService}
     * @param source  The source {@link IvoaResource}
     * @throws NameFormatException
     *
     */
    public OgsaIvoaResourceEntity(final OgsaService service, final IvoaResource source, final String name) throws NameFormatException
        {
        super(
            name
            );
        this.source  = source  ;
        this.service = service ;
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = OgsaServiceEntity.class
        )
    @JoinColumn(
        name = DB_RESOURCE_SERVICE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private OgsaService service;
    @Override
    public OgsaService service()
        {
        return this.service;
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = IvoaResourceEntity.class
        )
    @JoinColumn(
        name = DB_RESOURCE_SOURCE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private IvoaResource source;
    @Override
    public IvoaResource source()
        {
        return this.source;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_RESOURCE_OGSAID_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String ogsaid;
    @Override
    public String ogsaid()
        {
        return ogsaid;
        }

    @Column(
        name = DB_RESOURCE_STATUS_COL,
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
    public Status ping()
        {
        // TODO Auto-generated method stub
        return null;
        }

    @Override
    public String link()
        {
        return factories().ogsa().resources().ivoa().links().link(
            this
            );
        }
    }
