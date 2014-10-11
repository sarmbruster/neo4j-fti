package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.util.Version;

public final class French extends AbstractDelegatingAnalyzer {

    public French() {
        delegate = new FrenchAnalyzer(Version.LUCENE_36);
    }
}
