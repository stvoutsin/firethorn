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
package uk.ac.roe.wfau.firethorn.tuesday;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayIvoaColumnEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        }
    )
public class TuesdayIvoaColumnEntity
    extends TuesdayBaseColumnEntity
    implements TuesdayIvoaColumn
    {
    protected static final String DB_TABLE_NAME = "TuesdayIvoaColumnEntity";

    protected TuesdayIvoaColumnEntity()
        {
        }

    protected TuesdayIvoaColumnEntity(TuesdayIvoaTable table, String name)
        {
        super(name);
        this.table = table;
        }

    @Override
    public TuesdayIvoaColumn ogsa()
        {
        return this ;
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayIvoaTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private TuesdayIvoaTable table;
    @Override
    public TuesdayIvoaTable table()
        {
        return this.table;
        }
    @Override
    public TuesdayIvoaSchema schema()
        {
        return this.table().schema();
        }
    @Override
    public TuesdayIvoaResource resource()
        {
        return this.table().resource();
        }
    }
