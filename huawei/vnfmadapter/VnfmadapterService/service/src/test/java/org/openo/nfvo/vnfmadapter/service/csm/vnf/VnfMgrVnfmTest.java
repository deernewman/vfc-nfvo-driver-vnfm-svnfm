/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.nfvo.vnfmadapter.service.csm.vnf;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openo.nfvo.vnfmadapter.common.ResultRequestUtil;
import org.openo.nfvo.vnfmadapter.service.constant.Constant;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <br/>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Aug 10, 2016
 */
public class VnfMgrVnfmTest {

    @Test
    public void testCreateVnf() {
        new MockUp<ResultRequestUtil>() {

            @Mock
            public JSONObject call(JSONObject vnfmObjcet, String path, String methodName, String paramsJson) {
                JSONObject resultJson = new JSONObject();
                resultJson.put("retCode", Constant.HTTP_CREATED);
                JSONObject appInfo = new JSONObject();
                appInfo.put("vnfinstanceid", "id");
                appInfo.put("project_id", "project_id");
                JSONObject data = new JSONObject();
                data.put("app_info", appInfo);
                resultJson.put("data", data);
                return resultJson;
            }
        };
        String data = "{\"vnfmInfo\":{\"url\":\"url\"}}";
        JSONObject subJsonObject = JSONObject.fromObject(data);
        JSONObject vnfmObjcet = new JSONObject();
        VnfMgrVnfm vnfMgrVnfm = new VnfMgrVnfm();
        JSONObject result = vnfMgrVnfm.createVnf(subJsonObject, vnfmObjcet);

        assertEquals(Constant.REST_SUCCESS, result.get("retCode"));
    }

    @Test
    public void testCreateVnfByBadRequest() {
        new MockUp<ResultRequestUtil>() {

            @Mock
            public JSONObject call(JSONObject vnfmObjcet, String path, String methodName, String paramsJson) {
                JSONObject resultJson = new JSONObject();
                resultJson.put("retCode", Constant.HTTP_BAD_REQUEST);
                return resultJson;
            }
        };
        String data = "{\"vnfmInfo\":{\"url\":\"url\"}}";
        JSONObject subJsonObject = JSONObject.fromObject(data);
        VnfMgrVnfm vnfMgrVnfm = new VnfMgrVnfm();
        JSONObject vnfmObjcet = new JSONObject();
        JSONObject result = vnfMgrVnfm.createVnf(subJsonObject, vnfmObjcet);

        JSONObject resultJson = new JSONObject();
        resultJson.put("retCode", Constant.REST_FAIL);
        assertEquals(resultJson, result);
    }

    @Test
    public void testCreateVnfByNotFound() {
        new MockUp<ResultRequestUtil>() {

            @Mock
            public JSONObject call(JSONObject vnfmObjcet, String path, String methodName, String paramsJson) {
                JSONObject resultJson = new JSONObject();
                resultJson.put("retCode", Constant.HTTP_NOTFOUND);
                return resultJson;
            }
        };
        String data = "{\"vnfmInfo\":{\"url\":\"url\"}}";
        JSONObject subJsonObject = JSONObject.fromObject(data);
        JSONObject vnfmObjcet = new JSONObject();
        VnfMgrVnfm vnfMgrVnfm = new VnfMgrVnfm();
        JSONObject result = vnfMgrVnfm.createVnf(subJsonObject, vnfmObjcet);

        JSONObject resultJson = new JSONObject();
        resultJson.put("retCode", Constant.REST_FAIL);
        assertEquals(resultJson, result);
    }

    @Test
    public void testCreateVnfByJSONException() {
        new MockUp<ResultRequestUtil>() {

            @Mock
            public JSONObject call(JSONObject vnfmObjcet, String path, String methodName, String paramsJson) {
                JSONObject resultJson = new JSONObject();
                return resultJson;
            }
        };
        String data = "{\"vnfmInfo\":{\"url\":\"url\"}}";
        JSONObject subJsonObject = JSONObject.fromObject(data);
        JSONObject vnfmObjcet = new JSONObject();
        VnfMgrVnfm vnfMgrVnfm = new VnfMgrVnfm();
        JSONObject result = vnfMgrVnfm.createVnf(subJsonObject, vnfmObjcet);

        JSONObject resultJson = new JSONObject();
        resultJson.put("retCode", Constant.REST_FAIL);
        assertEquals(resultJson, result);
    }

    @Test
    public void testRemoveVnf() {
        new MockUp<ResultRequestUtil>() {

            @Mock
            public JSONObject call(JSONObject vnfmObjcet, String path, String methodName, String paramsJson) {
                JSONObject resultJson = new JSONObject();
                resultJson.put("retCode", Constant.HTTP_NOCONTENT);
                return resultJson;
            }
        };
        VnfMgrVnfm vnfMgrVnfm = new VnfMgrVnfm();
        JSONObject vnfmObject = new JSONObject();
        JSONObject vnfObject = new JSONObject();
        vnfmObject.put("url", "url");
        JSONObject result = vnfMgrVnfm.removeVnf(vnfmObject, "vnfId", vnfObject);

        JSONObject resultJson = new JSONObject();
        resultJson.put("retCode", Constant.REST_SUCCESS);
        JSONObject retJson = new JSONObject();
        retJson.put("jobId", "vnfId" + "_" + Constant.DELETE);
        resultJson.put("data", retJson);
        assertEquals(resultJson, result);
    }

