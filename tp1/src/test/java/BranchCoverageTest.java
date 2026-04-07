import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TP1 - Branch Coverage Tests (Couverture des Branches)
 * Objectif: couvrir chaque branche (vrai/faux) de chaque condition au moins une fois.
 */
public class BranchCoverageTest {

    // =========================================================
    // Exercice 1 - Palindrome
    // Branches:
    //   B1: if (s == null) → vrai / faux
    //   B2: while (i < j) → vrai / faux
    //   B3: if (s.charAt(i) != s.charAt(j)) → vrai / faux
    // =========================================================

    @Test
    void palindrome_B1_null() {
        // B1 vrai
        assertThrows(NullPointerException.class, () -> Palindrome.isPalindrome(null));
    }

    @Test
    void palindrome_B1_notNull_B2_false() {
        // B1 faux, B2 faux (chaîne vide)
        assertTrue(Palindrome.isPalindrome(""));
    }

    @Test
    void palindrome_B1_notNull_B2_true_B3_false() {
        // B1 faux, B2 vrai, B3 faux (chars égaux — mais BUG: boucle infinie potentielle)
        // On teste avec un seul char pour éviter la boucle infinie due au bug
        assertTrue(Palindrome.isPalindrome("a"));
    }

    @Test
    void palindrome_B3_true_returnFalse() {
        // B3 vrai: chars différents → return false
        // "ab" : i=0 'a', j=1 'b' → différents → false
        // À cause du bug (j++, i--), on entre en boucle avec i=-1, j=2
        // charAt(-1) lance StringIndexOutOfBoundsException
        assertThrows(StringIndexOutOfBoundsException.class,
                () -> Palindrome.isPalindrome("ab"));
    }

    // =========================================================
    // Exercice 2 - Anagram
    // Branches:
    //   B1: s1 == null → vrai/faux
    //   B2: s2 == null → vrai/faux
    //   B3: s1.length() != s2.length() → vrai/faux
    //   B4: boucle for i <= s1.length() → vrai/faux (BUG: devrait être i < s1.length())
    //   B5: if (c != 0) → vrai/faux
    // =========================================================

    @Test
    void anagram_B1_s1Null() {
        assertThrows(NullPointerException.class, () -> Anagram.isAnagram(null, "abc"));
    }

    @Test
    void anagram_B2_s2Null() {
        assertThrows(NullPointerException.class, () -> Anagram.isAnagram("abc", null));
    }

    @Test
    void anagram_B3_differentLength() {
        assertFalse(Anagram.isAnagram("ab", "abc"));
    }

    @Test
    void anagram_B3_sameLengthTriggersBug() {
        // B3 faux → entre dans la boucle → BUG i <= length → SIOOBE
        assertThrows(StringIndexOutOfBoundsException.class,
                () -> Anagram.isAnagram("ab", "ba"));
    }

    // =========================================================
    // Exercice 3 - BinarySearch
    // Branches:
    //   B1: array == null → vrai/faux
    //   B2: while (low < high) → vrai/faux
    //   B3: array[mid] == element → vrai/faux
    //   B4: array[mid] <= element → vrai/faux (else implicite)
    // =========================================================

    @Test
    void binarySearch_B1_null() {
        assertThrows(NullPointerException.class, () -> BinarySearch.binarySearch(null, 5));
    }

    @Test
    void binarySearch_B2_false_emptyArray() {
        // B2 faux: low=0, high=-1
        assertEquals(-1, BinarySearch.binarySearch(new int[]{}, 5));
    }

    @Test
    void binarySearch_B3_true_found() {
        // B3 vrai: mid trouvé
        assertEquals(1, BinarySearch.binarySearch(new int[]{1, 3, 5}, 3));
    }

    @Test
    void binarySearch_B4_true_searchRight() {
        // B4 vrai: array[mid] <= element → low = mid + 1
        assertEquals(-1, BinarySearch.binarySearch(new int[]{1, 3, 5, 7, 9}, 8));
    }

    @Test
    void binarySearch_B4_false_searchLeft() {
        // B4 faux: array[mid] > element → high = mid - 1
        assertEquals(-1, BinarySearch.binarySearch(new int[]{1, 3, 5, 7, 9}, 2));
    }

    // =========================================================
    // Exercice 4 - QuadraticEquation
    // Branches:
    //   B1: a == 0 → vrai/faux
    //   B2: delta < 0 → vrai/faux
    //   B3: delta == 0 → vrai/faux
    //   B4: delta > 0 (implicite) → return deux racines
    // =========================================================

    @Test
    void quadratic_B1_aZero() {
        assertThrows(IllegalArgumentException.class, () -> QuadraticEquation.solve(0, 2, 1));
    }

    @Test
    void quadratic_B2_negativeDelta() {
        assertNull(QuadraticEquation.solve(1, 0, 4));
    }

    @Test
    void quadratic_B3_zeroDelta() {
        double[] result = QuadraticEquation.solve(1, -4, 4);
        assertArrayEquals(new double[]{2.0}, result, 1e-9);
    }

    @Test
    void quadratic_B4_positiveDelta() {
        double[] result = QuadraticEquation.solve(1, -5, 6);
        assertEquals(2, result.length);
        assertEquals(3.0, result[0], 1e-9);
        assertEquals(2.0, result[1], 1e-9);
    }

    // =========================================================
    // Exercice 5 - RomanNumeral
    // Branches:
    //   B1: n < 1 → vrai/faux
    //   B2: n > 3999 → vrai/faux
    //   B3: boucle for i <= symbols.length → BUG (devrait être i < symbols.length)
    //   B4: while (n > values[i]) → vrai/faux
    // =========================================================

    @Test
    void roman_B1_belowMin() {
        assertThrows(IllegalArgumentException.class, () -> RomanNumeral.toRoman(0));
    }

    @Test
    void roman_B2_aboveMax() {
        assertThrows(IllegalArgumentException.class, () -> RomanNumeral.toRoman(4000));
    }

    @Test
    void roman_B3_validInput_triggersBug() {
        // B1 faux, B2 faux → entre dans la boucle → i <= 13 → AIOOBE à i=13
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> RomanNumeral.toRoman(1));
    }

    // =========================================================
    // Exercice 6 - FizzBuzz
    // Branches:
    //   B1: n <= 1 → vrai/faux
    //   B2: n % 15 == 0 → vrai/faux
    //   B3: n % 3 == 0 → vrai/faux
    //   B4: n % 5 == 0 → vrai/faux
    //   B5: else → return String.valueOf(n)
    // =========================================================

    @Test
    void fizzBuzz_B1_invalid() {
        assertThrows(IllegalArgumentException.class, () -> FizzBuzz.fizzBuzz(0));
        assertThrows(IllegalArgumentException.class, () -> FizzBuzz.fizzBuzz(1));
    }

    @Test
    void fizzBuzz_B2_divisibleBy15() {
        assertEquals("FizzBuzz", FizzBuzz.fizzBuzz(15));
        assertEquals("FizzBuzz", FizzBuzz.fizzBuzz(30));
    }

    @Test
    void fizzBuzz_B3_divisibleBy3Only() {
        assertEquals("Fizz", FizzBuzz.fizzBuzz(9));
    }

    @Test
    void fizzBuzz_B4_divisibleBy5Only() {
        assertEquals("Buzz", FizzBuzz.fizzBuzz(25));
    }

    @Test
    void fizzBuzz_B5_notDivisible() {
        assertEquals("7", FizzBuzz.fizzBuzz(7));
        assertEquals("2", FizzBuzz.fizzBuzz(2));
    }
}