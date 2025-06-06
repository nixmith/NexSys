// src/test/java/com/nexsys/util/SslUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import javax.net.ssl.SSLContext;
import static org.assertj.core.api.Assertions.*;

class SslUtilsTest {

    @Test
    void testClientContextWithVerification() {
        SSLContext context = SslUtils.clientContext(
                SslUtils.SslProfile.PYTHON_DEFAULT, true);

        assertThat(context).isNotNull();
        assertThat(context.getProtocol()).isEqualTo("TLS");
    }

    @Test
    void testClientContextWithoutVerification() {
        SSLContext context = SslUtils.clientContext(
                SslUtils.SslProfile.MODERN, false);

        assertThat(context).isNotNull();
    }
}