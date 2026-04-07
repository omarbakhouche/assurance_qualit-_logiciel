<<<<<<< HEAD
Conformément aux instructions du TP, voici les bugs identifiés dans les classes fournies et les corrections apportées :

Pour l'exercice 1 sur le Palindrome, le bug trouvé réside dans la boucle while qui contient une erreur d'incrémentation et de décrémentation. Les indices s'éloignent (j++ et i--) au lieu de converger vers le centre, ce qui provoque une boucle infinie ou une erreur de mémoire. La correction consiste à modifier j++ en j-- et i-- en i++. Le fichier corrigé est nommé Exo1Correction.java.

Concernant l'exercice 2 sur l'Anagramme, le bug principal est que la boucle for utilise i <= s1.length(). Cela provoque une StringIndexOutOfBoundsException car l'indice maximal autorisé doit être length() - 1. Un bug additionnel a été identifié : le calcul charAt(i) - 'a' plante si la chaîne contient autre chose que des lettres minuscules, comme des espaces ou des caractères spéciaux. La correction implique de changer la condition de boucle en i < s1.length() et d'utiliser une expression régulière pour nettoyer les chaînes avant le traitement.

Enfin, pour l'exercice 5 sur les chiffres romains (RomanNumeral), un premier bug d'index a été relevé car la boucle for utilise i <= symbols.length. Cela provoque une ArrayIndexOutOfBoundsException lors de l'accès au dernier élément du tableau. Un second bug de logique apparaît dans la condition n > values[i]. Cette condition empêche de traiter les nombres qui correspondent exactement à une valeur de référence, comme le chiffre 10 pour 'X', car 10 n'est pas strictement supérieur à 10. La correction consiste à utiliser i < symbols.length pour la boucle et n >= values[i] pour la condition de sélection du symbole.
=======
# AQL
>>>>>>> 32307a20620fc7c05bb400364975971987fc8be4
