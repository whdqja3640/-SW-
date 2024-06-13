import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ShowCropList extends JFrame {
    private JPanel panel;
    private JTable cropTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, harvestButton;
    private Server server;
    private String userId;
    private List<String[]> crops;

    public ShowCropList(JFrame parent, String userId) {
        super("재배 중인 농작물");
        this.userId = userId;
        this.server = new Server();

        setSize(800, 600);
        setLocationRelativeTo(parent);

        panel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"농작물 명", "예상 수확 시간", "예상 수확량(Kg)", "예약 가능 여부", "판매 여부", "Kg당 예상 가격(원)"}, 0);
        cropTable = new JTable(tableModel);
        cropTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadCrops();

        panel.add(new JScrollPane(cropTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("추가");
        editButton = new JButton("수정");
        deleteButton = new JButton("삭제");
        harvestButton = new JButton("수확");

        addButton.addActionListener(e -> showCropDialog(null));
        editButton.addActionListener(e -> {
            int selectedIndex = cropTable.getSelectedRow();
            if (selectedIndex != -1) {
                showCropDialog(crops.get(selectedIndex));
            } else {
                JOptionPane.showMessageDialog(this, "수정할 농작물을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedIndex = cropTable.getSelectedRow();
            if (selectedIndex != -1) {
                crops.remove(selectedIndex);
                saveCrops();
                loadCrops();
            } else {
                JOptionPane.showMessageDialog(this, "삭제할 농작물을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });

        harvestButton.addActionListener(e -> {
            int selectedIndex = cropTable.getSelectedRow();
            if (selectedIndex != -1) {
                showHarvestDialog(crops.get(selectedIndex));
            } else {
                JOptionPane.showMessageDialog(this, "수확할 농작물을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(harvestButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private void loadCrops() {
        tableModel.setRowCount(0);
        crops = new ArrayList<>();
        for (String[] crop : server.loadCrops()) {
            if (crop.length == 7 && crop[0].equals(userId)) {
                crops.add(crop);
                tableModel.addRow(new Object[]{
                        crop[1], crop[2], crop[3], crop[4], crop[5], crop[6]
                });
            }
        }
    }

    private void saveCrops() {
        server.saveCrops(userId, crops);
    }

    private void showCropDialog(String[] cropData) {
        JDialog dialog = new JDialog(this, "농작물 정보", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        JTextField cropNameField = new JTextField();
        JTextField dateField = new JTextField("yy/MM/dd");
        JTextField timeField = new JTextField("HH");
        JTextField quantityField = new JTextField();
        JComboBox<String> reservationComboBox = new JComboBox<>(new String[]{"예", "아니오"});
        JComboBox<String> saleComboBox = new JComboBox<>(new String[]{"예", "아니오"});
        JTextField priceField = new JTextField();

        if (cropData != null) {
            cropNameField.setText(cropData[1]);
            String[] dateTime = cropData[2].split("/");
            dateField.setText(dateTime[0]);
            timeField.setText(dateTime[1]);
            quantityField.setText(cropData[3]);
            reservationComboBox.setSelectedItem(cropData[4]);
            saleComboBox.setSelectedItem(cropData[5]);
            priceField.setText(cropData[6]);
        }

        saleComboBox.addActionListener(e -> priceField.setEnabled(saleComboBox.getSelectedItem().equals("예")));

        dialogPanel.add(new JLabel("농작물 이름:"));
        dialogPanel.add(cropNameField);
        dialogPanel.add(new JLabel("예상 수확 날짜(년/월/일):"));
        dialogPanel.add(dateField);
        dialogPanel.add(new JLabel("예상 수확 시간:"));
        dialogPanel.add(timeField);
        dialogPanel.add(new JLabel("예상 수확량(단위:Kg):"));
        dialogPanel.add(quantityField);
        dialogPanel.add(new JLabel("예약 가능 여부:"));
        dialogPanel.add(reservationComboBox);
        dialogPanel.add(new JLabel("판매 여부:"));
        dialogPanel.add(saleComboBox);
        dialogPanel.add(new JLabel("Kg당 예상 가격(원):"));
        dialogPanel.add(priceField);

        priceField.setEnabled(saleComboBox.getSelectedItem().equals("예"));

        JButton saveButton = new JButton("저장");
        JButton cancelButton = new JButton("취소");

        saveButton.addActionListener(e -> {
            String cropName = cropNameField.getText();
            String harvestDate = dateField.getText();
            String harvestTime = timeField.getText();
            String harvestDateTime = harvestDate + "/" + harvestTime;
            String quantity = quantityField.getText();
            String reservation = (String) reservationComboBox.getSelectedItem();
            String sale = (String) saleComboBox.getSelectedItem();
            String price = priceField.getText();

            if (cropData != null) {
                cropData[1] = cropName;
                cropData[2] = harvestDateTime;
                cropData[3] = quantity;
                cropData[4] = reservation;
                cropData[5] = sale;
                cropData[6] = price;
            } else {
                crops.add(new String[]{userId, cropName, harvestDateTime, quantity, reservation, sale, price});
            }
            saveCrops();
            loadCrops();
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(dialogPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showHarvestDialog(String[] cropData) {
        JDialog dialog = new JDialog(this, "수확 정보", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField cropNameField = new JTextField(cropData[1]);
        cropNameField.setEditable(false);
        JTextField quantityField = new JTextField();
        JComboBox<String> saleComboBox = new JComboBox<>(new String[]{"예", "아니오"});
        saleComboBox.setSelectedItem(cropData[5]);
        JTextField priceField = new JTextField(cropData[6]);

        dialogPanel.add(new JLabel("농작물 이름:"));
        dialogPanel.add(cropNameField);
        dialogPanel.add(new JLabel("총 수확량(단위:Kg):"));
        dialogPanel.add(quantityField);
        dialogPanel.add(new JLabel("판매 여부:"));
        dialogPanel.add(saleComboBox);
        dialogPanel.add(new JLabel("Kg당 가격(원):"));
        dialogPanel.add(priceField);

        JButton harvestButton = new JButton("수확");
        JButton cancelButton = new JButton("취소");

        harvestButton.addActionListener(e -> {
            String cropName = cropNameField.getText();
            String quantity = quantityField.getText();
            String sale = (String) saleComboBox.getSelectedItem();
            String price = priceField.getText();

            if (quantity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "총 수확량을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            server.deleteCrop(cropData);
            server.addToStorage(new String[]{userId, cropName, quantity, price, sale});

            loadCrops();
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(harvestButton);
        buttonPanel.add(cancelButton);

        dialog.add(dialogPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

}
