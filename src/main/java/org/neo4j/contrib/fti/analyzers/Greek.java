package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.el.GreekAnalyzer;
import org.apache.lucene.util.Version;

public final class Greek extends AbstractDelegatingAnalyzer {

    public Greek() {
        delegate = new GreekAnalyzer(Version.LUCENE_36);
    }
}
