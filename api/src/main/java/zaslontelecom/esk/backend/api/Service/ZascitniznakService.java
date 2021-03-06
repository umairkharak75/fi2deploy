package zaslontelecom.esk.backend.api.Service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.ZascitniznakDAO;
import zaslontelecom.esk.backend.api.DAO.ZascitniznakProizvodDAO;
import zaslontelecom.esk.backend.api.Model.Subjekt;
import zaslontelecom.esk.backend.api.Model.Zascitniznak;
import zaslontelecom.esk.backend.api.Model.ZascitniznakProizvod;
import zaslontelecom.esk.backend.api.Utils.ExcelReader;
import zaslontelecom.esk.backend.api.Utils.HandledException;
import zaslontelecom.esk.backend.api.Utils.Utils;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class ZascitniznakService extends BaseService{

    @Autowired
    private ZascitniznakDAO dbService;

    @Autowired
    private ZascitniznakProizvodDAO zascitniznakProizvodDao;

    @Autowired
    EntityManager em;

    @Autowired
    private SubjektService subjektService;

    public Optional<Zascitniznak> get(long id){
        return dbService.findById(id);
    }

    public Zascitniznak insert(Zascitniznak item){
        item.setSpremenil(GetCurrentUserId());
        return dbService.save(item);
    }

    public Zascitniznak update(Zascitniznak item){
        boolean existing = dbService.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");
        item.setSpremenil(GetCurrentUserId());
        dbService.save(item);

        return item;
    }

    public void delete(long id){
        Zascitniznak item = get(id).get();
        item.setSpremenil(GetCurrentUserId());
        update(item);
        dbService.deleteById(id);
    }

    public Page<Zascitniznak> findByQuery(int page, int resultPerPage, String query){
        Pageable pageSortById = PageRequest.of(page, resultPerPage, Sort.by("id").descending());
        if (query == null || query.trim() == ""){
            return  dbService.findAll(pageSortById);
        } else {
            return dbService.findAllByNazivProizvodaContainingIgnoreCase(query.trim().toLowerCase(), pageSortById);
        }
    }

    public void validateObject(Zascitniznak zznak) throws HandledException {

        if (zznak.getId() == 0) {
            if (dbService.countByStevilka(zznak.getStevilka()) > 0){
                throw new HandledException("Za????itni znak s to ??tevilko ??e obstaja!");
            }
        }

        boolean hasNoProduct = (Utils.isNullOrEmpty(zznak.getNazivProizvoda()));

        if (hasNoProduct)
            throw new HandledException("Za????itni znak mora imeti dolo??en proizvod!");

        if (Strings.isEmpty(zznak.getCertOrgan()))
            throw new HandledException("Za????itni znak mora imeti dolo??eno kontrolno organizacijo!");

        if (Strings.isEmpty(zznak.getStevilka()))
            throw new HandledException("Manjka ??tevilka za????itnega znaka");

        if (Strings.isEmpty(zznak.getStOdl()))
            throw new HandledException("Manjka ??tevilka odlo??be");

        if (Strings.isEmpty(zznak.getZzShema()))
            throw new HandledException("Morate dolo??iti shemo za????itnih znakov.");

        if (zznak.getDatOdl() == null)
            throw new HandledException("Datum odlo??be ne obstaja!");

        if (!Utils.existsSubject(zznak.getImetnik()))
            throw new HandledException("Imetnik za????itnega znaka ne obstaja.");
    }

    @Transactional
    public void importFromExcel(InputStream inputStream) throws IOException, InvalidFormatException, HandledException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        ArrayList<ArrayList<String>> excel = ExcelReader.read(inputStream, true);
        String errors = "";
        int index = 1;
        String current = "";
        for (ArrayList<String> row: excel) {
            try {
                index++;

                Zascitniznak zznak = new Zascitniznak();
                String zzShema = row.get(0);
                // get data into namely variables
                String nazivProizvoda = row.get(3);
                String datOdl = row.get(2);
                String stOdl = row.get(1);
                String certOrgan = row.get(4);
                String kmgmid = row.get(5);
                String maticna = row.get(6);
                String stevilka = row.get(7);
                // first try to get existing certificate in a case of update
                Optional<Zascitniznak> existZZnak = this.getByStevilka(stevilka);

                if (existZZnak.isPresent())
                {
                    zznak = existZZnak.get();
                }

                // populate data
                zznak.setDatOdl(Utils.toSqlDate(datOdl));
                zznak.setCertOrgan(certOrgan);
                zznak.setNazivProizvoda(nazivProizvoda);
                zznak.setZzShema(zzShema);
                zznak.setStOdl(stOdl);
                zznak.setStevilka(stevilka);

                Subjekt subjekt = this.subjektService.getSubjekt(kmgmid, maticna);
                zznak.setImetnik(this.subjektService.getAndSaveBeforeIfNecessary(subjekt));
                zznak.setSpremenil(GetCurrentUserId());

                dbService.save(zznak);
            } catch (Exception e) {
                errors += "Vrstica " + index + ": " +  e.getMessage() + System.lineSeparator();
                e.printStackTrace();
            }
        }

        if (!Utils.isNullOrEmpty(errors)){
            throw new HandledException(errors);
        }
    }

    private Optional<Zascitniznak> getByStevilka(String stevilka) {
        return dbService.getByStevilka(stevilka);
    }
}