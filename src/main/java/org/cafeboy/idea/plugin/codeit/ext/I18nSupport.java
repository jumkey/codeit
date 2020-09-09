package org.cafeboy.idea.plugin.codeit.ext;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class I18nSupport {
    @NonNls private static final String BUNDLE = "messages.Codeit";
    @NonNls
    private static final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE);

    public static String i18n_str(@PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
        String value = bundle.getString(key);

        if (params.length > 0) return MessageFormat.format(value, params);

        return value;
    }
}
