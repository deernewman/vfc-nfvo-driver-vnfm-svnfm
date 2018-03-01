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

package org.onap.vfc.nfvo.driver.vnfm.svnfm.nokia.util;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Create Truststore from the given certificates and keys
 */
public final class StoreLoader {

    public static final String PASSWORD = "password";
    private static final String RSA = "RSA";
    private static final String X_509 = "X.509";
    private static final String SUN = "SUN";
    private static final String JKS = "JKS";
    private static final String RSA_PRIVATE_KEY = "RSA PRIVATE KEY";
    private static final String CERTIFICATE = "CERTIFICATE";
    private static org.slf4j.Logger logger = getLogger(StoreLoader.class);

    private StoreLoader() {
    }

    private static String getScope(String content, String scope) {
        int rindex = content.indexOf(begin(scope));
        int lindex = content.indexOf(end(scope));
        if (rindex == -1 || lindex == -1) {
            return "";
        }
        String substring = content.substring(rindex, lindex) + end(scope);
        return substring;
    }

    /**
     * @param content the content of the PEM ( a PEM may contain multiple certificates)
     * @return the collection of certificates in the PEM
     */
    public static Set<String> getCertifacates(String content) {
        String lastCertificate = "";
        Set<String> certificates = new HashSet<>();
        do {
            lastCertificate = getScope(content, CERTIFICATE);
            content = content.replace(lastCertificate, "");
            if (!"".equals(lastCertificate)) {
                certificates.add(lastCertificate);
            }
        } while (!"".equals(lastCertificate));
        return certificates;
    }

    private static byte[] toDer(String pem, String scope) {
        return Base64.decodeBase64(pem
                .replace(begin(scope), "")
                .replace(end(scope), "")
                .replaceAll("\\s", ""));
    }

    private static String begin(String scope) {
        return "-----BEGIN " + scope + "-----";
    }

    private static String end(String scope) {
        return "-----END " + scope + "-----";
    }

    /**
     * Create new truststore from the given certificate
     *
     * @param pem           the certificate which used to create the store
     * @param storePassword the password to protect the store
     * @param keyPassword   the password to protect the key
     * @return the created key store
     */
    public static KeyStore loadStore(String pem, String storePassword, String keyPassword) {
        Optional<PrivateKey> privateKey = generatePrivateKey(pem);
        Certificate[] certs = createCertificates(pem);
        try {
            KeyStore ks = KeyStore.getInstance(JKS, SUN);
            ks.load(null, storePassword.toCharArray());
            if (privateKey.isPresent()) {
                ks.setKeyEntry(PASSWORD, privateKey.get(), keyPassword.toCharArray(), certs);
            } else if (certs != null) {
                int index = 0;
                for (Certificate cert : certs) {
                    TrustedCertificateEntry ts = new TrustedCertificateEntry(cert);
                    ks.setEntry(PASSWORD + index, ts, null);
                    index++;
                }
            }
            return ks;
        } catch (Exception e) {
            throw new RuntimeException("Unable to create keystore", e);
        }
    }

    private static Certificate[] createCertificates(String pem) {
        Set<Certificate> certificates = new HashSet<>();
        try {
            for (String certificate : getCertifacates(pem)) {
                CertificateFactory certFactory = CertificateFactory.getInstance(X_509);

                InputStream is = new ByteArrayInputStream(toDer(certificate, CERTIFICATE));
                Collection<? extends Certificate> c = certFactory.generateCertificates(is);
                certificates.addAll(c);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Unable to load certificates", e);
        }

        if (certificates.size() > 0) {
            return certificates.toArray(new Certificate[certificates.size()]);
        } else {
            return null;
        }
    }

    private static Optional<PrivateKey> generatePrivateKey(String pem) {
        try {
            String key = getScope(pem, RSA_PRIVATE_KEY);
            if (!key.isEmpty()) {
                KeyFactory keyFactory = KeyFactory.getInstance(RSA);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(toDer(key, RSA_PRIVATE_KEY));
                return Optional.of(keyFactory.generatePrivate(keySpec));
            }
            return Optional.empty();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Unable to load key", e);
            throw new RuntimeException("Unable to load key", e);
        }
    }

}