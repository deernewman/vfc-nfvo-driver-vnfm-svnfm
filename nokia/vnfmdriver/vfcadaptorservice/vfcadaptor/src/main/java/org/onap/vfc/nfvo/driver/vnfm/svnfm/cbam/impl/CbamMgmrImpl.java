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

package org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.impl;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMCreateVnfRequest;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMCreateVnfResponse;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMHealVnfRequest;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMHealVnfResponse;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMInstantiateVnfRequest;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMInstantiateVnfResponse;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMQueryOperExecutionResponse;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMQueryVnfResponse;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMScaleVnfRequest;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMScaleVnfResponse;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMTerminateVnfRequest;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.bo.CBAMTerminateVnfResponse;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.cbam.inf.CbamMgmrInf;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.common.bo.AdaptorEnv;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.common.util.CommonUtil;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.constant.CommonConstants;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.http.client.HttpClientProcessorInf;
import org.onap.vfc.nfvo.driver.vnfm.svnfm.http.client.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

@Component
public class CbamMgmrImpl implements CbamMgmrInf {
	private static final Logger logger = Logger.getLogger(CbamMgmrImpl.class);
	private Gson gson = new Gson();
	
	@Autowired 
	private AdaptorEnv adaptorEnv;
	
	@Autowired
	HttpClientProcessorInf httpClientProcessor;
	
