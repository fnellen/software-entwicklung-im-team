# Chicken

## Datums Überprüfung

Wir erhalten ein Datum: 
- Praktikumszeitraum (7.03 - 25.03) 
- Wochentag (Mo.-Fr.)

## Zeit Überprüfung

Wir erhalten einen Zeitraum:
- Praktikumszeit (9:30 - 13:30)
- Startzeit vor Endzeit
- Startblock (00, 15, 30, 45)

## Klausur Überprüfung

### LSF-ID überprüfung
Vorgehen: Link aufrufen mit der entsprechenden ID und überprüfen, ob folgendes im Response-Body zurückgegeben wird:
````
<th class="mod" id="basic_3">VeranstaltungsID</th>
<td class="mod_n_basic" headers="basic_3">214444</td>
````
Vll mit HTMLUnit...

### Datum und Zeitraum Überprüfung

### Präsenz oder Online

### Urlaubüberschneidung



## ToDo:
#### Service:
- [ ] Service klausurBelegen
- [ ] Urlaub Stornieren
- [ ] Klausur Stornieren
- [ ] Testing
- [ ] Logging
#### Domain:
- [ ] ArchTest
- [ ] ZeitraumDto Konfiguration (Praktikumsstart / Ende)
#### Datenbank:
- [ ] Test
#### Web:
- [ ] Controller mit Html
- [ ] Authentifizierung mit Annotationen (ArchTest Überprüfung)
- [ ] Security
- [ ] Klausur hinzufügen
- [ ] Urlaub belegen
- [ ] Klausur belegen
- [ ] Resturlaub Übersicht
- [ ] Organisator und Tutor Übersicht
#### Spring:
- [ ] Auth
- [ ] Integration Tests Ende-zu-Ende
#### LSF-ID:
- [ ] In anderen Layer schieben