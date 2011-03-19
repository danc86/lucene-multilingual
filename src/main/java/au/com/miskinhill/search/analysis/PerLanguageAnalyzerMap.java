package au.com.miskinhill.search.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Returns an analyzer according to based on the language of the text
 * being analysed. The default sub-analyzer is given in the constructor; this is
 * used when the language is not specified, or when a language is specified for
 * which we have no specific sub-analyzer. Use
 * {@link #addAnalyzer(String, Analyzer)} to add a sub-analyzer for a specific
 * language.
 * <p>
 * Note that languages are matched by prefix, so that if a sub-analyzer has been
 * added for "en" (but not "en-AU"), it will be returned for "en-AU".
 */
public class PerLanguageAnalyzerMap {
    
    private static final Logger LOG = Logger.getLogger(PerLanguageAnalyzerMap.class.getName());

	protected Trie<Analyzer> analyzers;
	private List<Analyzer> analyzersList = new ArrayList<Analyzer>(); // easier than traversing the trie
	
	public PerLanguageAnalyzerMap(Analyzer defaultAnalyzer) {
		analyzers = new Trie<Analyzer>(defaultAnalyzer);
		analyzersList.add(defaultAnalyzer);
	}
	
	public void addAnalyzer(String language, Analyzer analyzer) {
		analyzers.put(language, analyzer);
		analyzersList.add(analyzer);
	}
	
	/**
	 * Returns a list of all sub-analyzers in this analyzer (including the default one).
	 */
	public List<Analyzer> getAnalyzers() {
		return analyzersList;
	}
	
	/**
	 * Returns an appropriate analyzer for the given language.
	 * 
	 * @param language ISO-639 language identifier
	 */
	// XXX TODO use java.util.Locale eventually (maybe with Locale#forLanguageTag added in 1.7?)
	public Analyzer getAnalyzer(String language) {
		if (language == null) language = "";
		Analyzer a = analyzers.get(language);
		if (a == analyzersList.get(0))
		    LOG.warning("Using default analyzer for language " + language);
		return a;
	}

}
