# Neo4j fulltext indexing enhancements

![Travis CI status](https://travis-ci.org/sarmbruster/neo4j-fti.svg?branch=master)

This project provides enhancements to Neo4j's fulltext indexing capabilities. Fulltext indexes are legacy indexes configured with a specific analyzer. 

Lucene features language specific analyzers, e.g. for German, English, etc. To integrate them with Neo4j we need to wrap them since they do not provide a default no-arg constructor which is expected by Neo4j.

To use this extension:

* build it using: ./gradlew assemble
* copy the jar file from `build/libs` to `<NEO4J_DIR>/plugins`
* copy http://central.maven.org/maven2/org/apache/lucene/lucene-queries/3.6.2/lucene-queries-3.6.2.jar to `<NEO4J_DIR>/plugins`
* copy http://central.maven.org/maven2/org/apache/lucene/lucene-analyzers/3.6.2/lucene-analyzers-3.6.2.jar to `<NEO4J_DIR>/plugins`
* setting up fulltext indexes in `neo4j.properties` using

    fullTextIndexes="fulltext_de:org.neo4j.contrib.fti.analyzers.German,fulltext_en:org.neo4j.contrib.fti.analyzers.English,generic:fulltext"
    
* the config property `fullTextIndexes` contains a comma seperated list of `<indexName>`:`<analyzerClass>` pairs
* Using a value of `fulltext` for `analyzerClass` creates a non-language specific fulltext index.  
* upon neo4j startup the indexes will be created
* be aware these are manual indexes, so populating them is up to you

Optionally this project features a Neo4j unmanaged extension to run regex queries on a manual index. To activate this, set in your ´neo4j-server.properties`

    org.neo4j.server.thirdparty_jaxrs_classes=org.neo4j.contrib.fti=/fti

By sending an http GET to http://localhost:7474/fti/<indexname>/<field>/<regex> you get back a list of node ids matching your query.

### populating indexes

To populate a index being setup via `fullTextIndexes`, send a HTTP POST to:

    curl -X POST http://localhost:7474/fti/populate/<indexName>?label=<label>&properties=<property1>&properties=<property2>&batchSize=20000
    
`label` is the name of the label to be indexed, `properties` are the property keys to be put into the fulltext index and
the optional parameter `batchSize` is the size of the transaction defaulting to 10000 ops. 
For every 100000 index ops a line is printed in `data/log/console.log`.
