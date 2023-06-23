/**
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Firestore, setLogFunction } from '@google-cloud/firestore';

setLogFunction((msg: string) => console.log('zzyzx', msg));

async function main(): Promise<void> {
  const db = new Firestore({
    projectId: 'INSERT_YOUR_PROJECT_ID_HERE'
  });

  try {
    const clients = db.collection('concierge');
    console.log('listDocuments() starting');
    const documentReferences = await clients.listDocuments();
    for (const documentReference of documentReferences) {
      console.log(`Got document: ${documentReference.path}`);
    }
    console.log('listDocuments() done');
  } finally {
    await db.terminate();
  }
}

main();
