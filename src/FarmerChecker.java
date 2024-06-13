import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FarmerChecker extends JFrame {
    private JTable farmersTable;
    private DefaultTableModel tableModel;
    private Server server;
    private JButton approveButton, rejectButton;
    private AdministratorMainScreen mainScreen;

    public FarmerChecker(AdministratorMainScreen mainScreen, Server server) {
        this.mainScreen = mainScreen;
        this.server = server;

        setTitle("농부 회원 승인");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"농장명", "농장주명", "키울 작물", "농장주 ID"}, 0);
        farmersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(farmersTable);

        JPanel buttonPanel = new JPanel();
        approveButton = new JButton("승인");
        rejectButton = new JButton("거절");
        approveButton.addActionListener(new ApproveActionListener());
        rejectButton.addActionListener(new RejectActionListener());

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadFarmersData();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadFarmersData() {
        List<String[]> farmers = server.loadFarmerChecker();
        for (String[] farmer : farmers) {
            tableModel.addRow(farmer);
        }
    }

    private class ApproveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = farmersTable.getSelectedRow();
            if (selectedRow >= 0) {
                String[] farmerData = new String[tableModel.getColumnCount()];
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    farmerData[i] = (String) tableModel.getValueAt(selectedRow, i);
                }
                server.saveFarm(farmerData);
                tableModel.removeRow(selectedRow);
                server.removeFarmerCheck(farmerData);
                JOptionPane.showMessageDialog(FarmerChecker.this, "농부 회원이 승인되었습니다.");
            } else {
                JOptionPane.showMessageDialog(FarmerChecker.this, "승인할 농부를 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class RejectActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = farmersTable.getSelectedRow();
            if (selectedRow >= 0) {
                String[] farmerData = new String[tableModel.getColumnCount()];
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    farmerData[i] = (String) tableModel.getValueAt(selectedRow, i);
                }
                tableModel.removeRow(selectedRow);
                server.removeFarmerCheck(farmerData);
                JOptionPane.showMessageDialog(FarmerChecker.this, "농부 회원이 거절되었습니다.");
            } else {
                JOptionPane.showMessageDialog(FarmerChecker.this, "거절할 농부를 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
