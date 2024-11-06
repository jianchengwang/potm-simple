package org.example.potm.framework.config.permission.menu;

import lombok.Data;

import java.util.List;

@Data
public class MenuJson {
    private String parentId;
    private String id;
    private String name;
    private String code;
    private List<String> permissions;
    private List<MenuJson> children;
    private Boolean hidden;
}
