import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.imis.storageconnector.Document;
import com.imis.storageconnector.Storage;
import com.imis.storageconnector.StorageConnector;
import com.imis.storageconnector.StorageConnectorException;


public class ObjectRetrieve3
{
  private static StorageConnector mStorageConnector = null;
  private static String mHost = null;
  private static int mPort = 0;
  private static String mObjectId = null;
  private static String mFileName = null;
  
  private static void retrieveToSpecifiedFileThroughStream() throws StorageConnectorException, IOException
  {
    /*
     * Open IMiS/ARC Storage...
     */
    Storage storage = mStorageConnector.openIMiSARCStorage(mHost, mPort);

    /*
     * Open the specified object for reading and
     * acquire its data stream...
     */
    Document doc = storage.openObject(mObjectId, Document.MODE_READONLY);
    String mime = doc.getDefaultMime();
    InputStream docInStream = doc.getInputDataStream();

    /*
     * Open output file for writing...
     */
    if ((null == mFileName) || (0 == mFileName.length())) {
      mFileName = System.getProperty("java.io.tmpdir");
      mFileName = mFileName.substring(0, mFileName.lastIndexOf('.') + 1) + doc.getDefaultExtension();
    }

    FileOutputStream fileOutStream = new FileOutputStream(mFileName);

    /*
     * Loop through the object data and write it's content to the output file
     */    
    int bytesRead;
    byte[] buffer = new byte[8192];
    while (-1 != (bytesRead = docInStream.read(buffer)))
      fileOutStream.write(buffer, 0, bytesRead);

    // Close object data stream.
    docInStream.close();

    // Close the newly created object.
    doc.close();

    // Close output file
    fileOutStream.close();

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
       * Retrieve object from storage to a specified file
       * with the use of stream.
       */
      retrieveToSpecifiedFileThroughStream();
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
