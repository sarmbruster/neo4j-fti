package org.neo4j.contrib.fti;

import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.MapUtil;

import java.util.Collections;
import java.util.Map;

public class IndexUtil {

    public static void createIndexForNodes(IndexManager indexManager, String indexName, String analyzerClassName) {
        if (indexManager.existsForNodes(indexName)) {
            throw new IllegalStateException("Index " + indexName + " does already exist!"); // TODO: consider not throwing an exception and silently do nothing
        } else {

            Map<String, String> params;
            if ("fulltext".equals(analyzerClassName)) {
                params = MapUtil.stringMap("type", "fulltext");
            } else {
                params = MapUtil.stringMap("analyzer", analyzerClassName);
            }
            indexManager.forNodes(indexName, params);
        }
    }

}
