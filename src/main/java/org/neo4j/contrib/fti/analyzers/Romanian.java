package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.util.Version;

public final class Romanian extends AbstractDelegatingAnalyzer {

    public Romanian() {
        delegate = new RomanianAnalyzer(Version.LUCENE_36);
    }
}
