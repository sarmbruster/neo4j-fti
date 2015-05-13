package org.neo4j.contrib.fti

import org.junit.ClassRule
import org.neo4j.extension.spock.Neo4jServerResource
import org.neo4j.extension.spock.Neo4jUtils
import org.neo4j.graphdb.Result
import org.neo4j.helpers.collection.IteratorUtil
import spock.lang.Shared
import spock.lang.Specification

/**
 * @author Stefan Armbruster
 */
class IndexPopulationExtensionSpec extends Specification {

    @ClassRule
    @Shared
    Neo4jServerResource neo4j = new Neo4jServerResource(
            thirdPartyJaxRsPackages: [ "org.neo4j.contrib.fti": "/fti"],
            config: [ fullTextIndexes: "personIndex:fulltext,partnerIndex:fulltext"]
    )

    def setupSpec() {
        """create (:Person {name:'John Doe', city:'London'}), (:Person{name:'Foo Bar', city:'Munich'})""".cypher()
        def response = neo4j.http.POST("fti/populate/personIndex?label=Person&properties=name&properties=city")
        assert response.status() == 200
        assert response.content() == 2
    }

    def "full text search on populated indexes should work"() {

        when:
        Result r = "start n=node:personIndex({luceneString}) return n.name as name".cypher(luceneString: luceneString)

        then:
        IteratorUtil.asList(r.columnAs("name")) == result

        where:
        luceneString                 | result
        "name:Doe"                   | ["John Doe"]
        "name:Do*"                   | ["John Doe"]
        "name:*oe"                   | ["John Doe"]
        "name:Londo* OR city:Londo*" | ["John Doe"]
        "city:*ndo*"                 | ["John Doe"]
    }

    def "populate a index and check batching"() {
        setup:
        Neo4jUtils.withSuccessTransaction(neo4j.graphDatabaseService) {
            (0..<1000).each {
                "create (:Partner {name:{name}})".cypher(name:"Partner${it}".toString())
            }
        }

        when:
        def response = neo4j.http.POST("fti/populate/partnerIndex?label=Partner&properties=name")

        then:
        response.status() == 200
        response.content() == 1000

        when:
        when:
        def r = "start n=node:partnerIndex('name:Partner666') return n.name as name".cypher()

                then:
                IteratorUtil.single(r.columnAs("name")) == "Partner666"

    }
}
