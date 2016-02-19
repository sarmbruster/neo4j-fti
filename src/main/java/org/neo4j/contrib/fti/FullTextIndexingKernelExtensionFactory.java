package org.neo4j.contrib.fti;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.lifecycle.Lifecycle;

public class FullTextIndexingKernelExtensionFactory extends KernelExtensionFactory<FullTextIndexingKernelExtensionFactory.DEPENDENCIES> {

    public interface DEPENDENCIES {
        GraphDatabaseService getGraphDatabaseService();
        Config getConfig();
    }

    public FullTextIndexingKernelExtensionFactory() {
        super("fullTextIndexing");
    }

    @Override
    public Lifecycle newKernelExtension(DEPENDENCIES dependencies) throws Throwable {
        return new FullTextIndexingLifeCycle(
                dependencies.getGraphDatabaseService(),
                dependencies.getConfig()
        );
    }
}
