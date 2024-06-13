import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StorageDialog extends JDialog {
    private JTextField cropNameField, quantityField, priceField;
    private JRadioButton yesButton, noButton;
    private JButton saveButton, cancelButton;
    private String userId;
    private Server server;
    private String[] storageData;

    public StorageDialog(JFrame parent, String title, String[] storageData, String userId) {
        super(parent, title, true);
        this.userId = userId;
        this.server = new Server();
        this.storageData = storageData;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel cropNameLabel = new JLabel("농작물 명:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(cropNameLabel, gbc);

        cropNameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(cropNameField, gbc);

        JLabel quantityLabel = new JLabel("재고(kg):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(quantityLabel, gbc);

        quantityField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(quantityField, gbc);

        JLabel priceLabel = new JLabel("Kg 당 가격:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(priceLabel, gbc);

        priceField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(priceField, gbc);

        JLabel saleLabel = new JLabel("판매 여부:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(saleLabel, gbc);

        yesButton = new JRadioButton("예");
        noButton = new JRadioButton("아니오");
        ButtonGroup saleGroup = new ButtonGroup();
        saleGroup.add(yesButton);
        saleGroup.add(noButton);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(yesButton, gbc);
        gbc.gridx = 2;
        gbc.gridy = 3;
        add(noButton, gbc);

        if (storageData != null) {
            cropNameField.setText(storageData[1]);
            quantityField.setText(storageData[2]);
            priceField.setText(storageData[3]);
            if (storageData[4].equals("예")) {
                yesButton.setSelected(true);
            } else {
                noButton.setSelected(true);
            }
        }

        saveButton = new JButton("저장");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cropName = cropNameField.getText();
                String quantity = quantityField.getText();
                String price = priceField.getText();
                String saleStatus = yesButton.isSelected() ? "예" : "아니오";

                String[] newStorageData = new String[]{userId, cropName, quantity, price, saleStatus};

                if (storageData == null) {
                    server.addToStorage(newStorageData);
                } else {
                    server.updateStorage(storageData, newStorageData);
                }
                ((ShowStorageInfo) parent).refreshStorageData();
                dispose();
            }
        });

        cancelButton = new JButton("취소");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(saveButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(cancelButton, gbc);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
