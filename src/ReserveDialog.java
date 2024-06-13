import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ReserveDialog extends JDialog {
    private String cropName;
    private int expectedYield;
    private int pricePerKg;
    private String userId;
    private String customerId;
    private Server server;

    public ReserveDialog(JDialog parentDialog, String cropName, int expectedYield, int pricePerKg, String userId, String customerId) {
        super(parentDialog, "예약", true);
        this.cropName = cropName;
        this.expectedYield = expectedYield;
        this.pricePerKg = pricePerKg;
        this.userId = userId;
        this.customerId = customerId;
        this.server = new Server();

        JPanel panel = new JPanel(new GridLayout(5, 2));

        panel.add(new JLabel("농작물 명:"));
        JTextField cropNameField = new JTextField(cropName);
        cropNameField.setEditable(false);
        panel.add(cropNameField);

        panel.add(new JLabel("예약량 (Kg):"));
        JTextField quantityField = new JTextField();
        panel.add(quantityField);

        panel.add(new JLabel("예약자 명:"));
        JTextField reserverNameField = new JTextField();
        panel.add(reserverNameField);

        panel.add(new JLabel("전화 번호:"));
        JTextField phoneNumberField = new JTextField();
        panel.add(phoneNumberField);

        JLabel totalPriceLabel = new JLabel("총 가격: 0원");
        panel.add(totalPriceLabel);

        quantityField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    if (quantity > expectedYield) {
                        JOptionPane.showMessageDialog(ReserveDialog.this, "예상 수확량이 부족합니다.");
                        quantityField.setText("");
                    } else {
                        int totalPrice = quantity * pricePerKg;
                        totalPriceLabel.setText("총 가격: " + totalPrice + "원");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ReserveDialog.this, "유효한 숫자를 입력하세요.");
                }
            }
        });

        JButton reserveButton = new JButton("예약");
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String quantityStr = quantityField.getText();
                String reserverName = reserverNameField.getText();
                String phoneNumber = phoneNumberField.getText();
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity <= expectedYield && !reserverName.isEmpty() && !phoneNumber.isEmpty()) {
                        // 예약 정보를 파일에 저장
                        String reservationInfo = String.join(",", customerId, reserverName, userId, cropName, phoneNumber, String.valueOf(quantity));
                        saveReservation(reservationInfo);
                        // crops.txt 파일 업데이트
                        server.updateCropQuantity(userId, cropName, expectedYield - quantity);
                        JOptionPane.showMessageDialog(ReserveDialog.this, "예약이 완료되었습니다.");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(ReserveDialog.this, "모든 필드를 올바르게 입력하세요.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ReserveDialog.this, "유효한 숫자를 입력하세요.");
                }
            }
        });

        add(panel, BorderLayout.CENTER);
        add(reserveButton, BorderLayout.SOUTH);

        setSize(300, 200);
        setLocationRelativeTo(parentDialog);
        setVisible(true);
    }

    private void saveReservation(String reservationInfo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("FarmReservationData/reservations.txt", true))) {
            writer.write(reservationInfo);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
