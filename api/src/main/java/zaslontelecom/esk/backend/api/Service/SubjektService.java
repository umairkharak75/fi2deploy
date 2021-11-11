package zaslontelecom.esk.backend.api.Service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.EvsubjektDAO;
import zaslontelecom.esk.backend.api.DAO.SubjektDAO;
import zaslontelecom.esk.backend.api.Model.Evsubjekt;
import zaslontelecom.esk.backend.api.Model.Subjekt;
import zaslontelecom.esk.backend.api.Utils.ExcelReader;
import zaslontelecom.esk.backend.api.Utils.HandledException;
import zaslontelecom.esk.backend.api.Utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class SubjektService extends BaseService {

    @Autowired
    private SubjektDAO dbService;
    @Autowired
    private EvsubjektDAO viewService;

    public Optional<Subjekt> get(long id){
        return dbService.findById(id);
    }

    public Optional<Evsubjekt> getFromViewByMaticna(String maticna){
        return viewService.findLastByMaticna(maticna);
    }

    public Optional<Evsubjekt> getFromViewAllByMaticna(String maticna){
        return viewService.listAllLastByMaticna(maticna);
    }

    public Optional<Evsubjekt> getFromViewByKmgmid(String kmgmid){
        return viewService.findLastByKmgmid(kmgmid);
    }

    public Optional<Evsubjekt> getFromViewAllByKmgmid(String kmgmid){
        return viewService.listAllLastByKmgmid(kmgmid);
    }

    public Collection<Evsubjekt> findByQuery(String query){
        query = query.toLowerCase();

        if (Utils.isNumeric(query)){
            ArrayList<Evsubjekt> result = new ArrayList<>();
            if (query.length() == 8){ // davčna
                Optional<Evsubjekt> data =  this.viewService.findLastByDavcna(query);
                if (data.isPresent()){
                    result.add(data.get());
                }
            }
            if (query.length() == 10){ // matična
                Optional<Evsubjekt> data =  this.viewService.findLastByMaticna(query);
                if (data.isPresent()){
                    result.add(data.get());
                }
            }
            if (query.length() == 9){ // KMGMID
                Optional<Evsubjekt> data =  this.viewService.findLastByKmgmid(query);
                if (data.isPresent()){
                    result.add(data.get());
                }
            }

            return result;
        } else {
            return this.viewService.findAllByName(query);
        }
    }

    public Optional<Subjekt> getLastAndSaveBeforeIfNecessary(long subjId){
        // get from view
        Optional<Evsubjekt> found = viewService.findLastBySubjId(subjId);

        if (!found.isPresent()){
            return null;
        }

        // convert to our structure
        Subjekt subjekt = this.convert(found.get());

        // check if exists in our database
        Optional<Subjekt> result = Optional.ofNullable(this.getAndSaveBeforeIfNecessary(subjekt));

        return result;
    }

    public Subjekt convert(Evsubjekt evsubjekt) {
        Evsubjekt res = evsubjekt;
        Subjekt subj = new Subjekt();
        subj.setId(-1);
        subj.setDatZs(res.getDatZs());
        subj.setDavcna(res.getDavcna());
        subj.setEmail(res.getEmail());
        subj.setIdPoste(res.getIdPoste());
        subj.setPosta(res.getPosta());
        subj.setIme(res.getIme());
        subj.setMaticna(res.getMaticna());
        subj.setKmgmid(res.getKmgmid());
        subj.setNaslov(res.getNaslov());
        subj.setObcina(res.getObcina());
        subj.setPriimek(res.getPriimek());
        subj.setNaziv(Utils.isNullOrEmpty(res.getNaziv()) ? res.getIme() + " " + res.getPriimek() : res.getNaziv());
        subj.setObId(res.getObId());
        subj.setTelSt(res.getTelSt());
        subj.setSubjId(res.getSubjId());
        subj.setSpremenil(GetCurrentUserId());
        return subj;
    }

    public Subjekt getAndSaveBeforeIfNecessary(Subjekt subjekt){
        Optional<Subjekt> found = dbService.findFirstBySubjIdAndDatZsGreaterThanEqualOrderByIdDesc(subjekt.getSubjId(), subjekt.getDatZs());

        if (found.isPresent()){
            return found.get();
        } else {
            subjekt.setId(0); // HACK negative id is returned when data is coming from ESK evSubjekt VIEW
            subjekt.setSpremenil(GetCurrentUserId());
            dbService.save(subjekt);
            return subjekt;
        }
    }

    public ArrayList<Subjekt> importFromExcel(InputStream inputStream) throws HandledException, IOException, InvalidFormatException {
        // NOSILEC_KMGMID	NOSILEC_MATICNA
        ArrayList<ArrayList<String>> excel = ExcelReader.read(inputStream, true);
        String errors = "";
        int index = 1;
        ArrayList<Subjekt> result = new ArrayList<>();
        for (ArrayList<String> row : excel) {
            try {
                index++;
                String kmgmid = row.get(0);
                String maticna = row.get(1);
                Subjekt subjekt = this.getSubjekt(kmgmid, maticna);
                result.add(subjekt);
            } catch (Exception e) {
                errors += "Vrstica " + index + ": " + e.getMessage() + System.lineSeparator();
                e.printStackTrace();
            }
        }

        if (!Utils.isNullOrEmpty(errors)) {
            throw new HandledException(errors);
        }

        return result;
    }

    public ArrayList<ArrayList<String>> getMembersFromExcel(String stevilka, int row, ArrayList<ArrayList<String>> excel) {
        String current = stevilka;
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        int index = row - 2; // decrease index for two because second row is starting record
        for (int i = index; i < excel.spliterator().getExactSizeIfKnown(); i++) {
            stevilka = excel.get(i).get(0);

            if (current != stevilka)
                break;

            result.add(excel.get(i));
        }

        return result;
    }

    public Subjekt getSubjekt(String kmgmid, String maticna) throws HandledException {
        Optional<Evsubjekt> subjekt;

        if (maticna == null && kmgmid == null) {
            throw new HandledException("Subjekt nima definirane matične številke ali KMGMID številke.");
        }

        if (maticna != null ) subjekt = this.getFromViewByMaticna(maticna);
        else subjekt = this.getFromViewByKmgmid(kmgmid);

        if (!subjekt.isPresent()){
            throw new HandledException("Subjekt ne obstaja. (KMGMID="+ Utils.getValueOrDefault(kmgmid) + ", matična=" + Utils.getValueOrDefault(maticna) + ")");
        }

        Evsubjekt res = subjekt.get();
        Subjekt subj = this.convert(res);
        return subj;
    }

    public Subjekt getSubjektForImport(String kmgmid, String maticna) throws HandledException {
        Optional<Evsubjekt> subjekt;

        if (maticna == null && kmgmid == null) {
            throw new HandledException("Subjekt nima definirane matične številke ali KMGMID številke.");
        }

        if (maticna != null ) subjekt = this.getFromViewAllByMaticna(maticna);
        else subjekt = this.getFromViewAllByKmgmid(kmgmid);

        if (!subjekt.isPresent()){
            throw new HandledException("Subjekt ne obstaja. (KMGMID="+ Utils.getValueOrDefault(kmgmid) + ", matična=" + Utils.getValueOrDefault(maticna) + ")");
        }

        Evsubjekt res = subjekt.get();
        Subjekt subj = this.convert(res);
        return subj;
    }

    public Subjekt getSubjektAll(String kmgmid, String maticna) throws HandledException {
        Optional<Evsubjekt> subjekt;

        if (maticna == null && kmgmid == null) {
            throw new HandledException("Subjekt nima definirane matične številke ali KMGMID številke.");
        }

        if (maticna != null ) subjekt = this.getFromViewByMaticna(maticna);
        else subjekt = this.getFromViewByKmgmid(kmgmid);

        if (!subjekt.isPresent()){
            throw new HandledException("Subjekt ne obstaja. (KMGMID="+ Utils.getValueOrDefault(kmgmid) + ", matična=" + Utils.getValueOrDefault(maticna) + ")");
        }

        Evsubjekt res = subjekt.get();
        Subjekt subj = this.convert(res);
        return subj;
    }
}