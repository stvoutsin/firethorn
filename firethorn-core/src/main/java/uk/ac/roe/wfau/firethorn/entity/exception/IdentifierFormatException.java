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
package uk.ac.roe.wfau.firethorn.entity.exception ;

/**
 *
 *
 */
public class IdentifierFormatException
extends InputFormatException
    {
    /**
     * Serialzable version UID.
     *
     */
    private static final long serialVersionUID = -7604911350682400120L;

    /**
     * Default message for simple constructor.
     *
     */
    public static final String DEFAULT_MESSAGE = "Identifier syntax error [:ident:]" ;

    /**
     * Create a default message.
     *
     */
    public static String message(final String ident)
        {
        return DEFAULT_MESSAGE.replace(":ident:", ident);
        }

    public IdentifierFormatException(final String ident)
        {
        this(
            ident,
            message(
                ident
                )
            );
        }

    public IdentifierFormatException(final String ident, final String message)
        {
        super(
            ident,
            message
            );
        }

    public IdentifierFormatException(final String ident, final Throwable cause)
        {
        this(
            ident,
            message(
                ident
                ),
            cause
            );
        }

    public IdentifierFormatException(final String ident, final String message, final Throwable cause)
        {
        super(
            ident,
            message,
            cause
            );
        }
    }

