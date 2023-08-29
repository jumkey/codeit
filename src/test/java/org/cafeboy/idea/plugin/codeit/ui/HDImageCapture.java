package org.cafeboy.idea.plugin.codeit.ui;

import sun.awt.ComponentFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.awt.peer.RobotPeer;
import java.io.File;
import java.io.IOException;

public class HDImageCapture {
    private static GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    public static void main(String[] args) throws AWTException, IOException {

        GraphicsDevice[] screenDevices = localGraphicsEnvironment.getScreenDevices();
        Rectangle allBounds = new Rectangle();
        for (int i = 0; i < screenDevices.length; i++) {
            GraphicsDevice screenDevice = screenDevices[i];
            GraphicsConfiguration gc = screenDevice.getDefaultConfiguration();
            int width = screenDevice.getDisplayMode().getWidth();
            int height = screenDevice.getDisplayMode().getHeight();
            // 获取 GraphicsConfiguration 的 Bounds，它包含更高分辨率信息
            Rectangle screenBounds = gc.getBounds();
            Rectangle rectangle = new Rectangle(screenBounds.x, screenBounds.y, width, height);
            allBounds.add(rectangle);
        }
        // allBounds.height+=2;
        screenshot1( allBounds);
        screenshot2( allBounds);
    }


    public static void screenshot1(Rectangle allBounds) throws AWTException, IOException {
        RobotPeer robotPeer = ((ComponentFactory) Toolkit.getDefaultToolkit()).createRobot(localGraphicsEnvironment.getDefaultScreenDevice());

        int[] pixels = robotPeer.getRGBPixels(allBounds);
        DirectColorModel screenCapCM = new DirectColorModel(24,
                /* red mask */ 0x00FF0000,
                /* green mask */ 0x0000FF00,
                /* blue mask */ 0x000000FF);
        DataBufferInt buffer = new DataBufferInt(pixels, pixels.length);
        int[] bandmasks = new int[3]    ;
        bandmasks[0] = screenCapCM.getRedMask();
        bandmasks[1] = screenCapCM.getGreenMask();
        bandmasks[2] = screenCapCM.getBlueMask();

        WritableRaster raster = Raster.createPackedRaster(buffer, allBounds.width,
                allBounds.height, allBounds.width, bandmasks, null);
        BufferedImage highResolutionImage = new BufferedImage(screenCapCM,raster,false,null);

        // BufferedImage highResolutionImage = getHighResolutionImage(allBounds);
        ImageIO.write(highResolutionImage, "png", new File("screenshot_ALL1.png"));
    }

    /**
     * @deprecated 这个方法相比于上个方法有时候会丢一个像素的宽度。
     * @param allBounds
     * @throws AWTException
     * @throws IOException
     */
    @Deprecated
    public static void screenshot2(Rectangle allBounds) throws AWTException, IOException {
        AffineTransform defaultTransform = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getDefaultTransform();
        double scaleX = defaultTransform.getScaleX();
        double scaleY = defaultTransform.getScaleY();

        BufferedImage highResolutionImage = getHighResolutionImage(scaleX,scaleY,allBounds);
        ImageIO.write(highResolutionImage, "png", new File("screenshot_ALL2.png"));
    }

    public static BufferedImage getHighResolutionImage(double scaleX,double scaleY,Rectangle screenBounds) throws AWTException {

        System.out.println("screenBounds=" + screenBounds );
        Robot robot = new Robot();

        MultiResolutionImage multiResolutionImage = robot.createMultiResolutionScreenCapture(new Rectangle((int)(screenBounds.x/scaleX),(int)(screenBounds.y/scaleY),(int)(screenBounds.width/scaleX),(int)(screenBounds.height/scaleY)));
        Image image = multiResolutionImage.getResolutionVariant(screenBounds.getWidth(), screenBounds.getHeight());
        BufferedImage screenCapture = (BufferedImage) image;
        return screenCapture;
    }

}
