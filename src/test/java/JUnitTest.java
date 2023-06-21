import org.hamcrest.Matcher;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import uni.makarov.verification.Main;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

public class JUnitTest {

    private static Main repetitionExclusion;

    @BeforeAll
    public static void setUpAll() {
        System.out.println("Running all tests...");
    }

    @BeforeEach
    public void setUp() {
        repetitionExclusion = new Main();
    }

    @Test
    public void RepeatingLetters_WordsRemoved() {
        String input = "marinara nori joker Sheffield Worcestershire mango Kuwait";
        ArrayList<String> expected = new ArrayList<>(List.of("nori joker mango Kuwait".split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(input);
        assertEquals(actual, expected);
    }

    @Test
    public void RepeatingLettersWithSymbolsNewlines_WordsRemovedSymbolsIgnored() {
        String input = "MOOOOO!1!!!1! \n\n scar?y, is it ???? \t\t crazy. cow!";
        ArrayList<String> expected = new ArrayList<>(List.of("scary is it crazy cow".split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(input);
        assertEquals(actual, expected);
    }

    @Test
    public void UnicodeLatinLetters_NormalizedToASCII() {
        //Note: Some unicode latin extensions are not supported
        //for decomposition, and are ignored as special symbols
        String input = "À ç ì ñ û ÿ : ł Ʈ Ǿ";
        ArrayList<String> expected = new ArrayList<>(List.of("A c i n u y".split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(input);
        assertEquals(expected, actual);
    }

    @Test
    public void UnicodeNonLatinLetters_IgnoredAsSpecialSymbols() {
        String input = "ぁ ヘ ᄚ ㄶ ㌇ ሕ Թ";
        assertTrue(repetitionExclusion.removeLetterRepeatingWords(input).isEmpty());
    }

    @Test
    public void WordsWithHyphensAndDashes_HyphensAcceptDashIgnore() {
        String input = "na-do - ko-ko, kan-ryo-chi!";
        ArrayList<String> expected = new ArrayList<>(List.of("na-do kan-ryo-chi".split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(input);
        assertEquals(expected, actual);
    }

    @Test
    public void Hamcrest_IsBiggerThanNum_HasWordsThatStartsWithString() {
        String input = "Billy Burgy went for a burg at the Burgy Burg";
        String start = "burg";
        ArrayList<String> expected = new ArrayList<>(List.of("Burgy went for a burg at the Burg".split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(input);
        assertThat(actual, is(expected));
        assertThat(actual, allOf((Matcher<? super ArrayList<String>>) hasSize(greaterThanOrEqualTo(4)), hasItems(startsWith(start))));
    }

    @Test
    public void Hamcrest_DoesNotHaveItem() {
        String input = "kan-ryo-chi, kanryochi? kAnRyOcHi, kanryo-ssi!";
        ArrayList<String> expected = new ArrayList<>(List.of("kan-ryo-chi kanryochi kAnRyOcHi".split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(input);
        assertThat(actual, is(expected));
        assertThat(actual, not(hasItem("kanryo-ssi")));
    }

    @ParameterizedTest
    @CsvSource(value = {":", "Termite, melting the Granite of Science : melting the Granite of",
            "Tango Echo Sierra Tango : Tango Echo",
            "ufo??? Flin-ging?!!!? \t alìen ufo mysti-1que much! 61 79 79 20 6c 6d 61 6f : ufo alien mysti-que much c d f"},
            delimiter = ':')
    public void ParameterizedTest(String actualText, String expectedText) {
        Assumptions.assumeFalse(actualText == null,"Assumption failed: Source is null");
        Assumptions.assumeFalse(actualText.isBlank(),"Assumption failed: Source is empty");

        ArrayList<String> expected = new ArrayList<>(List.of(expectedText.split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(actualText);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EmptySource
    public void EmptyText_ReturnsEmptyArray(String text) {
        assertTrue(repetitionExclusion.removeLetterRepeatingWords(text).isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void NullText_ThrowsException(String text) {
        assertThrows(NullPointerException.class, () -> repetitionExclusion.removeLetterRepeatingWords(text));
    }
}
