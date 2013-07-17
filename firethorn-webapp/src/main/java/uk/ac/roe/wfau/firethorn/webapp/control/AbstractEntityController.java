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
package uk.ac.roe.wfau.firethorn.webapp.control;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import uk.ac.roe.wfau.firethorn.entity.Entity;

/**
 * Abstract base class for Spring MVC controllers.
 *
 */
@Slf4j
@Controller
public abstract class AbstractEntityController<EntityType extends Entity>
extends AbstractController
    {

    /**
     * Protected constructor.
     *
     */
    protected AbstractEntityController()
        {
        super();
        }

    /**
     * Wrap an entity as a bean.
     *
     */
    public abstract EntityBean<EntityType> bean(final EntityType entity);

    /**
     * Wrap a set of entities as beans.
     *
     */
    public abstract Iterable<EntityBean<EntityType>> bean(final Iterable<EntityType> iter);

    /**
     * Generate a 'created' HTTP response.
     *
     */
    private ResponseEntity<EntityBean<EntityType>> created(final EntityBean<EntityType> bean)
        {
        return new ResponseEntity<EntityBean<EntityType>>(
            bean,
            new RedirectHeader(
                bean
                ),
            HttpStatus.CREATED
            );
        }

    /**
     * Generate a 'created' HTTP response.
     *
     */
    public ResponseEntity<EntityBean<EntityType>> created(final EntityType entity)
        {
        return created(
            bean(
                entity
                )
            );
        }
    }

