package org.neo4j.contrib.fti;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.neo4j.kernel.lifecycle.LifecycleAdapter;

import java.util.Collections;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FullTextIndexingLifeCycle extends LifecycleAdapter {

    private IndexManager indexManager;
    private GraphDatabaseService graphDatabaseService;
    private Config config;
    private Future indexCreationFuture;

    public FullTextIndexingLifeCycle(GraphDatabaseService graphDatabaseService, Config config) {
        this.graphDatabaseService = graphDatabaseService;
        this.config = config;
        this.indexManager = graphDatabaseService.index();
    }

    /*@Override
    public void init() throws Throwable {

        System.out.println("HURZ init");

    }*/

    @Override
    public void start() throws Throwable {
        final String fulltextConfig = config.getParams().get("fullTextIndexes");
        if (fulltextConfig!=null) {
            // direct index creation will fail since DB is not yet started,
            // so using a scheduler to run this as soon as DB is up&running
            indexCreationFuture = new ScheduledThreadPoolExecutor(1).scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {

                    try (Transaction tx = graphDatabaseService.beginTx()) {
                        String[] indexes = fulltextConfig.split(",");
                        for (int i = 0; i < indexes.length; i++) {
                            String[] parts = indexes[i].split(":");
                            String indexName = parts[0];
                            String analyzerClassName = parts[1];
                            IndexUtil.createIndexForNodes(indexManager, indexName, analyzerClassName);
                        }
                        indexCreationFuture.cancel(false);
                        tx.success();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        // thrown if index creation fails while startup is still in progress
                    } catch (RuntimeException e) {
                        indexCreationFuture.cancel(false);
                        e.printStackTrace();
                        throw e;
                    }
                }

            }, 1, 5, TimeUnit.MILLISECONDS);
        }
    }
}
