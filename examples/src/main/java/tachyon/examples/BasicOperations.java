/*
 * Licensed to the University of California, Berkeley under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package tachyon.examples;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tachyon.Constants;
import tachyon.TachyonURI;
import tachyon.Version;
import tachyon.client.file.FileInStream;
import tachyon.client.file.FileOutStream;
import tachyon.client.file.TachyonFileSystem;
import tachyon.thrift.BlockInfoException;
import tachyon.thrift.FileAlreadyExistException;
import tachyon.thrift.FileDoesNotExistException;
import tachyon.thrift.InvalidPathException;
import tachyon.util.CommonUtils;
import tachyon.util.FormatUtils;

public class BasicOperations implements Callable<Boolean> {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  private final TachyonURI mFilePath;
  private final int mNumbers = 20;

  public BasicOperations(TachyonURI filePath) {
    mFilePath = filePath;
  }

  @Override
  public Boolean call() throws Exception {
    TachyonFileSystem fileSystem = TachyonFileSystem.get();
    FileOutStream outStream = createFile(fileSystem);
    writeFile(fileSystem, outStream);
    return false;
//    return readFile(fileSystem);
  }

  private FileOutStream createFile(TachyonFileSystem fileSystem) throws IOException, InvalidPathException,
          FileAlreadyExistException, BlockInfoException {
    LOG.debug("Creating file...");
    long startTimeMs = CommonUtils.getCurrentMs();
    FileOutStream outStream = fileSystem.getOutStream(mFilePath);
    LOG.info(FormatUtils.formatTimeTakenMs(startTimeMs, "createFile with path " + mFilePath.getPath()));
    return outStream;
  }

  private void writeFile(TachyonFileSystem fileSystem, FileOutStream outStream) throws IOException {
    ByteBuffer buf = ByteBuffer.allocate(mNumbers * 4);
    buf.order(ByteOrder.nativeOrder());
    for (int k = 0; k < mNumbers; k ++) {
      buf.putInt(k);
    }

    buf.flip();
    LOG.debug("Writing data...");
    buf.flip();

    long startTimeMs = CommonUtils.getCurrentMs();
    outStream.write(buf.array());
    outStream.close();

    LOG.info(FormatUtils.formatTimeTakenMs(startTimeMs, "writeFile to file " + mFilePath));
  }

  private boolean readFile(TachyonFileSystem fileSystem) throws IOException,
          InvalidPathException, FileDoesNotExistException {
    FileInStream inStream = fileSystem.getInStream(fileSystem.open(mFilePath));

    boolean pass = true;
    LOG.debug("Reading data...");

    final long startTimeMs = CommonUtils.getCurrentMs();
    for (int k = 0; k < mNumbers; k ++) {
      pass = pass && (inStream.read() == k);
    }
    inStream.close();

    LOG.info(FormatUtils.formatTimeTakenMs(startTimeMs, "readFile file " + mFilePath));
    return pass;
  }

  public static void main(String[] args) throws IllegalArgumentException {
    if (args.length != 1) {
      System.out.println("java -cp target/tachyon-" + Version.VERSION
          + "-jar-with-dependencies.jar "
          + "tachyon.examples.BasicOperations <FilePath>");
      System.exit(-1);
    }

    Utils.runExample(new BasicOperations(new TachyonURI(args[0])));
  }
}
