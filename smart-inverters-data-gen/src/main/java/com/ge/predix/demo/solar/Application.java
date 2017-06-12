package com.ge.predix.demo.solar;

import com.ge.predix.uaa.token.lib.FastTokenServices;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableResourceServer
@ImportResource(
        {
                "classpath*:META-INF/spring/predix-rest-client-scan-context.xml",
                "classpath*:META-INF/spring/ext-util-scan-context.xml",
                "classpath*:META-INF/spring/predix-rest-client-scan-context.xml",
                "classpath*:META-INF/spring/predix-websocket-client-scan-context.xml",
                "classpath*:META-INF/spring/timeseries-bootstrap-scan-context.xml"
        })
@ComponentScan(basePackages={"com.ge.predix.solsvc","com.ge.predix.demo.solar"})
public class Application {

    @Value("${predix.oauth.issuerId.url}")
    private String predixIssuer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Qualifier("fastTokenServices")
    @Primary
    ResourceServerTokenServices resourceServerTokenServices() {
        FastTokenServices services = new FastTokenServices();
        List<String> trustedIssuers = new ArrayList<>();
        trustedIssuers.add(predixIssuer);
        services.setTrustedIssuers(trustedIssuers);
        services.setStoreClaims(true);
        return services;
    }

}
