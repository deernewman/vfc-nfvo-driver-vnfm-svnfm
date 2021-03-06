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
 
package org.onap.vfc.nfvo.driver.vnfm.svnfm.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.msb.inf.IMsbMgmr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class MsbServiceRegister implements ApplicationRunner{
	private static final Logger logger = LoggerFactory.getLogger(MsbServiceRegister.class);
	
	@Autowired
	private IMsbMgmr msbMgmr;

	@Override
	public void run(ApplicationArguments arg0) {
		logger.info("Start to register nokia vnfm driver service.");
		msbMgmr.register();
		logger.info("nokia vnfm driver successfully registered on the MSB");
	}

}
