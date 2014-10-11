package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.eu.BasqueAnalyzer;
import org.apache.lucene.util.Version;

public final class Basque extends AbstractDelegatingAnalyzer {

    public Basque() {
        delegate = new BasqueAnalyzer(Version.LUCENE_36);
    }
}
