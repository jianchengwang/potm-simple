package org.example.potm.framework.config.dict;

/**
 * @author jianchengwang
 * @date 2023/4/11
 */
public class DictSqlTemplate {

        public static final String DELETE_SYSTEM_DICT_SQL = """
            delete from sys_dict_item where system_flag = 1;
            delete from sys_dict where system_flag = 1;
        """;

        public static final String REPLACE_INSERT_DICT_SQL = """
            replace into sys_dict(`svc_name`, `dict_key`, `description`, `remark`, `system_flag`, `enum_flag`) values (?,?,?,?,?,?);
        """;

        public static final String REPLACE_INSERT_DICT_ITEM_SQL = """
            replace into sys_dict_item(`svc_name`, `dict_key`, `value`, `label`, `type`, `sort_order`, `system_flag`, `remark`, `color`, `parent_item_value`) values (?,?,?,?,?,?,?,?,?,?);
         """;

}
