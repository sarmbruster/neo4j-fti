package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.util.Version;

public final class Persian extends AbstractDelegatingAnalyzer {

    public Persian() {
        delegate = new PersianAnalyzer(Version.LUCENE_36);
    }
}
