package org.example.potm.framework.config.permission.menu;

import lombok.Data;

import java.util.List;

@Data
public class RouteItem {
    private String path;
    private String name;
    private RouteMeta meta;
}
