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

#### Klausur Buchen

- **Fall 1**: Kein Urlaub an dem Tag
    - Klausur hinzufügen
- **Fall 2**: Urlaub an dem Tag
    - Fall 1: Urlaub fängt vor der Klausur an und hört innerhalb des Klausurzeitraums auf
        - Urlaub schneiden
        - alten aus student löschen
        - geschnittenen Urlaub hinzufügen
        - Klausur hinzufügen
    - Fall 2: Urlaub fängt vor der Klausur an und hört vor der Klausur auf
        - Klausur hinzufügen
    - Fall 3: Urlaub fängt vor der Klausur an und hört nach der Klausur auf
        - Urlaub schneiden
        - alten aus student löschen
        - geschnittenen Urlaub hinzufügen
        - Klausur hinzufügen
    - Fall 4: Urlaub fängt innerhalb der Klausur an und hört nach der Klausur auf
        - Urlaub schneiden
        - alten aus student löschen
        - geschnittenen Urlaub hinzufügen
        - Klausur hinzufügen
    - Fall 5: Urlaub fängt innerhalb der Klausur an und hört innerhalb der Klausur auf
        - vorhandenen Urlaub löschen
        - Klausur hinzufügen
    - Fall 6: Urlaub fängt nach der Klausur an und hört nach der Klausur auf
        - Klausur hinzufügen
- **Fall 3**: **(Optional)** zwei Klausuren schneiden sich
    - Klausur belegung ablehnen.

## Stornieren

Urlaube und Klausuren können bis zum Vortag storniert werden. Die Stornierung am selben Tag ist nicht möglich. Urlaube
und Klausuren sollen nachträglich nicht storniert werden können.

### Vorgehen

Interface in die Service KLasse Injizieren lassen, das das aktuelle Datum des heutigen Tag zurück gibt.

## ToDo:
#### Allgemein:
- [ ] Konfiguration docker-compose für DB und Anwendung
- [ ] Dokumentation

#### Service:

- [x] Service klausurBelegen
- [x] Urlaub Stornieren
- [x] Klausur Stornieren
- [x] Testing
- [x] Logging
    - [ ] Testing
- [ ] opt. Refactoring    

#### Domain:

- [ ] ArchTest
- [ ] ZeitraumDto Konfiguration (Praktikumsstart / Ende)

#### Datenbank:

- [x] Test

#### Web:

- [ ] Controller mit Html
- [ ] Authentifizierung mit Annotationen (ArchTest Überprüfung)
- [ ] Security
- [ ] Klausur hinzufügen
- [x] Urlaub belegen
- [x] Klausur belegen
- [x] Urlaub stornieren
- [x] Klausur stornieren
- [x] Resturlaub Übersicht
- [ ] Organisator und Tutor Übersicht
- [ ] Accessibility
#### Spring:

- [ ] Auth
- [ ] Integration Tests Ende-zu-Ende

#### LSF-ID:

- [x] In anderen Layer schieben