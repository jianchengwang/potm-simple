package org.example.potm.framework.utils;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class ChineseComparator implements Comparator<String> {
    private static final Collator collator = Collator.getInstance(Locale.CHINA);

    @Override
    public int compare(String o1, String o2) {
        return collator.compare(o1, o2);
    }
}
