import javax.swing.*;
import java.awt.*;

public class AdministratorMainScreen extends MainScreen {
    private JButton approveFarmerButton;
    private Server server;

    public AdministratorMainScreen() {
        super("Administrator Main Screen");
        server = new Server();
        initializeAdministratorComponents();
    }

    private void initializeAdministratorComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 1;

        approveFarmerButton = createButton("농부 회원 승인", e -> {
            new FarmerChecker(this, server);
        });
        panel.add(approveFarmerButton, gbc);
        // 로그아웃 버튼을 가장 아래에 배치
        addLogoutButton();
        setVisible(true);
    }
}
