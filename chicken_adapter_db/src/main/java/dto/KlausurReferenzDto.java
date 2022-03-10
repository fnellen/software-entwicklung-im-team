package dto;

import chicken.aggregates.klausur.VeranstaltungsId;
import org.springframework.data.relational.core.mapping.Embedded;

public record KlausurReferenzDto(
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL) VeranstaltungsIdDto id) {
}
