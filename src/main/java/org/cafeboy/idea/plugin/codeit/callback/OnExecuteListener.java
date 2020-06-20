package org.cafeboy.idea.plugin.codeit.callback;

import javax.swing.*;

/**
 * @author jumkey
 */
public interface OnExecuteListener {

    /**
     * 执行开始时回调
     */
    void onStart();

    /**
     * 获取成功时回调
     *
     * @param icon icon
     */
    void onSuccess(Icon icon);

    /**
     * 发生错误回调
     *
     * @param msg 消息
     */
    void onError(String msg);

    /**
     * 执行完成时回调
     */
    void onComplete();
}
