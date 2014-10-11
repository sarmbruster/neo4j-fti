package org.neo4j.contrib.fti

import org.apache.lucene.index.Term
import org.apache.lucene.search.regex.RegexQuery
import org.junit.Rule
import org.neo4j.contrib.fti.analyzers.English
import org.neo4j.contrib.fti.analyzers.German
import org.neo4j.extension.spock.Neo4jResource
import org.neo4j.extension.spock.Neo4jUtils
import org.neo4j.extension.spock.WithNeo4jTransaction
import org.neo4j.kernel.GraphDatabaseAPI
import org.neo4j.kernel.extension.KernelExtensions
import spock.lang.Specification
import spock.lang.Unroll

class SetupIndexesViaAPISpec extends Specification {

    @Rule
    @Delegate
    Neo4jResource neo4j = new Neo4jResource();

    def setup() {
        def indexManager = graphDatabaseService.index()
        Neo4jUtils.withSuccessTransaction(graphDatabaseService) {
            indexManager.forNodes("fulltext_de", [analyzer: "org.neo4j.contrib.fti.analyzers.German"])
        }
    }

    @WithNeo4jTransaction
    def "should fulltext indexes exist"() {

        when: "add something to the index"
        def indexManager = graphDatabaseService.index()

        then:
        indexManager.existsForNodes("fulltext_de")

        when:
        def indexDe = indexManager.forNodes("fulltext_de")

        then:
        indexManager.getConfiguration(indexDe).analyzer == German.class.name
    }

    @Unroll("#indexName index for #searchTerm should have #numberOfResults results")
    @WithNeo4jTransaction
    def "should classic fulltext indexes return different results from german analyzer"() {
        when:

        def indexManager = graphDatabaseService.index()
        def index = indexManager.forNodes(indexName)
        def node = graphDatabaseService.createNode()
        index.add(node, "description", text)

        then:
        index.query("description", searchTerm).size() == numberOfResults

        where:
        indexName     | text                                | searchTerm                                      | numberOfResults
        "fulltext_de" | "Auf der Straße stehen fünf Häuser" | "Straße"                                        | 1
        "fulltext_de" | "Auf der Straße stehen fünf Häuser" | "Strasse"                                       | 1
        "fulltext_de" | "Auf der Straße stehen fünf Häuser" | "Häuser"                                        | 1
        "fulltext_de" | "Auf der Straße stehen fünf Häuser" | "Haus"                                          | 1
        "fulltext_de" | "Auf der Straße stehen fünf Häuser" | "haus"                                          | 1
        "fulltext_de" | "Auf der Straße stehen fünf Häuser" | "h.*s"                                          | 0
        "fulltext_de" | "Auf der Straße stehen fünf Häuser" | new RegexQuery(new Term("description", "h.*s")) | 1
    }


}
