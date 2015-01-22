#IHM: Robot Hexapod

##Dommaire

- [Présentation](#Presentation)
	- [Scénario du projet](#Scenario_du_projet)
		- [Idée d'origine](#Idee_d_origine)
		- [Idée retenue](#Idee_retenue)
	- [Choix technologique](#Choix_technologique)
		- [Hexapod](#Hexapod)
		- [Matériel externe](#Materiel_externe)
		- [Logiciel](#Logiciel)
- [Documentation](#Documentation)
	- [Configuration système](#Configuration_systeme)
	- [Fonctionnement](#Fonctionnement)
	- [Listes de codes du robot](#Listes_de_codes_du_robot)
		- [GENERAL](#GENERAL)
		- [DEPLACEMENTS](#DEPLACEMENTS)
		- [MODES](#MODES)
	- [Séquences pour les scénarios du projet](#Sequences_pour_les_scenarios_du_projet)
		- [Démarrage (une fois au lancement)](#Demarrage)
		- [Respiration (constant)](#Respiration)
		- [Peur (choc)](#Peur)
		- [Intérêt (lumière)](#Interet)
		- [Curiosité (aléatoire / à définir)](#Curiosite)
- [Bilan](#Bilan)
	- [Limites de la solution actuelle](#Limites_de_la_solution_actuelle)
- [Informations annexes](#Informations_annexes)
	- [Sites et repos GitHub](#Sites_et_repos_GitHub)
		- [Constructeur Hexapod](#Constructeur_Hexapod)
		- [Projets annexes](#Projets_annexes)
		- [Manette PS2 via robot ou PC](#Manette_PS2_via_robot_ou_PC)
		- [Instinct](#Instinct)
	- [Contacts](#Contacts)
		- [Responsable](#Responsable)
		- [Développeurs](#Developpeurs)


##Présentation <a id="Presentation"></a>
Le but de ce projet consiste à créer un programme permettant d'automatiser plusieurs réactions d'un robot de type Hexapod, en vue de la réalisation d'une oeuvre artistique destinée à être exposée à la [Maison d'Ailleurs](http://www.ailleurs.ch/) d'Yverdon-le-Bains.  
Le programme devra permettre de rendre le robot le plus ***vivant*** possible.

###Scénario du projet <a id="Scenario_du_projet"></a>
####Idée d'origine <a id="Idee_d_origine"></a>
À l'origine, le robot devait simplement démontrer une possible évolution de la marche générée par le programme [GeneCraft](http://ape.iict.ch/teaching/SBI/SBI_Labo/labo5-Genecraft/) pour une créature insectoïde ressemblant à l'Hexapod.  

Nous avons pris la décision d'abandonner cette idée car trop peu ludique à notre goût. De plus, par la suite, nous nous sommes rendus compte que la marche de l'Hexapod était codé dans son programme interne ( sur microcontrôleur, en ***C*** ).  
Hors, notre projet repose sur la possiblité d'envoyer des codes d'ordres au robot via un Stream Java, mais ces codes ne permettent pas de prendre indépendemment le contrôle des servomoteurs du robot, on ne peut qu'exécuter les fonctions prédéfinies du microcontrôleur.  

Créer des "marches" différentes aurait donc nécessité la modification du programme Arduino stocké dans le robot. Pas impossible, mais beaucoup plus contraignant.

####Idée retenue <a id="Idee_retenue"></a>
Nous avons imaginé que l'hexapod devienne un insecte crée par l'homme, et conservé dans un bocal géant.  
Si il nous est permis une touche de scénario fantastique, une sorte de projet fictif baptisé **I.N.E.S** ( **I**maginons de **N**ouvelles **E**spèces **S**yntéthiques ) pourrait en être à l'origine.  
***Genecraft***, bien que présent pour expliquer ***l'évolution*** du robot, ne serait plus directement lié à son comportement.  

Le robot, ainsi placé, devra réagir à certains stimulis environnementaux tel qu'un choc sur son bocal ou une modification de l'éclairage.  
Au repos, le robot effectuera en boucle plusieurs mouvements visant à le faire passer pour vivant, tel qu'une respiration ou des déplacements où il essaiera d'entrer en contact avec les bords de sa prison. Un tracking vidéo pourra être utilisé pour détecter l'emplacement de l'hexapod dans le bocal.

###Choix technologique <a id="Choix_technologique"></a>
Comme cité plus haut, nous avons esquissé un concept utilisant Java comme langage de programmation. Pour détecter les modifications environnementales nous avons eu recours à trois capteurs différents:

######Hexapod <a id="Hexapod"></a>
- **Phoenix 3DOF**: Contrôlleur des servomoteurs de l'hexapod. Piloté par un microcontrôlleur *BotBoarduino*. 
- ***BotBoarduino***: Microcontrôlleur compatible avec l'environnement de développement *Arduino*. Utilise l'environnement de programmation **Arduino Duemilanove w/ ATmega328**.

######Matériel externe <a id="Materiel_externe"></a>
- **Webcam Logitech**: Permet de filmer le robot depuis le haut du bocal. Un symbole placé sur son dos devrait permettre un tracking graçe à la bibliothèque JavaCV.
- **Capteur de vibration**: Un capteur de vibrations [Phidget](http://www.phidgets.com/) et son contrôlleur.
- **Capteur de luminosité**: *Nous ne disposons pas encore ce capteur*

######Logiciel <a id="Logiciel"></a>
- **RXTX**: [RXTX](http://mfizz.com/oss/rxtx-for-java) est une bibliotèque Java permettant les communications de type série et parallèle. Utilisée pour communiquer en USB avec l'hexapod.
- **JavaCV**: [JavaCV](https://github.com/bytedeco/javacv) est une déclinaison du wrapper Java/C++ conçu par l'équipe de [Bytedeco](http://bytedeco.org/) visant à adapter la bibliothèque [OpenCV](http://opencv.org/) pour Java. Elle a l'avantage d'être documentée et de disposer de classes remplaçants efficacement celles qui ne peuvent être utilisée de la bibliothèque d'origine (en C++), comme la gestion des fenêtres par exemple.  
***Note***: Un [wrapper officiel d'OpenCV pour Java](http://docs.opencv.org/2.4.4-beta/doc/tutorials/introduction/desktop_java/java_dev_intro.html) existe, mais il ne dispose pas des avantages précité pour JavaCV.
- **Phidgets**: La bibliothèque de programmation de la marque [Phidgets](http://www.phidgets.com/docs/Language_-_Java#Quick_Downloads). Permet actuellement de communiquer avec le capteur de vibration et peut-être également avec le capteur de lumière si il proviens du même fabriquant.

##Documentation <a id="Documentation"></a>
###Configuration système <a id="Configuration_systeme"></a>
Au court de notre développement, nous avons utilisé successivement Mac OS X Maverick et Linux Mint comme Système d'exploitation, ainsi que Java 8.  
Le point crucial reste l'ajout correct des librairies listées dans le chapitre des choix technologiques - logiciel. Normalement si ces librairies sont correctement ajoutées, n'importe quel système devrait pouvoir intéragir avec l'hexapod.

###Fonctionnement <a id="Fonctionnement"></a>
Le microcontrôlleur de l'hexapod fait tourner un programme en ***C*** conçu par les créateur du robot (sources disponibles dans les annexes) auquel nous pouvons transmettre des ordres sous la forme de codes de 6 bytes (octets).  

Les codes ci-dessous doivent être envoyés à l'hexapod en réponse à un "ping" de sa part (un InputStream est nécessaire). Le code par défaut signifiant "reste comme tu es" est le suivant:

|   CODE   | Byte 5 | Byte 4 | Byte 3 | Byte 2 | Byte 1 | Byte 0 |
|---------:|:------:|:------:|:------:|:------:|:------:|:------:|
|Défaut    |01111111|01111111|01111111|01111111|00000000|00000000|

***Il est nécessaire d'envoyer continuellement ce code, sinon l'hexapod s'arrête.***  

Les quatre premiers Bytes (Octets) contrôlent les pattes (le déplacement de manière générale), le code ***01111111*** (127) ordonne aux servomoteurs de maintenir leur tension actuelle.  
Les deux Bytes restant offrent des commandes particulières, on les laisse à ***0x0*** pour qu'il ne se passe rien.

###Listes de codes du robot <a id="Listes_de_codes_du_robot"></a>

######GENERAL <a id="GENERAL"></a>

Liste de codes permettant de modifier les options générales du robot.

*Ces codes peuvent être mélangés sans autres entre eux et avec les codes de déplacement (ci-dessous), pour autant qu'ils restent cohérents (haut / bas par exemple...)*

|   CODE   | Byte 5 | Byte 4 | Byte 3 | Byte 2 | Byte 1 | Byte 0 |
|---------:|:------:|:------:|:------:|:------:|:------:|:------:|
|Start/stop|01111111|01111111|01111111|01111111|00000000|00000100|
|Haut      |01111111|01111111|01111111|01111111|00010000|00000000|
|Bas       |01111111|01111111|01111111|01111111|01000000|00000000|
|Vitesse + |01111111|01111111|01111111|01111111|00100000|00000000|
|Vitesse - |01111111|01111111|01111111|01111111|10000000|00000000|

######DEPLACEMENTS <a id="DEPLACEMENTS"></a>

Liste de codes permettant de déplacer le robot sur tous les axes.  

*Ces codes peuvent être mélangés sans autres entre eux et avec les codes de comportement général (ci-dessus), pour autant qu'ils restent cohérents (gauche / droite par exemple...)*

|     CODE      | Byte 5 | Byte 4 | Byte 3 | Byte 2 | Byte 1 | Byte 0 |
|--------------:|:------:|:------:|:------:|:------:|:------:|:------:|
|Avancer        |01111111|01111111|01111111|00000000|00000000|00000000|
|Reculer        |01111111|01111111|01111111|11111110|00000000|00000000|
|Pivoter gauche |00000000|01111111|01111111|01111111|00000000|00000000|
|Pivoter droit  |11111110|01111111|01111111|01111111|00000000|00000000|
|Décaler gauche |01111111|01111111|00000000|01111111|00000000|00000000|
|Décaler droit  |01111111|01111111|11111110|01111111|00000000|00000000|

######MODES <a id="MODES"></a>

Liste de code permettant d'activer / désactiver des modes prédéfinis (en renvoyant le même code).  

*L'activation de ces modes peuvent modifier le comportement des codes de déplacement, par exemple le mode de contrôlle de la patte avant droite rendra le robot immobile, les codes de déplacement servant exclusivement à déplacer la patte en question.*

- **Peur**: Idem au mouvement d'arrêt: le robot recroqueville ses pattes.  
Peut-être utilisé deux fois de suite pour "ré-équilibrer" l'hexapod en cas de problème.
- **Patte avant droite**: Active le mode permettant de prendre le contrôlle exclusif de la patte avant droite.
- **Gyroscopique**: L'hexapod bouge son corps sur place en conservant sa position horizontale.
- **Angles**: L'hexapod bouge son corps sur place en inclinant l'angle de son corps.
- **Militaire**: L'hexapod marche de manière brutale, comme un soldat.

|       CODE       | Byte 5 | Byte 4 | Byte 3 | Byte 2 | Byte 1 | Byte 0 |
|-----------------:|:------:|:------:|:------:|:------:|:------:|:------:|
|Peur              |01111111|01111111|01111111|01111111|00000001|00000000|
|Patte avant droite|01111111|01111111|01111111|01111111|00000010|00000000|
|Gyroscopique      |01111111|01111111|01111111|01111111|00000000|10000000|
|Angles            |01111111|01111111|01111111|01111111|00000000|00100000|
|Militaire         |01111111|01111111|01111111|01111111|00000000|01000000|

###Séquences pour les scénarios du projet <a id="Sequences_pour_les_scenarios_du_projet"></a>
Ci-dessous nous avons théoriquement relevé des séquences de codes permettant de répondre aux divers stimulis externes précités dans l'idée retenue.

######Démarrage (une fois au lancement) <a id="Demarrage"></a>

La séquence ci-dessous démarre le robot, au utilise deux fois le mode "peur" afin de s'assurer que le robot se déploie bien à la hauteur par défaut stockée dans son programme interne.

|     Séquence     | Byte 5 | Byte 4 | Byte 3 | Byte 2 | Byte 1 | Byte 0 |
|:-----------------|:------:|:------:|:------:|:------:|:------:|:------:|
|1. Start          |01111111|01111111|01111111|01111111|00000000|00000100|
|2. Peur           |01111111|01111111|01111111|01111111|00000001|00000000|
|3. Peur           |01111111|01111111|01111111|01111111|00000001|00000000|

######Respiration (constant) <a id="Respiration"></a>

Les attentes corespondent à de simple ***sleep*** sur le thread responsable.

|     Séquence     | Byte 5 | Byte 4 | Byte 3 | Byte 2 | Byte 1 | Byte 0 |
|:-----------------|:------:|:------:|:------:|:------:|:------:|:------:|
|1. Haut           |01111111|01111111|01111111|01111111|00010000|00000000|
|2. Attente (500ms)|01111111|01111111|01111111|01111111|00000000|00000000|
|3. Bas            |01111111|01111111|01111111|01111111|01000000|00000000|
|4. Attente (1s)   |01111111|01111111|01111111|01111111|00000000|00000000|

######Peur (choc) <a id="Peur"></a>

En cas de choc sur la vitre du bocal, le robot se recroqueville immédiatement et se relève après 5-6 secondes

|     Séquence     | Byte 5 | Byte 4 | Byte 3 | Byte 2 | Byte 1 | Byte 0 |
|:-----------------|:------:|:------:|:------:|:------:|:------:|:------:|
|1. Peur           |01111111|01111111|01111111|01111111|00000001|00000000|
|2. Attente (6s)   |01111111|01111111|01111111|01111111|00000000|00000000|
|3. Peur           |01111111|01111111|01111111|01111111|00000001|00000000|

######Intérêt (lumière) <a id="Interet"></a>

Si le robot détecte une interruption de lumière, signifiant que quelqu'un se tiens devant lui, entre son bocal et le projecteur, l'hexapod se penchera en arrière, comme pour regarder le visiteur qui l'observe.

|     Séquence     | Byte 5 | Byte 4 | Byte 3 | Byte 2 | Byte 1 | Byte 0 |
|:-----------------|:------:|:------:|:------:|:------:|:------:|:------:|
|1. Angles - on -  |01111111|01111111|01111111|01111111|00000000|00100000|
|2. Se pencher *   |01111111|01111111|01111111|00000000|00000000|00000000|
|3. Attente (500ms)|01111111|01111111|01111111|01111111|00000000|00000000|
|4. Angles - off - |01111111|01111111|01111111|01111111|00000000|00100000|

\* *Pendant une dizaine de secondes...*

######Curiosité (aléatoire / à définir) <a id="Curiosite"></a>

Si aucune interraction extérieur n'arrive pendant un certain temps, l'hexapod peut aléatoirement entreprendre de *toucher* le bord de sa prison à l'aide de sa patte avant droite.  
Bien que pas encore implémenté, il faudra utiliser le système de tracking de JavaCV pour placer le robot de manière à ce qu'il ait toujours l'espace pour réagir à de future interractions.

|     Séquence     | Byte 5 | Byte 4 | Byte 3 | Byte 2 | Byte 1 | Byte 0 |
|:-----------------|:------:|:------:|:------:|:------:|:------:|:------:|
|1. Angles - on -  |01111111|01111111|01111111|01111111|00000000|00100000|
|2. Se pencher *   |01111111|01111111|01111111|00000000|00000000|00000000|
|3. Angles - off - |01111111|01111111|01111111|01111111|00000000|00100000|
|4. Ctrl. Patte    |01111111|01111111|01111111|01111111|00000010|00000000|
|5. Lever Patte 6x |01111111|00000000|01111111|01111111|00000000|00000000|
|6. Tapper vitre **|01111111|01111111|11111110|00100000|00000000|00000000|
|7. Ctrl. Patte off|01111111|01111111|01111111|01111111|00000010|00000000|
|8. Angles on      |01111111|01111111|01111111|01111111|00000000|00100000|
|9. Angles off *** |01111111|01111111|01111111|01111111|00000000|00100000|


\* *Pendant une ou deux secondes, le temps de s'incliner...*  
\*\* *Plusieurs fois... Code à re-tester...*   
\*\*\* *Permet de remettre le robot droit. Utiliser le code de peur deux fois ne fonctionne pas ici car l'inclinaison est conservée.*

##Bilan <a id="Bilan"></a>
###Limites de la solution actuelle <a id="Limites_de_la_solution_actuelle"></a>
La solution actielle nommée ***Instinct*** est un code Java permettant de faire "respirer" le robot et de le faire se recroqueviller (pour simuler la peur) en cas de choc détecté par le capteur **Phidgets**. Cependant, la communication entre notre programme et l'hexapod souffre de grave problèmes, entrainant souvent l'extinction de ce dernier (ainsi que des bips d'erreurs).  
Nous pensons que cela est du soit à une erreur de communication (oubli d'un code, d'une réponse), soit à un problème de cache avec le microcontrôlleur *BotBoarduino*. Si ce problème venait à être régler, il suffirait alors d'ajouter des threads pour les réactions manquantes ainsi que la solution de tracking en JavaCV pour permettre au robot de bouger de son propre chef.  

Pour l'instant, ce code n'est qu'une ébauche est doit donc être considéré comme ***non-fonctionnel***.

##Informations annexes <a id="Informations_annexes"></a>
###Sites et repos GitHub <a id="Sites_et_repos_GitHub"></a>
######Constructeur Hexapod <a id="Constructeur_Hexapod"></a>
- [http://www.lynxmotion.com/p-948-phoenix-combo-kit-for-botboarduino.aspx]()
- [http://www.lynxmotion.com/images/html/build185.htm]()

######Projets annexes <a id="Projets_annexes"></a>

######Manette PS2 via robot ou PC <a id="Manette_PS2_via_robot_ou_PC"></a>
- [Site du développeur](http://www.billporter.info/2010/06/05/playstation-2-controller-arduino-library-v1-0/)
- [https://github.com/KurtE/Phoenix-Code/tree/master/BotBoarduino (via robot)]() (via robot)
- [https://github.com/itzkha/phoenix_code]() (via PC)

######Instinct <a id="Instinct"></a>
- [https://github.com/Iosis555/Instinct]()

###Contacts <a id="Contacts"></a>
######Responsable <a id="Responsable"></a>
- Professeur Andres Perez-Uribe

######Développeurs <a id="Developpeurs"></a>

- Pierre-Alain Curty
- Nicolas Butticaz Leal
- Dung Quang Ngo
