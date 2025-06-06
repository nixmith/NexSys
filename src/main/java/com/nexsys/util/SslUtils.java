// src/main/java/com/nexsys/util/SslUtils.java
package com.nexsys.util;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.X509Certificate;

/**
 * SSL/TLS utilities.
 */
public final class SslUtils {

    public enum SslProfile {
        PYTHON_DEFAULT,
        INTERMEDIATE,
        MODERN,
        INSECURE
    }

    private SslUtils() {}

    /**
     * Create SSL context with specified profile and verification.
     */
    public static SSLContext clientContext(SslProfile profile, boolean verify) {
        try {
            SSLContext context = SSLContext.getInstance("TLS");

            if (!verify) {
                TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() { return null; }
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                        }
                };
                context.init(null, trustAllCerts, new SecureRandom());
            } else {
                context.init(null, null, null);
            }

            // TODO build.gradle: org.bouncycastle:bcprov-jdk18on for cipher suite configuration

            return context;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSL context", e);
        }
    }
}