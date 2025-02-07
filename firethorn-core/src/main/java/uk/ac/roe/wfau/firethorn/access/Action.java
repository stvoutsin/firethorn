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
package uk.ac.roe.wfau.firethorn.access;

import java.net.URI;

/**
 *
 *
 */
public interface Action
    {
    /**
     * The simple name for this {@link Action}.
     * 
     */
    public String name();

    /**
     * The URI identifier for this {@link Action}.
     * 
     */
    public URI uri();

    /**
     * Core types of {@link Action}.
     *  
     */
    public enum ActionType
        {
        SELECT(false, true),
        UPDATE(true,  true),
        CREATE(false, false),
        DELETE(false, false);

        /**
         * Private constructor. 
         * @param modify True if this {@link Action} modifies something.
         * 
         */
        private ActionType(boolean modify, boolean select)
            {
            this.modify = modify ;
            this.select = select ;
            }

        /**
         * Flag to indicate if this {@link Action} modifies something.
         * 
         */
        private boolean modify;

        /**
         * Flag to indicate if this {@link Action} modifies something.
         * 
         */
        public boolean modify()
            {
            return this.modify;
            }

        /**
         * Flag to indicate if this {@link Action} selects something.
         * 
         */
        private boolean select;

        /**
         * Flag to indicate if this {@link Action} selects something.
         * 
         */
        public boolean select()
            {
            return this.select;
            }

        }

    /**
     * The type of {@link Action}.
     *  
     */
    public ActionType type();

    /**
     * Simple create {@link Action}.
     * 
     */
    public static final Action create = new BaseAction(
        Action.ActionType.CREATE,
        "core:create",
        "create"
        );
    
    /**
     * Simple select (read) {@link Action}.
     * 
     */
    public static final Action select = new BaseAction(
        Action.ActionType.SELECT,
        "core:select",
        "select"
        );

    /**
     * Simple update (write) {@link Action}.
     * 
     */
    public static final Action update = new BaseAction(
        Action.ActionType.UPDATE,
        "core:update",
        "update"
        );

    /**
     * Simple delete {@link Action}.
     * 
     */
    public static final Action delete = new BaseAction(
        Action.ActionType.DELETE,
        "core:delete",
        "delete"
        );
    }
