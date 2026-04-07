import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TP1 - Condition Coverage Tests (Couverture des Conditions)
 * Objectif: chaque condition booléenne atomique doit être évaluée à vrai ET à faux.
 *
 * Note: Pour Palindrome et Anagram, la couverture des conditions est identique
 * à la couverture des branches car les conditions sont simples (voir README).
 * Pour BinarySearch et FizzBuzz, des combinaisons supplémentaires sont nécessaires.
 */
public class ConditionCoverageTest {

    // =========================================================
    // Exercice 1 - Palindrome
    // Conditions atomiques:
    //   C1: s == null         → vrai / faux
    //   C2: i < j             → vrai / faux
    //   C3: charAt(i) != charAt(j) → vrai / faux
    //
    // NOTE: Tests identiques à BranchCoverageTest (conditions simples).
    //       Mentionné dans le README.
    // =========================================================

    @Test
    void palindrome_C1_null() {
        // C1 = vrai
        assertThrows(NullPointerException.class, () -> Palindrome.isPalindrome(null));
    }

    @Test
    void palindrome_C1_notNull_C2_false() {
        // C1 = faux, C2 = faux (chaîne vide)
        assertTrue(Palindrome.isPalindrome(""));
    }

    @Test
    void palindrome_C2_true_C3_false() {
        // C2 = vrai (i < j), C3 = faux (chars égaux)
        // "a" ne rentre pas dans la boucle, on utilise "" pour C2 faux seulement
        // Pour avoir C3 faux avec C2 vrai, il faudrait un vrai palindrome
        // mais le BUG empêche cela. On documente ici l'intention:
        // isPalindrome("aa") devrait tester C3=faux (deux chars identiques)
        // mais à cause du bug, charAt(-1) ou charAt(2) serait appelé.
        assertDoesNotThrow(() -> Palindrome.isPalindrome("a"));
    }

    @Test
    void palindrome_C3_true() {
        // C3 = vrai: chars différents → return false
        // "ab": charAt(0)='a' != charAt(1)='b' → mais BUG → exception
        assertThrows(StringIndexOutOfBoundsException.class,
                () -> Palindrome.isPalindrome("ab"));
    }

    // =========================================================
    // Exercice 2 - Anagram
    // Conditions atomiques:
    //   C1: s1 == null        → vrai / faux
    //   C2: s2 == null        → vrai / faux
    //   C3: s1.length() != s2.length() → vrai / faux
    //   C4: i <= s1.length()  → vrai / faux (BUG: devrait être i < s1.length())
    //   C5: c != 0            → vrai / faux
    // =========================================================

    @Test
    void anagram_C1_true() {
        assertThrows(NullPointerException.class, () -> Anagram.isAnagram(null, "test"));
    }

    @Test
    void anagram_C1_false_C2_true() {
        assertThrows(NullPointerException.class, () -> Anagram.isAnagram("test", null));
    }

    @Test
    void anagram_C1_false_C2_false_C3_true() {
        // C1=faux, C2=faux, C3=vrai (longueurs différentes)
        assertFalse(Anagram.isAnagram("ab", "abc"));
    }

    @Test
    void anagram_C3_false_entryLoop() {
        // C3=faux → entre dans la boucle → BUG C4 (i<=length)
        assertThrows(StringIndexOutOfBoundsException.class,
                () -> Anagram.isAnagram("ab", "ba"));
    }

    // =========================================================
    // Exercice 3 - BinarySearch
    // Conditions atomiques:
    //   C1: array == null        → vrai / faux
    //   C2: low < high           → vrai / faux
    //   C3: array[mid] == element → vrai / faux
    //   C4: array[mid] <= element → vrai / faux
    // =========================================================

    @Test
    void binarySearch_C1_true() {
        assertThrows(NullPointerException.class, () -> BinarySearch.binarySearch(null, 5));
    }

    @Test
    void binarySearch_C1_false_C2_false() {
        assertEquals(-1, BinarySearch.binarySearch(new int[]{}, 5));
    }

    @Test
    void binarySearch_C2_true_C3_true() {
        // C2=vrai, C3=vrai: élément trouvé au milieu
        assertEquals(2, BinarySearch.binarySearch(new int[]{1, 3, 5, 7, 9}, 5));
    }

