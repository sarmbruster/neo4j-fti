package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.sv.SwedishAnalyzer;
import org.apache.lucene.util.Version;

public final class Swedish extends AbstractDelegatingAnalyzer {

    public Swedish() {
        delegate = new SwedishAnalyzer(Version.LUCENE_36);
    }
}
