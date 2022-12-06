package info.nightscout.comboctl.parser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TokenizationTest {
    @Test
    fun checkBasicPatternMatch() {
        // Try to match the LARGE_BASAL symbol pattern in the testFrameMainScreenWithTimeSeparator.
        // That symbol is present at position (0,9).
        // Trying to match it at those coordinates is expected to succeed,
        // while trying to match it slightly to the right should fail.

        val largeBasalGlyphPattern = glyphPatterns[Glyph.LargeSymbol(LargeSymbol.BASAL)]!!

        val result1 = checkIfPatternMatchesAt(
            testFrameMainScreenWithTimeSeparator,
            largeBasalGlyphPattern,
            0, 8
        )
        Assertions.assertTrue(result1)

        val result2 = checkIfPatternMatchesAt(
            testFrameMainScreenWithTimeSeparator,
            largeBasalGlyphPattern,
            1, 8
        )
        Assertions.assertFalse(result2)
    }

    @Test
    fun checkMainScreenTokenization() {
        // Look for tokens in the main menu display frame.
        // The pattern matching algorithm scans the frame
        // left to right, top to bottom, and tries the
        // large patterns first.
        // The main screen contains symbols that yield
        // ambiguities due to overlapping tokens. For
        // example, the basal icon contains sub-patterns
        // that match the cyrillic letter "п". These must
        // be filtered out by findTokens().

        val tokens = findTokens(testFrameMainScreenWithTimeSeparator)

        Assertions.assertEquals(13, tokens.size)

        val iterator = tokens.iterator()

        Assertions.assertEquals(Glyph.SmallSymbol(SmallSymbol.CLOCK), iterator.next().glyph)

        Assertions.assertEquals(Glyph.SmallDigit(1), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallDigit(0), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallSymbol(SmallSymbol.SEPARATOR), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallDigit(2), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallDigit(0), iterator.next().glyph)

        Assertions.assertEquals(Glyph.LargeSymbol(LargeSymbol.BASAL), iterator.next().glyph)

        Assertions.assertEquals(Glyph.LargeDigit(0), iterator.next().glyph)
        Assertions.assertEquals(Glyph.LargeSymbol(LargeSymbol.DOT), iterator.next().glyph)
        Assertions.assertEquals(Glyph.LargeDigit(2), iterator.next().glyph)
        Assertions.assertEquals(Glyph.LargeDigit(0), iterator.next().glyph)

        Assertions.assertEquals(Glyph.LargeSymbol(LargeSymbol.UNITS_PER_HOUR), iterator.next().glyph)

        Assertions.assertEquals(Glyph.SmallDigit(1), iterator.next().glyph)
    }

    @Test
    fun checkStandardBolusTokenization() {
        // Look for tokens in the standard bolus display frame.
        // The pattern matching algorithm scans the frame
        // left to right, top to bottom, and tries the
        // large patterns first.
        // The standard bolus screen contains mostly letters,
        // but also a LARGE_BOLUS symbol at the very bottom of
        // the screen, thus testing that patterns are also properly
        // matched if they are at a border.

        val tokens = findTokens(testFrameStandardBolusMenuScreen)

        Assertions.assertEquals(14, tokens.size)

        val iterator = tokens.iterator()

        Assertions.assertEquals(Glyph.SmallCharacter('S'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('T'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('A'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('N'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('D'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('A'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('R'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('D'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('B'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('O'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('L'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('U'), iterator.next().glyph)
        Assertions.assertEquals(Glyph.SmallCharacter('S'), iterator.next().glyph)

        Assertions.assertEquals(Glyph.LargeSymbol(LargeSymbol.BOLUS), iterator.next().glyph)
    }
}
