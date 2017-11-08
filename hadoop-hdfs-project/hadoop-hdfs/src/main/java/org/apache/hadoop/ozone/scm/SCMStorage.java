/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.ozone.scm;


import org.apache.hadoop.conf.OzoneConfiguration;
import org.apache.hadoop.ozone.common.Storage;
import org.apache.hadoop.ozone.OzoneConsts.NodeType;
import org.apache.hadoop.ozone.web.utils.OzoneUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

/**
 * SCMStorage is responsible for management of the StorageDirectories used by
 * the SCM.
 */
public class SCMStorage extends Storage {

  public static final String STORAGE_DIR = "scm";
  public static final String SCM_ID = "scmUuid";

  /**
   * Construct SCMStorage.
   * @throws IOException if any directories are inaccessible.
   */
  public SCMStorage(OzoneConfiguration conf) throws IOException {
    super(NodeType.SCM, OzoneUtils.getScmMetadirPath(conf), STORAGE_DIR);
  }

  public void setScmUuid(String scmUuid) throws IOException {
    if (getState() == StorageState.INITIALIZED) {
      throw new IOException("SCM is already initialized.");
    } else {
      getStorageInfo().setProperty(SCM_ID, scmUuid);
    }
  }

  /**
   * Retrieves the SCM ID from the version file.
   * @return SCM_ID
   */
  public String getscmUuid() {
    return getStorageInfo().getProperty(SCM_ID);
  }

  @Override
  protected Properties getNodeProperties() {
    String scmUuid = getscmUuid();
    if (scmUuid == null) {
      scmUuid = UUID.randomUUID().toString();
    }
    Properties scmProperties = new Properties();
    scmProperties.setProperty(SCM_ID, scmUuid);
    return scmProperties;
  }

}