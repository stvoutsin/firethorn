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
package uk.ac.roe.wfau.firethorn.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import lombok.extern.slf4j.Slf4j;

/**
 * An abstract class to wrap a {@link ResultSet} as an {@link Iterator}.
 * http://stackoverflow.com/a/1870090 
 *
 */
@Slf4j
public abstract class ResultSetIterator<EntityType>
implements Iterator<EntityType>
    {
    /**
     * An abstract factory class to wrap a {@link ResultSet} as an {@link Iterable}.
     * 
     */
    public abstract static class Factory<EntityType>
    implements Iterable<EntityType>
        {
        /**
         * Abstract method to get the next EntityType.
         *
         */
        protected abstract EntityType getNext()
        throws SQLException; 

        /**
         * Our target {@link ResultSet}.
         * 
         */
        private ResultSet results ;

        /**
         * Our target {@link ResultSet}.
         * 
         */
        protected ResultSet results()
            {
            return this.results;
            }
        
        /**
         * Public constructor.
         * 
         */
        public Factory(final ResultSet results)
            {
            this.results = results ;
            }
        
        @Override
        public Iterator<EntityType> iterator()
            {
            return new ResultSetIterator<EntityType>(this.results)
                {
                @Override
                protected EntityType getNext()
                    throws SQLException
                    {
                    return Factory.this.getNext();
                    }
                };
            }
        }
    
    /**
     * Abstract method to get the next EntityType.
     *
     */
    protected abstract EntityType getNext()
    throws SQLException; 

    /**
     * Our target {@link ResultSet}.
     * 
     */
    private ResultSet results ;

    /**
     * Public constructor.
     *
     */
    public ResultSetIterator(final ResultSet results)
        {
        this.results = results ;
        }

    /**
     * Boolean flag to indicate we have another result.
     * 
     */
    private boolean hasNext = false ;

    /**
     * Boolean flag to indicate we have called {@link ResultSet#next()}.
     * 
     */
    private boolean didNext = false ;
    
    @Override
    public boolean hasNext()
        {
        try {
            if (didNext == false)
                {
                didNext = true ;
                hasNext = results.next();
                }
            }
        catch (SQLException ouch)
            {
            log.warn("Exception while checking for next row [{}]", ouch.getMessage());
            didNext = true ;
            hasNext = false ;
            throw new RuntimeException(
                ouch
                );
            }
        return hasNext;
        }

    @Override
    public EntityType next()
        {
        try {
            if (didNext == false)
                {
                hasNext = results.next();
                }
            didNext = false ;

            if (hasNext == true)
                {
                return getNext();
                }
            else {
                throw new NoSuchElementException();
                }
            }
        catch (SQLException ouch)
            {
            log.warn("Exception while fetching next row [{}]", ouch.getMessage());
            hasNext = false ;
            didNext = false ;
            throw new RuntimeException(
                ouch
                );
            }
        }

    @Override
    public void remove()
        {
        throw new UnsupportedOperationException();
        }
    }
