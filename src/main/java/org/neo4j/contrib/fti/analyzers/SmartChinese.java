package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.util.Version;

public final class SmartChinese extends AbstractDelegatingAnalyzer {

    public SmartChinese() {
        delegate = new SmartChineseAnalyzer(Version.LUCENE_36);
    }
}
