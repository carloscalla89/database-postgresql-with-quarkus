package org.acme.cross.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "panache-starter")
public interface PanacheStarterConfig {

    @WithDefault("true")
    boolean enabled();

}
