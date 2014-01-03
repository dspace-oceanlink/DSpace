/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.adapters.dspace.vocabularies;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/"> <dcterms:title
 * xml:lang="en-US">DCMI Namespace for metadata terms of the DCMI Type
 * Vocabulary</dcterms:title> <rdfs:comment>To comment on this schema, please
 * contact dcmifb@dublincore.org.</rdfs:comment> <dcterms:publisher
 * xml:lang="en-US">The Dublin Core Metadata Initiative</dcterms:publisher>
 * <dcterms:modified>2008-01-14</dcterms:modified> </rdf:Description>
 * 
 * @author mdiggory
 * 
 */
public class DCMITYPE
{

    private static final ValueFactory vf = ValueFactoryImpl.getInstance();

    public static final String NAMESPACE = "http://purl.org/dc/dcmitype/";

    public static final URI NS = vf.createURI(NAMESPACE);

    /**
     * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/Collection">
     * <rdfs:label xml:lang="en-US">Collection</rdfs:label> <rdfs:comment
     * xml:lang="en-US">An aggregation of resources.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">A collection is described as a
     * group; its parts may also be separately described.</dcterms:description>
     * <rdfs:isDefinedBy rdf:resource="http://purl.org/dc/dcmitype/"/>
     * <dcterms:issued>2000-07-11</dcterms:issued> <dcterms:modified>2008-01-14</dcterms:modified>
     * <rdf:type rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#Collection-003"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * </rdf:Description>
     */
    public static final URI Collection = vf.createURI(NAMESPACE, "Collection");
    
    /**
     * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/Dataset">
     * <rdfs:label xml:lang="en-US">Dataset</rdfs:label> <rdfs:comment
     * xml:lang="en-US">Data encoded in a defined structure.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">Examples include lists, tables, and
     * databases. A dataset may be useful for direct machine processing.</dcterms:description>
     * <rdfs:isDefinedBy rdf:resource="http://purl.org/dc/dcmitype/"/>
     * <dcterms:issued>2000-07-11</dcterms:issued> <dcterms:modified>2008-01-14</dcterms:modified>
     * <rdf:type rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#Dataset-003"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * </rdf:Description>
     */
    public static final URI Dataset = vf.createURI(NAMESPACE, "Dataset");
    
    /**
     * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/Event">
     * <rdfs:label xml:lang="en-US">Event</rdfs:label> <rdfs:comment
     * xml:lang="en-US">A non-persistent, time-based occurrence.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">Metadata for an event provides
     * descriptive information that is the basis for discovery of the purpose,
     * location, duration, and responsible agents associated with an event.
     * Examples include an exhibition, webcast, conference, workshop, open day,
     * performance, battle, trial, wedding, tea party, conflagration.</dcterms:description>
     * <rdfs:isDefinedBy rdf:resource="http://purl.org/dc/dcmitype/"/>
     * <dcterms:issued>2000-07-11</dcterms:issued> <dcterms:modified>2008-01-14</dcterms:modified>
     * <rdf:type rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#Event-003"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * </rdf:Description>
     */
    public static final URI Event = vf.createURI(NAMESPACE, "Event");

    /**
     * 
     * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/Image">
     * <rdfs:label xml:lang="en-US">Image</rdfs:label> <rdfs:comment
     * xml:lang="en-US">A visual representation other than text.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">Examples include images and
     * photographs of physical objects, paintings, prints, drawings, other
     * images and graphics, animations and moving pictures, film, diagrams,
     * maps, musical notation. Note that Image may include both electronic and
     * physical representations.</dcterms:description> <rdfs:isDefinedBy
     * rdf:resource="http://purl.org/dc/dcmitype/"/> <dcterms:issued>2000-07-11</dcterms:issued>
     * <dcterms:modified>2008-01-14</dcterms:modified> <rdf:type
     * rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#Image-004"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * </rdf:Description>
     */
    public static final URI Image = vf.createURI(NAMESPACE, "Image");
    
    /**
     * <rdf:Description
     * rdf:about="http://purl.org/dc/dcmitype/InteractiveResource"> <rdfs:label
     * xml:lang="en-US">Interactive Resource</rdfs:label> <rdfs:comment
     * xml:lang="en-US">A resource requiring interaction from the user to be
     * understood, executed, or experienced.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">Examples include forms on Web
     * pages, applets, multimedia learning objects, chat services, or virtual
     * reality environments.</dcterms:description> <rdfs:isDefinedBy
     * rdf:resource="http://purl.org/dc/dcmitype/"/> <dcterms:issued>2000-07-11</dcterms:issued>
     * <dcterms:modified>2008-01-14</dcterms:modified> <rdf:type
     * rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#InteractiveResource-003"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * </rdf:Description>
     */
    public static final URI InteractiveResource = vf.createURI(NAMESPACE,
    "InteractiveResource");
    
    /**
     * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/Service">
     * <rdfs:label xml:lang="en-US">Service</rdfs:label> <rdfs:comment
     * xml:lang="en-US">A system that provides one or more functions.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">Examples include a photocopying
     * service, a banking service, an authentication service, interlibrary
     * loans, a Z39.50 or Web server.</dcterms:description> <rdfs:isDefinedBy
     * rdf:resource="http://purl.org/dc/dcmitype/"/> <dcterms:issued>2000-07-11</dcterms:issued>
     * <dcterms:modified>2008-01-14</dcterms:modified> <rdf:type
     * rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#Service-003"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * </rdf:Description>
     */
    public static final URI Service = vf.createURI(NAMESPACE, "Service");

