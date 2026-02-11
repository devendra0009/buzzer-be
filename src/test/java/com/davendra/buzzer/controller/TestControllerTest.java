package com.davendra.buzzer.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TestControllerTest {

    @InjectMocks
    private TestController testController;

    @Test
    public void test() {
        log.info("tst");
        testController.test();
    }
}
