package org.cafeboy.idea.plugin.codeit.core;

import javax.swing.*;

/**
 * @author jumkey
 */
public interface Api {
    String MODE_0 = "0";
    String MODE_1 = "1";
    String MODE_2 = "2";

    /**
     * 获取二维码
     *
     * @param text text
     * @param mode mode
     * @return Icon
     */
    Icon getCode(String text, String mode);
}
