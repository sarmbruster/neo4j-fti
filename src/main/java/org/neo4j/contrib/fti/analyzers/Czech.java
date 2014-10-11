package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.util.Version;

public final class Czech extends AbstractDelegatingAnalyzer {

    public Czech() {
        delegate = new CzechAnalyzer(Version.LUCENE_36);
    }
}
