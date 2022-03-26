# 03 Kontextabgrenzung

---
---
## Grobe Abgrenzung
Das System verwaltet die Freistellungen der Studierenden vom Praktikum des Kurses ProPra 2. Die Authentifizierung der Studierenden bzw. der Tutoren und Organisatoren erfolgt durch den Authetification-Provider GitHub.
![[Kontextabgrenzung.png]]
## Authentifizierungs Ablauf
(http://chicken.org/ Beispielhafte Url)
![[Auth-Flow.png]]
## Komponenten
### Datenbank
Speichert Studenteninformationen:
* GitHub-Handle
* Belegte Urlaube
* Belegte Klausuren

Speichert Klausurinformationen:
* VeranstaltungsId
* Veranstaltungsname
* Klausurzeitraum
* Freistellungszeitraum
* Präsenz oder nicht

Darstellung der Beziehungen innerhalb der Datenbank:
![[klausur_dto.png]]

### Web-Controller
Zeigt Html Seiten zur:
* Übersicht 
* Klausurbelegung
* Klausuranmeldung
* Urlaubsbelegung
an.

Leitet Aufgaben an den Service weiter

## Service
Übernimmt die Validierung der Anfragen zur Belegung oder Stornierung von Urlauben und Klausuren. Speichert bei erfolgreicher Überprüfung die Anfragen in der Datenbank.

## Logging
Alle Änderungen an belegten Urlauben bzw. Klausuren werden mittels des Loggings in einer Log-Datei festgehalten. Diese kann nicht durch die Benutzer verändert werden, lediglich das Programm kann weitere Einträge hinzufügen.

## Domain
Definiert Aggregate und spiegelt die Geschäftslogik wieder:
* Studierende
* Klausuren
* Zeiträume

## LSF-Validierung
Baut eine Verbindung zum LSF auf, um eintragene Veranstaltungs-Id's zu überprüfen.

