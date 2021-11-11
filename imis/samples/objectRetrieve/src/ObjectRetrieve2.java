import java.io.File;
import java.io.IOException;

import com.imis.storageconnector.Storage;
import com.imis.storageconnector.StorageConnector;
import com.imis.storageconnector.StorageConnectorException;


public class ObjectRetrieve2
{
  private static StorageConnector mStorageConnector = null;
  private static String mHost = null;
  private static int mPort = 0;
  private static String mObjectId = null;
  private static String mFileName = null;
    
  private static void retrieveToSpecifiedFile() throws StorageConnectorException, IOException
  {
    /*
     * Open IMiS/ARC Storage...
     */
    Storage storage = mStorageConnector.openIMiSARCStorage(mHost, mPort);

    /*
     * Retrieve the specified object and store it in a file passed as the
     * second parameter. Objects Content Type is returned.
     */
    String mime = storage.retrieveObject(mObjectId, mFileName);
    
    /*
     * Print object metadata
     */
    System.out.println("FilePath = " + mFileName);
    System.out.println("FileSize = " + Long.toString(new File(mFileName).length()));
    System.out.println("MimeType = " + mime);

    /*
     * Launch the output file with associated application
     */
    Runtime.getRuntime().exec("cmd /c " + mFileName);
  }
  
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    /*
     * Initializes IMiS/Storage Connector singleton object. This call
     * is required to be called at least once before any other call
     * to IMiS/Storage Connector. All consequent calls to this method
     * will return the same instance of IMiS/Storage Connector object.
     */
    mStorageConnector = StorageConnector.getInstance();
    try {
      if (4 > args.length) {
        System.out.println("Command line arguments required by this sample:\n" +
        	"storage host, port, object id and file name.");
        return;
      }
      
      /**
       * Storage host, port, object id and a file name are passed 
       * through command line arguments
       */
      mHost = args[0];
      mPort = Integer.parseInt(args[1]);
      mObjectId = args[2];
      mFileName = args[3];
            
      /**
       * Retrieve object from storage to a specified file.
       */
      retrieveToSpecifiedFile();
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
    finally {
      /*
       * Terminates IMiS/Storage Connector singleton object. All calls 
       * to IMiS/Storage Connector objects after this point are invalid
       * and will produce an error.
       */
      StorageConnector.freeInstance();
    }
  }

}
