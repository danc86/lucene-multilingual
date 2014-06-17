Utilities for working with multilingual text in Lucene.

``CyrillicTransliteratingFilter`` injects a Latin transliteration in the 
same position as tokens containing Cyrillic characters. For example, 
this makes it possible to match the text ``Pasternak’s Повесть`` with 
the query ``pasternak's povest``.

``XMLTokenizer`` tokenizes an XML document, using different Analyzers 
for each language in the document identified by the ``lang`` attribute.
