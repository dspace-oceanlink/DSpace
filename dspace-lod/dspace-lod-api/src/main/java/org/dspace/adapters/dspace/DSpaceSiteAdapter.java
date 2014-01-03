/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.adapters.dspace;

import org.dspace.adapters.AbstractDSpaceEventAdapter;
import org.dspace.adapters.dspace.vocabularies.*;
import org.dspace.content.*;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.event.Event;
import org.openrdf.model.Resource;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;



public class DSpaceSiteAdapter extends AbstractDSpaceEventAdapter
{

    @Override
    protected void install(Context ctx, DSpaceObject subject) throws Exception {

        this.create(subject);

        this.modifyMetadata(ctx, subject);

        // Remove all preexisting top level hasCommunity references to clean orphans before adding
        this.getProvider().getConnection().remove(createResource((Site)subject),DS.hasCommunity,null,DS.Relationship);

        for(Community community : Community.findAllTop(ctx))
        {
            this.add(ctx,subject,community);
        }

        for(Community community : Community.findAllTop(ctx))
        {
            try {
                provider.handle(ctx, new Event(Event.INSTALL, Constants.COMMUNITY, community.getID(), "Install Community"));
                this.getProvider().getConnection().commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    @Override
    protected Resource create(DSpaceObject object) throws RepositoryException {
        Resource aggregation = createResource((Site)object);
        handleStatement(aggregation,RDF.TYPE, DS.Site);

        //VoID
        handleStatement(aggregation,RDF.TYPE, VOID.dataset);
        return aggregation;
    }
    @Override
    protected void modifyMetadata(Context ctx, DSpaceObject subject) throws Exception {
        Resource aggregation = createResource((Site)subject);

        /*
        * =================================================================
        * REMOVE RDF for site with context as DS.DescriptiveMetadata
        * =================================================================
        */

        this.getProvider().getConnection().remove(aggregation,null,null,DS.DescriptiveMetadata);

        /*
        * =================================================================
        * ADDING RDF for site metadata
        * =================================================================
        */

        String title = ConfigurationManager.getProperty("lod", "title");
        String description = ConfigurationManager.getProperty("lod","description");
        String creator = ConfigurationManager.getProperty("lod","creator");
        String homepage = ConfigurationManager.getProperty("lod","homepage");
        String sparqlEndPoint = ConfigurationManager.getProperty("lod","sparqlEndPoint");

        handleStatement(aggregation,
                DC.title_, valueFactory.createLiteral(subject.getName()),DS.DescriptiveMetadata);

        handleStatement(aggregation,
                VOID.title, valueFactory.createLiteral(subject.getName()),DS.DescriptiveMetadata);


        // more Void statements
        handleStatement(aggregation,
                VOID.title, valueFactory.createLiteral(title),DS.DescriptiveMetadata);
        handleStatement(aggregation,
                VOID.description, valueFactory.createLiteral(description),DS.DescriptiveMetadata);
        handleStatement(aggregation,
                VOID.creator, valueFactory.createLiteral(creator),DS.DescriptiveMetadata);
        handleStatement(aggregation,
                VOID.homepage, valueFactory.createLiteral(homepage),DS.DescriptiveMetadata);
        handleStatement(aggregation,
                VOID.sparqlEndPoint, valueFactory.createLiteral(sparqlEndPoint),DS.DescriptiveMetadata);

    }

    @Override
    protected void add(Context ctx, DSpaceObject subject, DSpaceObject object) throws Exception {

        // site has community
        handleStatement(
                createResource((Site)subject),
                DS.hasCommunity,
                createResource((Community)object),DS.Relationship);

        // community is part of site
        handleStatement(
                createResource((Community)object),
                DS.isPartOfSite,
                createResource((Site)subject),DS.Relationship);

    }


    @Override
    public boolean handlerFor(DSpaceObject dso) {
        return dso instanceof Site;
    }



}
