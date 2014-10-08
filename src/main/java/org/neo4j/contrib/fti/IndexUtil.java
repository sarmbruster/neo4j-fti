package org.neo4j.contrib.fti;

import org.neo4j.graphdb.index.IndexManager;

import java.util.Collections;

public class IndexUtil {

    public static void createIndexForNodes(IndexManager indexManager, String indexName, String analyzerClassName) {
        if (indexManager.existsForNodes(indexName)) {
            throw new IllegalStateException("Index " + indexName + " does already exist!"); // TODO: consider not throwing an exception and silently do nothing
        } else {
            indexManager.forNodes(indexName, Collections.singletonMap("analyzer", analyzerClassName));
        }
    }

}
