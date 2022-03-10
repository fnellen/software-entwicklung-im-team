import chicken.aggregates.klausur.Klausur;
import chicken.aggregates.klausur.VeranstaltungsId;
import org.springframework.stereotype.Repository;
import repositories.KlausurRepository;

@Repository
public class KlausurRepositoryImpl implements KlausurRepository {
  @Override
  public Klausur findeKlausurMitId(VeranstaltungsId id) {
    return null;
  }

  @Override
  public Klausur findeKlausurMitName(String veranstaltungsName) {
    return null;
  }

  @Override
  public void speicherKlausur(Klausur klausur) {

  }
}