    @Test
    void binarySearch_C3_false_C4_true() {
        // C3=faux, C4=vrai: élément > mid → chercher à droite
        assertEquals(-1, BinarySearch.binarySearch(new int[]{1, 3, 5, 7, 9}, 8));
    }

    @Test
    void binarySearch_C3_false_C4_false() {
        // C3=faux, C4=faux: élément < mid → chercher à gauche
        assertEquals(-1, BinarySearch.binarySearch(new int[]{1, 3, 5, 7, 9}, 2));
    }

    // =========================================================
    // Exercice 4 - QuadraticEquation
    // Conditions atomiques:
    //   C1: a == 0    → vrai / faux
    //   C2: delta < 0 → vrai / faux
    //   C3: delta == 0 → vrai / faux
    //   (delta > 0 est implicite: C2=faux ET C3=faux)
    //
    // NOTE: Couverture identique à BranchCoverageTest (conditions simples).
    //       Mentionné dans le README.
    // =========================================================

    @Test
    void quadratic_C1_true() {
        assertThrows(IllegalArgumentException.class, () -> QuadraticEquation.solve(0, 1, 1));
    }

    @Test
    void quadratic_C1_false_C2_true() {
        // delta = 0 - 4*1*1 = -4 < 0
        assertNull(QuadraticEquation.solve(1, 0, 1));
    }

    @Test
    void quadratic_C2_false_C3_true() {
        // delta = 4 - 4*1*1 = 0
        double[] r = QuadraticEquation.solve(1, -2, 1);
        assertEquals(1, r.length);
        assertEquals(1.0, r[0], 1e-9);
    }

    @Test
    void quadratic_C2_false_C3_false() {
        // delta = 25 - 4*1*6 = 1 > 0
        double[] r = QuadraticEquation.solve(1, -5, 6);
        assertEquals(2, r.length);
        assertEquals(3.0, r[0], 1e-9);
        assertEquals(2.0, r[1], 1e-9);
    }

    // =========================================================
    // Exercice 5 - RomanNumeral
    // Conditions atomiques:
    //   C1: n < 1     → vrai / faux
    //   C2: n > 3999  → vrai / faux
    //   C3: i <= symbols.length → vrai / faux (BUG: devrait être i < symbols.length)
    //   C4: n > values[i]  → vrai / faux
    // =========================================================

    @Test
    void roman_C1_true() {
        assertThrows(IllegalArgumentException.class, () -> RomanNumeral.toRoman(-1));
    }

    @Test
    void roman_C1_false_C2_true() {
        assertThrows(IllegalArgumentException.class, () -> RomanNumeral.toRoman(4000));
    }

    @Test
    void roman_C1_false_C2_false_bugTriggered() {
        // C1=faux, C2=faux → boucle → BUG à i=13 (i <= 13 = symbols.length)
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> RomanNumeral.toRoman(1));
    }

    // =========================================================
    // Exercice 6 - FizzBuzz
    // Conditions atomiques:
    //   C1: n <= 1       → vrai / faux
    //   C2: n % 15 == 0  → vrai / faux
    //   C3: n % 3 == 0   → vrai / faux
    //   C4: n % 5 == 0   → vrai / faux
    //
    // NOTE: Couverture identique à BranchCoverageTest pour cet exercice.
    //       Mentionné dans le README.
    // =========================================================

    @Test
    void fizzBuzz_C1_true() {
        assertThrows(IllegalArgumentException.class, () -> FizzBuzz.fizzBuzz(1));
    }

    @Test
    void fizzBuzz_C1_false_C2_true() {
        assertEquals("FizzBuzz", FizzBuzz.fizzBuzz(15));
    }

    @Test
    void fizzBuzz_C2_false_C3_true() {
        // n=9: 9%15!=0, 9%3==0
        assertEquals("Fizz", FizzBuzz.fizzBuzz(9));
    }

    @Test
    void fizzBuzz_C2_false_C3_false_C4_true() {
        // n=25: 25%15!=0, 25%3!=0, 25%5==0
        assertEquals("Buzz", FizzBuzz.fizzBuzz(25));
    }

    @Test
    void fizzBuzz_C4_false_returnNumber() {
        // n=7: aucune condition satisfaite
        assertEquals("7", FizzBuzz.fizzBuzz(7));
    }

    @Test
    void fizzBuzz_C1_false_C2_false_C3_false_boundary() {
        // n=2: valeur minimale valide non divisible
        assertEquals("2", FizzBuzz.fizzBuzz(2));
    }
}