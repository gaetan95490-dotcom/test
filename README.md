# Philips Minimal — prototype Android TV

Projet source initial pour Philips 55POS9002/12 (Android TV ancien; réglé pour minSdk 23 et targetSdk 28).

## Fonctionnalités du prototype
- Interface sombre pensée pour une télécommande et un écran OLED.
- Horloge 24 h.
- Raccourcis YouTube, Netflix, Prime Video et Disney+ par nom de package Android courant.
- Bouton Paramètres Android et bouton Accueil Android.
- Bouton Sources avec tentative générique, puis explication si l'interface propriétaire Philips n'est pas exposée.
- L'activité est déclarée comme HOME et Leanback : Android peut proposer de la choisir comme application d'accueil si le firmware Philips l'autorise.

## Limites connues — à vérifier sur la TV
1. Ce projet est du **code source**, pas un APK compilé. Il n'a pas été compilé ni testé sur le téléviseur.
2. Philips n'expose pas forcément son sélecteur de sources à une application tierce; la touche SOURCES de la télécommande reste le recours fiable.
3. L'accueil HOME ne garantit pas que le firmware Philips permettra de remplacer son lanceur ni que le bouton HOME physique ouvrira ce lanceur.
4. Le lancement automatique ne peut être confirmé qu'après installation et essai sur l'appareil. On privilégie le mécanisme HOME plutôt qu'un démarrage forcé en arrière-plan.
5. Les noms de package d'applications peuvent varier selon la version/région. Si une appli n'est pas trouvée, il faudra ajuster le package après vérification sur la TV.

## Compiler
Ouvrir ce projet dans Android Studio avec un JDK compatible et installer Android SDK 35; puis construire l'APK debug. Le workflow GitHub Actions fourni tente aussi une compilation à distance si le projet est importé dans un dépôt GitHub. Le fichier Gradle Wrapper n'est pas inclus dans cette première archive.

## Sécurité
L'application ne demande pas de permission réseau, compte ou stockage. Ne l'installez pas avant qu'un APK ait été effectivement compilé et que sa provenance soit claire.


## Fonds d’écran personnalisables
Voir `LISEZ-MOI_FONDS_ECRAN.txt` et le dossier `FONDS_ECRAN_A_REMPLACER`. Les images de démonstration `fond_1.jpg` à `fond_6.jpg` sont dans `app/src/main/res/drawable/`. Remplace-les en conservant les noms, puis recompile l’APK.
