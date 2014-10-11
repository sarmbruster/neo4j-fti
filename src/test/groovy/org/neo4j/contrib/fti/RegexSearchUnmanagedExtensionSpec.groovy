package org.neo4j.contrib.fti

import org.junit.ClassRule
import org.neo4j.contrib.fti.analyzers.German
import org.neo4j.extension.spock.Neo4jServerResource
import org.neo4j.graphdb.DynamicLabel
import spock.lang.Shared
import spock.lang.Specification


class RegexSearchUnmanagedExtensionSpec extends Specification {

    @ClassRule
    @Shared
    Neo4jServerResource neo4j = new Neo4jServerResource(
            thirdPartyJaxRsPackages: [ "org.neo4j.contrib.fti": "/regex"]
    );


    def "test unmanaged extension for regex search on fulltext index"() {
        setup: "create fulltext index"

        def index = neo4j.withTransaction {
            neo4j.graphDatabaseService.index().forNodes("fulltext_de", [analyzer: German.class.name])
        }

        and: "add node and put it in the index"
        def node = neo4j.withTransaction {
            def n = neo4j.graphDatabaseService.createNode(DynamicLabel.label("Blog"))
            n.setProperty("description", "Auf der Straße stehen fünf Häuser")
            index.add(n, "description", "Auf der Straße stehen fünf Häuser")
            return n
        }

        when:
        def response = neo4j.http.GET("regex/fulltext_de/description/h.*s")

        then:
        response.status() == 200
        response.content().size() == 1
        response.content()[0] == node.id

    }

}