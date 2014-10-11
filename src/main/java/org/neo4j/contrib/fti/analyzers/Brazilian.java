package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.util.Version;

public final class Brazilian extends AbstractDelegatingAnalyzer {

    public Brazilian() {
        delegate = new BrazilianAnalyzer(Version.LUCENE_36);
    }
}
