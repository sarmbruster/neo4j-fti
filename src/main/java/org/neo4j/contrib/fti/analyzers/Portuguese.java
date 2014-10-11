package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.util.Version;

public final class Portuguese extends AbstractDelegatingAnalyzer {

    public Portuguese() {
        delegate = new PortugueseAnalyzer(Version.LUCENE_36);
    }
}
