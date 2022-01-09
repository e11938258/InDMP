package at.tuwien.indmp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({ PersistenceConfiguration.class, ServiceConfiguration.class })
public class ApplicationConfiguration {
}
