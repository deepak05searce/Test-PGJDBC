package org.postgresql.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import java.util.Date;
import junit.framework.TestCase;
import org.junit.*;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.omg.SendingContext.RunTime;
//import org.postgresql.util.Constants;

//import org.postgresql.util.GetClassName;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public abstract class SetUpTearDownIMPL {

  private static int n;

  private static String results;
  private static Object InterruptedException;
  private static Object IOException;
  Timestamp timestamp1;
  Timestamp timestamp2;



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
      //This will set className to = FooTest!

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
    //System.out.println("current timestamp as yyyy.MM.dd.HH.mm.ss " + timeStamp);
    String className = classWatcher.getClassName();//
    String[] arrOfStr = (new String (className)).split("\\.");
    n = arrOfStr.length;
    if(n <=0)
      return;
    results = arrOfStr[n-1];
   String filename = arrOfStr[n-1];
   String filePath = className.replace('.','/');

    String finalPath =  System.getProperty("user.dir") + "/Wireshark-Logs/JDBC/" + ts + "/"+"Java"+ "/" + filePath;
      String logFilePath = finalPath;
   System.out.println(logFilePath);
    Path path = Paths.get(logFilePath);
    Files.createDirectories(path);

        boolean t = Files.exists(path);
        System.out.println(t);
//
// Process process;
//
//     process = Runtime.getRuntime().exec(String.format("chmod -R 777 "+logFilePath));
//
//     process = Runtime.getRuntime().exec(String.format("tshark -i any  -f \"host " + "192.168.29.250"  + " && port " + "5432" + "\" -w " + logFilePath +"/" + ".pcapng &"));
//     System.out.println("starting wiresharklog");
   // ProcessBuilder processBuilder = new ProcessBuilder();
// processBuilder.command("sh","-c","ls");
// processBuilder.command("sh","-c","chmod -R 777 "+logFilePath+"/");
//processBuilder.command("sh","-c","sudo tshark -i any -f \"host 192.168.34.49 && port 5432\" -w " + logFilePath +"/testing.pcapng &") ;
   Process process ;
   Process process1 ;
   System.out.println("Logfile Path :"+logFilePath);
    process1 = Runtime.getRuntime()
        .exec("sh -c chmod -R 777 "+ logFilePath);

   // System.out.println((String.format("sh","-c","tshark","-i","any","-f\"host 192.168.152.49 && port 5432\"", "-w"," %s/testing.pcapng &",logFilePath)));
   String logFileNamePath = logFilePath+"/testing.pcapng";

    Runtime currentRuneTime = Runtime.getRuntime();
    //process = currentRuneTime.exec("sh -c echo hello123 > "+logFileNamePath);
    process = currentRuneTime.exec("tshark -i any -f \"host 192.168.182.49 && port 5432\"  -w "+logFileNamePath+" &");
    //tshark -i any -f "host 192.168.34.49 && port 5432" -w log.pcapng
    //System.out.println(currentRuneTime.totalMemory());


    //    .exec("sh -c tshark -i any -f \"host 192.168.34.49 && port 5432\" -w " + logFileNamePath + " &");
   // process = Runtime.getRuntime()
    //         .exec((String.format("sh","-c","tshark -i any -f \"host 192.168.152.49 && port
    //         5432\" -w  %s/testing.pcapng &",logFilePath)));
    // process = Runtime.getRuntime()
    //         .exec((String.format("sh","-c","tshark -i any -f \"host 192.168.152.49 && port
    //         5432\" -w  %s/testing.pcapng &",logFilePath)));
    //process.waitFor();
    Thread.sleep(5000);
    StreamGobbler streamGobbler =
         new StreamGobbler(process.getInputStream(),logFileNamePath);
    Executors.newSingleThreadExecutor().submit(streamGobbler);



//
   System.out.println(System.getProperty("server", "localhost")+ "  "+ Integer.parseInt(System.getProperty("port", System.getProperty("def_pgport", "5432"))));

//     StringBuilder output = new StringBuilder();
//
//    BufferedReader reader = new BufferedReader(
//        new InputStreamReader(process.getInputStream()));
//
//     String line;
//     while ((line = reader.readLine()) != null) {
//       output.append(line + "\n");
//     }
//     System.out.println(output);
    //int exitCode = process.waitFor();
    //System.out.println("Exit code: "+exitCode);
    TimeUnit.SECONDS.sleep(5);
    Timestamp instant = Timestamp.from(Instant.now());
    results = arrOfStr[n-1];
    System.out.println(className);
    System.out.println("test started of " + results + " at time "+ instant);
    }
//System.out.println(output.toString());


  @AfterClass
  public static void afterCl() throws InterruptedException, IOException {
//     String className = classWatcher.getClassName();
//     //Here now finally is FooTest!
//     Timestamp instant= Timestamp.from(Instant.now());
//
//     String[] arrOfStr = className.split("\\.");
//
//     n = arrOfStr.length;
//
//     if(n <=0)
//       return;
//     results = arrOfStr[n-1];
//     System.out.println("Tear down and Kill the wireshark");
//
//     Process process;
//     process = Runtime.getRuntime().exec(String.format("ps aux | grep tshark | awk 'NR==1 {print $2}' | sudo xargs kill"));
//   //  ProcessBuilder processBuilder = new ProcessBuilder();
//   //  processBuilder.command("ps aux | grep tshark | awk 'NR==1 {print $2}' | sudo xargs kill");
//  //  Process process = processBuilder.start();
//
//     TimeUnit.SECONDS.sleep(5);
//
//     System.out.println("execution  of "+results+ " finished at time "+instant );
//
//     System.out.println(" path of your test class " + className);
  }

  public void setTimeStamp2(int timeStamp2) {
    this.timestamp2 = timestamp2;
  }

  public void setTimeStamp1(int timeStamp1) {
    this.timestamp1 = timestamp1;
  }
}




