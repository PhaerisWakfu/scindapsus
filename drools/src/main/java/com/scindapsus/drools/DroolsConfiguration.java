package com.scindapsus.drools;

import lombok.AllArgsConstructor;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wyh
 * @date 2021/11/15 15:39
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(DroolsProperties.class)
public class DroolsConfiguration {

    private final DroolsProperties properties;

    @Bean
    public KieContainer kieContainer() throws IOException {
        KieServices kieServices = KieServices.Factory.get();
        KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
        //从配置的路径中读取规则文件
        recoverKieFileSystem(kieServices, properties.getRulesPath());
        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }

    /**
     * 使用读文件路径的方式替换kmodule.xml
     *
     * @param kieServices 核心类
     * @param rulesPath   规则文件路径
     * @throws IOException 可能抛的错
     */
    private void recoverKieFileSystem(KieServices kieServices, String rulesPath) throws IOException {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        getRuleFilesName(rulesPath)
                .forEach(fileName ->
                        kieFileSystem.write(ResourceFactory.newClassPathResource(rulesPath + fileName, "UTF-8"))
                );
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
    }

    /**
     * 读取目录获取规则文件名
     *
     * @param rulesPath 规则文件路径
     * @return 规则文件名
     * @throws IOException 可能抛的错
     */
    private List<String> getRuleFilesName(String rulesPath) throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + rulesPath + "/*.*");
        return Arrays.stream(resources).map(Resource::getFilename).collect(Collectors.toList());
    }
}