package org.postgresql.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.nio.file.Files;
import java.util.function.Consumer;

public  class StreamGobbler implements Runnable {
  private InputStream inputStream;
  private String consumer;

  public StreamGobbler(InputStream inputStream, String consumer) {
    this.inputStream = inputStream;
    this.consumer = consumer;
  }


  @Override
  public void run() {
    final BufferedReader reader;
    final BufferedWriter writer;
    String line;
    Path logFile = Paths.get(consumer);
    reader = new BufferedReader(new InputStreamReader(inputStream));

    try {
      writer = Files.newBufferedWriter(logFile,StandardCharsets.UTF_8);
      while ((line = reader.readLine()) != null) {

        writer.write(line);
        // must do this: .readLine() will have stripped line endings
        writer.newLine();
      }
    }
    catch (Exception ae){

    }
  }
}
