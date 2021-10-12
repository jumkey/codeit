package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import org.apache.commons.lang3.StringUtils;
import org.cafeboy.idea.plugin.codeit.core.QRCodeUtils;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.cafeboy.idea.plugin.codeit.ext.Utils;
import org.cafeboy.idea.plugin.codeit.ui.CodeitView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 读取屏幕二维码
 *
 * @author jumkey
 */
public class ReadQRCodeAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final CodeitView codeitView = (CodeitView) e.getRequiredData(PlatformDataKeys.CONTEXT_COMPONENT);
        // remove icon
        final Icon icon = codeitView.getContentWidget().getCode();
        codeitView.getContentWidget().setCode(null);

        EventQueue.invokeLater(() -> {
            final List<String> list = QRCodeUtils.captureScreenAndRead();
            if (list.isEmpty()) {
                Utils.warning(I18nSupport.i18n_str("action.read.qrcode.msg.title"), I18nSupport.i18n_str("action.read.qrcode.msg.notfound"));
            } else {
                final String join = StringUtils.join(list, System.lineSeparator());
                list.forEach(text -> codeitView.getHistoryWidget().insert(text));
                Utils.message(I18nSupport.i18n_str("action.read.qrcode.msg.title"), join);
            }
            // restore icon
            codeitView.getContentWidget().setCode(icon);
        });
    }
}
