package org.cafeboy.idea.plugin.codeit.callback;

/**
 * @author jumkey
 */
public interface OnExecuteListener<T> {

    /**
     * 执行开始时回调
     */
    void onStart();

    /**
     * 获取成功时回调
     *
     * @param result 结果
     */
    void onSuccess(T result);

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
