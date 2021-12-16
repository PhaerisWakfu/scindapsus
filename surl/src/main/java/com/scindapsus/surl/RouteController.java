package com.scindapsus.surl;

import com.scindapsus.surl.config.ShortUrlProperties;
import com.scindapsus.surl.exception.UnknownMappingException;
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
 * @since 1.0
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

    @ExceptionHandler(UnknownMappingException.class)
    @ResponseBody
    public String handle(UnknownMappingException e) {
        return String.format("The mapping of [%s] was not found.", e.getMessage());
    }

    @Data
    public static class ConvertRequestDTO {

        /**
         * 原始链接
         */
        private String originalUrl;
    }
}
