import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;

public class MakeOverlay {
    public static void main(String[] args) throws Exception {
        int size = 16;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Color c = new Color(255,255,255,180); // semi-opaque white
        for (int x=0;x<size;x++){
            for (int y=0;y<size;y++){
                img.setRGB(x,y,c.getRGB());
            }
        }
        File out = new File("src/main/resources/assets/be/textures/block/stained_glass_overlay.png");
        out.getParentFile().mkdirs();
        ImageIO.write(img, "png", out);
        System.out.println("WROTE:"+out.getAbsolutePath());
    }
}

