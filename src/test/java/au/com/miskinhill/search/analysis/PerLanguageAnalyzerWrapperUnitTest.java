package au.com.miskinhill.search.analysis;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class PerLanguageAnalyzerWrapperUnitTest {
	
	private Analyzer defaultAnalyzer = createMock(Analyzer.class);
	private Analyzer enAnalyzer = createMock(Analyzer.class);
	private Analyzer ruAnalyzer = createMock(Analyzer.class);
	private PerLanguageAnalyzerMap plam;
	
	@Before
	public void setUp() {
	   plam = new PerLanguageAnalyzerMap(defaultAnalyzer);
       plam.addAnalyzer("en", enAnalyzer);
       plam.addAnalyzer("ru", ruAnalyzer);
	}

	@Test
	public void testGetAnalyzers() {
		assertThat(plam.getAnalyzers(), 
				equalTo(Arrays.asList(defaultAnalyzer, enAnalyzer, ruAnalyzer)));
	}
	
	@Test
	public void testTokenStreamEmptyLanguage() {
	    assertThat(plam.getAnalyzer(""), equalTo(defaultAnalyzer));
	}
	
	@Test
	public void testTokenStreamNullLanguage() {
	    assertThat(plam.getAnalyzer(null), equalTo(defaultAnalyzer));
	}
	
	@Test
	public void testTokenStreamSomeLanguage() {
	    assertThat(plam.getAnalyzer("en"), equalTo(enAnalyzer));
	}
	
}
