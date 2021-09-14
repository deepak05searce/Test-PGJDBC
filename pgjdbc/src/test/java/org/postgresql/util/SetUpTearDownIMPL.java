package org.postgresql.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Date;

import org.junit.*;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public abstract class SetUpTearDownIMPL {

  private static int n;
  private static String results;
  private static Object InterruptedException;
  private static Object IOException;

  @ClassRule
  public static CustomTestWatcher classWatcher = new CustomTestWatcher();

  //   private static Map<String, String> testConfig;
//   private static FakeApplication fakeApplication;
  public static class CustomTestWatcher extends TestWatcher {
    private String className = SetUpTearDownIMPL.class.getSimpleName();

    public String getClassName() {
      return className;
    }

    private void setClassName(String className) {
      this.className = className;
    }

    @Override
    protected void starting(Description description) {
      setClassName(description.getClassName());
      //System.out.println("\nStarting test class: " + className);
    }
  }

  @BeforeClass
  public static void beforeCl() throws InterruptedException, IOException {
    //Here now finally is FooTest!
    Date now = new java.util.Date();
    Timestamp current = new java.sql.Timestamp(now.getTime());
    String ts = new SimpleDateFormat("yyyyMMddHHmmss").format(current);
    String className = classWatcher.getClassName();//
    String filePath = className.replace('.', '/');
    String finalPath = System.getProperty("user.dir") + "/Wireshark-Logs/JDBC/" + ts + "/" +
        "Java" + "/" + filePath;
    String logFilePath = finalPath;
    System.out.println(logFilePath);
    Path path = Paths.get(logFilePath);
    Files.createDirectories(path);

    boolean t = Files.exists(path);
    System.out.println(t);

    String dbhost = System.getProperty("server", System.getProperty("PGXHOST","127.0.0.1"));
    String dbport = System.getProperty("port", System.getProperty("port", System.getProperty(
        "PGXPORT", "5432")));

     System.out.println(dbhost+" "+dbport);
    Process process;
//    Process process1 ;
//    System.out.println("Logfile Path :"+logFilePath);
//     process1 = Runtime.getRuntime().exec("sh -c chmod -R 777 "+ logFilePath);
    String logFileNamePath = logFilePath + "/testing.pcapng";
    Runtime currentRuneTime = Runtime.getRuntime();
    process =
        currentRuneTime.exec("tshark -f \"host " + dbhost + " && port " + dbport + "\" -w " + logFileNamePath + " &");

    Thread.sleep(5000);
    StreamGobbler streamGobbler =
        new StreamGobbler(process.getInputStream(), logFileNamePath + "-log");
    Executors.newSingleThreadExecutor().submit(streamGobbler);

    TimeUnit.SECONDS.sleep(5);
    Timestamp instant = Timestamp.from(Instant.now());
    System.out.println(className);
    System.out.println("test started  at time " + instant);
  }

  @AfterClass
  public static void afterCl() throws InterruptedException, IOException {
    System.out.println("Tear down and Kill the wireshark");
    Process process;
    process = Runtime.getRuntime().exec(String.format("ps aux | grep tshark | awk 'NR==1 {print " +
        "$2}' | sudo xargs kill"));
    TimeUnit.SECONDS.sleep(5);
  }
}




