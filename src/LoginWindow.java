import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoginWindow extends JFrame {
    private JPanel panel;
    private JLabel titleLabel, userLabel, passwordLabel;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton loginButton, signUpButton;
    private JComboBox<String> loginOption;

    private Server server;

    public LoginWindow() {
        server = new Server();
        setTitle("Farmer's Manager");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (screenSize.width * 0.6), (int) (screenSize.height * 0.6));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image backgroundImage = ImageIO.read(new File("farm_background.jpg"));
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                    g.setColor(new Color(255, 255, 255, 100));
                    g.fillRect(0, 0, getWidth(), getHeight());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        titleLabel = UIUtils.createLabel("Farmer's Manager", new Font("Serif", Font.BOLD, 136), new Color(0, 102, 0));
        gbc = UIUtils.createGBC(0, 0, 2, 1, new Insets(20, 0, 20, 0));
        panel.add(titleLabel, gbc);

        userLabel = UIUtils.createLabel("ID:", new Font("Serif", Font.BOLD, 18), new Color(0, 102, 0));
        gbc = UIUtils.createGBC(0, 1, 1, 1, new Insets(0, 0, 0, 10));
        panel.add(userLabel, gbc);

        userTextField = UIUtils.createTextField(20);
        gbc = UIUtils.createGBC(1, 1, 1, 1, new Insets(0, 0, 0, 0));
        panel.add(userTextField, gbc);

        passwordLabel = UIUtils.createLabel("PW:", new Font("Serif", Font.BOLD, 18), new Color(0, 102, 0));
        gbc = UIUtils.createGBC(0, 2, 1, 1, new Insets(10, 0, 0, 10));
        panel.add(passwordLabel, gbc);

        passwordField = UIUtils.createPasswordField(20);
        gbc = UIUtils.createGBC(1, 2, 1, 1, new Insets(10, 0, 0, 0));
        panel.add(passwordField, gbc);

        loginButton = new JButton("로그인");
        loginButton.setBackground(new Color(153, 204, 0));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        gbc = UIUtils.createGBC(0, 3, 2, 1, new Insets(20, 0, 5, 10));
        gbc.ipady = 15;
        panel.add(loginButton, gbc);

        signUpButton = new JButton("회원가입");
        signUpButton.setBackground(new Color(189, 189, 189));
        gbc = UIUtils.createGBC(0, 5, 2, 1, new Insets(20, 10, 5, 0));
        gbc.ipady = 5;
        panel.add(signUpButton, gbc);

        loginOption = new JComboBox<>(new String[]{"Customer", "Farmer", "Administrator"});
        gbc = UIUtils.createGBC(0, 4, 2, 1, new Insets(0, 0, 0, 0));
        panel.add(loginOption, gbc);

        add(panel);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUpWindow(LoginWindow.this, server);
            }
        });

        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void handleLogin() {
        String username = userTextField.getText();
        String password = new String(passwordField.getPassword());
        String selectedRole = (String) loginOption.getSelectedItem();

        String[] user = server.validateUser(username, password, selectedRole);

        if (user != null) {
            String role = user[3];
            JOptionPane.showMessageDialog(this, role + "님 환영합니다!!");
            dispose();
            switch (role) {
                case "Customer":
                    new CustomerMainScreen(username);
                    break;
                case "Farmer":
                    new FarmerMainScreen(username);
                    break;
                case "Administrator":
                    new AdministratorMainScreen();
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(this, "로그인 실패. 다시 시도하세요.", "경고", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LoginWindow();
    }
}
