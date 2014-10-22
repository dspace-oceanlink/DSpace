/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.content.authority;

import java.util.ArrayList;
 import com.hp.hpl.jena.query.Query;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.SelfNamedPlugin;
import com.hp.hpl.jena.query.*;

/**
 * SPARQLChoiceAuthority source that reads the same input-forms which drive
 * configurable submission.
 *
 * Configuration:
 *   This MUST be configured aas a self-named plugin, e.g.:
 *     plugin.selfnamed.org.dspace.content.authority.ChoiceAuthority = \
 *        org.dspace.content.authority.SPARQLChoiceAuthority
 *
 * It AUTOMATICALLY configures a plugin instance for each <value-pairs>
 * element (within <form-value-pairs>) of the input-forms.xml.  The name
 * of the instance is the "value-pairs-name" attribute, e.g.
 * the element: <value-pairs value-pairs-name="common_types" dc-term="type">
 * defines a plugin instance "common_types".
 *
 * IMPORTANT NOTE: Since these value-pairs do NOT include authority keys,
 * the choice lists derived from them do not include authority values.
 * So you should not use them as the choice source for authority-controlled
 * fields.
 *
 * @author Mini Pillai minipillai@atmire.com
 */
public class SPARQLChoiceAuthority extends SelfNamedPlugin implements ChoiceAuthority
{
    // contact URL from configuration
    private static String endpoint_url = null;
    private static String sparql_query = null;


    private String prefix="PREFIX db: <http://linked.rvdata.us/resource/>\n"+
            "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "PREFIX r2rmodel: <http://voc.rvdata.us/>\n" +
            "PREFIX sf: <http://www.opengis.net/ont/sf#>\n" +
            "PREFIX r2r: <http://linked.rvdata.us/vocab/resource/class/>\n" +
            "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
            "PREFIX dcterms: <http://purl.org/dc/terms/>\n" +
            "PREFIX gn: <http://www.geonames.org/ontology#>\n" +
            "PREFIX geosparql: <http://www.opengis.net/ont/geosparql#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX d2r: <http://sites.wiwiss.fu-berlin.de/suhl/bizer/d2r-server/config.rdf#>\n" +
            "PREFIX map: <http://linked.rvdata.us/resource/#>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" ;


    public SPARQLChoiceAuthority(){
        if (endpoint_url == null)
        {
            endpoint_url = ConfigurationManager.getProperty("endpoint.url");

            if (endpoint_url == null)
            {
                throw new IllegalStateException("Missing endpoint url");
            }
        }
        /*if(sparql_query ==null)
        {
            sparql_query = prefix+ConfigurationManager.getProperty("sparql.query");

            if(sparql_query == null)
            {
                 throw new IllegalStateException("Missing sparq query");
            }
        }  */
    }

    public Choices getMatches(String field, String query, int collection, int start, int limit, String locale)
    {
        int dflt = -1;
        ArrayList<Choice> v = new ArrayList<Choice>();
        query = query.substring(0,1).toUpperCase()+query.substring(1,query.length());

        sparql_query =prefix + "SELECT ?title WHERE { ?s a <http://linked.rvdata.us/vocab/resource/class/Cruise> . ?s dcterms:title ?title FILTER regex(?title, \'^"+query+"\') } ORDER BY ?s LIMIT 100";

         queryEndPoint(v);

        // If query is wrong then fetch all titles
        if(v.size() == 0)
        {
            sparql_query =prefix+"SELECT DISTINCT ?s ?p ?o WHERE { ?s a <http://linked.rvdata.us/vocab/resource/class/Cruise> . ?s <http://purl.org/dc/terms/title> ?o } ORDER BY ?s LIMIT 100";

            queryAllTitle(v);
        }
        Choice[] choices = v.toArray(new Choice[v.size()]);
        return new Choices(choices, 0,v.size(), Choices.CF_AMBIGUOUS, false, dflt);
    }

    public Choices getBestMatch(String field, String text, int collection, String locale)
    {
        /*for (int i = 0; i < values.length; ++i)
        {
            if (text.equalsIgnoreCase(values[i]))
            {
                Choice v[] = new Choice[1];
                v[0] = new Choice(String.valueOf(i), values[i], labels[i]);
                return new Choices(v, 0, v.length, Choices.CF_UNCERTAIN, false, 0);
            }
        }  */
        return new Choices(Choices.CF_NOTFOUND);
    }

    public String getLabel(String field, String key, String locale)
    {
        //return labels[Integer.parseInt(key)];
        return null;
    }

    public void queryEndPoint(ArrayList<Choice> choiceList)
    {

        Query query = QueryFactory.create(sparql_query);
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint_url, query );
        ResultSet results = qExe.execSelect();
        while(results.hasNext())
        {
            QuerySolution row=results.nextSolution();
            choiceList.add(new Choice(row.getLiteral("title").getString(), row.getLiteral("title").getString(), row.getLiteral("title").getString()));
        }
    }

    public void queryAllTitle(ArrayList<Choice> choiceList)
    {

        Query query = QueryFactory.create(sparql_query);
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint_url, query );
        ResultSet results = qExe.execSelect();
        while(results.hasNext())
        {
            QuerySolution row=results.nextSolution();
            choiceList.add(new Choice(row.getLiteral("o").getString(), row.getLiteral("o").getString(), row.getLiteral("o").getString()));
        }
    }
}
