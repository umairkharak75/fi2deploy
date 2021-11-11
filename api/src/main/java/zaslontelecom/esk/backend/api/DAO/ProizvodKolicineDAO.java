package zaslontelecom.esk.backend.api.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.ProizvodKolicine;
import java.util.Optional;


@Service
@Repository
public interface ProizvodKolicineDAO extends CrudRepository<ProizvodKolicine, Integer> {
    Optional<ProizvodKolicine> getByShemaAndZascitenproizvodAndProizvodAndLetoAndKmgmid(String shema, String zp, String proizvod, Long leto, String kmgmid);
    Optional<ProizvodKolicine> getByShemaAndZascitenproizvodAndProizvodAndLetoAndNazivSubjAndNaslov(String shema, String zp, String proizvod, Long leto, String naziv, String naslov);

    Optional<ProizvodKolicine> getByShemaAndZascitenproizvodAndProizvodIsNullAndLetoAndKmgmid(String shema, String zp, Long leto, String kmgmid);
    Optional<ProizvodKolicine> getByShemaAndZascitenproizvodAndProizvodIsNullAndLetoAndNazivSubjAndNaslov(String shema, String zp, Long leto, String naziv, String naslov);
}