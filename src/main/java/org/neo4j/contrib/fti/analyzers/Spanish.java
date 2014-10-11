package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.util.Version;

public final class Spanish extends AbstractDelegatingAnalyzer {

    public Spanish() {
        delegate = new SpanishAnalyzer(Version.LUCENE_36);
    }
}
