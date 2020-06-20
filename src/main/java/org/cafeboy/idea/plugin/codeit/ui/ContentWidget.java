package org.cafeboy.idea.plugin.codeit.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.util.ui.JBUI;
import org.cafeboy.idea.plugin.codeit.callback.OnExecuteListener;
import org.cafeboy.idea.plugin.codeit.core.Api;
import org.cafeboy.idea.plugin.codeit.core.tasks.CodeGenerateTask;
import org.cafeboy.idea.plugin.codeit.core.tasks.SavingImageTask;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.cafeboy.idea.plugin.codeit.ext.Utils;
import org.apache.http.util.TextUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;

/**
 * @author jumkey
 */
public class ContentWidget {

    private final Project mProject;
    public JPanel mContent;
    private JTextArea textArea;
    private JLabel mCodeLabel;
    private JLabel labelInfo;
    private JRadioButton radioDefault;
    private JRadioButton radioColorful;
    private JRadioButton radioRound;
    private int currentMode = 0;

    public ContentWidget(Project mProject) {
        this.mProject = mProject;

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
            if (TextUtils.isEmpty(getText())) {
                return;
            }
            currentMode = mode;
            // FIXME text 校验
            new CodeGenerateTask(mProject, getText(), getMode(), isCli(), new OnExecuteListener() {
                @Override
                public void onStart() {
                    setInfo(I18nSupport.i18n_str("info.starting"));
                }

                @Override
                public void onSuccess(Icon icon) {
                    setCode(icon);
                    setInfo(I18nSupport.i18n_str("info.success"));
                }

                @Override
                public void onError(String msg) {
                    setInfo(I18nSupport.i18n_str("info.error"));
                }

                @Override
                public void onComplete() {
                }
            }).start();
        }
    }

    private void setupImage() {
        mCodeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
                    JPopupMenu jPopupMenu = new JPopupMenu();
                    JMenuItem saveLine = new JMenuItem(I18nSupport.i18n_str("menu.save.text"), AllIcons.Actions.Menu_saveall);
                    saveLine.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK, false));
                    saveLine.addActionListener(e1 -> saveCode());
                    jPopupMenu.add(saveLine);
                    if (mCodeLabel.getIcon() == null) {
                        saveLine.setEnabled(false);
                    }
                    jPopupMenu.show(mCodeLabel, e.getX(), e.getY());
                }
            }
        });
    }

    private void saveCode() {
        final Icon icon = getCode();
        if (mProject == null || icon == null) {
            return;
        }
        FileSaverDialog dialog = FileChooserFactory.getInstance()
                .createSaveFileDialog(new FileSaverDescriptor(I18nSupport.i18n_str("dialog.save.title"),
                        I18nSupport.i18n_str("dialog.save.description"), "png"), mProject);
        VirtualFileWrapper wrapper = dialog.save(ProjectUtil.guessProjectDir(mProject), Utils.getSuggestFileName());
        if (wrapper == null) {
            return;
        }
        new SavingImageTask(mProject, icon, wrapper).start();
    }

    private void setupTextArea() {
        textArea.setMargin(JBUI.insets(2));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        Utils.setRightMenu(textArea, createPopupMenu());
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        // copy
        JMenuItem copyLine = new JMenuItem(I18nSupport.i18n_str("menu.copy.text"), AllIcons.Actions.Copy);
        copyLine.setMnemonic('C');
        copyLine.setAccelerator(KeyStroke.getKeyStroke('C', KeyEvent.CTRL_MASK, false));
        // paste
        JMenuItem pasteLine = new JMenuItem(I18nSupport.i18n_str("menu.paste.text"), AllIcons.Actions.Menu_paste);
        pasteLine.setMnemonic('P');
        pasteLine.setAccelerator(KeyStroke.getKeyStroke('V', KeyEvent.CTRL_MASK, false));
        // delete
        JMenuItem deleteLine = new JMenuItem(I18nSupport.i18n_str("menu.delete.text"), AllIcons.Actions.Cancel);
        deleteLine.setMnemonic('D');
        deleteLine.setAccelerator(KeyStroke.getKeyStroke('D', KeyEvent.CTRL_MASK, false));
        // listener
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        copyLine.addActionListener(e -> {
            String str = textArea.getSelectedText();
            if (!TextUtils.isEmpty(str)) {
                clipboard.setContents(new StringSelection(str), null);
            } else {
                if (!TextUtils.isEmpty(getText())) {
                    clipboard.setContents(new StringSelection(getText()), null);
                }
            }
        });
        pasteLine.addActionListener(e -> {
            final Transferable transferable = clipboard.getContents(this);
            final DataFlavor dataFlavor = DataFlavor.stringFlavor;
            if (transferable.isDataFlavorSupported(dataFlavor)) {
                try {
                    String str = (String) transferable.getTransferData(dataFlavor);
                    if (!TextUtils.isEmpty(str)) {
                        textArea.insert(str, textArea.getCaretPosition());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        deleteLine.addActionListener(e -> {
            String str = textArea.getSelectedText();
            if (!TextUtils.isEmpty(str)) {
                textArea.replaceSelection("");
            } else {
                setText("");
            }
        });
        // add
        jPopupMenu.add(copyLine);
        jPopupMenu.add(pasteLine);
        jPopupMenu.addSeparator();
        jPopupMenu.add(deleteLine);

        return jPopupMenu;
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
