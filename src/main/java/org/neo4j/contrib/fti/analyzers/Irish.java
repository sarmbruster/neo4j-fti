package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.ga.IrishAnalyzer;
import org.apache.lucene.util.Version;

public final class Irish extends AbstractDelegatingAnalyzer {

    public Irish() {
        delegate = new IrishAnalyzer(Version.LUCENE_36);
    }
}
