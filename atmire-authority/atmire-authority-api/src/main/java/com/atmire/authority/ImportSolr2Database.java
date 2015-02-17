package com.atmire.authority;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dspace.authority.AuthoritySearchService;
import org.dspace.authority.AuthorityValue;
import org.dspace.authority.PersonAuthorityValue;
import org.dspace.authority.model.Concept;
import org.dspace.authority.model.Scheme;
import org.dspace.authority.model.Term;
import org.dspace.content.authority.*;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.apache.commons.cli.CommandLine;

import org.apache.commons.cli.Options;


import org.dspace.discovery.SearchService;
import org.dspace.event.EventManager;
import org.dspace.utils.DSpace;

/**
 * User: lantian @ atmire . com
 * Date: 4/17/14
 * Time: 1:25 PM
 */
public final class ImportSolr2Database {


    /** DSpace Context object */
    private Context context;

    /**
     * For invoking via the command line.  If called with no command line arguments,
     * it will negotiate with the user for the administrator details
     *
     * @param argv
     *            command-line arguments
     */
    public static void main(String[] argv)
            throws Exception
    {
        CommandLineParser parser = new PosixParser();
        Options options = new Options();

        ImportSolr2Database ca = new ImportSolr2Database();
        options.addOption("t", "test", true, "test mode");

        CommandLine line = parser.parse(options, argv);
        ca.importAuthority(line.getOptionValue("t"));
    }



    /**
     * constructor, which just creates and object with a ready context
     *
     * @throws Exception
     */
    private ImportSolr2Database()
            throws Exception
    {
        context = new Context();
    }

    /**
     * Create the administrator with the given details.  If the user
     * already exists then they are simply upped to administrator status
     *
     * @throws Exception
     */
    private void importAuthority(String test)
            throws Exception
    {
        // Of course we aren't an administrator yet so we need to
        // circumvent authorisation
        context.setIgnoreAuthorization(true);

        try {

            SolrQuery queryArgs = new SolrQuery();
            queryArgs.setQuery("*:*");
            queryArgs.setRows(-1);
            QueryResponse searchResponse = getSearchService().search(queryArgs);
            SolrDocumentList authDocs = searchResponse.getResults();
            int max = (int) searchResponse.getResults().getNumFound();

            queryArgs.setQuery("*:*");
            if(test!=null)
            {
                queryArgs.setRows(Integer.parseInt(test));
            }
            else
            {
                queryArgs.setRows(max);
            }

            searchResponse = getSearchService().search(queryArgs);
            authDocs = searchResponse.getResults();
            Date date = new Date();
            Scheme instituteScheme = Scheme.findByIdentifier(context,"Institution");
            if(instituteScheme==null){
                instituteScheme = Scheme.create(context,"Institution");
                instituteScheme.setLastModified(date);
                instituteScheme.setCreated(date);
                instituteScheme.setLang("en");
                instituteScheme.setStatus(Concept.Status.ACCEPTED.toString());
                instituteScheme.update();
                context.commit();
            }

            Scheme authScheme = Scheme.findByIdentifier(context,"Author");
            if(authScheme==null){
                authScheme = Scheme.create(context,"Author");
                authScheme.setLastModified(date);
                authScheme.setCreated(date);
                authScheme.setLang("en");
                authScheme.setStatus(Concept.Status.ACCEPTED.toString());
                authScheme.update();
                context.commit();
            }

            if(authDocs != null){
                int maxDocs = authDocs.size();

                //import all the authors
                for (int i = 0; i < maxDocs; i++) {
                    SolrDocument solrDocument = authDocs.get(i);
                    if(solrDocument != null){
                        AuthorityValue val = new AuthorityValue(solrDocument);
                        if(val.getId() != null){
                            ArrayList<Concept> aConcepts = Concept.findByIdentifier(context,val.getId());
                            Concept aConcept = null;
                            if(aConcepts==null||aConcepts.size()==0)  {
                                aConcept = authScheme.createConcept(val.getId());
                                aConcept.setLang("en");
                                aConcept.setTopConcept(true);
                                aConcept.setStatus(Concept.Status.ACCEPTED);
                                if(val.getField()!=null) {
                                    aConcept.setSource(PersonAuthorityValue.PERSON);
                                }
                                if(solrDocument.getFieldValue("email")!=null&&!solrDocument.getFieldValue("email").equals("null")){
                                    ArrayList<String> emails = (ArrayList<String>) solrDocument.getFieldValue("email");
                                    for (String email:emails)
                                        aConcept.addMetadata("person","email",null,"",email,val.getId(),0);
                                }
                                aConcept.update();
                                Term aTerm = aConcept.createTerm(val.getValue(),1);
                                aTerm.update();

                                if(solrDocument.getFieldValue("institution")!=null)
                                {
                                    ArrayList<String> in = (ArrayList<String>) solrDocument.getFieldValue("institution");
                                    String instituteName = in.get(0);
                                    Concept[] iConcepts = Concept.findByPreferredLabel(context,instituteName, instituteScheme.getID());
                                    Concept instituteConcept = null;
                                    if(iConcepts==null||iConcepts.length==0) {
                                        Concept iConcept = instituteScheme.createConcept();
                                        iConcept.setLang("en");
                                        iConcept.setTopConcept(true);
                                        iConcept.setStatus(Concept.Status.ACCEPTED);
                                        iConcept.update();

                                        Term iTerm = iConcept.createTerm(instituteName,1);
                                        iTerm.update();
                                        instituteConcept=iConcept;
                                    }
                                    else
                                    {
                                        instituteConcept = iConcepts[0];
                                    }
                                    if(instituteConcept!=null)
                                    {

                                        instituteConcept.addChildConcept(aConcept,1);

                                    }
                                }
                                context.commit();
                            }


                        }

                    }
                }

            }
        }catch (Exception e)
        {
            System.out.print(e);
            System.out.print(e.getStackTrace());
        }

        context.complete();

        System.out.println("Authority imported");
    }
    private AuthoritySearchService getSearchService(){
        DSpace dspace = new DSpace();

        org.dspace.kernel.ServiceManager manager = dspace.getServiceManager() ;

        return manager.getServiceByName(AuthoritySearchService.class.getName(),AuthoritySearchService.class);
    }

}
