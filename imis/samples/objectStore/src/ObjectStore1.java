import java.io.IOException;

import com.imis.storageconnector.Storage;
import com.imis.storageconnector.StorageConnector;
import com.imis.storageconnector.StorageConnectorException;


public class ObjectStore1
{
  private static StorageConnector mStorageConnector = null;
  private static String mHost = null;
  private static int mPort = 0;
  private static String mProfile = null;
  private static String mFileName = null;
    
  private static void storeFile() throws StorageConnectorException, IOException
  {
    /*
     * Open IMiS/ARC Storage...
     */
    Storage storage = mStorageConnector.openIMiSARCStorage(mHost, mPort);

    /*
     * Store object by letting IMiS/Storage Connector resolve
     * Content Type from file extension...
     * 
     * Object ID is printed to console. This peace of information
     * can be used to retrieve the same object later with one of
     * the RetrieveObject() methods or OpenObject() method.
     */
    String objectId = storage.storeObject(mFileName, mProfile, null);
    
    /*
     * Print object identifier
     */
    System.out.println("ObjectId = " + objectId);
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
        	"storage host, port, profile and file name.");
        return;
      }
      
      /**
       * Storage host, port, object id and a file name are passed 
       * through command line arguments
       */
      mHost = args[0];
      mPort = Integer.parseInt(args[1]);
      mProfile = args[2];
      mFileName = args[3];
      
      /**
       * Store a file on a storage.
       */
      storeFile();      
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
