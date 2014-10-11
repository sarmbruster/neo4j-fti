package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.fi.FinnishAnalyzer;
import org.apache.lucene.util.Version;

public final class Finnish extends AbstractDelegatingAnalyzer {

    public Finnish() {
        delegate = new FinnishAnalyzer(Version.LUCENE_36);
    }
}
