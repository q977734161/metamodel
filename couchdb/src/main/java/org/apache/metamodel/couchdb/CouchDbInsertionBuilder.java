/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.metamodel.couchdb;

import java.util.HashMap;
import java.util.Map;

import org.ektorp.CouchDbConnector;
import org.apache.metamodel.MetaModelException;
import org.apache.metamodel.insert.AbstractRowInsertionBuilder;
import org.apache.metamodel.schema.Column;
import org.apache.metamodel.schema.Table;

final class CouchDbInsertionBuilder extends AbstractRowInsertionBuilder<CouchDbUpdateCallback> {

    public CouchDbInsertionBuilder(CouchDbUpdateCallback updateCallback, Table table) {
        super(updateCallback, table);
    }

    @Override
    public void execute() throws MetaModelException {
        Table table = getTable();
        String name = table.getName();

        Object[] values = getValues();
        Column[] columns = getColumns();
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < columns.length; i++) {
            Column column = columns[i];
            if (isSet(column)) {
                map.put(column.getName(), values[i]);
            }
        }

        CouchDbConnector connector = getUpdateCallback().getConnector(name);
        connector.addToBulkBuffer(map);
    }
}
