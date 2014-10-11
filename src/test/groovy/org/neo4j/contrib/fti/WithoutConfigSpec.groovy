package org.neo4j.contrib.fti

import org.junit.Rule
import org.neo4j.extension.spock.Neo4jResource
import org.neo4j.kernel.GraphDatabaseAPI
import org.neo4j.kernel.extension.KernelExtensions
import spock.lang.Specification

class WithoutConfigSpec extends Specification {

    @Rule
    @Delegate
    Neo4jResource neo4j = new Neo4jResource();

    def "should kernel extension be available"() {
        expect:
        ((GraphDatabaseAPI)graphDatabaseService).dependencyResolver.resolveDependency(KernelExtensions).isRegistered(FullTextIndexingKernelExtensionFactory)
    }

}
