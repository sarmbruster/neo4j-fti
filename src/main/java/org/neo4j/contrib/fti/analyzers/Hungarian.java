package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.hu.HungarianAnalyzer;
import org.apache.lucene.util.Version;

public final class Hungarian extends AbstractDelegatingAnalyzer {

    public Hungarian() {
        delegate = new HungarianAnalyzer(Version.LUCENE_36);
    }
}
