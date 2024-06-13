import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFarm extends JFrame {
    private JPanel panel;
    private JTextField farmNameField, farmerNameField, cropsField;
    private JLabel userIdLabel;
    private JButton certificationRequestButton, backButton;
    private String userId;
    private Server server;
    private Navigable previousPage;

    public RegisterFarm(Navigable previousPage, String userId) {
        this.previousPage = previousPage;
        this.userId = userId;
        this.server = new Server();

        setTitle("농부 신상 명세서");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.6);
        int height = (int) (screenSize.height * 0.6);
        setSize(width, height);

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel farmNameLabel = new JLabel("농장명:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(farmNameLabel, gbc);

        farmNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(farmNameField, gbc);

        JLabel farmerNameLabel = new JLabel("농장주명:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(farmerNameLabel, gbc);

        farmerNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(farmerNameField, gbc);

        JLabel cropsLabel = new JLabel("수확할 작물명 (세미콜론 ; 으로 구분):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(cropsLabel, gbc);

        cropsField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(cropsField, gbc);

        userIdLabel = new JLabel("신청자 ID: " + userId);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(userIdLabel, gbc);

        certificationRequestButton = new JButton("인증 요청");
        certificationRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String farmName = farmNameField.getText().trim();
                String farmerName = farmerNameField.getText().trim();
                String crops = cropsField.getText().trim();

                if (farmName.isEmpty() || farmerName.isEmpty() || crops.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterFarm.this, "모든 필드를 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }


                String[] farmData = {farmName, farmerName, crops, userId};
                server.checkFarmer(farmData);
                JOptionPane.showMessageDialog(RegisterFarm.this, "인증 요청이 완료되었습니다.");
                previousPage.goBack();
                dispose();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(certificationRequestButton, gbc);

        backButton = new JButton("뒤로 가기");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousPage.goBack();
                dispose();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(backButton, gbc);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
