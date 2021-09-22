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

import org.postgresql.test.TestUtil;

public abstract class SetUpTearDownIMPL {

  private static int n;
  private static String results;
  private static Object InterruptedException;
  private static Object IOException;
  private static boolean b=false;

  static Date now = new java.util.Date();
  static Timestamp current = new java.sql.Timestamp(now.getTime());

//   public static void fillTimeStamp() {
//     Date now = new java.util.Date();
//     Timestamp current = new java.sql.Timestamp(now.getTime());
//     String t = new SimpleDateFormat("yyyyMMddHHmmss").format(current);
//   }

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
    Process process; // process object

    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(current);
    String classPathName = classWatcher.getClassName();//
      int index = getClassFolderPath(classPathName);
    String className = classPathName.substring(index+1);
      classPathName = classPathName.substring(0,index);

    String filePath = classPathName.replace('.', '/');
    //String className = classPathName.substring(index+1);
    String logFilePath = System.getProperty("user.dir") + "/Wireshark-Logs/JDBC/" + timeStamp + "/" +
        "Java" + "/" + filePath;
    Path path = Paths.get(logFilePath);
    Files.createDirectories(path);
    boolean isPath = Files.exists(path);
    String dbhost = TestUtil.getServer();
    String dbport = String.valueOf(TestUtil.getPort());
    String logFileNamePath = logFilePath+"/"+className+ ".pcapng";
    String cmd = "tshark -w "+logFileNamePath;
    //String permissionCmdLogFilePath = "sh -c chmod -R 777 "+ logFilePath;
    String[] tsharkRunCmd = new String[]{"tshark","-i","lo0","-f","host "+dbhost+" && port "+dbport,"-w", logFileNamePath};
    String[] permissionCmdLogFilePath = new String[]{"chmod","-R","777", logFileNamePath};
    //System.out.println("checking wheather the path exist :"+isPath);
    System.out.println("logFilePath "+logFilePath);
    //System.setProperty("ssl","false");
    System.out.println("dbhost :"+dbhost+" dbport :"+dbport);

    System.out.println("jdbc:postgresql://" + System.getProperty("server", "localhost") + ":"+Integer.parseInt(System.getProperty("port", System.getProperty("def_pgport", "5432"))) + "/"+
        System.getProperty("database", "test"));
    for(int i=0;i< tsharkRunCmd.length;i++)
       System.out.print(tsharkRunCmd[i]+" ");
    Runtime currentRuneTime = Runtime.getRuntime();
    process = currentRuneTime.exec(tsharkRunCmd);


   // Thread.sleep(000);
//     StreamGobbler streamGobbler =
//         new StreamGobbler(process.getInputStream(), logFileNamePath + "-log");
//     Executors.newSingleThreadExecutor().submit(streamGobbler);

    TimeUnit.SECONDS.sleep(5);
    Timestamp instant = Timestamp.from(Instant.now());
    System.out.println(className);
    System.out.println("test started  at time " + instant);
  }

  @AfterClass
  public static void afterCl() throws InterruptedException, IOException {
    System.out.println("Tear down and Kill the wireshark");
    Process process;
    String[] tsharkKillCmd = new String[]{"sh","-c","ps","ax","|","grep","tshark","|","awk","\'{print $1}\'","|","sudo","xargs","kill"};
    //String[] tshark = new String[]{"sudo","ps","o","pgid",",","args","|","grep","tshark:","|","sudo","awk" ,"{system(sudo kill --signal SIGSTOP -$1)}-"};
    process = Runtime.getRuntime().exec(tsharkKillCmd);
    TimeUnit.SECONDS.sleep(5);
    System.out.println();
  }

  public static int getClassFolderPath(String path){
    int len = path.length();
    int i;
    for(i=len-1;i>=0;i--){
        if(path.charAt(i)=='.'){
          break;
        }

    }

    return i;
  }
 }




