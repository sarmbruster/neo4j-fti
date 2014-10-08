package org.neo4j.contrib.fti.analyzers;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * simple wrapper to make {@link org.apache.lucene.analysis.de.GermanAnalyzer} available for Neo4j.
 * NB: Analyzers used by Neo4j need to have a default constructor, Lucene shipped analyzer don't have this always, so we need to wrap.
 */
public class AbstractDelegatingAnalyzer extends Analyzer {

    protected Analyzer delegate;

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return delegate.tokenStream(fieldName, reader);
    }
}
