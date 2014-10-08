package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.util.Version;

public class German extends AbstractDelegatingAnalyzer {

    public German() {
        delegate = new GermanAnalyzer(Version.LUCENE_36);
    }
}
