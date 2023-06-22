package org.example;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

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
}