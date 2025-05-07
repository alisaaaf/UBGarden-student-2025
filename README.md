# README - PROJET “SAUVE LE HÉRISSON !”

**Auteurs** : Alisa Fedoseeva et Nouha El Abed  
**Dépôt Git** : [https://github.com/alisaaaf/UBGarden-student-2025.git](https://github.com/alisaaaf/UBGarden-student-2025.git)

---

##  Description du jeu
Jeu 2D développé en JavaFX où le joueur contrôle un jardinier.  
**Objectif** : Collecter toutes les carottes sur la carte en évitant les ennemis et en gérant son énergie. Une fois toutes les carottes ramassées, les portes s'ouvrent pour accéder potentiellement à un autre niveau.

---

## Architecture
Le projet suit une architecture orientée objet avec une séparation claire des responsabilités :
- **Logique du jeu** : Classes `Game`, `Gardener`, `Decor`, etc.
- **Affichage** : `Sprite`, `GameEngine`, etc.
- **Configuration du monde** : Cartes, bonus, ennemis, etc.


### Modifications clé
1. **Héritage et polymorphisme**
    - Les bonus (pommes, carottes, bombes) héritent de la classe abstraite `Bonus` et implémentent l'interface `Pickupable`.
    - Mécanisme de double dispatch pour appliquer les effets spécifiques sans logique conditionnelle.

2. **Décors et portes**
    - Les classes `OpenedDoor` et `ClosedDoor` héritent de `Door`.
    - Vérification de la franchissabilité via `isOpen()` et ouverture dynamique des portes.

---

##  Fonctionnalités implémentées
- **Affichage** : Utilisation de `SpriteFactory` pour tous les éléments (hérisson, bonus, etc.).
- **Carte** :
    - Chargement par défaut via `MapLevelDefaultStart`.
    - Chargement depuis un fichier avec `MapLevelFromFile`.
- **Déplacements** : Blocage hors carte/obstacles via `walkableBy()` et `inside()`.
- **Énergie** :
    - Consommation selon le terrain.
    - Récupération après inactivité.
- **Bonus** :
    - Collecte automatique et effets cumulatifs (pommes boost, trognons maladie).
    - Bombes insecticides ramassables (`InsectBomb`).
- **Victoire/Défaite** : Gérées via `Gardener.win()` et `Gardener.die()`.
- **Barre d'état** : Affichage dynamique de l'énergie, `diseaseLevel` et nombre de bombes.

---

##  Ennemis et mécaniques associées

### Guêpes (`Wasp`)
- **Génération** : Par des nids (`WaspNest`) toutes les 5 secondes (si le jardinier est dans le niveau).
- **Comportement** :
    - Déplacement aléatoire (fréquence définie dans `waspMoveFrequency`).
    - Dégâts de piqûre : ~20 points (`stingDamage()`).
- **Mort** :
    - Après avoir piqué.
    - Sur une bombe (détection via `GameEngine.checkCollision()`).

### Frelons (`Hornet`)
- **Différences avec les guêpes** :
    - Piquent 2 fois avant de mourir (`isDeadAfterSting()`).
    - Dégâts de piqûre : -30 points.
    - Nécessitent 2 bombes pour être éliminés.
- **Génération** : Par `HornetNest` toutes les 10 secondes (2 bombes posées aléatoirement à chaque apparition).

### Bombes insecticides
- **Utilisation** : Déclenchées automatiquement si une guêpe/frelon est adjacent.
  ```java
  if (enemy instanceof Wasp) {
      enemy.onBombHit();
      bombCount--;
  }