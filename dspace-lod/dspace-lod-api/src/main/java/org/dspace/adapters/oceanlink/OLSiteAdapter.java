/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.adapters.oceanlink;

import org.dspace.adapters.dspace.vocabularies.DCTERMS;
import org.dspace.adapters.oceanlink.vocabularies.RepositoryObject;
import org.dspace.adapters.AbstractDSpaceEventAdapter;
import org.dspace.adapters.dspace.vocabularies.DC;
import org.dspace.adapters.dspace.vocabularies.DS;
import org.dspace.adapters.dspace.vocabularies.VOID;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Site;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.event.Event;
import org.openrdf.model.Resource;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;


/**
 * User: mini @ atmire . com
 * Date: 1/31/14
 * Time: 4:16 PM
 */


public class OLSiteAdapter extends AbstractDSpaceEventAdapter
{
    private String title;
    private String description;
    private String creator;
    private String homepage;
    private String sparqlEndPoint;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getSparqlEndPoint() {
        return sparqlEndPoint;
    }

    public void setSparqlEndPoint(String sparqlEndPoint) {
        this.sparqlEndPoint = sparqlEndPoint;
    }


    @Override
    protected void install(Context ctx, DSpaceObject subject) throws Exception {

        this.create(subject);

        this.modifyMetadata(ctx, subject);

        this.getProvider().getConnection().remove(createResource((Site)subject), RepositoryObject.contains ,null,DS.Relationship);

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
        handleStatement(aggregation, RDF.TYPE, RepositoryObject.Repository);

        //VoID
        handleStatement(aggregation,RDF.TYPE, VOID.dataset);
        return aggregation;
    }
    @Override
    protected void modifyMetadata(Context ctx, DSpaceObject subject) throws Exception {
        Resource repository = createResource((Site)subject);

        /*
        * =================================================================
        * REMOVE RDF for site with context as DS.DescriptiveMetadata
        * =================================================================
        */

        this.getProvider().getConnection().remove(repository,null,null,DS.DescriptiveMetadata);

        /*
        * =================================================================
        * ADDING RDF for site metadata
        * =================================================================
        */
        String ol_title = ConfigurationManager.getProperty("lod","ol_title");
        String ol_description = ConfigurationManager.getProperty("lod","ol_description");
        String ol_creator = ConfigurationManager.getProperty("lod","ol_creator");
        String ol_homepage = ConfigurationManager.getProperty("lod","ol_homepage");
        String ol_sparqlEndPoint = ConfigurationManager.getProperty("lod","ol_sparqlEndPoint");

        handleStatement(repository,
                DC.title_, valueFactory.createLiteral(subject.getName()),DS.DescriptiveMetadata);

        // more Void statements
        handleStatement(repository,
                VOID.title, valueFactory.createLiteral(ol_title),DS.DescriptiveMetadata);
        handleStatement(repository,
                VOID.description, valueFactory.createLiteral(ol_description),DS.DescriptiveMetadata);
        handleStatement(repository,
                VOID.creator, valueFactory.createLiteral(ol_creator),DS.DescriptiveMetadata);
        handleStatement(repository,
                VOID.homepage, valueFactory.createLiteral(ol_homepage),DS.DescriptiveMetadata);
        handleStatement(repository,
                VOID.sparqlEndPoint, valueFactory.createLiteral(ol_sparqlEndPoint),DS.DescriptiveMetadata);


    }

    @Override
    protected void add(Context ctx, DSpaceObject subject, DSpaceObject object) throws Exception {

        /* =================================================================
        * List all the collection - items relationships
        * =================================================================
        */
        // collection has item
        handleStatement(
                createResource((Site)subject),
                RepositoryObject.contains,
                createResource((Community)object),DS.Relationship);

        //item has collection
        handleStatement(
                createResource((Community)object),
                RepositoryObject.isContainedBy,
                createResource((Site)subject),DS.Relationship);


    }

    @Override
    public boolean handlerFor(DSpaceObject dso) {
        return dso instanceof Site;
    }

    public void handleNamespaces() throws RepositoryException {
        super.handleNamespaces();
        this.getProvider().getConnection().setNamespace("ro", RepositoryObject.NAMESPACE);
    }

}
