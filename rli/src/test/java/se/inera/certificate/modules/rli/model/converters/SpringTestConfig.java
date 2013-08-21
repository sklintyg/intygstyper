package se.inera.certificate.modules.rli.model.converters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringTestConfig {

    public SpringTestConfig() {

    }

    @Bean(name = { "externalToInternalConverter" })
    public ExternalToInternalConverterImpl getExternalToInternalConverterImpl() {
        return new ExternalToInternalConverterImpl();
    }

    @Bean
    public UndersokningPopulator getUndersokningPopulator() {
        return new UndersokingPopulatorImpl();
    }

    @Bean
    public RekommendationPopulator getRekommendationPopulator() {
        return new RekommendationPopulatorImpl();
    }

}
