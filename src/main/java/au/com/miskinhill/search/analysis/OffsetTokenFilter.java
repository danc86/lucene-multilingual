package au.com.miskinhill.search.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

public class OffsetTokenFilter extends TokenFilter {

    private final OffsetAttribute offsetAttribute;
	private int offset;

	protected OffsetTokenFilter(TokenStream input, int offset) {
		super(input);
		this.offset = offset;
        this.offsetAttribute = addAttribute(OffsetAttribute.class);
	}
	
    @Override
    public boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
            if (offset != 0) {
                offsetAttribute.setOffset(offsetAttribute.startOffset() + offset,
                        offsetAttribute.endOffset() + offset);
            }
            return true;
        } else {
            return false;
        }
    }

}
