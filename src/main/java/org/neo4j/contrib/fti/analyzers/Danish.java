package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.da.DanishAnalyzer;
import org.apache.lucene.util.Version;

public final class Danish extends AbstractDelegatingAnalyzer {

    public Danish() {
        delegate = new DanishAnalyzer(Version.LUCENE_36);
    }
}
