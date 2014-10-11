package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.lv.LatvianAnalyzer;
import org.apache.lucene.util.Version;

public final class Latvian extends AbstractDelegatingAnalyzer {

    public Latvian() {
        delegate = new LatvianAnalyzer(Version.LUCENE_36);
    }
}
