import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FarmerMainScreen extends MainScreen implements Navigable {
    private JButton registerFarmButton, viewReservationsButton, viewFarmInfoButton;
    private Server server;
    private String userId;

    public FarmerMainScreen(String userId) {
        super("농부 메인 화면");
        this.userId = userId;
        this.server = new Server();

        Color backgroundColor = new Color(221, 239, 187);

        panel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("환영합니다, " + userId + "님");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTH; // 환영 라벨을 위쪽에 배치
        panel.add(welcomeLabel, gbc);

        registerFarmButton = new JButton("농부 인증");
        registerFarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFarm(FarmerMainScreen.this, userId);
                dispose();
            }
        });
        gbc.gridy = 2;
        panel.add(registerFarmButton, gbc);

        viewReservationsButton = new JButton("예약 정보 보기");
        viewReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReservationInfoWindow(FarmerMainScreen.this, userId, "Farmer");
                dispose();
            }
        });
        gbc.gridy = 3;
        panel.add(viewReservationsButton, gbc);

        viewFarmInfoButton = new JButton("농장 정보 보기");
        viewFarmInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShowFarmInfo(FarmerMainScreen.this, userId);
                dispose();
            }
        });
        gbc.gridy = 4;
        panel.add(viewFarmInfoButton, gbc);

        checkFarmerStatus();

        // 로그아웃 버튼을 가장 아래에 배치
        addLogoutButton();
        setVisible(true);
    }

    private void checkFarmerStatus() {
        List<String[]> farms = server.loadFarms();
        boolean isFarmer = false;
        for (String[] farm : farms) {
            if (farm[3].equals(userId)) {
                isFarmer = true;
                break;
            }
        }

        viewReservationsButton.setEnabled(isFarmer);
        viewFarmInfoButton.setEnabled(isFarmer);

        if (!isFarmer) {
            JOptionPane.showMessageDialog(this, "농부 인증을 완료해야 합니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
