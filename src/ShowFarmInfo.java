import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowFarmInfo extends JFrame implements Navigable {
    private JPanel panel;
    private JLabel titleLabel;
    private JButton growingCropsButton, sellingCropsButton, storageInfoButton, backButton;
    private Navigable previousPage;
    private String userId;
    private Server server;

    public ShowFarmInfo(Navigable previousPage, String userId) {
        this(previousPage, null ,userId, "Owner");
    }

    public ShowFarmInfo(Navigable previousPage, String CustomerId, String farmId, String role) {
        this.previousPage = previousPage;
        this.server = new Server();

        if(role.equals("Customer")) {
            this.userId = server.farmNameToId(farmId);
        }
        else{
            this.userId = farmId;
        }

        setTitle("농장 정보");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.6);
        int height = (int) (screenSize.height * 0.6);
        setSize(width, height);

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        titleLabel = new JLabel("농장 정보");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        growingCropsButton = new JButton("재배 중인 농작물");
        growingCropsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShowCropList(ShowFarmInfo.this, userId);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(growingCropsButton, gbc);

        sellingCropsButton = new JButton("판매 중인 농작물");
        sellingCropsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShowSellingCrops(ShowFarmInfo.this, userId, CustomerId);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(sellingCropsButton, gbc);

        storageInfoButton = new JButton("저장고 내역 확인");
        storageInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShowStorageInfo(userId);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(storageInfoButton, gbc);

        backButton = new JButton("뒤로가기");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                goBack();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(backButton, gbc);

        add(panel);

        if (role.equals("Customer")) {
            growingCropsButton.setEnabled(false);
            storageInfoButton.setEnabled(false);
        }

        Color backgroundColor = new Color(153, 204, 102);
        panel.setBackground(backgroundColor);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void goBack() {
        previousPage.goBack();
    }
}
