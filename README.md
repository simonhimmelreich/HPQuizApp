Harry Potter Quizapp:

Simple Quizapp mit Startingscreen, Quiz und Ergebnis.

Übersichtlich durch Composable Functions.

Startscreen: Hogwartswappen, Titel und Start Button um Quiz zu starten.

Quizscreen: 
  10 fragen mit jeweils 3 Antwortmöglichkeiten. Wenn richtig benantwortet wurde wird die Box mit der Antwort grün gehighlighted, bei falscher Antwort rot und es wird gezeigt welche Antwort die richtige gewesen wäre (durch grünes highlighten).
  1.5 sec zwischen Fragen/Antworten.
  Lizensfreie Hintergrundmusik die initialisiert wird sobald das Composable Quizscreen aufgerufen wird und beendet sobald die App verlassen, neugestartet oder das Composable Resultscreen initialisiert wird.
    --> leider hört die Musik oft einfach auf nachdem es ein wenig stottert, bin mir leider nicht sicher ob es am Code, an der mp3 oder meinem Laptop liegt.
  Score wird mitgerechnet und unter den Antwortmöglichkeiten angezeigt.

Resultscreen:
  Zeigt Score von 10 und ein Bild eines Charakters abhängig vom erreichten Score (0 = Argus Filch, 5 > Wurmschwanz, 5 < Harry Potter, 10 = Dumbeldore)
    + kleiner Fehler da bei 5 auch Dumbeldore angezeigt wird -> besser wäre 5 >= Wurmschwanz
  Return to Start Button.

Mögliche Erweiterungen: 
Highscore Display
Shuffled Antwortmöglichkeiten (damit nicht immer an der gleichen Stelle)
Musik aus Button in einer oberen Ecke
Timer bei Fragen
Overall timer -> Highscore mit Zeit
Smokey bg nicht nur als Bild sondern bewegend im bg
Anstatt Bild bei Resultscreen, ein Gif von Charakter
Verschiede Charakter bei jedem möglichen Score

KI:
KI wurde genutzt um mögliche Imports vorzuschlagen
Zur Fehlersuche
es wurden viele kleinere Teilfunktionen extern geschrieben und dann durch KI kombiniert
Zum Erklären von bestimmten Sachen, e.g. wie man mp3 einbaut oder warum code bestimmt strukturiert sein muss
