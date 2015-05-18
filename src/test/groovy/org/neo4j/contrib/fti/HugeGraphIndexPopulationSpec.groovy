package org.neo4j.contrib.fti

import groovy.transform.CompileStatic
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.neo4j.extension.spock.Neo4jUtils
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.kernel.impl.store.NodeStore
import org.neo4j.kernel.logging.SystemOutLogging
import org.neo4j.unsafe.impl.batchimport.Configuration
import org.neo4j.unsafe.impl.batchimport.InputIterable
import org.neo4j.unsafe.impl.batchimport.InputIterator
import org.neo4j.unsafe.impl.batchimport.ParallelBatchImporter
import org.neo4j.unsafe.impl.batchimport.cache.idmapping.IdGenerator
import org.neo4j.unsafe.impl.batchimport.cache.idmapping.IdGenerators
import org.neo4j.unsafe.impl.batchimport.cache.idmapping.IdMapper
import org.neo4j.unsafe.impl.batchimport.cache.idmapping.IdMappers
import org.neo4j.unsafe.impl.batchimport.input.BadRelationshipsCollector
import org.neo4j.unsafe.impl.batchimport.input.Collector
import org.neo4j.unsafe.impl.batchimport.input.Input
import org.neo4j.unsafe.impl.batchimport.input.InputNode
import org.neo4j.unsafe.impl.batchimport.input.InputRelationship
import org.neo4j.unsafe.impl.batchimport.staging.ExecutionMonitors
import spock.lang.Ignore
import spock.lang.Specification

/**
 * @author Stefan Armbruster
 */
@Ignore
class HugeGraphIndexPopulationSpec extends Specification {


    @Rule
    TemporaryFolder storeDir = new TemporaryFolder()

    def words

    def random = new Random()

    def setup() {
        words = new File('/usr/share/hunspell/en_GB.dic').collect {

            def pos = it.indexOf("/")
            if (pos==-1) {
                it
            } else {
                it[0..pos-1]
            }
        }
    }

    def "create large graph"() {
        setup:
        populateLargeGraph()
        def db = new GraphDatabaseFactory().newEmbeddedDatabase(storeDir.root.absolutePath);
        Neo4jUtils.withSuccessTransaction(db) {
            IndexUtil.createIndexForNodes(db.index(), "tagIndex", "fulltext")
        }
        def cut = new IndexPopulationUnamanagedExtension(graphDatabaseService: db)

        when:
        def response = cut.populate("tagIndex", "Tag", ["name"], 10000)

        then:
        response.status == 200

        cleanup:
        db.shutdown()
    }


    @CompileStatic
    private void populateLargeGraph() {
        def importer = new ParallelBatchImporter(storeDir.root.absolutePath , Configuration.DEFAULT, new SystemOutLogging(), ExecutionMonitors.defaultVisible())
        importer.doImport(new Input() {

            @Override
            InputIterable<InputNode> nodes() {
                new InputIterable<InputNode>() {

                    @Override
                    InputIterator<InputNode> iterator() {
                        return new InputIterator.Adapter<InputNode>() {

                            long count = 0

                            @Override
                            boolean hasNext() {
                                return count < 1_000_000
                            }

                            @Override
                            InputNode next() {
                                count++
                                String[] labels = [ "Tag"];
                                def i = random.nextInt(words.size())
                                Object[] props = ["name", words[i]];
                                return new InputNode("null", count,  count*NodeStore.RECORD_SIZE, count, props, null, labels, null)
                            }
                        }
                    }

                    @Override
                    boolean supportsMultiplePasses() {
                        return false
                    }
                }
            }

            @Override
            InputIterable<InputRelationship> relationships() {
                new InputIterable<InputRelationship>() {
                    @Override
                    InputIterator<InputRelationship> iterator() {
                        return new InputIterator.Adapter<InputRelationship>()
                    }

                    @Override
                    boolean supportsMultiplePasses() {
                        return false
                    }
                }
            }

            @Override
            IdMapper idMapper() {
                return IdMappers.actual()
            }

            @Override
            IdGenerator idGenerator() {
                return IdGenerators.fromInput()
            }

            @Override
            boolean specificRelationshipIds() {
                return false
            }

            @Override
            Collector<InputRelationship> badRelationshipsCollector(OutputStream out) {
                return new BadRelationshipsCollector(out, 0)
            }
        })

    }
}
