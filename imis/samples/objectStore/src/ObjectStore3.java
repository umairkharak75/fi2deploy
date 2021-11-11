import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.imis.storageconnector.Document;
import com.imis.storageconnector.Storage;
import com.imis.storageconnector.StorageConnector;
import com.imis.storageconnector.StorageConnectorException;


public class ObjectStore3
{
  private static StorageConnector mStorageConnector = null;
  private static String mHost = null;
  private static int mPort = 0;
  private static String mProfile = null;
  private static String mFileName = null;
  private static String mMime = null;
    
  private static void storeSpecifiedFileThroughStream() throws StorageConnectorException, IOException
  {
    /*
     * Open IMiS/ARC Storage...
     */
    Storage storage = mStorageConnector.openIMiSARCStorage(mHost, mPort);
    
    /*
     * Open input file for reading...
     */
    FileInputStream fileInStream = new FileInputStream(mFileName);

    /*
     * Create an empty IMiS/Storage Connector document for writing and
     * acquire its data stream...
     */
    Document doc = storage.createObject(mProfile, mMime);
    OutputStream docOutStream = doc.getOutputDataStream();

    /*
     * Loop through the file and write it's content to the archiving
     * document
     */
    int bytesRead;
    byte[] buffer = new byte[8192];
    while (-1 != (bytesRead = fileInStream.read(buffer)))
      docOutStream.write(buffer, 0, bytesRead);

    /*
     * Commit newly written data by saving the document. This operation
     * flushes any uncommitted data to the server
     */
    doc.save();
    
    /*
     * Save object identifier.
     */
    String objectId = doc.getId();

    /*
     * Close object data stream.
     */
    docOutStream.close();

    /*
     * Close the newly created object.
     */
    doc.close();

    /*
     * Object ID is printed to console. This peace of information
     * can be used to retrieve the same object later with one of
     * the RetrieveObject() methods or OpenObject() method.
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
      if (5 > args.length) {
        System.out.println("Command line arguments required by this sample:\n" +
        	"storage host, port, profile, file name and mime type.");
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
      mMime = args[4];
            
      /**
       * Store a file with a specified mime on a storage
       * with the use of stream.
       */
      storeSpecifiedFileThroughStream();
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
