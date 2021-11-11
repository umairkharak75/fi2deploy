package zaslontelecom.esk.backend.api.Service;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.DAO.ProizvodKolicineDAO;
import zaslontelecom.esk.backend.api.Model.ProizvodKolicine;
import zaslontelecom.esk.backend.api.Model.Report;
import zaslontelecom.esk.backend.api.Utils.ExcelReader;
import zaslontelecom.esk.backend.api.Utils.HandledException;
import zaslontelecom.esk.backend.api.Utils.Utils;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProizvodKolicineService extends BaseService {

    @Autowired
    private ProizvodKolicineDAO dbService;

    @Autowired
    ZascitenProizvodService zps;

    @Autowired
    ShemaService shemaService;

    @Autowired
    ProizvodService proizvodService;

    @Autowired
    ZakonskaPodlagaService zakonskaService;

    @Autowired
    EntityManager em;

    public Iterable<ProizvodKolicine> list(){
        return dbService.findAll();
    }

    public Optional<ProizvodKolicine> get(int id){
        return dbService.findById(id);
    }

    public ProizvodKolicine insert(ProizvodKolicine item){
        item.setSpremenil(GetCurrentUserId());
        return dbService.save(item);
    }

    public ProizvodKolicine update(ProizvodKolicine item){
        boolean existing = dbService.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");
        item.setSpremenil(GetCurrentUserId());
        dbService.save(item);

        return item;
    }

    public void delete(int id){
        ProizvodKolicine item = get(id).get();
        item.setSpremenil(GetCurrentUserId());
        update(item);
        dbService.deleteById(id);
    }

    public void importFromExcel(InputStream inputStream) throws IOException, InvalidFormatException, HandledException {
        ArrayList<ArrayList<String>> excel = ExcelReader.read(inputStream, true);
        String errors = "";
        int index = 1;
        for (ArrayList<String> row: excel) {
            try {
                index++;
                long length = row.spliterator().getExactSizeIfKnown();

                if (length != 11 ){
                    throw new HandledException("Tabela količin mora imeti 11 stolpcev.");
                }

                String kmgmid = row.get(0);
                String naziv = row.get(1);
                String naslov = row.get(2);
                String idPoste = row.get(3);
                String posta = row.get(4);
                String shema = row.get(5);
                String zp = row.get(6);
                String proizvod = row.get(7);
                Double kolicina = Utils.toDoubleTwoDecimals(row.get(8));
                String enota = row.get(9);
                Long leto = Long.valueOf(row.get(10));

                Optional<ProizvodKolicine> foundKolicine = this.getByIdProizvodAndLeto(shema, zp, proizvod, leto, kmgmid, naziv, naslov);
                ProizvodKolicine kolicine = foundKolicine.isPresent() ? foundKolicine.get() : new ProizvodKolicine();
                kolicine.setEnota(enota);
                kolicine.setVrednost(kolicina);
                kolicine.setLeto(leto);
                kolicine.setIdPoste(idPoste);
                kolicine.setKmgmid(kmgmid);
                kolicine.setPosta(posta);
                kolicine.setProizvod(proizvod);
                kolicine.setNazivSubj(naziv);
                kolicine.setZascitenproizvod(zp);
                kolicine.setShema(shema);
                kolicine.setNaslov(naslov);

                this.save(kolicine);
            } catch (Exception e) {
                errors += "Vrstica " + index + ": " + e.getMessage() + System.lineSeparator();
                e.printStackTrace();
            }
        }

        if (!Utils.isNullOrEmpty(errors)){
            throw new HandledException(errors);
        }
    }

    public void save(ProizvodKolicine kolicine){
        kolicine.setSpremenil(GetCurrentUserId());
        dbService.save(kolicine);
    }

    public Optional<ProizvodKolicine> getByIdProizvodAndLeto(String shema, String zp, String proizvod, Long leto, String kmgmid, String naziv, String naslov){
        if (Utils.isNullOrEmpty(proizvod)) {
            if (Utils.isNullOrEmpty(kmgmid)) {
                return dbService.getByShemaAndZascitenproizvodAndProizvodIsNullAndLetoAndNazivSubjAndNaslov(shema, zp, leto, naziv, naslov);
            }else{
                return dbService.getByShemaAndZascitenproizvodAndProizvodIsNullAndLetoAndKmgmid(shema, zp, leto, kmgmid);
            }
        }else
        {
            if (Utils.isNullOrEmpty(kmgmid)) {
                return dbService.getByShemaAndZascitenproizvodAndProizvodAndLetoAndNazivSubjAndNaslov(shema, zp, proizvod, leto, naziv, naslov);
            }else{
                return dbService.getByShemaAndZascitenproizvodAndProizvodAndLetoAndKmgmid(shema, zp,  proizvod, leto, kmgmid);
            }
        }
    }
    public List<ProizvodKolicine> findByParams(PagedQuery request) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.getKolicineList");
        // register parameters
        sp.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        sp.setParameter(1, request.getQuery());
        sp.registerStoredProcedureParameter(2, Object.class, ParameterMode.REF_CURSOR);
        sp.execute();
        List<ProizvodKolicine> resultList = new ArrayList<>();
        List<Object> result = sp.getResultList();
        if (result != null) {
            for (Object obj : result) {
                Object[] item = (Object[]) obj;
                ProizvodKolicine row = new ProizvodKolicine();
                row.setId(item[0] == null ? 0 : Integer.valueOf(item[0].toString()));
                row.setKmgmid(item[1] == null ? "" : item[1].toString());
                row.setNazivSubj(item[2] == null ? "" : item[2].toString());
                row.setNaslov(item[3] == null ? "": item[3].toString());
                row.setIdPoste(item[4] == null ? "": item[4].toString());
                row.setPosta(item[5] == null ? "" : item[5].toString());
                row.setVrednost(item[6] == null ? 0 : Utils.toDoubleTwoDecimals(item[6].toString()));
                row.setEnota(item[7] == null ? "" : item[7].toString());
                row.setLeto(item[8] == null ? 0 : Integer.valueOf(item[8].toString()));
                row.setShema(item[9] == null ? "" : item[9].toString());
                row.setZascitenproizvod(item[10] == null ? "" : item[10].toString());
                row.setProizvod(item[11] == null ? "" : item[11].toString());
                resultList.add(row);
            }
        }
        return resultList;
    }
}