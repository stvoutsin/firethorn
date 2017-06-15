/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.query.blue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.annotations.NamedQueries;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.Handle.Listener;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTaskLogEntry.Level;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateConvertException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.util.EmptyIterable;

/**
 * {@link BlueTask} implementation. 
 *
 */
@Slf4j
@Entity()
@Access(
   AccessType.FIELD
   )
@Table(
   name = BlueTaskEntity.DB_TABLE_NAME
   )
@Inheritance(
   strategy = InheritanceType.JOINED
   )
@NamedQueries(
       {
       }
   )
public abstract class BlueTaskEntity<TaskType extends BlueTask<?>>
extends AbstractNamedEntity
implements BlueTask<TaskType>
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "BlueTaskEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_STATE_COL = "state";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_QUEUED_COL = "queued";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_STARTED_COL = "started";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_COMPLETED_COL = "completed";

    /**
     * {@link BlueTask.Services} implementation.
     * 
    @Service
    public static abstract class Services<TaskType extends BlueTask<?>>
        implements BlueTask.Services<TaskType>
        {
        }
     */

    /**
     * Our {@link BlueTask.Services} instance.
     *
    protected abstract BlueTaskEntity.Services<TaskType> services();
     */

    /**
     * {@link BlueTask.EntityFactory} implementation.
     * 
     */
    @Slf4j
    @Repository
    public abstract static class EntityFactory<TaskType extends BlueTask<?>>
        extends AbstractEntityFactory<TaskType>
        implements BlueTask.EntityFactory<TaskType>
        {

        @Override
        @UpdateAtomicMethod
        public TaskType advance(final Identifier ident, final TaskState prev, final TaskState next, long wait)
        throws IdentifierNotFoundException, InvalidStateRequestException
            {
            log.debug("advance(Identifier, TaskState, TaskState, long)");
            log.debug("  ident [{}]", ident);
            log.debug("  prev  [{}]", prev);
            log.debug("  next  [{}]", next);
            log.debug("  wait  [{}]", wait);
            TaskType task = select(
        		ident
            	);
            task.advance(
        		prev,
        		next,
        		wait
        		); 
            return task ;
            }
        }

    /**
     * Our {@link BlueTask.EntityFactory} instance.
     * 
     *
    @Override
    protected abstract BlueTask.EntityFactory<TaskType> factory();
     */

    /**
     * Base class for {@link BlueTaskEntity} task runners.
     * 
     */
    @Slf4j
    @Component
    public static class TaskRunner<TaskType extends BlueTask<?>>
    implements BlueTask.TaskRunner<TaskType>
        {
        
        @Autowired
        private BlueTask.EntityServices<TaskType> services ;
        protected BlueTask.EntityServices<TaskType> services()
        	{
        	return this.services;
        	}

        @Override
        @UpdateAtomicMethod
        public TaskState thread(final Updator<?> updator)
            {
            log.debug("update(Updator)");
            log.debug("  ident [{}]", updator.ident());
            log.debug("  thread [{}][{}]", Thread.currentThread().getId(), Thread.currentThread().getName());

            log.debug("Before execute()");
            final Future<TaskState> future = services.runner().future(
                updator
                );
            log.debug("After execute()");
            
            try {
                log.debug("Before future()");
                final TaskState result = future.get();
                log.debug("After future()");
                log.debug("  result [{}]", result);
                return result ;
                }
//TODO Much better error handling.
            catch (ExecutionException ouch)
                {
                final Throwable cause = ouch.getCause();
                log.error("ExecutionException executing Future [{}][{}]", cause.getClass().getName(), cause.getMessage());
                log.debug("ExecutionException executing Future", cause);

                return TaskState.ERROR;
                }
            catch (InterruptedException ouch)
            	{
                log.error("Interrupted waiting for Future [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                return TaskState.ERROR;
            	}
            }

        @Async
        @Override
        @UpdateAtomicMethod
        public Future<TaskState> future(final Updator<?> updator)
            {
            log.debug("future(Updator)");
            log.debug("  ident [{}]", updator.ident());
            log.debug("  thread [{}][{}]", Thread.currentThread().getId(), Thread.currentThread().getName());
            return new AsyncResult<TaskState>(
                updator.update()
                );
            }

        @Override
        @UpdateAtomicMethod
        public TaskType thread(final Creator<TaskType> creator)
        throws InvalidStateTransitionException
            {
            log.debug("thread(Creator)");
            log.debug("  thread [{}][{}]", Thread.currentThread().getId(), Thread.currentThread().getName());
            
            log.debug("Before future()");
            final Future<TaskType> future = services.runner().future(
                creator
                );
            log.debug("After future()");
            log.debug("  thread [{}][{}]", Thread.currentThread().getId(), Thread.currentThread().getName());
            
            try {
                log.debug("Before future.get()");
                final TaskType result = future.get();
                log.debug("After future.get()");
// Easier to do the convert back in the calling Thread.
                //log.debug("  initial [{}]", initial);
                // Convert the initial result to the current thread/session
            	//final TaskType result = (TaskType) initial.current();
                //log.debug("After select()");
                log.debug("  result [{}]", result);
                return result ;
                }
// TODO Much better error handling
            catch (final ExecutionException ouch)
                {
                final Throwable cause = ouch.getCause();
                log.error("ExecutionException executing Creator [{}][{}]", cause.getClass().getName(), cause.getMessage());
				if (cause instanceof InvalidStateTransitionException)
					{
					throw (InvalidStateTransitionException) cause ;
					}
				else {
				    // TODO Creator shouldn't return null ..
					return null;
					}
                }
            catch (final InterruptedException ouch)
        	    {
                log.error("Interrupted waiting for Creator [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                // TODO Creator shouldn't return null ..
                return null;
        	    }
/*
 * 
            catch (final HibernateConvertException ouch)
	    	    {
	            log.error("HibernateConvertException [{}]");
	            return null;
	    	    }
 * 
 */
            }

        @Async
        @Override
        @UpdateAtomicMethod
        public Future<TaskType> future(final Creator<TaskType> creator)
            {
            log.debug("future(Creator)");
            log.debug("  thread [{}][{}]", Thread.currentThread().getId(), Thread.currentThread().getName());
            // TODO Much better error handling
            try {
				return new AsyncResult<TaskType>(
				    creator.create()
				    );
				}
            catch (final InvalidStateTransitionException ouch)
            	{
                // TODO Much better error handling
                // TODO Needs Spring 4.2
            	return null ;
            	}
            catch (final HibernateConvertException ouch)
            	{
                // TODO Much better error handling
                // TODO Needs Spring 4.2
            	return null ;
            	}
            /*
             * 
            // Needs Spring 4.2
            try {
            	return AsyncResult.forValue(
                    creator.create()
        			);
            	}
            catch(final Throwable ouch)
            	{
            	return AsyncResult.forExecutionException(
                        ouch
            			);
            	}
 * 
 */
            }
        }

    /**
     * Our {@link BlueTaskEntity.TaskRunner} instance.
     * 
    protected abstract BlueTask.TaskRunner<TaskType> runner();
     */

    /**
     * {@link Updator} base class.
     * 
     */
    public abstract static class Creator<TaskType extends BlueTask<?>>
    implements TaskRunner.Creator<TaskType>
        {
        }
    
    /**
     * {@link Updator} base class.
     * 
     */
    public abstract static class Updator<TaskType extends BlueTask<?>>
    implements TaskRunner.Updator<TaskType>
        {
        /**
         * Our initial {@link BlueTask} entity.
         * 
         */
        private TaskType initial;
        
        /**
         * Protected constructor.
         *
         */
        protected Updator(final TaskType initial)
            {
        	this.initial = initial;
            }

        @Override
        public Identifier ident()
            {
            return initial.ident();
            }
        }

    // TODO Move this to base class
    protected void refresh()
    	{
        log.debug("Refreshing entity [{}]", ident());
        factories().hibernate().refresh(
    		this
    		);    	
    	}

    protected abstract BlueTask.TaskRunner<TaskType> runner();
    protected abstract BlueTask.EntityFactory<TaskType>  factory();
    protected abstract BlueTask.EntityServices<TaskType> services();

    /**
     * Protected constructor.
     * 
     */
    protected BlueTaskEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     * @param owner 
     * 
     */
    protected BlueTaskEntity(final Identity owner)
        {
        this(
            owner,
            null
            );
        }

    /**
     * Protected constructor.
     * @param owner 
     * 
     */
    protected BlueTaskEntity(final Identity owner, final String name)
        {
        super(
    		owner,
    		name
            );
        this.state = TaskState.EDITING;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Enumerated(
        EnumType.STRING
        )
    @Column(
        name = DB_STATE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private TaskState state;

    @Override
    public TaskState state()
        {
        return this.state;
        }

    @Column(
        name = DB_QUEUED_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime queued ;
    @Override
    public DateTime queued()
        {
        return this.queued ;
        }

    @Column(
        name = DB_STARTED_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime started ;
    @Override
    public DateTime started()
        {
        return this.started ;
        }

    @Column(
        name = DB_COMPLETED_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime completed ;
    @Override
    public DateTime completed()
        {
        return this.completed ;
        }

    /**
     * Prepare our task.
     * 
     */
    protected abstract void prepare()
    throws InvalidStateTransitionException;

    /**
     * Execute our task.
     * 
     */
    protected abstract void execute()
    throws InvalidStateTransitionException;

    /**
     * {@link BlueTask.Handle} implementation.
     * 
     */
    protected static class Handle
    implements BlueTask.Handle
        {
        /**
         * Protected constructor.
         * 
         */
        protected Handle(final BlueTask<?> task)
            {
            this.state = task.state();
            this.ident = task.ident().toString();
            }

        private boolean active = false ;
        public boolean active()
            {
            return this.active;
            }
        
        private String ident;
        @Override
        public String ident()
            {
            return this.ident;
            }

        private TaskState state;
        @Override
        public TaskState state()
            {
            return this.state;
            }

        @Override
        public synchronized void event(final TaskState next)
            {
            this.event(
                next,
                false
                );
            }
        
        @Override
        public synchronized void event(final TaskState next, final boolean activate)
            {
            log.debug("event(TaskState, boolean)");
            log.debug("  state  [{}][{}]", this.state,  next);
            log.debug("  active [{}][{}]", this.active, activate);

            this.state = next;
            this.active |= activate ;

            log.debug("notify start");
            this.notifyAll();
            log.debug("notify done");

// Two states, controller and processor.
// Processor updates via callback.
// Controller sets state directly.
// Processor callback releases handle IF controller has completed its processing.

// Controller sets flag to indicate handle can be released.
            
// Controller sets the state to sending
// Controller sends the request
// Callback might happen, running, completed or error.
// Controller checks state and IF still active, updates state to running.
// Controller releases handle IF no longer active..

            
            
            log.debug("-- diHohj8a Reez1OeY --");
            log.debug("Checking Handler activation");
            log.debug("  active [{}]", this.active);
            if (this.active)
                {
                log.debug("Handler is active, checking TaskState ");
                log.debug("  state [{}]", this.state);
                if (this.state.active())
                    {
                    log.debug("TaskState is active, keeping Handler");
                    }
                else {
                    log.debug("TaskState is inactive, removing Handler");
                    handles.remove(
                        this.ident
                        );
                    }
                }
            else {
                log.debug("Handler has not been activated yet");
                }
            }
        }
        
    /**
     * Base class for {@link Listener}. 
     *
     */
    public static abstract class BaseEventListener
    implements BlueTask.Handle.Listener
        {
        /**
         * The default timeout.
         * TODO Make this configurable.
         * TODO Provide settings for default and absolute values.
         *  
         */
        private static final long DEFAULT_TIMEOUT = 5000 ;

    	public BaseEventListener()
            {
        	this(
        		DEFAULT_TIMEOUT
        		);
            }

    	public BaseEventListener(long timeout)
            {
        	this.timeout = timeout;
            }

        protected long count = 0 ;
        protected long count()
            {
            return this.count;
            }

        @Override
        public void waitfor(final BlueTask.Handle handle)
            {
            log.debug("waitfor(Handle)");
            log.debug("  state [{}]", handle.state());

            synchronized(handle)
                {
				if (handle.state().active())
    				{
                	while (this.done(handle) == false)
                        {
                        try {
                            this.count++;
                            long time = remaining();
                            if (time == Long.MAX_VALUE)
                                {
                                log.debug("wait start []");
                                handle.wait();
                                }
                            else {
                                log.debug("wait start [{}]", time);
                                handle.wait(
                                    time
                                    );
                                }
                            log.debug("wait done");
                            }
                        catch (Exception ouch)
                            {
                            log.debug("Exception during wait [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                            }
                        }
                    }
                }
            }
        
        private long started = System.currentTimeMillis();
        protected long started()
            {
            return started ;
            }
        
        private long timeout ;
        protected long timeout()
        	{
        	return this.timeout;
        	}

        protected long elapsed()
            {
            return System.currentTimeMillis() - started ;
            }

        protected long remaining()
            {
            if (timeout() == Long.MAX_VALUE)
                {
                return Long.MAX_VALUE ;
                }
            else {
                return timeout() - elapsed();
                }
            }

        protected boolean done(final BlueTask.Handle handle)
            {
            log.debug("done()");
            log.debug("  elapsed [{}]", elapsed());
            log.debug("  timeout [{}]", timeout());
            if (timeout() == Long.MAX_VALUE)
                {
                return false ;
                }
            else {
                if (elapsed() >= timeout())
                	{
                	log.debug("done (elapsed >= timeout)");
                	return true ;
                	}
                else {
                	return false ;
                	}
                }
            }
        }

    public static class AnyEventListener
    extends BaseEventListener
        {
    	public AnyEventListener(long timeout)
            {
            super(
        		timeout
        		);
            }

        @Override
        public boolean done(final BlueTask.Handle handle)
            {
            log.debug("done()");
            log.debug("  count [{}]", count);
            // Skip the first test.
        	if (count != 0)
        		{
            	log.debug("done (count != 0)");
        		return true ;
        		}
            // Check the timeout.
        	else {
        		return super.done(
    				handle
    				);
        		}
            }
        }

    public static class StatusEventListener
    extends BaseEventListener
        {
        public StatusEventListener(final Handle handle, final TaskState next, long timeout)
            {
            this(
                handle.state(),
                next,
    			timeout
                );
            }
       
        public StatusEventListener(final TaskState prev, final TaskState next, long timeout)
            {
            super(
        		timeout
            	);
            this.prev = prev;
            this.next = next;
            }

        protected TaskState prev; 
        protected TaskState next; 

        @Override
        protected boolean done(final BlueTask.Handle handle)
            {
            log.debug("done()");
            log.debug("  prev  [{}]", prev);
            log.debug("  state [{}]", handle.state());
            log.debug("  next  [{}]", next);
            // If the current state is not active
            if (handle.state().active() == false)
        		{
            	log.debug("done - handle state is not active");
        		return true ;
            	}
            // If the state has changed.
            if ((prev != null) && (handle.state() != prev))
        		{
            	log.debug("done - prev state has changed");
        		return true ;
            	}
            // If the next state has been reached. 
            if ((next != null) && (handle.state().ordinal() >= next.ordinal()))
        		{
            	log.debug("done - next state reached");
        		return true ;
            	}
            // Check the timeout.
            else {
            	return super.done(
            		handle
            		);
            	}
            }
        }
    
    /**
     * Our map of active {@link Handle}s.
     * 
     */
    protected static Map<String, BlueTaskEntity.Handle> handles = new HashMap<String, BlueTaskEntity.Handle>();

    /**
     * Resolve an active {@link Handle}.
     * 
     */
    public static Handle handle(final Identifier ident)
        {
        return handle(
            ident.toString()
            );
        }

    /**
     * Resolve an active {@link Handle}.
     * 
     */
    public static Handle handle(final String key)
        {
        log.debug("handle(String)");
        log.debug("  key [{}]", key);
        return handles.get(
            key
            );
        }

    /**
     * Create a {@link Handle} for this task.
     * 
     */
    protected Handle newhandle()
        {
        log.debug("newhandle()");
        return new Handle(
            this
            );
        }

    @Override
    public Handle handle()
        {
        log.debug("handle()");
        log.debug("  ident [{}]", ident());
        if (ident() != null)
            {
            synchronized (handles)
                {
                final String key = ident().toString();
                final Handle found = handle(key);
                if (found != null)
                    {
                    log.debug("Found existing Handle [{}]", key);
                    return found;
                    }
                else {
                    // Only create a new handle if we are active.
					if (state().active())
						{
	                	log.debug("State is active - Creating new Handle [{}]", key);
	                    final Handle created = newhandle();
	                    handles.put(
	                        created.ident,
	                        created
	                        );
	                    return created;
						}
					else {
	                	log.debug("State is not active - no handle");
	                    return null ;
						}
                    }
                }
            }
        //
        // If created but not saved, ident is null.
        else {
        	log.error("Ident is null - no handle");
            return null ;
            }
        }

    /**
     * Internal state machine transitions.
     * 
     */
    protected void transition(final TaskState next)
    throws InvalidStateTransitionException
        {
        final TaskState prev = this.state;
        log.debug("transition(TaskState)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", prev.name(), (next != null) ? next.name() : null);

        if (next == null)
            {
            log.debug("Null TaskState, no change");
            }
        else {
            if (prev == next)
                {
                log.debug("No-op status change [{}]", next);
                }
    
            else if (prev == TaskState.EDITING)
                {
                switch (next)
                    {
                    case READY :
                        accept(next);
                        break ;
    
                    case CANCELLED:
                    case FAILED:
                    case ERROR:
                        accept(next);
                        break ;
    
                    default :
                        invalid(prev, next);
                    }
                }
            
            else if (prev == TaskState.READY)
                {
                switch (next)
                    {
                    case EDITING:
                        accept(next);
                        break ;
    
                    case QUEUED:
                    case SENDING:
                    case RUNNING:
                    case COMPLETED:
                        accept(next);
                        break ;

                    case CANCELLED:
                    case FAILED:
                    case ERROR:
                        accept(next);
                        break ;
        
                    default :
                        invalid(prev, next);
                    }
                }
    
            else if (prev == TaskState.QUEUED)
                {
                switch (next)
                    {
                    case SENDING:
                    case RUNNING:
                    case COMPLETED:
                        accept(next);
                        break ;
        
                    case CANCELLED:
                    case FAILED:
                    case ERROR:
                        accept(next);
                        break ;
        
                    default :
                        invalid(prev, next);
                    }
                }

            else if (prev == TaskState.SENDING)
                {
                switch (next)
                    {
                    case RUNNING:
                    case COMPLETED:
                        accept(next);
                        break ;
        
                    case CANCELLED:
                    case FAILED:
                    case ERROR:
                        accept(next);
                        break ;
        
                    default :
                        invalid(prev, next);
                    }
                }

            else if (prev == TaskState.RUNNING)
                {
                switch (next)
                    {
                    case COMPLETED:
                        accept(next);
                        break ;
        
                    case CANCELLED:
                    case FAILED:
                    case ERROR:
                        accept(next);
                        break ;
        
                    default :
                        invalid(prev, next);
                    }
                }
    
            else {
                invalid(prev, next);
                }
            }
        }

    /**
     * Accept a valid state transition.
     * 
     */
    protected void accept(final TaskState next)
        {
        log.debug("accept(TaskState)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", state().name(), next.name());
        this.state = next ;
        }

    /**
     * Reject an invalid state transition.
     * 
     */
    private void invalid(final TaskState prev, final TaskState next)
    throws InvalidStateTransitionException
        {
        // this.state = TaskState.ERROR;
        // TODO Should we throw an Exception, or just ignore the transition ?
        throw new InvalidStateTransitionException(
            this,
            prev,
            next
            );
        }
    
    @Override
    public void advance(final TaskState prev, final TaskState next, final Long wait)
    throws InvalidStateRequestException
        {
        log.debug("advance(TaskState, TaskState, Long)");
        log.debug("  ident [{}]", ident());
        log.debug("  prev  [{}]", prev);
        log.debug("  curr  [{}]", this.state());
        log.debug("  next  [{}]", next);
        log.debug("  wait  [{}]", wait);

        final TaskState current = this.state(); 
        if (current == next)
            {
            log.debug("No-op status change [{}][{}]", current, next);
            }

        else if (current == TaskState.EDITING)
            {
            switch (next)
                {
                case READY :
                    ready();
                    break ;

                case SENDING:
                    break ;

                case RUNNING:
                case COMPLETED:
                    running();
                    break ;

                case CANCELLED:
                    finish(next);
                    break ;

                default :
                    reject(prev, next);
                    break;
                }
            }
        
        else if (current == TaskState.READY)
            {
            switch (next)
                {
                case SENDING:
                    break ;

                case RUNNING:
                case COMPLETED:
                    running();
                    break ;

                case CANCELLED:
                    finish(next);
                    break ;

                default :
                    reject(prev, next);
                }
            }

        else if (current == TaskState.QUEUED)
            {
            switch (next)
                {
                case SENDING:
                case COMPLETED:
                    break ;

                case CANCELLED:
                    finish(next);
                    break ;
    
                default :
                    reject(prev, next);
                }
            }

        else if (current == TaskState.SENDING)
            {
            switch (next)
                {
                case COMPLETED:
                    break ;

                case CANCELLED:
                    finish(next);
                    break ;
    
                default :
                    reject(prev, next);
                }
            }

        else if (current == TaskState.RUNNING)
            {
            switch (next)
                {
                case COMPLETED:
                    break ;

                case CANCELLED:
                    finish(next);
                    break ;
    
                default :
                    reject(prev, next);
                }
            }

        else {
            reject(current, next);
            }
        //
        // Wait for the next state change.
// BUG This keeps a write transaction open for the duration of the wait.
// This should be outside the transaction.
/*
 * 
        this.waitfor(
    		prev,
    		next,
    		wait
    		);
        //
        // Update this instance with the result of the wait.
        this.refresh();
 * 
 */
        }

    /**
     * Update our Handle and notify any Listeners.
     * 
     */
    protected void event()
    	{
    	this.event(
    		true
            );
    	}

    /**
     * Update our Handle and notify any Listeners.
     * 
     */
    protected void event(boolean active)
    	{
        final Handle handle = this.handle();
        if (handle != null)
            {
            handle.event(
        		this.state,
                active
                );
            }
    	}

    @Override
    public void waitfor(final TaskState prev, final TaskState next, final Long wait)
    	{
        log.debug("waitfor(TaskState, Long)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state());
        log.debug("  prev  [{}]", prev);
        log.debug("  next  [{}]", next);
        log.debug("  wait  [{}]", wait);

        if (wait == null)
            {
            log.debug("Wait is null - skipping wait");
            }
        else if (wait <= 0)
			{
			// TODO Do we need an absolute max value of -1 for sync TAP requests ?
			log.debug("Wait is zero - skipping wait");
			}
		else {
			if (this.state().active())
				{
	            log.debug("State is active - getting handle");
	            final BlueTaskEntity.Handle handle = handle();
	            if (handle != null)
	            	{
		            log.debug("  ident [{}]", handle.ident());
		            log.debug("  state [{}]", handle.state());
		
		            log.debug("Before listener.waitfor()");
		            log.debug("  ident [{}]", this.ident());
		            log.debug("  ident [{}]", handle.ident());
		            log.debug("  state [{}]", this.state());
		            log.debug("  state [{}]", handle.state());

		            //
		            // Wait for a state change event.
					if ((prev != null) || (next != null))
						{
			            log.debug("Waiting for state change event");
			            log.debug("  prev  [{}]", prev);
			            log.debug("  next  [{}]", next);
					    final Handle.Listener listener = new StatusEventListener(
							prev,
							next,
							wait
					        );
					    listener.waitfor(
							handle
							);
						}
		            //
		            // Wait any event.
					else {
		            	log.debug("Waiting for any event");
					    final Handle.Listener listener = new AnyEventListener(
							wait
					        );
					    listener.waitfor(
							handle
							);
						}

					log.debug("After listener.waitfor()");
		            log.debug("  ident [{}]", this.ident());
		            log.debug("  ident [{}]", handle.ident());
		            log.debug("  prev  [{}]", prev);
		            log.debug("  state [{}]", this.state());
		            log.debug("  state [{}]", handle.state());
		            log.debug("  next  [{}]", next);
/*
 * Already done in advance() anyway ..		
		            //
		            // Update our entity from the DB.
		            log.debug("Before refresh()");
		            log.debug("  ident [{}]", this.ident());
		            log.debug("  state [{}]", this.state());
		            refresh();
		            log.debug("After refresh()");
		            log.debug("  ident [{}]", this.ident());
		            log.debug("  state [{}]", this.state());
*/
    				}
	            else {
	                log.debug("Null handle - skipping wait");
	            	}
				}
			else {
				log.debug("State is not active - skipping wait");
				}
			}
    	}
    
    /**
     * Prepare our task.
     * Calling {@link #prepare()} in a new {@link Thread} performs the operation in a separate Hibernate {@link Session}.
     * 
     */
    protected void ready()
    	{
        log.debug("Starting ready()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state().name());
        services().runner().thread(
            new Updator<BlueTask<?>>(this)
                {
                @Override
                public TaskState update()
                    {
                	try {
	                    BlueTaskEntity<?> task = (BlueTaskEntity<?>) rebase();
	                    log.debug("Before prepare()");
	                    log.debug("  state [{}]", task.state().name());
	                    task.prepare();
	                    log.debug("After prepare()");
	                    log.debug("  state [{}]", task.state().name());
	                    return task.state();
                    	}
                	catch (final InvalidStateTransitionException ouch)
    	    	    	{
	    	            log.error("InvalidStateTransitionException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    catch (HibernateConvertException ouch)
	    	    	    {
	    	            log.error("HibernateConvertException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    }
                }
            );
        log.debug("Finished thread()");
        log.debug("  state [{}]", state().name());

        log.debug("Refreshing state");
        this.refresh();

        log.debug("Finished ready()");
        log.debug("  state [{}]", state().name());
    	}

    /**
     * Run our task in a new {@link Thread}.
     * 
     */
    protected void running()
        {
        log.debug("Starting running()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state().name());
        services().runner().thread(
            new Updator<BlueTask<?>>(this)
                {
                @Override
                public TaskState update()
                    {
                	try {
	                    BlueTaskEntity<?> task = (BlueTaskEntity<?>) rebase();
	                    log.debug("Before prepare()");
	                    log.debug("  state [{}]", task.state().name());
	                    task.prepare();
	                    log.debug("After prepare()");
	                    log.debug("  state [{}]", task.state().name());
	                    //
	                    // If the task is ready.
	                    if (task.state() == TaskState.READY)
	                        {
	                        log.debug("Before execute()");
	                        log.debug("  state [{}]", task.state().name());
	                        task.execute();
	                        log.debug("After execute()");
	                        log.debug("  state [{}]", task.state().name());
	                        }
	                    //
	                    // If the task is not ready.
	                    else {
	                    	log.debug("Task is not READY, execute FAILED");
	                        task.transition(
                                TaskState.FAILED
                                );
	                    	}
	                    return task.state();
	                    }
                	catch (final InvalidStateTransitionException ouch)
    	    	    	{
	    	            log.error("InvalidStateTransitionException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    catch (final HibernateConvertException ouch)
	    	    	    {
	    	            log.error("HibernateConvertException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    }
                }
            );
        log.debug("Finished thread()");
        log.debug("  state [{}]", state().name());

        log.debug("Refreshing state");
        this.refresh();
        
        log.debug("Finished running()");
        log.debug("  state [{}]", state().name());
        }

    /**
     * Finish our task.
     * 
     */
    protected void finish(final TaskState next)
        {
        log.debug("Starting finish()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", state().name(), next.name());
        services().runner().thread(
            new Updator<BlueTask<?>>(this)
                {
                @Override
                public TaskState update()
                    {
                	try {
	                    BlueTaskEntity<?> task = (BlueTaskEntity<?>) rebase();
	                    log.debug("Before change()");
	                    log.debug("  state [{}]", task.state().name());
	                    task.transition(
	                        next
	                        );
	                    log.debug("After change()");
	                    log.debug("  state [{}]", task.state().name());
	                    return task.state();
	                    }
                	catch (final InvalidStateTransitionException ouch)
    	    	    	{
	    	            log.error("InvalidStateTransitionException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    catch (HibernateConvertException ouch)
	    	    	    {
	    	            log.error("HibernateConvertException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    }
                }
            );
        log.debug("Finished thread()");
        log.debug("  state [{}]", state().name());
// TODO we want to merge existing changes 
// TODO we don't want to loose any other changes.
        log.debug("Refreshing state");
        this.refresh();
        
        log.debug("Finished finish()");
        log.debug("  state [{}]", state().name());
        }

    /**
     * Reject an invalid status change.
     * 
     */
    private void reject(final TaskState prev, final TaskState next)
    throws InvalidStateRequestException
        {
        log.warn("Invalid status change [{}][{}]", prev.name(), next.name());
        throw new InvalidStateRequestException(
    		this,
    		prev,
    		next
    		);
        }

    /**
     * Hibernate embedded map of Strings.
     * http://stackoverflow.com/questions/3393649/storing-a-mapstring-string-using-jpa
     *
     */
    @ElementCollection(
        fetch = FetchType.EAGER
        )
    @MapKeyColumn(
        name="name"
        )
    @Column(
        name="value"
        )
    @CollectionTable(
        name=DB_TABLE_PREFIX + "BlueTaskParam",
        joinColumns= @JoinColumn(name="task")
        )
    private Map<String, String> params = new HashMap<String, String>();
    
    @Override
    public BlueTask.Param param()
        {
        return new BlueTask.Param()
            {
            @Override
            public Map<String, String> map()
                {
                return BlueTaskEntity.this.params;
                }
            };
        }

    @Override
    public History history()
        {
        return new History()
            {
            /*
             * 
                @Override
                public BlueTaskLogEntry create(Level level, String message)
                    {
                    // TODO Auto-generated method stub
                    return null;
                    }

                @Override
                public BlueTaskLogEntry create(uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState state,
                        Level level, String message)
                    {
                    // TODO Auto-generated method stub
                    return null;
                    }

                @Override
                public BlueTaskLogEntry create(Object source, Level level, String message)
                    {
                    // TODO Auto-generated method stub
                    return null;
                    }

                @Override
                public BlueTaskLogEntry create(Object source,
                        uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState state, Level level, String message)
                    {
                    // TODO Auto-generated method stub
                    return null;
                    }

                @Override
                public Iterable<BlueTaskLogEntry> select()
                    {
                    // TODO Auto-generated method stub
                    return new EmptyIterable<BlueTaskLogEntry>();
                    }

                @Override
                public Iterable<BlueTaskLogEntry> select(Integer limit)
                    {
                    // TODO Auto-generated method stub
                    return new EmptyIterable<BlueTaskLogEntry>();
                    }

                @Override
                public Iterable<BlueTaskLogEntry> select(Level level)
                    {
                    // TODO Auto-generated method stub
                    return new EmptyIterable<BlueTaskLogEntry>();
                    }

                @Override
                public Iterable<BlueTaskLogEntry> select(Integer limit, Level level)
                    {
                    // TODO Auto-generated method stub
                    return new EmptyIterable<BlueTaskLogEntry>();
                    }
             *
             */
            /*
             */ 
            @Override
            public BlueTaskLogEntry create(Level level, String message)
                {
                return factories().logger().entities().create(
                    BlueTaskEntity.this,
                    level,
                    message
                    );
                }

            @Override
            public BlueTaskLogEntry create(Object source, Level level, String message)
                {
                return factories().logger().entities().create(
                    source,
                    BlueTaskEntity.this,
                    level,
                    message
                    );
                }

            @Override
            public BlueTaskLogEntry create(final BlueTask.TaskState state, final Level level, final String message)
                {
                return factories().logger().entities().create(
                    BlueTaskEntity.this,
                    state,
                    level,
                    message
                    );
                }

            @Override
            public BlueTaskLogEntry create(final Object source, final BlueTask.TaskState state, final Level level, final String message)
                {
                return factories().logger().entities().create(
                    source,
                    BlueTaskEntity.this,
                    state,
                    level,
                    message
                    );
                }

            @Override
            public Iterable<BlueTaskLogEntry> select()
                {
                return factories().logger().entities().select(
                    BlueTaskEntity.this
                    );
                }

            @Override
            public Iterable<BlueTaskLogEntry> select(final Integer limit)
                {
                return factories().logger().entities().select(
                    BlueTaskEntity.this,
                    limit
                    );
                }

            @Override
            public Iterable<BlueTaskLogEntry> select(final Level level)
                {
                return factories().logger().entities().select(
                    BlueTaskEntity.this,
                    level
                    );
                }

            @Override
            public Iterable<BlueTaskLogEntry> select(final Integer limit, final Level level)
                {
                return factories().logger().entities().select(
                    BlueTaskEntity.this,
                    limit,
                    level
                    );
                }
            /*
             *  
             */
            };
        }
    }
