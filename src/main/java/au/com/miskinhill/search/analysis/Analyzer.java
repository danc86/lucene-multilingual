package au.com.miskinhill.search.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.AttributeSource;

/**
 * Same as {@link org.apache.lucene.analysis.Analyzer Lucene's Analyzer} but
 * with a saner API.
 */
public interface Analyzer {
    
    TokenStream tokenizer(Reader input);
    
    TokenStream tokenizer(AttributeSource attributeSource, Reader input);
    
    TokenStream applyFilters(TokenStream input);

}
