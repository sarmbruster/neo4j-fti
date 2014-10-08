package org.neo4j.contrib.fti

import org.junit.Rule
import org.neo4j.contrib.fti.analyzers.English
import org.neo4j.contrib.fti.analyzers.German
import org.neo4j.extension.spock.Neo4jResource
import org.neo4j.extension.spock.WithNeo4jTransaction
import org.neo4j.kernel.GraphDatabaseAPI
import org.neo4j.kernel.extension.KernelExtensions
import spock.lang.Specification
import spock.lang.Unroll

class FullTextIndexingKernelExtensionSpec extends Specification {

    @Rule
    @Delegate
    Neo4jResource neo4j = new Neo4jResource(config: [ fullTextIndexes: "fulltext_de:org.neo4j.contrib.fti.analyzers.German,fulltext_en:org.neo4j.contrib.fti.analyzers.English"]);

    def "should kernel extension be available"() {
        expect:
        ((GraphDatabaseAPI)graphDatabaseService).dependencyResolver.resolveDependency(KernelExtensions).isRegistered(FullTextIndexingKernelExtensionFactory)
    }

    @WithNeo4jTransaction
    def "should fulltext indexes exist"() {

        when: "add something to the index"
        def indexManager = graphDatabaseService.index()

        then:
        indexManager.existsForNodes("fulltext_de")
        indexManager.existsForNodes("fulltext_en")

        when:
        def indexDe = indexManager.forNodes("fulltext_de")
        def indexEn = indexManager.forNodes("fulltext_en")

        then:
        indexManager.getConfiguration(indexDe).analyzer == German.class.name
        indexManager.getConfiguration(indexEn).analyzer == English.class.name
    }

    @Unroll("#indexName index for #searchTerm should have #numberOfResults results")
    @WithNeo4jTransaction
    def "should classic fulltext indexes return different results from german analyzer"() {
        when:

        def indexManager = graphDatabaseService.index()
        indexManager.forNodes("fulltext", [type: "fulltext"] )

        // wait for indexes established by extension being available
        while (!indexManager.existsForNodes(indexName))  {
            sleep 1
        }

        def index = indexManager.forNodes(indexName)
        def node = graphDatabaseService.createNode()
        index.add(node, "description", text)

        then:
        index.query("description", searchTerm).size() == numberOfResults

        where:
        indexName     | text                                | searchTerm | numberOfResults
        "fulltext"    | "Auf der Straße stehen fünf Häuser" | "Straße"   | 1
        "fulltext"    | "Auf der Straße stehen fünf Häuser" | "Strasse"  | 0
        "fulltext"    | "Auf der Straße stehen fünf Häuser" | "Häuser"   | 1
        "fulltext"    | "Auf der Straße stehen fünf Häuser" | "Haus"     | 0
        "fulltext_de" | "Auf der Straße stehen fünf Häuser" | "Straße"   | 1
        "fulltext_de" | "Auf der Straße stehen fünf Häuser" | "Strasse"  | 1
        "fulltext_de" | "Auf der Straße stehen fünf Häuser" | "Häuser"   | 1
        "fulltext_de" | "Auf der Straße stehen fünf Häuser" | "Haus"     | 1
    }
}
