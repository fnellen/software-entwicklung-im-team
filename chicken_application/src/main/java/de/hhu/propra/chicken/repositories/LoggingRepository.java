package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.services.LogOperation;
import java.time.LocalDateTime;

public interface LoggingRepository {

  void logEntry(LocalDateTime dateTime, LogOperation action, String typ, String gitHubHandle,
                ZeitraumDto vonZustand, ZeitraumDto nachZustand);
}
