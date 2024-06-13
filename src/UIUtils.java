import javax.swing.*;
import java.awt.*;

public class UIUtils {
    public static JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    public static JTextField createTextField(int columns) {
        return new JTextField(columns);
    }

    public static JPasswordField createPasswordField(int columns) {
        return new JPasswordField(columns);
    }

    public static GridBagConstraints createGBC(int x, int y, int width, int height, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.insets = insets;
        return gbc;
    }
}