    /**
     * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/Software">
     * <rdfs:label xml:lang="en-US">Software</rdfs:label> <rdfs:comment
     * xml:lang="en-US">A computer program in source or compiled form.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">Examples include a C source file,
     * MS-Windows .exe executable, or Perl script.</dcterms:description>
     * <rdfs:isDefinedBy rdf:resource="http://purl.org/dc/dcmitype/"/>
     * <dcterms:issued>2000-07-11</dcterms:issued> <dcterms:modified>2008-01-14</dcterms:modified>
     * <rdf:type rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#Software-003"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * </rdf:Description>
     */
    public static final URI Software = vf.createURI(NAMESPACE, "Software");

    /**
     * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/Sound">
     * <rdfs:label xml:lang="en-US">Sound</rdfs:label> <rdfs:comment
     * xml:lang="en-US">A resource primarily intended to be heard.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">Examples include a music playback
     * file format, an audio compact disc, and recorded speech or sounds.</dcterms:description>
     * <rdfs:isDefinedBy rdf:resource="http://purl.org/dc/dcmitype/"/>
     * <dcterms:issued>2000-07-11</dcterms:issued> <dcterms:modified>2008-01-14</dcterms:modified>
     * <rdf:type rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#Sound-003"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * </rdf:Description>
     */
    public static final URI Sound = vf.createURI(NAMESPACE, "Sound");

    /**
     * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/Text">
     * <rdfs:label xml:lang="en-US">Text</rdfs:label> <rdfs:comment
     * xml:lang="en-US">A resource consisting primarily of words for reading.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">Examples include books, letters,
     * dissertations, poems, newspapers, articles, archives of mailing lists.
     * Note that facsimiles or images of texts are still of the genre Text.</dcterms:description>
     * <rdfs:isDefinedBy rdf:resource="http://purl.org/dc/dcmitype/"/>
     * <dcterms:issued>2000-07-11</dcterms:issued> <dcterms:modified>2008-01-14</dcterms:modified>
     * <rdf:type rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#Text-003"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * </rdf:Description>
     */
    public static final URI Text = vf.createURI(NAMESPACE, "Text");
    
    /**
     * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/PhysicalObject">
     * <rdfs:label xml:lang="en-US">Physical Object</rdfs:label> <rdfs:comment
     * xml:lang="en-US">An inanimate, three-dimensional object or substance.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">Note that digital representations
     * of, or surrogates for, these objects should use Image, Text or one of the
     * other types.</dcterms:description> <rdfs:isDefinedBy
     * rdf:resource="http://purl.org/dc/dcmitype/"/> <dcterms:issued>2002-07-13</dcterms:issued>
     * <dcterms:modified>2008-01-14</dcterms:modified> <rdf:type
     * rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#PhysicalObject-003"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * </rdf:Description>
     */
    public static final URI PhysicalObject = vf.createURI(NAMESPACE, "PhysicalObject");
    
    /**
     * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/StillImage">
     * <rdfs:label xml:lang="en-US">Still Image</rdfs:label> <rdfs:comment
     * xml:lang="en-US">A static visual representation.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">Examples include paintings,
     * drawings, graphic designs, plans and maps. Recommended best practice is
     * to assign the type Text to images of textual materials. Instances of the
     * type Still Image must also be describable as instances of the broader
     * type Image.</dcterms:description> <rdfs:isDefinedBy
     * rdf:resource="http://purl.org/dc/dcmitype/"/> <dcterms:issued>2003-11-18</dcterms:issued>
     * <dcterms:modified>2008-01-14</dcterms:modified> <rdf:type
     * rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#StillImage-003"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * <rdfs:subClassOf rdf:resource="http://purl.org/dc/dcmitype/Image"/>
     * </rdf:Description>
     */
    public static final URI StillImage = vf.createURI(NAMESPACE, "StillImage");
    
    /**
     * <rdf:Description rdf:about="http://purl.org/dc/dcmitype/MovingImage">
     * <rdfs:label xml:lang="en-US">Moving Image</rdfs:label> <rdfs:comment
     * xml:lang="en-US">A series of visual representations imparting an
     * impression of motion when shown in succession.</rdfs:comment>
     * <dcterms:description xml:lang="en-US">Examples include animations,
     * movies, television programs, videos, zoetropes, or visual output from a
     * simulation. Instances of the type Moving Image must also be describable
     * as instances of the broader type Image.</dcterms:description>
     * <rdfs:isDefinedBy rdf:resource="http://purl.org/dc/dcmitype/"/>
     * <dcterms:issued>2003-11-18</dcterms:issued> <dcterms:modified>2008-01-14</dcterms:modified>
     * <rdf:type rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
     * <dcterms:hasVersion
     * rdf:resource="http://dublincore.org/usage/terms/history/#MovingImage-003"/>
     * <dcam:memberOf rdf:resource="http://purl.org/dc/terms/DCMIType"/>
     * <rdfs:subClassOf rdf:resource="http://purl.org/dc/dcmitype/Image"/>
     * </rdf:Description>
     */
    public static final URI MovingImage = vf.createURI(NAMESPACE, "MovingImage");
    
}