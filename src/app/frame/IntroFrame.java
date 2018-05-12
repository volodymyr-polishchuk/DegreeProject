package app.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Vladimir on 30/04/18.
 **/
public class IntroFrame extends JFrame {
    private JPanel contentPane;
    private ImagePanel imagePanel;

    public IntroFrame() throws HeadlessException {
        setContentPane(contentPane);
        setSize(600, 400);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        imagePanel = new ImagePanel();
        contentPane.setLayout(new GridLayout(1, 1));
        contentPane.add(imagePanel);
    }

    public ImagePanel getImagePanel() {
        return imagePanel;
    }

    public class ImagePanel extends JPanel {
        private Image introImage;
        private int max, min, now;
        private Point maxPosition, minPosition;

        private LinearGradientPaint gradientPaint;
        private float[] fractions = {.0f, .5f, 1.0f};
        private Color[] colors = {new Color(103, 58, 183), new Color(95, 167, 241), new Color(163, 255, 219)};


        public ImagePanel() {
            super();
            try {
                introImage = ImageIO.read(getClass().getResourceAsStream("/resource/intro.png"));
            } catch (IOException | IllegalArgumentException e ) {
                introImage = new BufferedImage(600, 400, BufferedImage.TYPE_INT_ARGB);
                Graphics g = introImage.getGraphics();
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, 600, 400);
                g.setColor(Color.LIGHT_GRAY);
                String text = "Image load fail";
                g.drawString(text, 300 - g.getFontMetrics().stringWidth(text) / 2,  200 - g.getFontMetrics().getHeight() / 2);
                e.printStackTrace();
            }

            min = 0;
            max = 100;
            now = min;
            minPosition = new Point(30, 345);
            maxPosition = new Point(200, 348);

        }


        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(introImage, 0, 0, null);
            Graphics2D g2 = (Graphics2D) g;
            gradientPaint = new LinearGradientPaint(
                    minPosition.x, minPosition.y,
                    minPosition.x + (int) ((maxPosition.x - minPosition.x) * (now / ((max - min) * 1f))),
                    maxPosition.y,
                    fractions,
                    colors
            );
            g2.setPaint(gradientPaint);
            g2.fillRoundRect(minPosition.x, minPosition.y, (int) ((maxPosition.x - minPosition.x) * (now / ((max - min) * 1f))), maxPosition.y - minPosition.y, 4, 2);
        }

        public void setNowPosition(int now) {
            if (now > max) now = max;
            else if (now < min) now = min;
            this.now = now;
            repaint(new Rectangle(minPosition.x, minPosition.y, maxPosition.x - minPosition.x, maxPosition.y - minPosition.y));
        }

        public void setMaxPosition(int max) {
            this.max = max;
        }

        public void setMinPosition(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public int getMin() {
            return min;
        }

        public int getNow() {
            return now;
        }

        public int nextStep() {
            repaint(new Rectangle(minPosition.x, minPosition.y, maxPosition.x - minPosition.x, maxPosition.y - minPosition.y));
            if (now < max) return ++now; else return now;
        }
    }
}
