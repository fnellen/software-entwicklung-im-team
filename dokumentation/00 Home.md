
#Projekt mit @dehus101 @fnellen @ernaz100 @pifis102 @TeeJaey

---
---
## Starten des Systems

Das Projekt benutzt docker sowie `docker-compose` beides sollte installiert sein.

Es ist wichtig, dass die Umgebungsvariablen `CLIENT_ID` sowie `CLIENT_SECRET` gesetzt sind.

Zur Festlegung der systeminternen Rollen, sowie die Praktikumszeiten im Format *yyyy-mm-dd*
werden diese in einer .yml Datei verwendet:
```
rollen:
    tutoren: GitHubName
    organisatoren: GitHubName

praktikumszeitraum:
    praktikumsstart: Datum
    praktikumsende: Datum
```

Wenn alle vorigen Schritte ausgeführt wurden, kann das Projekt einfach mit 
 **docker-compose-dev.yml** und mit dem Befehl `gradle bootRun` gestartet werden.


