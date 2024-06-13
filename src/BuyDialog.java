import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuyDialog extends JDialog {
    private Server server; // 서버 객체
    private String userId; // 구매자 ID
    private String farmId; // 농장 ID
    private JButton buyButton; // 구매 버튼

    public BuyDialog(JDialog parentDialog, String userId, String farmId, String cropName, int stock, int pricePerKg) {
        super(parentDialog, "구매", true);

        this.userId = userId;
        this.farmId = farmId;
        this.server = new Server();

        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(new JLabel("농작물 명:"));
        JTextField cropNameField = new JTextField(cropName);
        cropNameField.setEditable(false);
        panel.add(cropNameField);

        panel.add(new JLabel("구매량 (Kg):"));
        JTextField quantityField = new JTextField();
        panel.add(quantityField);

        panel.add(new JLabel("구매자 명:"));
        JTextField buyerNameField = new JTextField();
        panel.add(buyerNameField);

        panel.add(new JLabel("전화 번호:"));
        JTextField phoneNumberField = new JTextField();
        panel.add(phoneNumberField);

        JButton checkStockButton = new JButton("재고 확인");
        panel.add(checkStockButton);

        JLabel totalPriceLabel = new JLabel("총 가격: 0원");
        panel.add(totalPriceLabel);

        checkStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    if (quantity > stock) {
                        JOptionPane.showMessageDialog(BuyDialog.this, "재고가 부족합니다.");
                        quantityField.setText("");
                        buyButton.setEnabled(false); // 재고가 부족한 경우 구매 버튼 비활성화
                    } else {
                        int totalPrice = quantity * pricePerKg;
                        totalPriceLabel.setText("총 가격: " + totalPrice + "원");
                        buyButton.setEnabled(true); // 재고가 충분한 경우 구매 버튼 활성화
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BuyDialog.this, "유효한 숫자를 입력하세요.");
                }
            }
        });

        buyButton = new JButton("구매");
        buyButton.setEnabled(false); // 초기에는 비활성화

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(BuyDialog.this, "정말 구매하시겠습니까?", "구매 확인", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    String quantityStr = quantityField.getText();
                    String buyerName = buyerNameField.getText();
                    String phoneNumber = phoneNumberField.getText();
                    try {
                        int quantity = Integer.parseInt(quantityStr);
                        if (quantity <= stock && !buyerName.isEmpty() && !phoneNumber.isEmpty()) {
                            server.updateCropQuantityBuy(farmId, cropName, stock - quantity);
                            System.out.println(farmId);
                            JOptionPane.showMessageDialog(BuyDialog.this, "구매가 완료되었습니다.");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(BuyDialog.this, "모든 필드를 올바르게 입력하세요.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(BuyDialog.this, "유효한 숫자를 입력하세요.");
                    }
                }
            }
        });

        add(panel, BorderLayout.CENTER);
        add(buyButton, BorderLayout.SOUTH);

        setSize(300, 200);
        setLocationRelativeTo(parentDialog);
        setVisible(true);
    }
}
