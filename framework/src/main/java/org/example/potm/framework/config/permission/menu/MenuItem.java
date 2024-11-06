package org.example.potm.framework.config.permission.menu;

import lombok.Data;

import java.util.List;

@Data
public class MenuItem {
    private String parentId;
    private String id;
    private String name;
    private String code;
    private List<String> permissions;
    private Boolean hidden;
}
