import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationInfoWindow extends JFrame {
    private JPanel panel;
    private JLabel titleLabel;
    private JTable reservationTable;
    private JScrollPane reservationTableScrollPane;
    private JButton backButton;
    private JButton cancelButton;

    private Navigable previousPage;
    private Server server;
    private String userId;
    private String role;

    public ReservationInfoWindow(Navigable previousPage, String userId, String role) {
        this.previousPage = previousPage;
        this.userId = userId;
        this.role = role;
        this.server = new Server();

        setTitle("예약 정보");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.6);
        int height = (int) (screenSize.height * 0.6);
        setSize(width, height);

        panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(200, 255, 200));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        titleLabel = new JLabel("예약 정보");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        String[] columnNames = {"작물 이름", "농장 이름", "예약 수량(Kg)"};
        Object[][] data = loadReservations(role);

        reservationTable = new JTable(new DefaultTableModel(data, columnNames));
        reservationTableScrollPane = new JScrollPane(reservationTable);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(reservationTableScrollPane, gbc);

        JPanel buttonPanel = new JPanel();

        backButton = new JButton("뒤로 가기");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousPage.goBack();
                dispose();
            }
        });
        buttonPanel.add(backButton);

        cancelButton = new JButton("예약 취소");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reservationTable.getSelectedRow();
                if (selectedRow != -1) {
                    String cropName = (String) reservationTable.getValueAt(selectedRow, 0);
                    String farmName = (String) reservationTable.getValueAt(selectedRow, 1);
                    String quantity = (String) reservationTable.getValueAt(selectedRow, 2);

                    int result = JOptionPane.showConfirmDialog(ReservationInfoWindow.this, "정말 예약을 취소하시겠습니까?", "예약 취소", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        boolean success = cancelReservation(cropName, farmName, quantity);
                        if (success) {
                            ((DefaultTableModel) reservationTable.getModel()).removeRow(selectedRow);
                            JOptionPane.showMessageDialog(ReservationInfoWindow.this, "예약이 취소되었습니다.");
                        } else {
                            JOptionPane.showMessageDialog(ReservationInfoWindow.this, "예약 취소 중 오류가 발생했습니다.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(ReservationInfoWindow.this, "취소할 예약을 선택하세요.");
                }
            }
        });
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Object[][] loadReservations(String role) {
        List<String[]> allReservations = server.loadReservations();
        List<String[]> userReservations;
        if (role.equals("Customer")) {
            userReservations = allReservations.stream()
                    .filter(reservation -> reservation[0].equals(userId))
                    .collect(Collectors.toList());
        } else {
            userReservations = allReservations.stream()
                    .filter(reservation -> reservation[2].equals(userId))
                    .collect(Collectors.toList());
        }
        Object[][] data = new Object[userReservations.size()][3];
        for (int i = 0; i < userReservations.size(); i++) {
            String[] reservation = userReservations.get(i);
            String cropName = reservation[3];
            String farmId = reservation[2];
            String farmName = server.getFarmNameById(farmId);
            String quantity = reservation[5];

            data[i][0] = cropName;
            data[i][1] = farmName;
            data[i][2] = quantity;
        }
        return data;
    }

    private boolean cancelReservation(String cropName, String farmName, String quantity) {
        List<String[]> allReservations = server.loadReservations();
        String farmId = server.farmNameToId(farmName);
        int quantityToCancel = Integer.parseInt(quantity);

        List<String[]> updatedReservations = allReservations.stream()
                .filter(reservation -> !(reservation[3].equals(cropName) && reservation[2].equals(farmId) && reservation[5].equals(quantity)))
                .collect(Collectors.toList());

        if (updatedReservations.size() == allReservations.size() - 1) {
            server.saveReservations(updatedReservations);
            server.updateCropQuantityMinus(farmId, cropName, quantityToCancel);
            return true;
        }
        return false;
    }
}
