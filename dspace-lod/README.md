# Installation instructions #

## Maven Artifact Locations ##

For convenience sake, dspace-oceanlink maven artifacts reside in a git repository on github:
```
[GIT](https://github.com/dspace-oceanlink/mvn-repo.git)
 ```
## Build Configuration ##

Build configuration require that you supply dependencies and addon module pom.xml file within your build source.

Changes are neccessary to the following files

* [dspace-parent/pom.xml](../pom.xml)
* [dspace/modules/pom.xml](../dspace/modules/pom.xml)
* [dspace/modules/additions/pom.xml](../dspace/modules/additions/pom.xml)
* [dspace/modules/xmlui/pom.xml](../dspace/modules/xmlui/pom.xml)

Two additional new poms will be needed to complete  support for Sesame:

* [dspace/modules/sesame/pom.xml](../dspace/modules/sesame/pom.xml)
* [dspace/modules/workbench/pom.xml](../dspace/modules/workbench/pom.xml)

### dspace-parent/pom.xml Changes ###

Add the following dependency management versions to the [dspace-parent.pom](../pom.xml). These changes provide version numbers to the rest of the dependencies added below.

```
          <dependency>
             <groupId>org.dspace</groupId>
             <artifactId>dspace-lod-api</artifactId>
             <version>3.2</version>
             <type>jar</type>
          </dependency>
          <dependency>
             <groupId>org.dspace</groupId>
             <artifactId>dspace-lod-xmlui</artifactId>
             <version>3.2</version>
             <type>jar</type>
             <classifier>classes</classifier>
          </dependency>
          <dependency>
             <groupId>org.dspace</groupId>
             <artifactId>dspace-lod-xmlui</artifactId>
             <version>3.2</version>
             <type>war</type>
          </dependency>
```

### dspace/modules/pom.xml POM Changes ###

Additional build module sections should be added to support the new sesame and workbench webapplications.

```
        <profile>
            <id>dspace-sesame</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <modules>
                <module>sesame</module>
            </modules>
        </profile>
	        
	<profile>
            <id>dspace-sesame-workbench</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <modules>
                <module>workbench</module>
            </modules>
        </profile>
```
        
### dspace/modules/additions/pom.xml POM Changes ###

Add the following in [additions.pom](../dspace/modules/additions/pom.xml)

Under the repositories section 
```
   <repositories>
        <repository>
            <id>ocean-link-release-repository</id>
            <name>DSpace OceanLink Release Repository</name>
            <url>https://raw.githubusercontent.com/dspace-oceanlink/mvn-repo/master/releases</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
   </repositories>
```   
Add the following under the dependencies section

```
  <dependency>
     <groupId>org.dspace</groupId>
     <artifactId>dspace-lod-api</artifactId>
  </dependency>
```

### dspace/modules/xmlui/pom.xml POM Changes ###

Add the following under the dependency section in [xmlui.pom](../dspace/modules/xmlui/pom.xml)

Under the repositories section 
```
   <repositories>
        <repository>
            <id>ocean-link-release-repository</id>
            <name>DSpace OceanLink Release Repository</name>
            <url>https://raw.githubusercontent.com/dspace-oceanlink/mvn-repo/master/releases</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
   </repositories>
```

Add the following under the dependencies section

```
  <dependency>
     <groupId>org.dspace</groupId>
     <artifactId>dspace-lod-xmlui</artifactId>
     <type>war</type>
  </dependency>

  <dependency>
     <groupId>org.dspace</groupId>
     <artifactId>dspace-lod-xmlui</artifactId>
     <type>jar</type>
     <classifier>classes</classifier>
  </dependency>
```

### dspace/modules/sesame/pom.xml POM Changes ###

Create the sesame module directory inside dspace/modules

Add the following sesame [dspace/modules/sesame/pom.xml](../dspace/modules/sesame/pom.xml) to dspace/modules/sesame


### dspace/modules/workbench/pom.xml POM Changes ###

Create the workbench module directory inside dspace/modules

Optionally, add the workbench [dspace/modules/workbench/pom.xml](../dspace/modules/workbench/pom.xml) to dspace/modules/workbench


## Configuration in dspace.cfg ##

Enabling sesame consumer in dspace.cfg:

The consumer should be enabled by adding sesame to the list of consumers in dspace.cfg
```
eg: event.dispatcher.default.consumers = versioning, search, browse, eperson, harvester, sesame
 ```
Add sesame class and filters as below:
```
event.consumer.sesame.class = org.dspace.app.sesame.SesameEventConsumer
event.consumer.sesame.filters = All+All
```

## Commandline Support for reindexing Sesame ##

Commandline routine to generate rdf statements in the triplestore

From the bin directory of deployment folder, run the following
```
./dspace dsrun org.dspace.app.sesame.SesameIndexClient -b

```