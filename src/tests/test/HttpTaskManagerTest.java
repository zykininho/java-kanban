package test;

import http.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {

    @BeforeEach
    void setUp() throws IOException {
        new KVServer().start();
    }

    @AfterEach
    void tearDown() throws IOException {
        new KVServer().stop();
    }

    @Test
    void save() {

    }
}
