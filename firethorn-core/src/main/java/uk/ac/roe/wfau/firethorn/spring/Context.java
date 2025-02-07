package uk.ac.roe.wfau.firethorn.spring;

import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.chaos.MonkeyParam;

/**
 * The current context.
 * @todo Move this to identity package ?
 *
 */
public interface Context
    {
    /**
     * The current Operation.
     *
     */
    public Operation oper();

    /**
     * The primary Authentication.
     *
     */
    public Authentication auth();

    /**
     * The primary Identity.
     *
     */
    public Identity identity();

    /**
     * The data storage space.
     * 
    public DataSpace space();
     */

    /**
     * The ChaosMonkey {@link MonkeyParam} for this {@link Context}.
     *
     */
    public MonkeyParam monkey();
    
    /**
     * Factory interface.
     * 
     */
    public interface Factory
        {
        /**
         * Access to the current Context.
         * 
         */
        public Context current();

        }
    }