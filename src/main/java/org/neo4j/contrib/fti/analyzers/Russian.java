package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.util.Version;

public final class Russian extends AbstractDelegatingAnalyzer {

    public Russian() {
        delegate = new RussianAnalyzer(Version.LUCENE_36);
    }
}
