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
package uk.ac.roe.wfau.firethorn.entity.access;

import uk.ac.roe.wfau.firethorn.access.Action;
import uk.ac.roe.wfau.firethorn.access.BaseProtector;
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * An abstract base class for {@Entity} {@link Protector} implementations.
 *
 */
public abstract class BaseEntityProtector
extends BaseProtector
implements EntityProtector
    {

    /**
     * Protected constructor.
     *
     */
    protected BaseEntityProtector(final Entity entity)
        {
        super(entity);
        this.entity = entity ;
        }

    private Entity entity;
    @Override
    public Entity entity()
        {
        return this.entity;
        }

    @Override
    public Protector affirm(final Identity identity, final Action action)
    throws ProtectionException
        {
        if (this.check(identity, action))
            {
            return this;
            }
        else {
            throw new EntityProtectorException(
                identity,
                action,
                entity
                );
            }
        }

    /**
     * Check if an {@link Identity} is our target {@Entity} owner.
     * 
     */
    public boolean isOwner(final Identity identity)
        {
        if (identity != null)
            {
            if (this.entity != null)
                {
                final Identity owner = this.entity.owner();
                if (owner != null)
                    {
                    return owner.equals(identity);
                    }
                }
            }
        return false;
        }
    }
