package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.hy.ArmenianAnalyzer;
import org.apache.lucene.util.Version;

public final class Armenian extends AbstractDelegatingAnalyzer {

    public Armenian() {
        delegate = new ArmenianAnalyzer(Version.LUCENE_36);
    }
}