	private String retrieveToken() throws ClientProtocolException, IOException, JSONException {
		String result = null;
		String url= adaptorEnv.getCbamApiUriFront() + CommonConstants.CbamRetrieveTokenPath;
		HashMap<String, String> map = new HashMap<>();
		map.put(CommonConstants.ACCEPT, "*/*");
		map.put(CommonConstants.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		
		String bodyPostStr = String.format(CommonConstants.CbamRetrieveTokenPostStr, adaptorEnv.getGrantType(), adaptorEnv.getClientId(), adaptorEnv.getClientSecret(), adaptorEnv.getCbamUserName(), adaptorEnv.getCbamPassword());
		
		String responseStr = httpClientProcessor.process(url, RequestMethod.POST, map, bodyPostStr).getContent();
		
		logger.info("CbamMgmrImpl -> retrieveToken, responseStr is " + responseStr);
		
		JSONObject tokenJsonObject = new JSONObject(responseStr);
		
		result = tokenJsonObject.getString(CommonConstants.CBAM_TOKEN_KEY);
		
		return result;
	}
	
	public CBAMCreateVnfResponse createVnf(CBAMCreateVnfRequest cbamRequest) throws ClientProtocolException, IOException {
		String httpPath = CommonConstants.CbamCreateVnfPath;
		RequestMethod method = RequestMethod.POST;
			
		HttpResult httpResult = operateCbamHttpTask(cbamRequest, httpPath, method);
		String responseStr = httpResult.getContent();
		
		logger.info("CbamMgmrImpl -> createVnf, responseStr is " + responseStr);
		int code = httpResult.getStatusCode();
		if(code == 201) {
			logger.info("CbamMgmrImpl -> createVnf success");
		}else {
			logger.error("CbamMgmrImpl -> createVnf error ");
		}
		CBAMCreateVnfResponse response = gson.fromJson(responseStr, CBAMCreateVnfResponse.class);
		
		return response;
	}
	
	/* (non-Javadoc)
	 * @see com.nokia.vfcadaptor.cbam.impl.CbamMgmrInf#instantiateVnf(com.nokia.vfcadaptor.cbam.bo.CBAMInstantiateVnfRequest, java.lang.String)
	 */
	public CBAMInstantiateVnfResponse instantiateVnf(CBAMInstantiateVnfRequest cbamRequest, String vnfInstanceId) throws ClientProtocolException, IOException {
		String httpPath = String.format(CommonConstants.CbamInstantiateVnfPath, vnfInstanceId);
		RequestMethod method = RequestMethod.POST;
			
		HttpResult httpResult = operateCbamHttpTask(cbamRequest, httpPath, method);
		String responseStr = httpResult.getContent();
		
		logger.info("CbamMgmrImpl -> instantiateVnf, responseStr is " + responseStr);
		int code = httpResult.getStatusCode();
		if(code == 202) {
			logger.info("CbamMgmrImpl -> instantiateVnf success " );
		}else {
			logger.error("CbamMgmrImpl -> instantiateVnf error " );
		}
		CBAMInstantiateVnfResponse response = gson.fromJson(responseStr, CBAMInstantiateVnfResponse.class);
		
		return response;
	}
	
	public CBAMTerminateVnfResponse terminateVnf(CBAMTerminateVnfRequest cbamRequest, String vnfInstanceId) throws ClientProtocolException, IOException {
		String httpPath = String.format(CommonConstants.CbamTerminateVnfPath, vnfInstanceId);
		RequestMethod method = RequestMethod.POST;
		
		HttpResult httpResult = operateCbamHttpTask(cbamRequest, httpPath, method);
		String responseStr = httpResult.getContent();
		
		logger.info("CbamMgmrImpl -> terminateVnf, responseStr is " + responseStr);
		int code = httpResult.getStatusCode();
		if(code == 202) {
			logger.info("CbamMgmrImpl -> terminateVnf  sucess " );
		}else {
			logger.error("CbamMgmrImpl -> terminateVnf error " );
		}
		CBAMTerminateVnfResponse response = gson.fromJson(responseStr, CBAMTerminateVnfResponse.class);
		
		return response;
	}
	
	public void deleteVnf(String vnfInstanceId) throws ClientProtocolException, IOException {
		String httpPath = String.format(CommonConstants.CbamDeleteVnfPath, vnfInstanceId);
		RequestMethod method = RequestMethod.DELETE;
		HttpResult httpResult = operateCbamHttpTask(null, httpPath, method);
		
		int code = httpResult.getStatusCode();
		if(code == 204) {
			logger.info("CbamMgmrImpl -> deleteVnf success.");
		}else {
		    logger.error("CbamMgmrImpl -> deleteVnf error. detail info is " + httpResult.getContent());
		}
		
	}
	
	public CBAMScaleVnfResponse scaleVnf(CBAMScaleVnfRequest cbamRequest, String vnfInstanceId) throws ClientProtocolException, IOException {
		String httpPath = String.format(CommonConstants.CbamScaleVnfPath, vnfInstanceId);
		RequestMethod method = RequestMethod.POST;
			
		HttpResult httpResult = operateCbamHttpTask(cbamRequest, httpPath, method);
		String responseStr = httpResult.getContent();
		int code = httpResult.getStatusCode();
		if(code == 202) {
			logger.info("CbamMgmrImpl -> scaleVnf success.");
		}else {
		    logger.error("CbamMgmrImpl -> scaleVnf error. " );
		}
		CBAMScaleVnfResponse response = gson.fromJson(responseStr, CBAMScaleVnfResponse.class);
		
		return response;
	}

	public CBAMHealVnfResponse healVnf(CBAMHealVnfRequest cbamRequest, String vnfInstanceId) throws ClientProtocolException, IOException {
		String httpPath = String.format(CommonConstants.CbamHealVnfPath, vnfInstanceId);
		RequestMethod method = RequestMethod.POST;
			
		HttpResult httpResult = operateCbamHttpTask(cbamRequest, httpPath, method);
		String responseStr = httpResult.getContent();
		
		logger.info("CbamMgmrImpl -> healVnf, responseStr is " + responseStr);
		int code = httpResult.getStatusCode();
		if(code == 202) {
			logger.info("CbamMgmrImpl -> healVnf success.");
		}else {
		    logger.error("CbamMgmrImpl -> healVnf error. " );
		}
		CBAMHealVnfResponse response = gson.fromJson(responseStr, CBAMHealVnfResponse.class);
		
		return response;
	}
	
	public CBAMQueryVnfResponse queryVnf(String vnfInstanceId) throws ClientProtocolException, IOException {
		String httpPath = String.format(CommonConstants.CbamQueryVnfPath, vnfInstanceId);
		RequestMethod method = RequestMethod.GET;
		
		HttpResult httpResult = operateCbamHttpTask(null, httpPath, method);
		String responseStr = httpResult.getContent();
		
		logger.info("CbamMgmrImpl -> queryVnf, responseStr is " + responseStr);
		int code = httpResult.getStatusCode();
		if(code == 200) {
			logger.info("CbamMgmrImpl -> queryVnf success.");
		}else {
		    logger.error("CbamMgmrImpl -> queryVnf error. " );
		}
		
		CBAMQueryVnfResponse response = gson.fromJson(responseStr, CBAMQueryVnfResponse.class);
		
		return response;
	}

	public HttpResult operateCbamHttpTask(Object httpBodyObj, String httpPath, RequestMethod method) throws ClientProtocolException, IOException {
		String token = null;
		try {
			token = retrieveToken();
		} catch (JSONException e) {
			logger.error("retrieveTokenError ", e);
		}
	
		String url= adaptorEnv.getCbamApiUriFront() + httpPath;
		
		HashMap<String, String> map = new HashMap<>();
		map.put(CommonConstants.AUTHORIZATION, "bearer " + token);
		map.put(CommonConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		
		return httpClientProcessor.process(url, method, map, gson.toJson(httpBodyObj));
	}

	public CBAMQueryOperExecutionResponse queryOperExecution(String execId) throws ClientProtocolException, IOException{
		String httpPath = String.format(CommonConstants.CbamGetOperStatusPath, execId);
		RequestMethod method = RequestMethod.GET;
		
		HttpResult httpResult = operateCbamHttpTask(null, httpPath, method);
		String responseStr = httpResult.getContent();
		
		logger.info("CbamMgmrImpl -> CBAMQueryOperExecutionResponse, responseStr is " + responseStr);
		
		int code = httpResult.getStatusCode();
		if(code == 200) {
			logger.info("CbamMgmrImpl -> CBAMQueryOperExecutionResponse, success" );
		}else if(code == 202) {
			logger.info("CbamMgmrImpl -> CBAMQueryOperExecutionResponse, ongoing" );
		}else {
			logger.error("CbamMgmrImpl -> CBAMQueryOperExecutionResponse, error" );
		}
		
		
		CBAMQueryOperExecutionResponse response = gson.fromJson(responseStr, CBAMQueryOperExecutionResponse.class);
		
		return response;
	}

	public void setAdaptorEnv(AdaptorEnv adaptorEnv) {
		this.adaptorEnv = adaptorEnv;
	}

	@Override
	public void uploadVnfPackage(String cbamPackageFilePath) throws ClientProtocolException, IOException {
		String httpPath = CommonConstants.CbamUploadVnfPackagePath;
		RequestMethod method = RequestMethod.POST;
		
		HttpResult httpResult = operateCbamHttpUploadTask(cbamPackageFilePath, httpPath, method);
		String responseStr = httpResult.getContent();
		
		logger.info("CbamMgmrImpl -> uploadVnfPackage, responseStr is " + responseStr);
		
		int code = httpResult.getStatusCode();
		if(code == 200) {
			logger.info("CbamMgmrImpl -> uploadVnfPackage, success" );
			logger.info("Upload vnf package " + cbamPackageFilePath + " to CBAM is successful.");
		}else {
			logger.error("CbamMgmrImpl -> uploadVnfPackage, error" );
		}
	}

	private HttpResult operateCbamHttpUploadTask(String filePath, String httpPath, RequestMethod method) throws ClientProtocolException, IOException {
		String token = null;
		try {
			token = retrieveToken();
		} catch (JSONException e) {
			logger.error("retrieveTokenError ", e);
		}
	
		String url = adaptorEnv.getCbamApiUriFront() + httpPath;
		
		HashMap<String, String> map = new HashMap<>();
		map.put(CommonConstants.AUTHORIZATION, "bearer " + token);
		map.put(CommonConstants.CONTENT_TYPE, "multipart/form-data, boundary=--fsgdsfgjgjdsgdfjgjgj");
		byte[] fileBytes = CommonUtil.getBytes(filePath);
		return httpClientProcessor.processBytes(url, method, map, fileBytes);
	}
}
