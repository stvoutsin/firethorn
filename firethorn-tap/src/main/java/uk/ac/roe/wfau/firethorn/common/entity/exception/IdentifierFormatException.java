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
package uk.ac.roe.wfau.firethorn.common.entity.exception ;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier ;

/**
 *
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IdentifierFormatException
extends RuntimeException
    {

    /**
     * Default message for simple constructor.
     *
     */
    public static final String DEFAULT_MESSAGE = "Invalid identifier [:ident:]" ;

    /**
     * Create a default message.
     *
     */
    public static String message(String ident)
        {
        return DEFAULT_MESSAGE.replace(":ident:", ident);
        }

    public IdentifierFormatException(String ident)
        {
        this(
            ident,
            message(
                ident
                ),
            null
            );
        }

    public IdentifierFormatException(String ident, String message)
        {
        this(
            ident,
            message,
            null
            );
        }

    public IdentifierFormatException(String ident, Throwable cause)
        {
        this(
            ident,
            message(
                ident
                ),
            cause
            );
        }

    public IdentifierFormatException(String ident, String message, Throwable cause)
        {
        super(
            message,
            cause
            );
        this.ident = ident ;
        }

    private String ident;

    public String ident()
        {
        return this.ident;
        }
    }

