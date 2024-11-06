package org.example.potm.plugins.storage;

import org.example.potm.framework.pojo.IBaseEnum;
import org.springframework.util.StringUtils;

import java.util.Calendar;

public interface KeyGenerator {

    default String getPrefixTmpl(String prefix, String tmpl) {
        if(StringUtils.hasText(prefix)) {
            return "%3$s/" + tmpl;
        }
        return tmpl;
    }

    String getKey(String name, KeyProvider keyNameProvider, String module, String prefix, Boolean useFileNameAsKeyName);

    enum Predefined implements KeyGenerator, IBaseEnum<String> {

        /**
         * All item stored in the bucket (root folder) without hierarchy
         */
        PLAIN {
            public String getValue() {
                return "PLAIN";
            }

            @Override
            public String getDescription() {
                return "PLAIN";
            }

            @Override
            protected String tmpl(String prefix) {
                return null;
            }
        },
        /**
         * Items stored in a hierarchy structured by date: prefix/yyyy/MM/dd/item
         */
        BY_DATE {

            public String getValue() {
                return "BY_DATE";
            }

            @Override
            public String getDescription() {
                return "BY_DATE";
            }

            @Override
            protected String tmpl(String prefix) {
                return getPrefixTmpl(prefix, "%1$tY/%1$tm/%1$td/%2$s");
            }
        },
        /**
         * Items stored in a hierarchy structured by date and time: prefix/yyyy/MM/dd/HH/item
         */
        BY_HOUR {

            public String getValue() {
                return "BY_HOUR";
            }

            @Override
            public String getDescription() {
                return "BY_HOUR";
            }

            @Override
            protected String tmpl(String prefix) {
                return getPrefixTmpl(prefix,"%1$tY/%1$tm/%1$td/%1$tH/%2$s");
            }
        },
        /**
         * Items stored in a hierarchy structured by date and time: prefix/yyyy/MM/dd/HH/mm/item
         */
        BY_MINUTE {

            public String getValue() {
                return "BY_MINUTE";
            }

            @Override
            public String getDescription() {
                return "BY_MINUTE";
            }

            @Override
            protected String tmpl(String prefix) {
                return getPrefixTmpl(prefix,"%1$tY/%1$tm/%1$td/%1$tH/%1$tM/%2$s");
            }
        },
        /**
         * Items stored in a hierarchy structured by date and time: prefix/yyyy/MM/dd/HH/mm/ss/item
         */
        BY_SECOND {

            public String getValue() {
                return "BY_SECOND";
            }

            @Override
            public String getDescription() {
                return "BY_SECOND";
            }

            @Override
            protected String tmpl(String prefix) {
                return getPrefixTmpl(prefix,"%1$tY/%1$tm/%1$td/%1$tH/%1$tM/%1$tS/%2$s");
            }
        },
        /**
         * Items stored in a hierarchy structured by date and time: prefix/yyyy/MM/dd/HH/mm/ss/item
         * <p>
         * Note this enum value is deprecated, please use `BY_SECOND` instead
         */
        BY_DATETIME {

            public String getValue() {
                return "BY_DATETIME";
            }

            @Override
            public String getDescription() {
                return "BY_DATETIME";
            }

            @Override
            protected String tmpl(String prefix) {
                return getPrefixTmpl(prefix,"%1$tY/%1$tm/%1$td/%1$tH/%1$tM/%1$tS/%2$s");
            }
        };

        protected abstract String tmpl(String prefix);

        @Override
        public String getKey(String name, KeyProvider keyProvider, String moduleDir, String prefix, Boolean useFileNameAsKeyName) {
            String prefix_ = "";

            if (keyProvider != null) {
                prefix_ = keyProvider.newPrefixName(moduleDir, prefix);

                // useFileNameAsKeyName is false，provider a new key name
                if(!useFileNameAsKeyName) {
                    name = keyProvider.newKeyName(name);
                }
            }
            String tmpl = tmpl(prefix_);
            if (!StringUtils.hasText(tmpl)) {
                if(StringUtils.hasText(prefix_)) {
                    return prefix_ + "/" + name;
                }
                return name;
            } else {
                return String.format(tmpl, Calendar.getInstance(), name, prefix_);
            }
        }
    }
}