import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerMainScreen extends MainScreen {
    private JButton searchFarmButton, reservationInfoButton;
    private String userId;

    public CustomerMainScreen(String userId) {
        super("Customer Main Screen");
        this.userId = userId;
        initializeCustomerComponents();
    }

    private void initializeCustomerComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 1;

        searchFarmButton = createButton("농장 검색", e -> {
            JOptionPane.showMessageDialog(this, "농장 검색 화면으로 이동합니다.");
            dispose();
            new FarmSearchWindow(this, userId);
        });
        panel.add(searchFarmButton, gbc);

        reservationInfoButton = createButton("예약 정보", e -> {
            JOptionPane.showMessageDialog(this, "예약 정보 화면으로 이동합니다.");
            dispose();
            new ReservationInfoWindow(this, userId, "Customer");
        });
        gbc.gridy = 2;
        panel.add(reservationInfoButton, gbc);

        // 로그아웃 버튼을 가장 아래에 배치
        addLogoutButton();
        setVisible(true);

    }
}
