package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.util.Version;

public final class English extends AbstractDelegatingAnalyzer {

    public English() {
        delegate = new EnglishAnalyzer(Version.LUCENE_36);
    }
}
