# Neo4j fulltext indexing enhancements

This project provides enhancements to Neo4j's fulltext indexing capabilities. Fulltext indexes are legacy indexes configured with a specific analyzer. 

Lucene features language specific analyzers, e.g. for German, English, etc. To integrate them with Neo4j we need to wrap them since they do not provide a default no-arg constructor which is expected by Neo4j.

To use this extension:

* build it using: ./gradlew assemble
* copy the jar file from `build/libs` to `<NEO4J_DIR>/plugins`
* copy http://central.maven.org/maven2/org/apache/lucene/lucene-queries/3.6.2/lucene-queries-3.6.2.jar to `<NEO4J_DIR>/plugins`
* copy http://central.maven.org/maven2/org/apache/lucene/lucene-analyzers/3.6.2/lucene-analyzers-3.6.2.jar to `<NEO4J_DIR>/plugins`
* setting up fulltext indexes in `neo4j.properties` using

    fullTextIndexes: "fulltext_de:org.neo4j.contrib.fti.analyzers.German,fulltext_en:org.neo4j.contrib.fti.analyzers.English"

* the config property `fullTextIndexes` contains a comma seperated list of `<indexName>`:`<analyzerClass>` pairs
* upon neo4j startup the indexes will be created
* be aware these are manual indexes, so populating them is up to you
