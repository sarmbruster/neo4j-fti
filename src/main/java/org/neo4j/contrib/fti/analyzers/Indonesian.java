package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.id.IndonesianAnalyzer;
import org.apache.lucene.util.Version;

public final class Indonesian extends AbstractDelegatingAnalyzer {

    public Indonesian() {
        delegate = new IndonesianAnalyzer(Version.LUCENE_36);
    }
}
