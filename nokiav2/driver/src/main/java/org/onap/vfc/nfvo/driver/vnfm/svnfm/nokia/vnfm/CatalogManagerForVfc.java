/*
 * Copyright 2016-2017, Nokia Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.vfc.nfvo.driver.vnfm.svnfm.nokia.vnfm;

import org.onap.vfc.nfvo.driver.vnfm.svnfm.nokia.onap.vfc.VfcPackageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CatalogManagerForVfc extends CatalogManager {

    @Autowired
    CatalogManagerForVfc(CbamRestApiProviderForVfc cbamRestApiProvider, VfcPackageProvider packageProvider) {
        super(cbamRestApiProvider, packageProvider);
    }
}
