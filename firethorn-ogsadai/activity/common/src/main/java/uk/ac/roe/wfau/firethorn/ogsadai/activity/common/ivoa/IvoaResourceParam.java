/**
 * Copyright (c) 2014, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.common.ivoa;

/**
 * Common interface for IvoaResource activities.
 *
 */
public interface IvoaResourceParam
    {
    /**
     * Activity input name for the TAP endpoint URL, {@value}.
     * 
     */
    public static final String IVOA_TAP_ENDPOINT = "ivoa.tap.endpoint"  ;

    /**
     * Activity input name for the UWS service polling interval, {@value}.
     * 
     */
    public static final String IVOA_UWS_INTERVAL = "ivoa.uws.interval"  ;
    
    /**
     * Activity input name for the UWS service polling timeout, {@value}.
     * 
     */
    public static final String IVOA_UWS_TIMEOUT = "ivoa.uws.timeout"  ;

    /**
     * Activity input name for the UWS quickstart flag, {@value}.
     * 
     */
    public static final String IVOA_UWS_QUICKSTART = "ivoa.uws.quickstart"  ;

    }

