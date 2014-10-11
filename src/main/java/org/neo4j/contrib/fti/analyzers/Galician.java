package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.gl.GalicianAnalyzer;
import org.apache.lucene.util.Version;

public final class Galician extends AbstractDelegatingAnalyzer {

    public Galician() {
        delegate = new GalicianAnalyzer(Version.LUCENE_36);
    }
}
