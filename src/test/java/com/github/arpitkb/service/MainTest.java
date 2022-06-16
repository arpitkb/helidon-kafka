
package com.github.arpitkb.service;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
class MainTest {

    @Inject
    private WebTarget target;

    @Test
    void testHelloWorld() {

    }
}
