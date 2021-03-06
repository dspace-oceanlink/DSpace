package org.dspace.content.authority;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.dspace.authority.AuthoritySearchService;
import org.dspace.authority.AuthorityValue;
import org.dspace.authority.model.AuthorityObject;
import org.dspace.authority.model.Concept;
import org.dspace.authority.model.Scheme;
import org.dspace.authority.model.Term;
import org.dspace.authority.sparql.SPARQLAuthorityValue;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.services.ConfigurationService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extensible base class to support a core Solr based Authority Control Service.
 *
 * @author Lantian Gai, Mark Diggory, Kevin Van de Velde
 */
public class SolrAuthorityServiceImpl implements EditableAuthorityIndexingService, AuthoritySearchService {

    private static final Logger log = Logger.getLogger(SolrAuthorityServiceImpl.class);


    /**
     * Non-Static CommonsHttpSolrServer for processing indexing events.
     */
    private HttpSolrServer solr = null;


    /**
     * Non-Static Singelton instance of Configuration Service
     */
    private ConfigurationService configurationService;

    protected HttpSolrServer getSolr() throws MalformedURLException, SolrServerException {
        if (solr == null) {

            String solrService = ConfigurationManager.getProperty("solr.authority.server");

            log.debug("Solr authority URL: " + solrService);

            solr = new HttpSolrServer(solrService);
            solr.setBaseURL(solrService);

            SolrQuery solrQuery = new SolrQuery().setQuery("*:*");

            solr.query(solrQuery);
        }

        return solr;
    }


    /* This method will only be called from AuthorityIndexClient */
    public void indexContent(AuthorityValue value, boolean force) {

        //internalIndexContent(value, force);

    }

    private void internalIndexContent(AuthorityValue value, boolean force) {
        SolrInputDocument doc = value.getSolrInputDocument();
        try{
            writeDocument(doc);
        }catch (Exception e){
            log.error("Error while writing authority value to the index: " + value.toString(), e);
        }
    }


    @Override
    /* this method is called from Consumer that update off concept changes. */
    public void indexContent(Context context, Concept concept, boolean force) throws SQLException {
        if(!Concept.Status.WITHDRAWN.name().equals(concept.getStatus()))
            internalIndexContent(AuthorityValue.fromConcept(concept), force);


    }

    /**
     * Unindex a Document in the Lucene Index.
     * @param context the dspace context
     * @param identifier the identifier of the object to be deleted
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public void unIndexContent(Context context, String identifier, boolean commit)
            throws SQLException, IOException {

        try {
            getSolr().deleteById(identifier);
            if(commit)
            {
                getSolr().commit();
            }
        } catch (SolrServerException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void unIndexContent(Context context, Concept concept) throws SQLException, IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void cleanIndex() throws Exception {
        try{
            getSolr().deleteByQuery("*:*");
        } catch (Exception e){
            log.error("Error while cleaning authority solr server index", e);
            throw new Exception(e);
        }
    }

    public void commit() {
        try {
            getSolr().commit();
        } catch (SolrServerException e) {
            log.error("Error while committing authority solr server", e);
        } catch (IOException e) {
            log.error("Error while committing authority solr server", e);
        }
    }

    @Override
    public boolean isConfiguredProperly() {
        boolean solrReturn = false;
        try {
            solrReturn = (getSolr()!=null);
        } catch (Exception e) {
            log.error("Authority solr is not correctly configured, check \"solr.authority.server\" property in the dspace.cfg", e);
        }
        return solrReturn;
    }

    @Override
    public void updateIndex(Context context, boolean force) {
        //To change body of implemented methods use File | Settings | File Templates.

        try {
            Concept[] concepts = Concept.findAll(context, AuthorityObject.ID);
            try {
                for (Concept concept : concepts) {

                    indexContent(context, concept, force);

                }
            } catch (Exception e)
            {
                log.error(e.getMessage());
            }

            getSolr().commit();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void optimize() {
        try {
            long start = System.currentTimeMillis();
            System.out.println(this.getClass().getName() + " - Optimize -- Process Started:"+start);
            getSolr().optimize();
            long finish = System.currentTimeMillis();
            System.out.println(this.getClass().getName() + " - Optimize -- Process Finished:"+finish);
            System.out.println(this.getClass().getName() + " - Optimize -- Total time taken:"+(finish-start) + " (ms).");
        } catch (SolrServerException sse) {
            System.err.println(sse.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    /**
     * Write the document to the solr index
     * @param doc the solr document
     * @throws java.io.IOException
     */
    private void writeDocument(SolrInputDocument doc) throws IOException {

        try {
            getSolr().add(doc);
        } catch (Exception e) {
            try {
                log.error("An error occurred for document: " + doc.getField("id").getFirstValue() + ", source: " + doc.getField("source").getFirstValue() + ", field: " + doc.getField("field").getFirstValue() + ", full-text: " + doc.getField("full-text").getFirstValue());
            } catch (Exception e1) {
                //shouldn't happen
            }
            log.error(e.getMessage(), e);
        }
    }

    public QueryResponse search(SolrQuery query) throws SolrServerException, MalformedURLException {
        return getSolr().query(query);
    }

    /**
     * Retrieves all the metadata fields which are indexed in the authority control
     * @return a list of metadata fields
     */
    public List<String> getAllIndexedMetadataFields() throws Exception {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.setFacet(true);
        solrQuery.addFacetField("field");

        QueryResponse response = getSolr().query(solrQuery);

        List<String> results = new ArrayList<String>();
        FacetField facetField = response.getFacetField("field");
        if(facetField != null){
            List<FacetField.Count> values = facetField.getValues();
            if(values != null){
                for (FacetField.Count facetValue : values) {
                    if (facetValue != null && facetValue.getName() != null) {
                        results.add(facetValue.getName());
                    }
                }
            }
        }
        return results;
    }
}
