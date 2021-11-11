package zaslontelecom.esk.backend.api.Utils;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import zaslontelecom.esk.backend.api.Model.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class Utils {

    public static Date today() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0); // same for minutes and seconds
        today.set(Calendar.MINUTE, 0); // same for minutes and seconds
        today.set(Calendar.SECOND, 0); // same for minutes and seconds
        today.set(Calendar.MILLISECOND, 0); // same for minutes and seconds

        return today.getTime();
    }

    public static java.sql.Date sqlToday() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0); // same for minutes and seconds
        today.set(Calendar.MINUTE, 0); // same for minutes and seconds
        today.set(Calendar.SECOND, 0); // same for minutes and seconds
        today.set(Calendar.MILLISECOND, 0); // same for minutes and seconds
        return new java.sql.Date(today.getTime().getTime());
    }
    public static java.sql.Date getCurrentSqlDate() {
        java.sql.Date now = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        return now;
    }

    public static java.sql.Date toSqlDate(String sDate) throws ParseException {
        return toSqlDateWithPattern(sDate, "dd.MM.yyyy");
    }
    public static java.sql.Date toSqlDateWithPattern(String sDate, String pattern) throws ParseException {
        if (isNullOrEmpty(sDate)){
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date date = formatter.parse(sDate);
        return new java.sql.Date(date.getTime());
    }

    public static void deleteAllFilesFromFolder(String workingPath) throws IOException {
        FileUtils.cleanDirectory(new File(workingPath));
    }

    public static Double toDoubleTwoDecimals(String o) {
        return (double) Math.round(Double.valueOf((o).replace(",",".")) * 100) / 100;
    }

    public enum DateCompareEnum{
        isHigher, isHigherOrEquals, isEquals, isLower, isLowerOrEquals
    }

    public static boolean dateComparer(java.sql.Date date1, DateCompareEnum comparer, java.sql.Date date2){
        if (date1 == null || date2 == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int d1 = Integer.parseInt(sdf.format(date1));
        int d2 = Integer.parseInt(sdf.format(date2));

        switch(comparer){
            case isEquals: { return d1 == d2; }
            case isHigher: { return d1 > d2; }
            case isHigherOrEquals: { return d1 > d2 || d1 == d2; }
            case isLower: { return d1 < d2; }
            case isLowerOrEquals: { return d1 < d2 || d1 == d2; }
            default: { return false;}
        }
    }

    public static boolean isNullOrEmpty(String value){
        if (value == null)
            return true;

        if (value.trim().equals(""))
            return true;

        return false;
    }

    public static boolean isNumeric(String value){
        if (isNullOrEmpty(value)){
            return false;
        }
        try {
            Double.parseDouble(value);
        }catch (Exception ex){
            return false;
        }
        return true;
    }

    public static boolean hasValue(long value){
        if (value == 0)
            return false;
        return true;
    }

    public static boolean hasNoValue(long value){
        return !Utils.hasValue(value);
    }

    public static boolean notExistsDejavnost(Dejavnost dejavnost) {
        return dejavnost == null || Utils.isNullOrEmpty(dejavnost.getNaziv()) || Utils.hasNoValue(dejavnost.getId());
    }

    public static boolean existsDejavnost(Dejavnost dejavnost) {
        return !Utils.notExistsDejavnost(dejavnost);
    }

    public static boolean notExistsSubjekt(Subjekt subjekt) {
        return (subjekt == null || subjekt.getId() == 0);
    }

    public static boolean existsSubject(Subjekt subjekt) {
        return !Utils.notExistsSubjekt(subjekt);
    }

    public static boolean notExistsZascitenProizvod(ZascitenProizvod zp){
        return (zp == null || zp.getId() == 0);
    }

    public static boolean existsZascitenProizvod(ZascitenProizvod zp){
        return !Utils.notExistsZascitenProizvod(zp);
    }

    public static boolean notExistsPrilogaProizvod(Collection<CertifikatPrilogaProizvod> priloga) {
        return (priloga == null || priloga.spliterator().getExactSizeIfKnown() == 0);
    }

    public static boolean existsPrilogaProizvod(Collection<CertifikatPrilogaProizvod> priloga) {
        return !notExistsPrilogaProizvod(priloga);
    }

    public static boolean notExistsPrilogaClan(Collection<CertifikatPrilogaClan> priloga) {
        return (priloga == null || priloga.spliterator().getExactSizeIfKnown() == 0);
    }

    public static boolean existsPrilogaClan(Collection<CertifikatPrilogaClan> priloga) {
        return !notExistsPrilogaClan(priloga);
    }

    public static boolean hasNoData(Priloga priloga) {
        return (Utils.isNullOrEmpty(priloga.getVsebina()) && Utils.isNullOrEmpty(priloga.getStevilka()) && priloga.getDatIzdaje() == null && priloga.getDatVelj() == null);
    }

    public static boolean hasData(Priloga priloga) {
        return !hasNoData(priloga);
    }

    public static String toYYYYMMDDD(java.sql.Date value) {
        if (value == null){
            return null;
        }

        SimpleDateFormat simpDate = new SimpleDateFormat("yyyyMMdd");
        return simpDate.format(value);
    }

    public static String toDateString(java.sql.Date value, String pattern) {
        if (value == null){
            return null;
        }

        SimpleDateFormat simpDate = new SimpleDateFormat(pattern);
        return simpDate.format(value);
    }

    public static String getCommaSeparatedProductNames(List<Proizvod> proizvod) {
        if (proizvod == null || proizvod.spliterator().getExactSizeIfKnown() == 0){
            return null;
        }
        String result = "";
        for(Proizvod pr: proizvod){
            result += pr.getNaziv() + ",";
        }

        result = result.substring(0,result.length() -1);

        return result;
    }

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static Date now(){
        return Date.from(Instant.now());
    }

    public static String readFile(String filePath)
    {
        return readFileAsUTF8(filePath);
    }

    public static String parseToXHtml(String html) {
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return doc.html();
    }

    public static byte[] readFileAsByteArray(String filePath) throws IOException {
         return Files.readAllBytes(Paths.get(filePath));
    }

    public static String readFileAsUTF8(String filePath){
        String result = "";
        try {
            File fileDir = new File(filePath);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(fileDir), "UTF8"));

            String str;

            while ((str = in.readLine()) != null) {
                result += str;
            }

            in.close();
        }
        catch (UnsupportedEncodingException e)
        {
            System.out.println(e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return Utils.getValueOrNull(result);
    }

    public static byte[] convertFileToByteArray(String filePath) {

        Path path = Paths.get(filePath);

        byte[] codedFile = null;

        try {
            codedFile = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return codedFile;
    }

    public static boolean saveFileFromByteArray(String filePath, byte[] decodedByteArray) {

        boolean success = false;

        Path path = Paths.get(filePath);

        try {
            Files.write(path, decodedByteArray);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    public static FileInputStream readFileAsInputStream(String filePath){
        try {
            return new FileInputStream(filePath);
        } catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static boolean saveFileFromString(String filePath, String value) {

        boolean success = false;

        Path path = Paths.get(filePath);

        try {
            Files.write(path, value.getBytes());
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    public static String image2Base64(String imagePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(imagePath));
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static String getValueOrDefault(String value) {
        if (value == null)
            return "";

        return value.trim();
    }

    public static String getValueOrNull(String value) {
        if (isNullOrEmpty(value))
            return null;

        return value.trim();
    }

    public static void deleteFiles(List<String> filenames) {
        for (String filename: filenames
        ) {
           deleteFile(filename);
        }
    }

    public static void deleteFile(String filename) {
        File file = new File(filename);
        file.delete();
    }

    public static void renameFile(String from, String to) {
        File file = new File(from);
        File file2 = new File(to);
        file.renameTo(file2);
    }

    public static byte[] HexToBin(String str){
        try{
            byte[] result=new byte[str.length()/2];
            for (int i = 0; i < result.length; i++)
                result[i]=(byte) Integer.parseInt(str.substring(2*i, (2*i)+2), 16);
            return result;
        }catch(Exception x){
            x.printStackTrace();
            return null;
        }
    }

    public static String BinToHex(byte[] b){
        try{
            StringBuffer sb=new StringBuffer();
            for (byte bb:b) {
                String hexStr = String.format("%02x", bb);
                sb.append((hexStr.length()<2)?"0":"");
                sb.append(hexStr);
            }
            return sb.toString();
        }catch(Exception x){
            x.printStackTrace();
            return null;
        }
    }
}
