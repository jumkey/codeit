package org.cafeboy.idea.plugin.codeit.ui;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import org.cafeboy.idea.plugin.codeit.core.Api;
import org.cafeboy.idea.plugin.codeit.ext.Constant;
import org.cafeboy.idea.plugin.codeit.ext.Utils;

import javax.swing.*;

/**
 * @author jumkey
 */
public class ContentWidget {

    private final Project mProject;
    private final CodeitView codeitView;
    public JPanel mContent;
    private JBTextArea textArea;
    private JLabel mCodeLabel;
    private JLabel labelInfo;
    private JRadioButton radioDefault;
    private JRadioButton radioColorful;
    private JRadioButton radioRound;
    private int currentMode = 0;

    public ContentWidget(Project mProject, CodeitView codeitView) {
        this.mProject = mProject;
        this.codeitView = codeitView;

        setupTextArea();
        setupImage();
        setupImageTools();
    }

    private void setupImageTools() {
        radioDefault.addActionListener(e -> changeMode(0));
        radioColorful.addActionListener(e -> changeMode(1));
        radioRound.addActionListener(e -> changeMode(2));
    }

    /**
     * 更换模型
     */
    private void changeMode(int mode) {
        if (mode != currentMode) {
            currentMode = mode;
            // TODO 使用当前输入框内容还是原先生成的内容
            codeitView.gen(mProject);
        }
    }

    private void setupImage() {
        ActionGroup action = (ActionGroup) ActionManager.getInstance().getAction("my.pop.qr");
        ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu(Constant.APP_ID, action);
        actionPopupMenu.setTargetComponent(this.codeitView);
        JPopupMenu jPopupMenu = actionPopupMenu.getComponent();
        Utils.setRightMenu(mCodeLabel, jPopupMenu);
    }

    private void setupTextArea() {
        textArea.getEmptyText().setText("...");
        textArea.setMargin(JBUI.insets(2));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        Utils.setRightMenu(textArea, createPopupMenu());
    }

    private JPopupMenu createPopupMenu() {
        ActionGroup action = (ActionGroup) ActionManager.getInstance().getAction("my.pop.edit");
        // 覆盖默认快捷键 TODO 是否有更好的方式实现快捷键control V 粘贴图片
        ActionManager.getInstance().getAction("paste").registerCustomShortcutSet(textArea, null);
        ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu(Constant.APP_ID, action);
        return actionPopupMenu.getComponent();
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public void setCode(Icon icon) {
        mCodeLabel.setIcon(icon);
    }

    public Icon getCode() {
        return mCodeLabel.getIcon();
    }

    public void setInfo(String text) {
        labelInfo.setText(text);
    }

    public String getMode() {
        String result = "";
        switch (currentMode) {
            case 0:
                result = Api.MODE_0;
                break;
            case 1:
                result = Api.MODE_1;
                break;
            case 2:
                result = Api.MODE_2;
                break;
            default:
                break;
        }
        return result;
    }

    private boolean isCli = false;

    public boolean isCli() {
        return isCli;
    }

    public void toggleGroupData(boolean state) {
        isCli = state;
    }
}
