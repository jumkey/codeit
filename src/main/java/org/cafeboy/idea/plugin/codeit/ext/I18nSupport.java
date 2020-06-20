package org.cafeboy.idea.plugin.codeit.ext;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class I18nSupport {
    @NonNls
    private static final ResourceBundle bundle = ResourceBundle.getBundle(Constant.APP_ID);

    public static String i18n_str(@PropertyKey(resourceBundle = Constant.APP_ID) String key, Object... params) {
        String value = bundle.getString(key);

        if (params.length > 0) return MessageFormat.format(value, params);

        return value;
    }
}
