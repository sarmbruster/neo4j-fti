package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.no.NorwegianAnalyzer;
import org.apache.lucene.util.Version;

public final class Norwegian extends AbstractDelegatingAnalyzer {

    public Norwegian() {
        delegate = new NorwegianAnalyzer(Version.LUCENE_36);
    }
}
