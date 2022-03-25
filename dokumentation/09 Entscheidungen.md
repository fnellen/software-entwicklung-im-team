# Entwurfsentscheidungen

---
---

###Schichtenarchitektur
Wir haben uns gegen Microservices entschieden, weil wir der Meinung waren, dass sich für die doch 
überschaubare Projektgröße eine Onion Architektur besser eignen würde und übersichtlicher wäre.
Außerdem haben wir uns dadurch die Kommunikation zwischen verschiedenen Systemen gespart.
Durch eine Schichtenarchitektur haben wir uns erhofft eine geringere Kopplung zu haben und 
das Single-Responsibility-Prinzip besser einhalten zu können.

###Domain Driven Design
Nachträglich haben wir uns dazu entschieden unser Projekt nach Domain Driven Design zu strukturieren, 
damit unsere Onion Architektur strukturierter ist und wir an der richtigen Stelle anfangen.
Durch die Modellierung nach DDD wurden wir nicht von der technischen Seite abgelenkt und
konnten uns vorerst nur auf die Strukturierung des Programms konzentrieren,
was sehr geholfen hat den Entwicklungsprozess voran zu treiben.

###Spring Data JDBC
Da wir unser Projekt nach Domain Driven Design modelliert haben, 
hat es sich angeboten Spring Data JDBC für die Zugriffe unserer Datenbank zu verwenden.

###Spring Security OAuth2
Da wir ein Spring Boot System entwickelt haben, haben wir Spring Security OAuth2 benutzt, 
um die Sicherheit in unserem Programm gewährleisten.

###Maria-Datenbank
Wir haben uns dazu entschlossen eine MariaDB-Datenbank zu verwenden, 
weil wir eine relationale Datenbank brauchten, welche dazu noch eine gute Performance leisten kann.

###Checkstyle & SpotBugs
Da wir bereits in der Vergangenheit mit CheckStyle und SpotBugs gearbeitet haben und
uns somit schon damit auskennen, haben wir auch dieses Mal diese beiden statischen Checker 
für die Sicherstellung der Codequalität verwendet.

###Rollenverteilung über YML-Config
Um die Benutzertypen bzw. die Rollen in unserem Programm festzulegen haben wir
uns für eine YML-Config Datei entschieden, in welcher die Tutoren und Organisatoren aufgelistet werden.