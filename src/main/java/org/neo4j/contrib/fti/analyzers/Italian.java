package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.util.Version;

public final class Italian extends AbstractDelegatingAnalyzer {

    public Italian() {
        delegate = new ItalianAnalyzer(Version.LUCENE_36);
    }
}
