# 06 Laufzeitsicht

---
---

Die Daten, die das System während der Laufzeit über die Benutzereingaben bekommt, 
werden den zuständigen Controllern übergeben.
Die Controller geben die Daten an die Services weiter, worin sie verarbeitet werden
und teilweise neu an die Controller zurückgegeben werden sowohl als auch 
in der Datenbank über die Repositories gespeichert werden.

Im Folgenden werden ein paar Abläufe der Funktionen erklärt:

### Urlaub belegen:
Studenten geben einen Urlaubzeitraum für sich selbst ein. 
Diese Informationen werden dann vom Controller in den Service und ins Repository gegeben, 
damit der Student und der Urlaubszeitraum gespeichert werden.
Nun sieht man auf der Seite, an welchem Tag Urlaub genommen wurde und wie viel Resturlaub der Student noch hat.

### Klausur anmelden:
Studenten können eine valide Klausur anmelden, um diese in der Klausurliste anzeigen zu können.
Die Klausur wird im Controller ermittelt und dann über den Service ans Repository zum Speichern in der Datenbank geleitet.

### Klausur belegen:
Studenten wählen eine vorhandene Klausur von der Klausurliste aus, die vorher angemeldet wurde.
Diese Informationen wird ebenfalls dann vom Controller in den Service und ins Repository gegeben, 
damit die Klausur und der Klausurzeitraum gespeichert werden.

### Urlaub- oder Klausur stornieren:
Studenten haben die Möglichkeit ihre Angaben zu ändern wie z.B ihre Urlaube oder Klausuren zu stornieren.
Diese Informationen wird ebenfalls dann vom Controller in den Service und ins Repository gegeben,
damit der Urlaub- oder Klausur zeitraum gelöscht wird.

