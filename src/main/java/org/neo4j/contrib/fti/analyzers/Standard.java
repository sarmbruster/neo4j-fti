package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public final class Standard extends AbstractDelegatingAnalyzer {

    public Standard() {
        delegate = new StandardAnalyzer(Version.LUCENE_36);
    }
}
