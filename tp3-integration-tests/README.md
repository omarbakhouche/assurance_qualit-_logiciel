# 🚀 TP3 : Tests d'intégration (Mockito)

## 📌 Objectifs
Ce TP valide les interactions entre les modules **Service**, **DAO** et **API** en utilisant le **Mocking** pour isoler les composants et garantir des tests déterministes.

## 📂 Structure du Projet
* **Exo 1** : `UserService` ↔ `UserRepository`
  * *Focus* : Vérification d'une interaction simple et injection de dépendance.
* **Exo 2** : `OrderController` → `Service` → `DAO`
  * *Focus* : Validation d'une chaîne d'appels complète à travers plusieurs couches.
* **Exo 3** : `ProductService` ↔ `API Client`
  * *Focus* : Gestion des scénarios d'erreurs (Timeouts, formats invalides, échecs réseau).

## 🧪 Concepts Clés
| Concept | Description |
| :--- | :--- |
| **Test d'intégration** | Vérifie le fonctionnement de plusieurs modules ensemble. |
| **Mock** | Objet simulé remplaçant une dépendance réelle (BD, API externe). |
| **Assert** | Vérifie le **résultat** final (valeur de retour ou exception). |
| **Verify** | Vérifie le **comportement** (si la méthode a été appelée avec les bons arguments). |

## ⚙️ Exécution
1. **IntelliJ** : Clic droit sur le dossier `src/test/java` → **Run with Coverage**.
2. **Maven** : Exécuter `mvn test` dans le terminal.

---
**Développeur** : Omar  
**Date** : Avril 2026