    @Test
    public void testRemoveVnfByCsmError() {
        new MockUp<ResultRequestUtil>() {

            @Mock
            public JSONObject call(JSONObject vnfmObjcet, String path, String methodName, String paramsJson) {
                JSONObject resultJson = new JSONObject();
                resultJson.put("retCode", Constant.HTTP_INNERERROR);
                return resultJson;
            }
        };
        VnfMgrVnfm vnfMgrVnfm = new VnfMgrVnfm();
        JSONObject vnfmObject = new JSONObject();
        vnfmObject.put("url", "url");
        JSONObject vnfObject = new JSONObject();
        JSONObject result = vnfMgrVnfm.removeVnf(vnfmObject, "vnfId", vnfObject);

        JSONObject resultJson = new JSONObject();
        resultJson.put("retCode", Constant.REST_FAIL);
        assertEquals(resultJson, result);
    }
    @Test
    public void getJobTestNormal(){
        new MockUp<ResultRequestUtil>(){
            @Mock
            public JSONObject call(JSONObject vnfmObject, String path, String methodName, String paramsJson) {
                JSONObject obj = new JSONObject();
                JSONObject dataobj = new JSONObject();
                dataobj.put("id", "2839");
                obj.put("retCode", 200);
                JSONArray basics = new JSONArray();
                basics.add("test123");
                JSONObject basicsData = new JSONObject();
                basicsData.put("basic", basics);
                obj.put("data", basicsData);
                return obj;
            }

        };
        VnfMgrVnfm vnfMgrVnfm = new VnfMgrVnfm();
        JSONObject vnfmObject = new JSONObject();

        String jobId = "1234";
        JSONObject resp = vnfMgrVnfm.getJob(vnfmObject, jobId);
        assertEquals(resp.get("retCode"),1);
    }

    @Test
    public void getJobTestCreatedNormal(){
        new MockUp<ResultRequestUtil>(){
            @Mock
            public JSONObject call(JSONObject vnfmObject, String path, String methodName, String paramsJson) {
                JSONObject obj = new JSONObject();
                JSONObject dataobj = new JSONObject();
                dataobj.put("id", "2839");
                obj.put("retCode", 201);
                JSONArray basics = new JSONArray();
                basics.add("test123");
                JSONObject basicsData = new JSONObject();
                basicsData.put("basic", basics);
                obj.put("data", basicsData);
                return obj;
            }

        };
        VnfMgrVnfm vnfMgrVnfm = new VnfMgrVnfm();
        JSONObject vnfmObject = new JSONObject();

        String jobId = "1234";
        JSONObject resp = vnfMgrVnfm.getJob(vnfmObject, jobId);
        assertEquals(resp.get("retCode"),1);
    }

    @Test
    public void getJobTestNullData(){
        new MockUp<ResultRequestUtil>(){
            @Mock
            public JSONObject call(JSONObject vnfmObject, String path, String methodName, String paramsJson) {
                JSONObject obj = new JSONObject();
                JSONObject dataobj = new JSONObject();
                dataobj.put("id", "2839");
                obj.put("retCode", 201);
                JSONArray basics = new JSONArray();
                basics.add("test123");
                JSONObject basicsData = new JSONObject();
                basicsData.put("basic", basics);
                obj.put("data", null);
                return obj;
            }

        };
        VnfMgrVnfm vnfMgrVnfm = new VnfMgrVnfm();
        JSONObject vnfmObject = new JSONObject();

        String jobId = "1234";
        JSONObject resp = vnfMgrVnfm.getJob(vnfmObject, jobId);
        assertEquals(resp.get("retCode"),-1);
    }

    @Test
    public void getJobTestInternalError(){
        new MockUp<ResultRequestUtil>(){
            @Mock
            public JSONObject call(JSONObject vnfmObject, String path, String methodName, String paramsJson) {
                JSONObject obj = new JSONObject();
                JSONObject dataobj = new JSONObject();
                dataobj.put("id", "2839");
                obj.put("retCode", 500);
                JSONArray basics = new JSONArray();
                basics.add("test123");
                JSONObject basicsData = new JSONObject();
                basicsData.put("basic", basics);
                obj.put("data", null);
                return obj;
            }

        };
        VnfMgrVnfm vnfMgrVnfm = new VnfMgrVnfm();
        JSONObject vnfmObject = new JSONObject();

        String jobId = "1234";
        JSONObject resp = vnfMgrVnfm.getJob(vnfmObject, jobId);
        assertEquals(resp.get("retCode"),-1);
    }


    @Test
    public void testScaleVnf() {
        new MockUp<ResultRequestUtil>() {

            @Mock
            public JSONObject call(JSONObject vnfmObjcet, String path, String methodName, String paramsJson) {
                JSONObject resultJson = new JSONObject();
                resultJson.put("retCode", Constant.HTTP_CREATED);
                JSONObject appInfo = new JSONObject();
                appInfo.put("vnfinstanceid", "id");
                appInfo.put("project_id", "project_id");
                JSONObject data = new JSONObject();
                data.put("app_info", appInfo);
                resultJson.put("data", data);
                return resultJson;
            }
        };
        String data = "{\"vnfmInfo\":{\"url\":\"url\"}}";
        JSONObject subJsonObject = JSONObject.fromObject(data);
        JSONObject vnfmObjcet = new JSONObject();
        VnfMgrVnfm vnfMgrVnfm = new VnfMgrVnfm();
        JSONObject result = vnfMgrVnfm.scaleVnf(subJsonObject,vnfmObjcet,"test123","test123");

        assertEquals(Constant.REST_SUCCESS, result.get("retCode"));
    }
}
