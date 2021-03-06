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

package tachyon.client.table;

import tachyon.annotation.PublicApi;

/**
 * An implementation of Tachyon Raw Table client. This is simply a wrapper around
 * {@link AbstractTachyonRawTables}. Users can obtain an instance of this class by calling
 * {@link TachyonRawTablesFactory#get()}.
 */
@PublicApi
public class TachyonRawTables extends AbstractTachyonRawTables {
  private static TachyonRawTables sTachyonRawTables;

  public static class TachyonRawTablesFactory {
    private TachyonRawTablesFactory() {} // prevent init

    public static synchronized TachyonRawTables get() {
      if (sTachyonRawTables == null) {
        sTachyonRawTables = new TachyonRawTables();
      }
      return sTachyonRawTables;
    }
  }

  private TachyonRawTables() {
    super();
  }
}
