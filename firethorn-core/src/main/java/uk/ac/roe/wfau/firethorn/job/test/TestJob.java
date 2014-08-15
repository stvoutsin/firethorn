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
package uk.ac.roe.wfau.firethorn.job.test;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.job.Job;

/**
 *
 *
 */
public interface TestJob
extends Job, NamedEntity
    {
    /**
     * Our local factory implementations.
     *
     */
    public static interface Services
        {
        /**
         * Our LinkFactory.
         *
         */
        public LinkFactory links();

        /**
         * Our IdentFactory.
         *
         */
        public IdentFactory idents();

        /**
         * Our TestJob Factory.
         *
         */
        public Factory factory();

        /**
         * Our TestJob executor.
         *
         */
        public Job.Executor executor();

        }

    /**
     * Our local service implementations.
     *
     */
    public Services services();

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<TestJob>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<TestJob>
        {
        }

    /**
     * Job factory interface.
     *
     */
    public static interface Factory
    extends Job.EntityFactory<TestJob>
        {
        /**
         * Create a test Job.
         *
         */
        public TestJob create(final String name, final Integer length);

        }

    /**
     * The test duration in seconds.
     *
     */
    public Integer length();

    /**
     * The test duration in seconds.
     *
     */
    public void length(final Integer pause);

    /**
     * The test limit in seconds.
     *
     */
    public Integer limit();

    /**
     * The test limit in seconds.
     *
     */
    public void limit(final Integer limit);

    /**
     * Something to configure.
     *
     */
    public TestJob.Factory factory();

    }
