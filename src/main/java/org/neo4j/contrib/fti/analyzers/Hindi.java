package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.hi.HindiAnalyzer;
import org.apache.lucene.util.Version;

public final class Hindi extends AbstractDelegatingAnalyzer {

    public Hindi() {
        delegate = new HindiAnalyzer(Version.LUCENE_36);
    }
}
