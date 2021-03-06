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

package org.onap.vfc.nfvo.vnfm.svnfm.vnfmadapter.common;

/**
 * VNFM exception.
 * .</br>
 *
 * @author
 * @version     VFC 1.0  Sep 10, 2016
 */
public class VnfmException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     *
     * Constructor<br>
     *
     * @since  VFC 1.0
     */
    public VnfmException() {
        super();
    }

    /**
     *
     * Constructor<br>
     *
     * @param message
     * @param cause
     * @since  VFC 1.0
     */
    public VnfmException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * Constructor<br>
     *
     * @param message
     * @since  VFC 1.0
     */
    public VnfmException(String message) {
        super(message);
    }

    /**
     *
     * Constructor<br>
     *
     * @param cause
     * @since  VFC 1.0
     */
    public VnfmException(Throwable cause) {
        super(cause);
    }

}
