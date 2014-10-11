package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.bg.BulgarianAnalyzer;
import org.apache.lucene.util.Version;

public final class Bulgarian extends AbstractDelegatingAnalyzer {

    public Bulgarian() {
        delegate = new BulgarianAnalyzer(Version.LUCENE_36);
    }
}
