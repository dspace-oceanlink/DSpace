/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.adapters;


import org.dspace.core.Context;
import org.dspace.event.Event;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

/**
 * User: mini @ atmire . com
 * Date: 1/28/14
 * Time: 1:50 PM
 */
public interface RepositoryProvider {

    void handle(Context context, Event event) throws Exception;

    void clean(boolean force) throws Exception;

    RepositoryConnection getConnection() throws RepositoryException;

}
