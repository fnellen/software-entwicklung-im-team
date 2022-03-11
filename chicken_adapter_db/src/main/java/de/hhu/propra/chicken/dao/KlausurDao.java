package de.hhu.propra.chicken.dao;

import de.hhu.propra.chicken.aggregates.klausur.KlausurDto;
import org.springframework.data.repository.CrudRepository;

public interface KlausurDao extends CrudRepository<KlausurDto, Long> {
}
