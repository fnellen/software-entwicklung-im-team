# 08 Konzepte

---
---

### Anmeldung / Rollenverteilung
Um das System nutzen zu können muss man Mitglied in einer bestimmten GitHub-Organisation sein.
Wenn das System den Nutzer in der Organisation findet wird er automatisch zu einem Studenten. 
Über die `application.yml` Datei bekommt das System dann noch die Informationen, welche Nutzer Tutoren oder 
Organisatoren sind und diese Rollen werden dann auch noch verteilt.
Somit hat jetzt jeder Benutzer nach der Anmeldung die passende Rolle 
und kann auf die Funktionen zugreifen die für ihn vorgesehen sind.

### Submodule und ArchUnit Tests
Damit wir die Schichtenarchitektur einhalten, benutzen wir Submodule und ArchUnit-Tests. 
Damit können wir sicherstellen, dass die Schichtenarchitektur nicht verletzt wird und 
wir nicht bei manchen Zugriffen Schichten überspringen. Außerdem prüfen wir ob wir die Regeln von DDD 
im Bezug auf Aggregate und deren Berechtigungen einhalten.