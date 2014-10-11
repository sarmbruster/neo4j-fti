package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.util.Version;

public final class Dutch extends AbstractDelegatingAnalyzer {

    public Dutch() {
        delegate = new DutchAnalyzer(Version.LUCENE_36);
    }
}
