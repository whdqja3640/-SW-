import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SignUpWindow extends JDialog {
    private JPanel panel;
    private JLabel idLabel, passwordLabel, confirmPasswordLabel, nameLabel, roleLabel;
    private JTextField idTextField, nameTextField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private JButton signUpButton;
    private Server server;

    public SignUpWindow(Frame parent, Server server) {
        super(parent, "회원가입", true);
        this.server = server;

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        idLabel = UIUtils.createLabel("ID:", new Font("Serif", Font.BOLD, 14), new Color(0, 102, 0));
        gbc = UIUtils.createGBC(0, 0, 1, 1, new Insets(10, 0, 0, 10));
        panel.add(idLabel, gbc);

        idTextField = UIUtils.createTextField(20);
        gbc = UIUtils.createGBC(1, 0, 2, 1, new Insets(10, 0, 0, 0));
        panel.add(idTextField, gbc);

        passwordLabel = UIUtils.createLabel("PW:", new Font("Serif", Font.BOLD, 14), new Color(0, 102, 0));
        gbc = UIUtils.createGBC(0, 1, 1, 1, new Insets(10, 0, 0, 10));
        panel.add(passwordLabel, gbc);

        passwordField = UIUtils.createPasswordField(20);
        gbc = UIUtils.createGBC(1, 1, 2, 1, new Insets(10, 0, 0, 0));
        panel.add(passwordField, gbc);

        confirmPasswordLabel = UIUtils.createLabel("PW 확인:", new Font("Serif", Font.BOLD, 14), new Color(0, 102, 0));
        gbc = UIUtils.createGBC(0, 2, 1, 1, new Insets(10, 0, 0, 10));
        panel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = UIUtils.createPasswordField(20);
        gbc = UIUtils.createGBC(1, 2, 2, 1, new Insets(10, 0, 0, 0));
        panel.add(confirmPasswordField, gbc);

        nameLabel = UIUtils.createLabel("전화번호:", new Font("Serif", Font.BOLD, 14), new Color(0, 102, 0));
        gbc = UIUtils.createGBC(0, 3, 1, 1, new Insets(10, 0, 0, 10));
        panel.add(nameLabel, gbc);

        nameTextField = UIUtils.createTextField(20);
        gbc = UIUtils.createGBC(1, 3, 2, 1, new Insets(10, 0, 0, 0));
        panel.add(nameTextField, gbc);

        roleLabel = UIUtils.createLabel("역할:", new Font("Serif", Font.BOLD, 14), new Color(0, 102, 0));
        gbc = UIUtils.createGBC(0, 4, 1, 1, new Insets(10, 0, 0, 10));
        panel.add(roleLabel, gbc);

        roleComboBox = new JComboBox<>(new String[]{"Customer", "Farmer", "Administrator"});
        gbc = UIUtils.createGBC(1, 4, 2, 1, new Insets(10, 0, 0, 0));
        panel.add(roleComboBox, gbc);

        signUpButton = new JButton("회원가입");
        signUpButton.setBackground(new Color(153, 204, 0));
        gbc = UIUtils.createGBC(0, 5, 3, 1, new Insets(20, 0, 0, 0));
        gbc.ipady = 15;
        panel.add(signUpButton, gbc);

        add(panel);
        pack();
        setLocationRelativeTo(parent);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });

        setVisible(true);
    }

    private void handleSignUp() {
        String id = idTextField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String name = nameTextField.getText();
        String role = (String) roleComboBox.getSelectedItem();

        if (id.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 필드를 채워주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (server.isUserIdTaken(id)) {
            JOptionPane.showMessageDialog(this, "이미 사용 중인 아이디입니다.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        server.saveUser(new String[]{id, password, name, role});
        JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
