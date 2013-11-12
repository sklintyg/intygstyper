package se.inera.certificate.modules.rli.model.converters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringTestConfig {

    public SpringTestConfig() {

    }

    @Bean(name = { "externalToInternalConverter" })
    public ExternalToInternalConverter getExternalToInternalConverterImpl() {
        return new ExternalToInternalConverter();
    }

    @Bean
    public UndersokningPopulator getUndersokningPopulator() {
        return new UndersokningPopulator();
    }

    @Bean
    public RekommendationPopulator getRekommendationPopulator() {
        return new RekommendationPopulator();
    }

}
