package zaslontelecom.esk.backend.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zaslontelecom.esk.backend.api.Utils.Settings;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Autowired
    Settings settings;

    private enum ResourceType {
        FILE_SYSTEM,
        CLASSPATH
    }

    private static final String FILE_DIRECTORY = "/var/files";

    /**
     * @param filename filename
     * @param response Http response.
     * @return file from system.
     */
    public Resource getFileSystem(String filename, HttpServletResponse response) {
        return getResource(filename, response, ResourceType.FILE_SYSTEM);
    }

    /**
     * @param filename filename
     * @param response Http response.
     * @return file from classpath.
     */
    public Resource getClassPathFile(String filename, HttpServletResponse response) {
        return getResource(filename, response, ResourceType.CLASSPATH);
    }

    private Resource getResource(String filename, HttpServletResponse response, ResourceType resourceType) {
        response.setContentType("text/csv; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setHeader("filename", filename);

        Resource resource = null;
        switch (resourceType) {
            case FILE_SYSTEM:
                resource = new FileSystemResource(filename);
                break;
            case CLASSPATH:
                resource = new ClassPathResource("data/" + filename);
                break;
        }

        return resource;
    }
}