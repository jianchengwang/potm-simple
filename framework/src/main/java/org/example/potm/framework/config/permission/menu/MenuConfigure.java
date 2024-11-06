package org.example.potm.framework.config.permission.menu;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuConfigure {

    @Value(value="classpath:permission/menu.json")
    private org.springframework.core.io.Resource menuResource;

    @Value(value="classpath:permission/adminAsyncRoutes.json")
    private org.springframework.core.io.Resource adminAsyncRoutesResource;

    @Bean
    public MenuOperator menuOperator() {
        return new MenuOperator(menuResource, adminAsyncRoutesResource);
    }

}
