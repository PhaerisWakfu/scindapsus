package com.scindapsus.pulsar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wyh
 * @since 2022/11/1
 */
@RestController
@RequestMapping("/pulsar")
public class PulsarController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PulsarController.class);

    @Autowired
    private PulsarSender pulsarSender;

    @GetMapping
    public String send() {
        LOGGER.info("send");
        pulsarSender.send("hello");
        return "success";
    }
}
