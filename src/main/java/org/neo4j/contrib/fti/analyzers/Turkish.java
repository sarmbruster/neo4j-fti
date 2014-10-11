package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.tr.TurkishAnalyzer;
import org.apache.lucene.util.Version;

public final class Turkish extends AbstractDelegatingAnalyzer {

    public Turkish() {
        delegate = new TurkishAnalyzer(Version.LUCENE_36);
    }
}
