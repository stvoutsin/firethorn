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
package uk.ac.roe.wfau.firethorn.webapp.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.log.LogEntry;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * {@link LogEntry.LinkFactory} implementation.
 *
 */
@Component
public class LogEntryLinkFactory
extends WebappLinkFactory<LogEntry>
implements LogEntry.LinkFactory
    {
    /**
     * Protected constructor.
     *
     */
    protected LogEntryLinkFactory()
        {
        super(
            SERVICE_PATH
            );
        }

    /**
     * The URI path for the service, ({@value}).
     *
     */
    protected static final String SERVICE_PATH = "/log-entity" ;

    /**
     * The URI path for individual entities, ({@value}).
     *
     */
    public static final String ENTITY_PATH = SERVICE_PATH + "/" + IDENT_TOKEN ;

    @Override
    public String link(final LogEntry entity)
        {
        return link(
            ENTITY_PATH,
            entity
            );
        }

    @Autowired
    private LogEntry.EntityFactory factory ;
    @Override
    public LogEntry resolve(String link)
    throws IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException
        {
        if (this.matches(link))
            {
            return factory.select(
                this.ident(
                    link
                    )
                );
            }
        else {
            throw new EntityNotFoundException(
                "Unable to resolve [" + link + "]"
                );
            }
        }
    }
