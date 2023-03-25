package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.cafeboy.idea.plugin.codeit.core.QRCodeUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class PasteAction extends AnAction {
    @Override
    public void update(@NotNull final AnActionEvent e) {
        final Project project = e.getProject();
        final Component textArea = e.getData(PlatformDataKeys.CONTEXT_COMPONENT);

        e.getPresentation().setEnabledAndVisible(project != null && textArea instanceof JTextArea);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        JTextArea textArea = (JTextArea) e.getRequiredData(PlatformDataKeys.CONTEXT_COMPONENT);
        final Transferable transferable = CopyPasteManager.getInstance().getContents();
        if (transferable == null) {
            // NOOP
        } else if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String str = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                if (!TextUtils.isEmpty(str)) {
                    textArea.replaceRange(str, textArea.getSelectionStart(), textArea.getSelectionEnd());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                Image image = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
                // 解析图片二维码
                textArea.setText(StringUtils.join(QRCodeUtils.readQRCodeWithoutSplash(image), "\r\n"));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
