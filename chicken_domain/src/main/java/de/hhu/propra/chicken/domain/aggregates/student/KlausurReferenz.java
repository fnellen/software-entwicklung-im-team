package de.hhu.propra.chicken.domain.aggregates.student;


import de.hhu.propra.chicken.domain.stereotypes.ValueObject;

/**
 * Referenziert Klausuren im Bezug zum Studenten.
 */
@ValueObject
public record KlausurReferenz(String id) {


}
