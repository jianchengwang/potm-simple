package org.example.potm.framework.config.permission.menu;

import lombok.Data;

@Data
public class RouteMeta {
    private String icon;
    private String title;
    private Boolean keepAlive;
    private String frameSrc;
    private String menuCode;
    private Integer rank;
    private String menuId;
    private Boolean showLink;
    private String activePath;
}
