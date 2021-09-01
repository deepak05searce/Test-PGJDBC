/*
 * Copyright (c) 2017, PostgreSQL Global Development Group
 * See the LICENSE file in the project root for more information.
 */

package org.postgresql.test.core;

import org.postgresql.core.JavaVersion;

import org.junit.Assert;
import org.junit.Test;

import org.postgresql.util.SetUpTearDownIMPL;

public class JavaVersionTest extends SetUpTearDownIMPL {
  @Test
  public void testGetRuntimeVersion() {
    String currentVersion = System.getProperty("java.version");
    String msg = "java.version = " + currentVersion + ", JavaVersion.getRuntimeVersion() = "
        + JavaVersion.getRuntimeVersion();
    System.out.println(msg);
    if (currentVersion.startsWith("1.8")) {
      Assert.assertEquals(msg, JavaVersion.v1_8, JavaVersion.getRuntimeVersion());
    }
  }
}
