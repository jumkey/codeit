package org.cafeboy.idea.plugin.codeit.core;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class ImageSelection implements Transferable {
    private final Image image;

    // 构造器，负责持有一个Image对象
    public ImageSelection(Image image) {
        this.image = image;
    }

    // 返回该Transferable对象所支持的所有DataFlavor
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }

    // 取出该Transferable对象里实际的数据
    @NotNull
    public Image getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor.equals(DataFlavor.imageFlavor)) {
            return image;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    // 返回该Transferable对象是否支持指定的DataFlavor
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(DataFlavor.imageFlavor);
    }
}
