package org.example;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import javax.naming.directory.InitialDirContext;

public final class Main {

  private Main() {
    throw new UnsupportedOperationException();
  }

  public static void main(String[] args) throws Exception {
    Logger logger = Logger.getLogger(Main.class.getName());

    Firestore db = null;
    try {
      db = firestore();
      var clients = db.collection("concierge");
      logger.log(Level.INFO, "listDocuments() starting");

      test1();

      clients.listDocuments().forEach(
          docRef ->
              logger.log(Level.INFO, "Got document: " + docRef.getPath()));
      logger.log(Level.INFO, "listDocuments() done");
    } finally {
      if (db != null) {
        db.shutdown();
      }
    }
  }

  private static Firestore firestore() throws Exception {
    FirestoreOptions firestoreOptions =
        FirestoreOptions.getDefaultInstance()
            .toBuilder()
            .setProjectId("INSERT_YOUR_PROJECT_ID_HERE")
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build();
    return firestoreOptions.getService();
  }

  private static void test1() {
    System.out.println("zzyzx1 START " + System.nanoTime());
    try {
      Hashtable<String, String> env = new Hashtable<>();
      env.put("com.sun.jndi.ldap.connect.timeout", "5000");
      env.put("com.sun.jndi.ldap.read.timeout", "5000");
      InitialDirContext dirContext = new InitialDirContext(env);
      dirContext.getAttributes("dns:///_grpclb._tcp.firestore.googleapis.com", new String[]{"SRV"});
    } catch (Exception e) {
      System.out.println("zzyzx1 EXCEPTION BEGIN");
      e.printStackTrace();
      System.out.println("zzyzx1 EXCEPTION END");
    } finally {
      System.out.println("zzyzx1 END " + System.nanoTime());
    }
  }
}