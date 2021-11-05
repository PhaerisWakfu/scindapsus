package com.scindapsus.surl;

import com.scindapsus.surl.config.ShortUrlProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wyh
 * @date 2021/11/4 17:53
 */
@Controller
@AllArgsConstructor
@ConditionalOnProperty(prefix = ShortUrlProperties.PREFIX, name = "enabled", havingValue = "true")
public class RouteController {

    private final UrlMappingService urlMappingService;

    private final HttpServletRequest request;

    private final HttpServletResponse response;


    @PostMapping("${scindapsus.surl.path}")
    @ResponseBody
    public String convert(@RequestBody ConvertRequestDTO requestDTO) {
        return urlMappingService.mapping(requestDTO.getOriginalUrl(), request.getRequestURL().toString());
    }

    @GetMapping("${scindapsus.surl.path}/{key}")
    public void routing(@PathVariable String key) throws IOException {
        response.sendRedirect(urlMappingService.find(key));
    }

    @Data
    public static class ConvertRequestDTO {

        /**
         * 原始链接
         */
        private String originalUrl;
    }
}
