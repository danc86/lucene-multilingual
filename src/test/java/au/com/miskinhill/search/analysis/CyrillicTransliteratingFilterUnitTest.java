package au.com.miskinhill.search.analysis;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;
import org.junit.Test;

public class CyrillicTransliteratingFilterUnitTest {
    
    private static final class FakeTokenStream extends TokenStream {
        private final CharTermAttribute termAttribute;
        private final OffsetAttribute offsetAttribute;
        private final PositionIncrementAttribute posIncAttribute;
        private final Queue<Token> tokens;
        
        public FakeTokenStream(Token... tokens) {
            this.tokens = new LinkedList<Token>(Arrays.asList(tokens));
            this.termAttribute = addAttribute(CharTermAttribute.class);
            this.offsetAttribute = addAttribute(OffsetAttribute.class);
            this.posIncAttribute = addAttribute(PositionIncrementAttribute.class);
        }

        @Override
        public final boolean incrementToken() throws IOException {
            if (tokens.isEmpty())
                return false;
            clearAttributes();
            Token next = tokens.remove();
            termAttribute.setEmpty();
            termAttribute.append(next);
            offsetAttribute.setOffset(next.startOffset(), next.endOffset());
            posIncAttribute.setPositionIncrement(next.getPositionIncrement());
            return true;
        }
    }

    @Test
    public void shouldPassOnTokensWithoutCyrillicUntouched() throws IOException {
        Token asdf = new Token();
        asdf.append("asdf");
        asdf.setStartOffset(1);
        asdf.setEndOffset(4);
        TokenFilter filter = new CyrillicTransliteratingFilter(
                new FakeTokenStream(asdf));
        assertTrue(filter.incrementToken());
        assertAttributes(filter, "asdf", 1, 4, 1);
        assertFalse(filter.incrementToken());
    }
    
    @Test
    public void shouldTransliterateCyrillicTokens() throws IOException {
        Token igraCyrillic = new Token();
        igraCyrillic.append("игра");
        igraCyrillic.setStartOffset(1);
        igraCyrillic.setEndOffset(4);
        TokenFilter filter = new CyrillicTransliteratingFilter(
                new FakeTokenStream(igraCyrillic));
        assertTrue(filter.incrementToken());
        assertAttributes(filter, "игра", 1, 4, 1);
        assertTrue(filter.incrementToken());
        assertAttributes(filter, "igra", 1, 4, 0);
        assertFalse(filter.incrementToken());
    }
    
    @Test
    public void shouldTransliterateTokensWithMixedLatinAndCyrillic() throws IOException {
        Token mixed = new Token();
        mixed.append("interнет");
        mixed.setStartOffset(1);
        mixed.setEndOffset(8);
        TokenFilter filter = new CyrillicTransliteratingFilter(
                new FakeTokenStream(mixed));
        assertTrue(filter.incrementToken());
        assertAttributes(filter, "interнет", 1, 8, 1);
        assertTrue(filter.incrementToken());
        assertAttributes(filter, "internet", 1, 8, 0);
        assertFalse(filter.incrementToken());
    }
    
    private void assertAttributes(AttributeSource source, String term,
            int start, int end, int posInc) {
        assertThat(source.getAttribute(CharTermAttribute.class).toString(),
                equalTo(term));
        assertThat(source.getAttribute(OffsetAttribute.class).startOffset(),
                equalTo(start));
        assertThat(source.getAttribute(OffsetAttribute.class).endOffset(),
                equalTo(end));
        assertThat(source.getAttribute(PositionIncrementAttribute.class)
                .getPositionIncrement(), equalTo(posInc));
    }

}
