package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.cafeboy.idea.plugin.codeit.core.QRCodeUtils;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.cafeboy.idea.plugin.codeit.ext.Utils;
import org.jetbrains.annotations.NotNull;

/**
 * 读取屏幕二维码
 *
 * @author jumkey
 */
public class ReadQRCodeAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Utils.message(I18nSupport.i18n_str("action.read.qrcode.msg.title"), QRCodeUtils.captureScreenAndRead().toString());
    }
}