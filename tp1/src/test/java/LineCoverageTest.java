import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TP1 - Line Coverage Tests (Couverture des Lignes)
 * Objectif: couvrir toutes les lignes de code exécutables au moins une fois.
 */
public class LineCoverageTest {

    // =========================================================
    // Exercice 1 - Palindrome
    // NOTE: La méthode isPalindrome contient un BUG (voir README).
    //       Les tests ci-dessous sont écrits pour le code tel quel.
    // =========================================================

    @Test
    void palindrome_nullThrowsException() {
        // Couvre: if (s == null) + throw
        assertThrows(NullPointerException.class, () -> Palindrome.isPalindrome(null));
    }

    @Test
    void palindrome_emptyString() {
        // Couvre: toLowerCase, replaceAll, i=0, j=-1, while(i<j) false, return true
        assertTrue(Palindrome.isPalindrome(""));
    }

    @Test
    void palindrome_singleChar() {
        // Couvre: chemin return true (i==j, boucle non exécutée)
        assertTrue(Palindrome.isPalindrome("a"));
    }

    @Test
    void palindrome_withSpaces() {
        // Couvre: replaceAll supprimant les espaces
        // "kayak" est un palindrome (sans bug), mais à cause du bug i--, j++
        // la boucle diverge. On teste juste que l'appel ne plante pas pour ""
        assertDoesNotThrow(() -> Palindrome.isPalindrome("a a"));
    }

    // =========================================================
    // Exercice 2 - Anagram
    // NOTE: La méthode isAnagram contient un BUG (voir README).
    // =========================================================

    @Test
    void anagram_nullFirstThrowsException() {
        // Couvre: if (s1 == null || s2 == null) + throw
        assertThrows(NullPointerException.class, () -> Anagram.isAnagram(null, "abc"));
    }

    @Test
    void anagram_nullSecondThrowsException() {
        assertThrows(NullPointerException.class, () -> Anagram.isAnagram("abc", null));
    }

    @Test
    void anagram_differentLengths() {
        // Couvre: if (s1.length() != s2.length()) return false
        assertFalse(Anagram.isAnagram("ab", "abc"));
    }

    @Test
    void anagram_sameLengthValid() {
        // Couvre: boucle for, count[], return true/false
        // BUG présent: i <= s1.length() provoque StringIndexOutOfBoundsException
        assertThrows(StringIndexOutOfBoundsException.class,
                () -> Anagram.isAnagram("chien", "niche"));
    }

    // =========================================================
    // Exercice 3 - BinarySearch
    // NOTE: La méthode binarySearch contient un BUG (voir README).
    // =========================================================

    @Test
    void binarySearch_nullThrowsException() {
        assertThrows(NullPointerException.class, () -> BinarySearch.binarySearch(null, 5));
    }

    @Test
    void binarySearch_emptyArray() {
        // Couvre: low=0, high=-1, while(low<high) false, return -1
        assertEquals(-1, BinarySearch.binarySearch(new int[]{}, 5));
    }

    @Test
    void binarySearch_singleElementNotFound() {
        // Couvre: low=0, high=0, while false, return -1
        assertEquals(-1, BinarySearch.binarySearch(new int[]{3}, 5));
    }

    @Test
    void binarySearch_elementInMiddle() {
        // Couvre: mid calculé, array[mid]==element, return mid
        assertEquals(1, BinarySearch.binarySearch(new int[]{1, 3, 5}, 3));
    }

    @Test
    void binarySearch_elementGreaterThanMid() {
        // Couvre: array[mid] <= element, low = mid + 1
        assertEquals(-1, BinarySearch.binarySearch(new int[]{1, 3, 5, 7}, 6));
    }

    @Test
    void binarySearch_elementLessThanMid() {
        // Couvre: else high = mid - 1
        assertEquals(-1, BinarySearch.binarySearch(new int[]{1, 3, 5, 7}, 2));
    }

    // =========================================================
    // Exercice 4 - QuadraticEquation
    // =========================================================

    @Test
    void quadratic_aZeroThrowsException() {
        // Couvre: if (a == 0) + throw
        assertThrows(IllegalArgumentException.class, () -> QuadraticEquation.solve(0, 1, 1));
    }

    @Test
    void quadratic_negativeDelta() {
        // Couvre: delta < 0, return null
        assertNull(QuadraticEquation.solve(1, 0, 1));
    }

    @Test
    void quadratic_zeroDelta() {
        // Couvre: delta == 0, return new double[]{-b/(2a)}
        double[] result = QuadraticEquation.solve(1, -2, 1);
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(1.0, result[0], 1e-9);
    }

    @Test
    void quadratic_positiveDelta() {
        // Couvre: delta > 0, return two roots
        double[] result = QuadraticEquation.solve(1, -5, 6);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    // =========================================================
    // Exercice 5 - RomanNumeral
    // NOTE: La méthode toRoman contient un BUG (voir README).
    // =========================================================

    @Test
    void roman_belowRangeThrowsException() {
        // Couvre: if (n < 1 || n > 3999) + throw
        assertThrows(IllegalArgumentException.class, () -> RomanNumeral.toRoman(0));
    }

    @Test
    void roman_aboveRangeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> RomanNumeral.toRoman(4000));
    }

    @Test
    void roman_validInput() {
        // BUG: i <= symbols.length provoque ArrayIndexOutOfBoundsException
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> RomanNumeral.toRoman(1));
    }

    // =========================================================
    // Exercice 6 - FizzBuzz
    // =========================================================

    @Test
    void fizzBuzz_invalidThrowsException() {
        // Couvre: if (n <= 1) + throw
        assertThrows(IllegalArgumentException.class, () -> FizzBuzz.fizzBuzz(1));
    }

    @Test
    void fizzBuzz_fizzBuzz() {
        // Couvre: n % 15 == 0, return "FizzBuzz"
        assertEquals("FizzBuzz", FizzBuzz.fizzBuzz(15));
    }

    @Test
    void fizzBuzz_fizz() {
        // Couvre: n % 3 == 0, return "Fizz"
        assertEquals("Fizz", FizzBuzz.fizzBuzz(9));
    }

    @Test
    void fizzBuzz_buzz() {
        // Couvre: n % 5 == 0, return "Buzz"
        assertEquals("Buzz", FizzBuzz.fizzBuzz(10));
    }

    @Test
    void fizzBuzz_number() {
        // Couvre: return String.valueOf(n)
        assertEquals("7", FizzBuzz.fizzBuzz(7));
    }
}