package org.example.potm.svc.config.flyway;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("app.flyway")
@Data
public class FlywayProperties {
    private boolean enable = false;
    private List<String> dataSourceNames;
}
