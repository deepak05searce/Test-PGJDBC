package org.postgresql.util;

import java.sql.Timestamp;
import java.time.Instant;
import junit.framework.TestCase;
import org.junit.*;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

//import org.postgresql.util.GetClassName;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public abstract class SetUpTearDownIMPL {

  private static int n;

  private static String results;
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
  public static void beforeCl() {
    //Here now finally is FooTest!
    Timestamp instant = Timestamp.from(Instant.now());

    String className = classWatcher.getClassName();
    String[] arrOfStr = (new String (className)).split("\\.");
    n = arrOfStr.length;
    if(n <=0)
      return;
    results = arrOfStr[n-1];
    System.out.println(className);
    System.out.println("test started of " + results + " at time "+ instant);
  }
  @AfterClass
  public static void afterCl() {
    String className = classWatcher.getClassName();
    //Here now finally is FooTest!
    Timestamp instant= Timestamp.from(Instant.now());

    String[] arrOfStr = className.split("\\.");

    n = arrOfStr.length;

    if(n <=0)
      return;
    results = arrOfStr[n-1];

    System.out.println("execution  of "+results+ " finished at time "+instant );

    System.out.println(" path of your test class " + className);
  }






// System.out.println("total time required while executing this testClasses is "+ );
//    System.out.println(" path of your test class " + className);


//   @BeforeClass
//   public static void setUps() throws Exception {
//
//   System.out.println("executing test "+  getClassContext()[1].getName());
//   }
//
//   @AfterClass
//   public static void tearDowns() throws SQLException, Exception {
//
//     System.out.println( new GetClassName().getClassName()+" executing test");
//   String s = new File("").getAbsolutePath();
//     String t = new File("").getCanonicalPath();
//     String currDirectory = System.getProperty("user.dir");
//   // System.out.println("execution completed of test "+currDirectory);
//     System.out.println(s+t);
//   }

  public void setTimeStamp2(int timeStamp2) {
    this.timestamp2 = timestamp2;
  }

  public void setTimeStamp1(int timeStamp1) {
    this.timestamp1 = timestamp1;
  }
}




