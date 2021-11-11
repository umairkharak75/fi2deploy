package zaslontelecom.esk.backend.api.Utils.Imis;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import com.imis.storageconnector.AuthType;
import com.imis.storageconnector.Document;
import com.imis.storageconnector.DocumentAccess;
import com.imis.storageconnector.Storage;
import com.imis.storageconnector.StorageConnector;
import com.imis.storageconnector.StorageConnectorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Utils.Settings;

import javax.security.auth.login.LoginException;

@Service
public class ImisDms
{
    @Autowired
    Settings settings;

    public String documentStore(String localPdfName) throws LoginException {
        ImisOptions imisOptions = new ImisOptions().invoke();
        String stageId = imisOptions.getStageId();
        Map<String, Object> options = imisOptions.get();

        String objectId = null;
        Storage storage = null;
        try {
            applyPassword(options);
            storage = StorageConnector.getInstance().openIMiSARCStorage(settings.getImisHost(), options);
            objectId = storage.storeObject(localPdfName, stageId, null);
        }
        catch (StorageConnectorException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (LoginException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Zapiranje seje do odložišča vsebin
            if (null != storage) {
                try {
                    storage.close();
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            clearPassword(options);
        }

        return objectId;
    }

    public void documentRetrieve(String idImis, String localPdfName) {
        ImisOptions imisOptions = new ImisOptions().invoke();
        Map<String, Object> options = imisOptions.get();

        Storage storage = null;
        try {
            applyPassword(options);
            storage = StorageConnector.getInstance().openIMiSARCStorage(settings.getImisHost(), options);
            Document doc = null;
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(localPdfName);
                doc = storage.openObject(idImis, DocumentAccess.READ);
                System.out.println(doc.getDefaultMime());
                is = doc.getInputDataStream();
                byte[] buffer = new byte[0x100000];
                int rdBytes;
                while (-1 != (rdBytes = is.read(buffer))) {
                    fos.write(buffer, 0, rdBytes);
                }
            } finally {
                if (null != is) {
                    try {
                        is.close();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                if (null != fos) {
                    try {
                        fos.close();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                if (null != doc) {
                    try {
                        doc.close();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (StorageConnectorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        } finally {
            // Zapiranje seje do odložišča vsebin
            if (null != storage) {
                try {
                    storage.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            clearPassword(options);
        }
    }

    private void clearPassword(Map<String, Object> options) {
        //char[] password = (char[])options.get(StorageConnector.OPTION_USER_PASSWORD);
        //Arrays.fill(password, '*');
        options.remove(StorageConnector.OPTION_USER_PASSWORD);
    }

    private void applyPassword(Map<String, Object> options) {
        options.put(StorageConnector.OPTION_USER_PASSWORD, String.valueOf(settings.getImisPassword()));
    }

    private class ImisOptions {
        private String appName;
        private String stageId;
        private Map<String, Object> options;

        public String getStageId() {
            return stageId;
        }

        public Map<String, Object> get() {
            return options;
        }

        public ImisOptions  invoke() {
            this.appName = "ESK-MKGP";
            String svcUsername = settings.getImisUsername();
            stageId = settings.getImisStage();

            AuthType svcAuthType = AuthType.LDAP_CREDENTIALS;

            options = new TreeMap();
            options.put(StorageConnector.OPTION_USER_NAME, svcUsername);
            options.put(StorageConnector.OPTION_APPLICATION_NAME, appName);
            options.put(StorageConnector.OPTION_AUTH_TYPE, svcAuthType);
            return this;
        }
    }
}
