package org.example.potm.framework.config.permission.menu;

import lombok.Data;

import java.util.List;

@Data
public class RouteJson {
    private String path;
    private String name;
    private RouteMeta meta;
    private List<RouteJson> children;
}
