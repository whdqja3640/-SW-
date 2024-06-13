import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class MainScreen extends JFrame implements Navigable {
    protected JPanel panel;
    protected JLabel titleLabel;
    protected JButton logoutButton;

    public MainScreen(String title) {
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeComponents();
    }

    private void initializeComponents() {
        // 사용자의 모니터 크기에 따라 창의 크기를 조정
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.6);
        int height = (int) (screenSize.height * 0.6);
        setSize(width, height);

        // 패널 생성
        panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(221, 239, 187)); // 배경 색상 설정
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 10, 10);

        // 제목 라벨 생성
        titleLabel = new JLabel("Farmer's Manager");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    protected void addLogoutButton() {
        logoutButton = createButton("로그아웃", e -> handleLogout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.SOUTH; // 버튼을 아래쪽에 배치
        panel.add(logoutButton, gbc);
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            new LoginWindow();
        }
    }

    @Override
    public void goBack() {
        setVisible(true); // 현재 페이지를 다시 보이게 함
    }
}
