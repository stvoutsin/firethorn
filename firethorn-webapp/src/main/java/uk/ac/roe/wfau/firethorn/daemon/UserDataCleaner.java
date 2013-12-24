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
package uk.ac.roe.wfau.firethorn.daemon;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 *
 *
 */
@Slf4j
@Component
public class UserDataCleaner
extends AbstractComponent
    {
    private static final Minutes lifetime = Minutes.minutes(5) ;
    
    private long count = 0L ;
    
    @Scheduled(fixedDelay=(60 * 1000))
    public void something()
        {
        log.debug("");
        log.debug("something()");
        log.debug("  count [{}]", count);

        //
        // Skip the first n iterations.
        if (count++ < 10)
            {
            log.debug("skipping");
            return ;
            }
        
        factories().spring().transactor().update(
            new Runnable()
                {
                @Override
                public void run()
                    {
                    try {
                        final DateTime date = new DateTime().minus(
                            lifetime
                            );
                        log.debug("  date [{}]", date);
                        final JdbcResource resource = factories().jdbc().resources().userdata();
                        log.debug("  resource [{}][{}]", resource.ident(), resource.name());
            
                        for (JdbcSchema schema : resource.schemas().select())
                            {
                            log.debug("  schema [{}][{}]", schema.ident(), schema.name());
                            for (JdbcTable table : schema.tables().pending(date))
                                {
                                log.debug("  table [{}][{}][{}]", table.ident(), table.name(), table.created());
                                //table.drop();
                                table.meta().jdbc().status(JdbcTable.JdbcStatus.DROPPED);
                                }
                            }
                        }
                    catch (final Exception ouch)
                        {
                        log.warn("Exception in something() [{}]", ouch.getMessage());
                        }
                    }
                }
            );
        log.debug("");
        }   
    }
