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
package uk.ac.roe.wfau.firethorn.widgeon ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import uk.ac.roe.wfau.firethorn.test.TestBase;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;


/**
 *
 */
@Slf4j
public class WidgeonViewTestCase
extends TestBase
    {

    private Widgeon.Base base ;

    @Before
    public void before()
    throws Exception
        {
        base = womble().widgeons().create(
            this.unique(
                "base"
                ),
            URI.create(
                "ivo://org.astrogrid.test/test-data"
                )
            );
        }

/*
    @Test
    public void test000()
    throws Exception
        {
        try {
            base.views().select(
                "view-A"
                );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "view-A",
                ouch.name()
                );            
            }
        }

    @Test
    public void test001()
    throws Exception
        {
        assertNotNull(
            base.views().create(
                "view-A"
                )
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        assertNotNull(
            base.views().create(
                "view-A"
                )
            );
        assertNotNull(
            base.views().select(
                "view-A"
                )
            );
        }

    @Test
    public void test003()
    throws Exception
        {
        assertNotNull(
            base.views().create(
                "view-A"
                )
            );
        try {
            base.views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        }
 */
    @Test
    public void test004()
    throws Exception
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                )
            );
        assertNotNull(
            base.views().create(
                "view-A"
                )
            );
        assertNotNull(
            base.schemas().create(
                "schema-B"
                )
            );
        assertNotNull(
            base.views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    )
            );
        }

/*

    @Test
    public void test002()
    throws Exception
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                )
            );
        }

    @Test
    public void test003()
    throws Exception
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                )
            );
        assertNotNull(
            base.schemas().select(
                "schema-A"
                )
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                )
            );
        try {
            base.schemas().select(
                "schema-A"
                ).catalogs().select(
                    "catalog-A"
                    );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }
        }

    @Test
    public void test005()
    throws Exception
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                ).catalogs().create(
                    "catalog-A"
                    )
            );

        assertNotNull(
            base.schemas().select(
                "schema-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        }

    @Test
    public void test006()
    throws Exception
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                ).catalogs().create(
                    "catalog-A"
                    )
            );
        try {
            base.schemas().select(
                "schema-A"
                ).catalogs().select(
                    "catalog-A"
                    ).tables().select(
                        "table-A"
                        );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "table-A",
                ouch.name()
                );            
            }
        }

    @Test
    public void test007()
    throws Exception
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                ).catalogs().create(
                    "catalog-A"
                    ).tables().create(
                        "table-A"
                        )
            );

        assertNotNull(
            base.schemas().select(
                "schema-A"
                ).catalogs().select(
                    "catalog-A"
                    ).tables().select(
                        "table-A"
                        )
            );
        }

    @Test
    public void test008()
    throws Exception
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                ).catalogs().create(
                    "catalog-A"
                    ).tables().create(
                        "table-A"
                        )
            );
        try {
            base.schemas().select(
                "schema-A"
                ).catalogs().select(
                    "catalog-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-A"
                            );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "column-A",
                ouch.name()
                );            
            }
        }

    @Test
    public void test009()
    throws Exception
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                ).catalogs().create(
                    "catalog-A"
                    ).tables().create(
                        "table-A"
                        ).columns().create(
                            "column-A"
                            )
            );

        assertNotNull(
            base.schemas().select(
                "schema-A"
                ).catalogs().select(
                    "catalog-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-A"
                            )
            );
        }

    @Test
    public void test010()
    throws Exception
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                ).catalogs().create(
                    "catalog-A"
                    ).tables().create(
                        "table-A"
                        ).columns().create(
                            "column-A"
                            )
            );

        try {
            base.schemas().select(
                "schema-A"
                ).catalogs().select(
                    "catalog-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-a"
                            );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "column-a",
                ouch.name()
                );            
            }
        }

    @Test
    public void test011()
    throws Exception
        {
        assertNotNull(
            base.schemas().create(
                "schema-A"
                ).catalogs().create(
                    "catalog-A"
                    ).tables().create(
                        "table-A"
                        ).columns().create(
                            "column-A"
                            )
            );

        try {
            base.schemas().select(
                "schema-A"
                ).catalogs().select(
                    "catalog-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-a"
                            );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "column-a",
                ouch.name()
                );            
            }

        base.schemas().select(
            "schema-A"
            ).catalogs().select(
                "catalog-A"
                ).tables().select(
                    "table-A"
                    ).columns().select(
                        "column-A"
                        ).name(
                            "column-a"
                            );

        assertNotNull(
            base.schemas().select(
                "schema-A"
                ).catalogs().select(
                    "catalog-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-a"
                            )
            );

        try {
            base.schemas().select(
                "schema-A"
                ).catalogs().select(
                    "catalog-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-A"
                            );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "column-A",
                ouch.name()
                );            
            }
        }
 */
    }

