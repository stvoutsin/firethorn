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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 * {@link BaseColumn} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Inheritance(
    strategy = InheritanceType.TABLE_PER_CLASS
    )
public abstract class BaseColumnEntity<ColumnType extends BaseColumn<ColumnType>>
extends TreeComponentEntity<ColumnType>
    implements BaseColumn<ColumnType>
    {
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_TYPE_COL  = "adqltype"  ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_SIZE_COL  = "adqlsize"  ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_UTYPE_COL = "adqlutype" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_DTYPE_COL = "adqldtype" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_UNIT_COL = "adqlunit" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_UCD_TYPE_COL  = "adqlucdtype"  ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_ADQL_UCD_VALUE_COL = "adqlucdvalue" ;

    /**
     * {@link BaseColumn.EntityFactory} implementation.
     *
     */
    @Repository
    public static abstract class EntityFactory<TableType extends BaseTable<TableType, ColumnType>, ColumnType extends BaseColumn<ColumnType>>
    extends TreeComponentEntity.EntityFactory<ColumnType>
    implements BaseColumn.EntityFactory<TableType, ColumnType>
        {
        }
    
    /**
     * Protected constructor.
     *
     */
    protected BaseColumnEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     * @todo Remove the parent reference.
     *
     */
    protected BaseColumnEntity(final BaseTable<?,ColumnType> parent, final String name)
        {
        super(
            name
            );
        }

    @Override
    public StringBuilder namebuilder()
    throws ProtectionException
        {
        StringBuilder builder = this.table().namebuilder();
        if (this.name() != null)
            {
            if (builder.length() > 0)
                {
                builder.append(".");
                }
            builder.append(this.name());
            }
        return builder;
        }

    @Override
    public BaseSchema<?,?> schema()
    throws ProtectionException
        {
        return this.table().schema();
        }
    @Override
    public BaseResource<?> resource()
    throws ProtectionException
        {
        return this.table().resource();
        }

    @Override
    public abstract BaseColumn<?> base()
    throws ProtectionException;

    @Override
    public abstract BaseColumn<?> root()
    throws ProtectionException;

    @Enumerated(
        EnumType.STRING
        )
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private AdqlColumn.AdqlType adqltype ;
    protected AdqlColumn.AdqlType adqltype()
    throws ProtectionException
        {
        return this.adqltype;
        }
    protected void adqltype(final AdqlColumn.AdqlType type)
    throws ProtectionException
        {
        if (type == null)
            {
            log.error("null column type");            
            }
        else {
            this.adqltype = type;
            }
        }
    protected void adqltype(final String type)
    throws ProtectionException
        {
        adqltype(
            AdqlColumn.AdqlType.resolve(
                type
                )
            );
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer adqlsize ;
    protected Integer adqlsize()
    throws ProtectionException
        {
        return this.adqlsize ;
        }
    protected void adqlsize(final Integer size)
    throws ProtectionException
        {
        if (size != null)
            {
            this.adqlsize = size;
            }
        else {
            this.adqlsize = AdqlColumn.NON_ARRAY_SIZE ;
            }
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_UNIT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqlunit ;
    protected String adqlunit()
    throws ProtectionException
        {
        return this.adqlunit ;
        }
    protected void adqlunit(final String value)
    throws ProtectionException
        {
        this.adqlunit = emptystr(
            value
            );
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_UTYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqlutype ;
    protected String adqlutype()
    throws ProtectionException
        {
        return this.adqlutype ;
        }
    protected void adqlutype(final String value)
    throws ProtectionException
        {
        this.adqlutype = emptystr(
            value
            );
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_ADQL_UCD_VALUE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected String adqlucd ;
    protected String adqlucd()
    throws ProtectionException
        {
        return this.adqlucd ;
        }
    protected void adqlucd(final String value)
    throws ProtectionException
        {
        this.adqlucd = emptystr(
            value
            );
        }

    /**
     * Generate the {@link AdqlColumn.Metadata.Adql adql} metadata.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    protected AdqlColumn.Modifier.Adql adqlmeta()
    throws ProtectionException
        {
        return new AdqlColumn.Modifier.Adql()
            {
            @Override
            public Integer arraysize()
            throws ProtectionException
                {
                return adqlsize();
                }
            @Override
            public void arraysize(final Integer size)
            throws ProtectionException
                {
                adqlsize(
                    size
                    );
                }
            @Override
            public AdqlColumn.AdqlType type()
            throws ProtectionException
                {
                return adqltype();
                }
            @Override
            public void type(final AdqlColumn.AdqlType type)
            throws ProtectionException
                {
                adqltype(
                    type
                    );
                }
            @Override
            public void type(final String dtype)
            throws ProtectionException
                {
                adqltype(
                    dtype
                    );
                }

            @Override
            public String units()
            throws ProtectionException
                {
                return adqlunit();
                }
            @Override
            public void units(final String units)
            throws ProtectionException
                {
                adqlunit(
                    units
                    );
                }

            @Override
            public String utype()
            throws ProtectionException
                {
                return adqlutype();
                }
            @Override
            public void utype(final String utype)
            throws ProtectionException
                {
                adqlutype(
                    utype
                    );
                }

            @Override
            public String ucd()
            throws ProtectionException
                {
                return adqlucd();
                }
            @Override
            public void ucd(final String value)
            throws ProtectionException
                {
                adqlucd(
                    value
                    );
                }
            @Override
            public String name()
            throws ProtectionException
                {
                return BaseColumnEntity.this.name();
                }
            @Override
            public String text()
            throws ProtectionException
                {
                return BaseColumnEntity.this.text();
                }
            };
        }

    @Override
    public AdqlColumn.Modifier meta()
    throws ProtectionException
        {
        return new AdqlColumn.Modifier()
            {
            @Override
            public String name()
            throws ProtectionException
                {
                return BaseColumnEntity.this.name();
                }
            @Override
            public AdqlColumn.Modifier.Adql adql()
            throws ProtectionException
                {
                return adqlmeta();
                }
            };
        }

    @Override
    public void update(final AdqlColumn.Metadata.Adql meta)
    throws ProtectionException
    	{
        if (meta.text() != null)
            {
            this.text(
        		meta.text()
        		);
            }
        //
        //TODO Check the type and size - warn/fail if they have changed ?
        //
    	}
    }
