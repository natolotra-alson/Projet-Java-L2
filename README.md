# Gestionnaire d'étudiants — Java Swing

Application réalisée pour le TP Java de gestion d'étudiants.

## Choix important

Le sujet fourni mentionne une moyenne dans la partie fonctionnelle, mais ce champ ne figure pas dans la maquette ni dans le modèle d'étudiant demandé. **La moyenne a donc été entièrement supprimée** : aucun champ, attribut, contrôle ou traitement ne la concerne.

## Fonctionnalités

- Ajout d'un étudiant avec les champs de la maquette : matricule, nom, prénom, date de naissance, email, téléphone, parcours, année universitaire et statut.
- Contrôle des champs obligatoires et message d'alerte en cas de saisie incomplète.
- Affichage dans un `JTable`.
- Recherche par nom/prénom ou par filière.
- Réinitialisation de la recherche.
- Suppression de la ligne sélectionnée.
- Tri automatique des colonnes.

## Organisation

```text
src/
├── model/
│   ├── Etudiant.java
│   └── GestionEtudiants.java
├── gui/
│   └── FenetreEtudiant.java
└── Main.java
```

Les fichiers `.java` dans le dossier `src` sont le code source lisible et modifiable. Ouvrez le dossier avec IntelliJ IDEA, Eclipse ou VS Code, puis ouvrez `src/Main.java` pour voir le point de départ de l'application.

Le matricule est généré automatiquement sous la forme `ETU0001`, `ETU0002`, etc. Il n'est pas demandé à l'utilisateur.

## Ouverture avec NetBeans

L'archive contient aussi les fichiers `nbproject/project.xml`, `nbproject/project.properties`, `build.xml` et `manifest.mf`. Dans NetBeans, choisissez **File > Open Project**, sélectionnez le dossier `gestion-etudiants` extrait du ZIP, puis cliquez sur **Run**. La structure `src/model`, `src/gui` et `src/Main.java` est conservée.

## Compilation et lancement

Depuis le dossier du projet :

```bash
mkdir -p out
javac -d out src/model/*.java src/gui/*.java src/Main.java
java -cp out Main
```

Java 8 ou version ultérieure est requis.
