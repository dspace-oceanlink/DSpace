package org.dspace.authority.sparql;

import com.hp.hpl.jena.query.*;
import org.dspace.authority.AuthoritySource;
import org.dspace.authority.AuthorityValue;
import org.dspace.content.authority.Choices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User: mini @ atmire . com
 * Date: 2/17/15
 * Time: 1:09 PM
 */
public class SPARQLSource implements AuthoritySource {




    // contact URL from configuration
    private static String endpoint_url = null;
    private static String sparql_query = null;

    private String cruise_url = "http://linked.rvdata.us/page/cruise/";


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


        public SPARQLSource(String url){
            this.endpoint_url = url;
        }


    @Override
    public List<AuthorityValue> queryAuthorities(String query, int max) {
        List<AuthorityValue> authorities = new ArrayList<AuthorityValue>();

        query = query.substring(0,1).toUpperCase()+query.substring(1,query.length());

        // If query is wrong then fetch all titles
        if(query != null)
        {
           // sparql_query =prefix+"SELECT DISTINCT ?title ?identifier\n" + "WHERE { ?s a <http://linked.rvdata.us/vocab/resource/class/Cruise> . ?s <http://purl.org/dc/terms/title> ?title . ?s dcterms:identifier ?identifier} ORDER BY ?s LIMIT 1000";
           // queryEndPoint(authorities);
            sparql_query =prefix + "SELECT DISTINCT ?title ?identifier WHERE { ?s a <http://linked.rvdata.us/vocab/resource/class/Cruise> . ?s <http://purl.org/dc/terms/title> ?title FILTER regex(?title, \'^"+query+"\') . ?s dcterms:identifier ?identifier } ORDER BY ?s LIMIT 100";
            queryEndPoint(authorities);
        }

        return authorities;
    }

    @Override
    public AuthorityValue queryAuthorityID(String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Choices getBestMatch(String field, String text, int collection, String locale)
    {

        return new Choices(Choices.CF_NOTFOUND);
    }

    public String getLabel(String field, String key, String locale)
    {
        return null;
    }

    private void queryEndPoint(List<AuthorityValue> authorities) {
        Query query_result = QueryFactory.create(sparql_query);
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint_url, query_result);
        ResultSet results = qExe.execSelect();
        while(results.hasNext())
        {
            QuerySolution row=results.nextSolution();

            AuthorityValue authorityValue = new AuthorityValue();
            authorityValue.setId(UUID.randomUUID().toString());
            //authorityValue.updateLastModifiedDate();
            authorityValue.setCreationDate(new Date());

            authorityValue.setValue(row.getLiteral("title").getString());
            authorityValue.addOtherMetadata("identifier",row.getLiteral("identifier").getString());

            authorities.add(authorityValue);
        }
    }

}
