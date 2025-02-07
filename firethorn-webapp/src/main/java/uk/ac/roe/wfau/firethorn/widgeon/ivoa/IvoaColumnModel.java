/*
 *  Copyright (C) 2018 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.widgeon.ivoa;

public interface IvoaColumnModel
    {
    /**
     * MVC property for the {@link IvoaColumn} {@link Identifier}.
     *
     */
    public static final String COLUMN_IDENT_PARAM = "ivoa.column.ident" ;

    /**
     * MVC property for the {@link IvoaColumn} name.
     *
     */
    public static final String COLUMN_NAME_PARAM = "ivoa.column.name" ;

    }
