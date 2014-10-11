package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.util.Version;

public final class CJK extends AbstractDelegatingAnalyzer {

    public CJK() {
        delegate = new CJKAnalyzer(Version.LUCENE_36);
    }
}
