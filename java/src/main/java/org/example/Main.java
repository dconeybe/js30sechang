package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
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
      test2();

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
      System.err.println("zzyzx1 EXCEPTION BEGIN");
      e.printStackTrace();
      System.err.println("zzyzx1 EXCEPTION END");
    } finally {
      System.out.println("zzyzx1 END " + System.nanoTime());
    }
  }

  private static void test2() throws IOException {
    ArrayList<String> nameServers = loadNameserversFromResolvConf();
    for (String nameServer : nameServers) {
      System.out.println("zzyzx2 START [" + nameServer + "] " + System.nanoTime());
      try {
        Hashtable<String, String> env = new Hashtable<>();
        env.put("com.sun.jndi.ldap.connect.timeout", "5000");
        env.put("com.sun.jndi.ldap.read.timeout", "5000");
        InitialDirContext dirContext = new InitialDirContext(env);
        System.out.println("zzyzx2 [" + nameServer + "] dirContext.getAttributes(\"dns://" + nameServer + "/_grpclb._tcp.firestore.googleapis.com\", new String[]{\"SRV\"})");
        dirContext.getAttributes("dns://" + nameServer + "/_grpclb._tcp.firestore.googleapis.com", new String[]{"SRV"});
      } catch (Exception e) {
        System.err.println("zzyzx2 [" + nameServer + "] EXCEPTION BEGIN");
        e.printStackTrace();
        System.err.println("zzyzx2 [" + nameServer + "] EXCEPTION END");
      } finally {
        System.out.println("zzyzx2 [" + nameServer + "] END " + System.nanoTime());
      }
    }
  }

  private static ArrayList<String> loadNameserversFromResolvConf() throws IOException {
    ArrayList<String> nameServers = new ArrayList<>();
    System.out.println("zzyzx loading /etc/resolv.conf started");
    try (BufferedReader reader = new BufferedReader(new FileReader("/etc/resolv.conf"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty())
          continue;
        if (line.charAt(0) == '#' || line.charAt(0) == ';')
          continue;
        if (!line.startsWith("nameserver"))
          continue;
        String value = line.substring("nameserver".length());
        if (value.isEmpty())
          continue;
        if (value.charAt(0) != ' ' && value.charAt(0) != '\t')
          continue;
        StringTokenizer st = new StringTokenizer(value, " \t");
        while (st.hasMoreTokens()) {
          String val = st.nextToken();
          if (val.charAt(0) == '#' || val.charAt(0) == ';') {
            break;
          }
          if (val.indexOf(':') >= 0 &&
              val.indexOf('.') < 0 && // skip for IPv4 literals with port
              val.indexOf('[') < 0 &&
              val.indexOf(']') < 0 ) {
            // IPv6 literal, in non-BSD-style.
            val = "[" + val + "]";
          }
          nameServers.add(val);
        }
      }
    }
    System.out.println("zzyzx loading /etc/resolv.conf done; loaded "
        + nameServers.size() + " name servers: " + nameServers);
    return nameServers;
  }
}