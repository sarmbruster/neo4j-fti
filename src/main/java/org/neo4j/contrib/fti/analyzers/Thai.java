package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.th.ThaiAnalyzer;
import org.apache.lucene.util.Version;

public final class Thai extends AbstractDelegatingAnalyzer {

    public Thai() {
        delegate = new ThaiAnalyzer(Version.LUCENE_36);
    }
}
