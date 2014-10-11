package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.util.Version;

public final class Arabic extends AbstractDelegatingAnalyzer {

    public Arabic() {
        delegate = new ArabicAnalyzer(Version.LUCENE_36);
    }
}
