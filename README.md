# Guide Git - Workflow de Développement

## 🌳 Structure des Branches

### Branche Principale
**`main`** est la branche principale du projet. Elle contient le code stable et testé, prêt pour la production.

---

## 🔄 Workflow de Développement

### 1. Créer une Branche Feature

Toujours créer une nouvelle branche depuis `main` pour développer une fonctionnalité :

```bash
# S'assurer d'être sur main et d'avoir la dernière version
git checkout main
git pull origin main

# Créer et basculer sur une nouvelle branche feature
git checkout -b feature/nom-de-la-feature
```

**Convention de nommage :** `feature/nom-descriptif`
- Exemple : `feature/ajout-authentification`
- Exemple : `feature/correction-formulaire-contact`

---

### 2. Développer et Commiter

Travaillez sur votre fonctionnalité et commitez régulièrement :

```bash
# Ajouter les fichiers modifiés
git add .

# Commiter avec un message descriptif
git commit -m "Description claire des modifications"
```

**Bonnes pratiques pour les messages de commit :**
- Utiliser l'impératif : "Ajoute", "Corrige", "Modifie"
- Être concis mais descriptif
- Exemple : `Ajoute la validation du formulaire de connexion`

---

### 3. Pousser sur GitHub

Une fois le développement terminé, poussez votre branche sur GitHub :

```bash
# Pousser la branche feature sur le dépôt distant
git push origin feature/nom-de-la-feature
```

Si c'est la première fois que vous poussez cette branche :
```bash
git push -u origin feature/nom-de-la-feature
```

---

### 4. Créer une Pull Request (PR)

1. Rendez-vous sur GitHub dans votre dépôt
2. Cliquez sur **"Compare & pull request"** qui apparaît après le push
3. Remplissez les informations de la PR :
   - **Titre** : Décrivez brièvement la fonctionnalité
   - **Description** : Expliquez en détail les modifications apportées
   - **Reviewers** : Assignez des personnes pour la revue de code
4. Cliquez sur **"Create pull request"**

---

### 5. Revue et Validation

- Les membres de l'équipe examinent le code
- Des modifications peuvent être demandées
- Les discussions se font directement dans la PR

**Si des modifications sont nécessaires :**
```bash
# Effectuer les corrections sur la branche feature
git add .
git commit -m "Applique les corrections demandées"
git push origin feature/nom-de-la-feature
```

La PR sera automatiquement mise à jour.

---

### 6. Merge de la PR

**Une fois la PR validée :**

1. Sur GitHub, cliquez sur **"Merge pull request"**
2. Confirmez le merge
3. La branche `feature/nom-de-la-feature` est fusionnée dans `main`
4. Supprimez la branche feature sur GitHub (option proposée après le merge)

**En local, mettez à jour votre branche main :**
```bash
git checkout main
git pull origin main

# Supprimer la branche feature locale (optionnel)
git branch -d feature/nom-de-la-feature
```

---

## 📋 Résumé du Workflow

```
main (branche principale)
  │
  └─→ Créer feature/ma-fonctionnalite
        │
        ├─→ Développement + Commits
        │
        ├─→ Push sur GitHub
        │
        ├─→ Créer une Pull Request
        │
        ├─→ Revue de code
        │
        └─→ Validation & Merge → main
```

---

## ✅ Checklist Avant de Créer une PR

- [ ] Le code fonctionne correctement
- [ ] Les tests passent (si applicable)
- [ ] Le code respecte les conventions du projet
- [ ] La branche est à jour avec `main`
- [ ] Les commits sont clairs et bien organisés
- [ ] La description de la PR est complète

---

## 🚨 Bonnes Pratiques

- **Ne jamais commiter directement sur `main`**
- Garder les branches feature à jour avec `main` :
  ```bash
  git checkout feature/ma-feature
  git merge main
  ```
- Faire des PR de taille raisonnable (éviter les PR de 1000+ lignes)
- Répondre aux commentaires de revue rapidement
- Supprimer les branches feature après le merge

---

## 🆘 Commandes Utiles

```bash
# Voir toutes les branches
git branch -a

# Voir le statut actuel
git status

# Voir l'historique des commits
git log --oneline

# Annuler le dernier commit (sans perdre les modifications)
git reset --soft HEAD~1

# Mettre à jour une branche feature avec les derniers changements de main
git checkout feature/ma-feature
git rebase main
```

---

## 📚 Ressources

- [Documentation Git officielle](https://git-scm.com/doc)
- [Guide GitHub sur les Pull Requests](https://docs.github.com/en/pull-requests)
- [Conventional Commits](https://www.conventionalcommits.org/)