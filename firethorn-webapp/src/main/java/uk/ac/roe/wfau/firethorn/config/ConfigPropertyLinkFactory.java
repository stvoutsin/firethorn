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
package uk.ac.roe.wfau.firethorn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>ConfigProperty</code>.
 *
 */
@Component
public class ConfigPropertyLinkFactory
extends WebappLinkFactory<ConfigProperty>
implements ConfigProperty.LinkFactory
    {
    protected ConfigPropertyLinkFactory()
        {
        super(
            SERVICE_PATH
            );
        }

    /**
     * The URI path for the service.
     *
     */
    public static final String SERVICE_PATH = "/conf/property";

    /**
     * The URI path for individual properties.
     *
     */
    public static final String PROPERTY_PATH = SERVICE_PATH + "/" + IDENT_TOKEN ;

    @Override
    public String link(final ConfigProperty entity)
        {
        return this.link(
            PROPERTY_PATH,
            entity
            );
        }

    @Autowired
    private ConfigProperty.EntityServices services ;
    @Override
    public ConfigProperty resolve(String link)
    throws IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException, ProtectionException
        {
        if (this.matches(link))
            {
            return services.entities().select(
                services.idents().ident(
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
