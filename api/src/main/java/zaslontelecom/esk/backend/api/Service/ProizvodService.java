package zaslontelecom.esk.backend.api.Service;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import zaslontelecom.esk.backend.api.DAO.ProizvodDAO;
import zaslontelecom.esk.backend.api.Model.*;
import zaslontelecom.esk.backend.api.Utils.ExcelReader;
import zaslontelecom.esk.backend.api.Utils.HandledException;
import zaslontelecom.esk.backend.api.Utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class ProizvodService extends BaseService{

    @Autowired
    private ProizvodDAO dbService;

    @Autowired
    ZascitenProizvodService zps;

    @Autowired
    ShemaService shs;

    @Autowired
    ZakonskaPodlagaService zakonskaService;

    public Iterable<Proizvod> list(){
        return dbService.findAll();
    }

    public Optional<Proizvod> get(long id){
        return dbService.findById(id);
    }

    public Proizvod insert(Proizvod item){

        item.setSpremenil(GetCurrentUserId());
        return dbService.save(item);
    }

    public Proizvod update(Proizvod item){
        boolean existing = dbService.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");
        item.setSpremenil(GetCurrentUserId());
        dbService.save(item);

        return item;
    }

    public void delete(long id){
        Proizvod item = get(id).get();
        item.setSpremenil(GetCurrentUserId());
        update(item);
        dbService.deleteById(id);
    }

    public Page<Proizvod> findByQuery(int page, int resultPerPage, String query){
        Pageable pageSortById = PageRequest.of(page, resultPerPage, Sort.by("naziv").descending());
        if (query == null || query.trim() == "")
            return dbService.findAll(pageSortById);
        else
            return dbService.findAllByNazivContainingIgnoreCaseOrZascitenProizvodNazivContainingIgnoreCaseOrZascitenProizvodShemaNazivContainingIgnoreCase(query, query, query, pageSortById);
    }

    public Page<Proizvod> findByZascitenProizvod(int page, int resultPerPage, long idZascitenProizvod){
        Pageable pageSortById = PageRequest.of(page, resultPerPage == 0 ? 100 : resultPerPage, Sort.by("naziv").descending());
        return dbService.findAllByZascitenProizvodIdEqualsAndNazivIsNotNull(idZascitenProizvod, pageSortById);
    }

    public boolean validateProizvod(Proizvod proizvod) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("ZascitenProizvodId", match -> match.exact())
                .withMatcher("naziv", match -> match.ignoreCase());
        boolean another;
        if (proizvod.getNaziv() == null || proizvod.getNaziv().trim() == "")
            another = dbService.existsByZascitenProizvodIdEqualsAndNazivIsNull(proizvod.getZascitenProizvod().getId());
        else
            another = dbService.existsByZascitenProizvodIdEqualsAndNazivIgnoreCaseEquals(proizvod.getZascitenProizvod().getId(), proizvod.getNaziv());

        boolean same =  false;
        if (proizvod.getId() != 0) {
            Optional<Proizvod> foundProizvod = dbService.findById(proizvod.getId());

            if (foundProizvod.isPresent()){
                same = true;
            }
        }

        return !another || same;
    }

    public void save(@RequestBody Proizvod item) throws HandledException {
        Shema shema = item.getZascitenProizvod().getShema();
        if (shema == null || shema.getNaziv() == null || shema.getNaziv().trim() == "") {
            throw new HandledException("Shema nima naziva!");
        }

        if (item.getEnota() == null || item.getEnota().trim() == "") {
            throw new HandledException("Enota je obvezen podatek!");
        }

        Optional<Shema> foundShema = this.shs.getByNaziv(shema.getNaziv());

        if (foundShema.isPresent()) {
            shema = foundShema.get();
        } else{
            this.shs.save(shema);
        }

        ZascitenProizvod zp = item.getZascitenProizvod();

        if (zp == null || Utils.isNullOrEmpty(zp.getNaziv())) {
            throw new HandledException("Zaščiten proizvod nima naziva!");
        }

        Optional<ZascitenProizvod> foundZp = this.zps.getByIdShemaAndNaziv(shema.getId(), zp.getNaziv());

        if (foundZp.isPresent()) {
            zp = foundZp.get();
        }else{
            zp.setShema(shema);
            this.zps.save(zp);
        }

        item.setZascitenProizvod(zp);
        item.setSpremenil(GetCurrentUserId());
        boolean validProizvod = validateProizvod(item);

        if (foundShema.isPresent() && foundZp.isPresent() && !validProizvod) {
            throw new HandledException("Proizvod že obstaja!");
        }

        dbService.save(item);
    }

    public void clearUnusedData(){
        dbService.clearUnusedData();
    }

    public Optional<Proizvod> getByIdZascitenProizvod(Long idZascitenProizvod, String naziv){
        if (naziv == null || naziv.trim().equals(""))
            return this.dbService.getByZascitenProizvodIdAndNazivIsNull(idZascitenProizvod);
        else
            return this.dbService.getByZascitenProizvodIdAndNazivIgnoreCaseEquals(idZascitenProizvod, naziv);
    }

    public void importFromExcel(InputStream inputStream) throws IOException, InvalidFormatException, HandledException {
        ArrayList<ArrayList<String>> excel = ExcelReader.read(inputStream, true);
        String errors = "";
        int index = 1;
        for (ArrayList<String> row: excel) {
            try {
                index++;
                long length = row.spliterator().getExactSizeIfKnown();

                if (length < 2 ){
                    throw new HandledException("Vrstica " + index + ". mora vsebovati najmanj shemo in IK?");
                }


                Shema sh = new Shema();
                sh.setNaziv(row.get(0));

                ZascitenProizvod zp = new ZascitenProizvod();
                zp.setNaziv(row.get(1));
                zp.setShema(sh);

                Proizvod prx = new Proizvod();
                prx.setZascitenProizvod(zp);

                if (length > 2) {
                    prx.setNaziv(row.get(2));
                }

                if (length > 3) {
                    String zkString = row.get(3);

                    if (!Utils.isNullOrEmpty(zkString)) {
                        Optional<ZakonskaPodlaga> zk = this.zakonskaService.getByStevilka(zkString);
                        if (!zk.isPresent()) {
                            throw new HandledException("Ne najdem zakonske podlage " + zkString);

                        }
                        prx.setZakonskaPodlaga(zk.get());
                    }
                }

                String enota = row.get(4);
                prx.setEnota(enota);

                this.save(prx);
            } catch (HandledException e) {
                errors += e.getMessage() + System.lineSeparator();
                e.printStackTrace();
            }
        }
        this.clearUnusedData();

        if (!Utils.isNullOrEmpty(errors)){
            throw new HandledException(errors);
        }
    }

    public long countByIdZascitenProizvod(long idZascitenProizvod) {
        return dbService.countByZascitenProizvodId(idZascitenProizvod);
    }

    public Iterable<Proizvod> getByIdCertifikat(long id) {
        return dbService.getByIdCertifikat(id);
    }
}