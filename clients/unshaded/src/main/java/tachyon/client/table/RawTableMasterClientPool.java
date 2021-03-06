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

import java.net.InetSocketAddress;

import tachyon.Constants;
import tachyon.client.ClientContext;
import tachyon.client.RawTableMasterClient;
import tachyon.resource.ResourcePool;

final class RawTableMasterClientPool extends ResourcePool<RawTableMasterClient> {
  private final InetSocketAddress mMasterAddress;

  public RawTableMasterClientPool(InetSocketAddress masterAddress) {
    super(ClientContext.getConf().getInt(Constants.USER_RAW_TABLE_MASTER_CLIENT_THREADS));
    mMasterAddress = masterAddress;
  }

  @Override
  public void close() {
    // TODO(calvin): Consider collecting all the clients and shutting them down
  }

  @Override
  protected RawTableMasterClient createNewResource() {
    return new RawTableMasterClient(mMasterAddress, ClientContext.getConf());
  }
}
