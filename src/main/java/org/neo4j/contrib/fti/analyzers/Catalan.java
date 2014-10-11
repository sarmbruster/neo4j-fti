package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.ca.CatalanAnalyzer;
import org.apache.lucene.util.Version;

public final class Catalan extends AbstractDelegatingAnalyzer {

    public Catalan() {
        delegate = new CatalanAnalyzer(Version.LUCENE_36);
    }
}
