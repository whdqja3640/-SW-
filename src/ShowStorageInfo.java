import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ShowStorageInfo extends JFrame {
    private String userId;
    private Server server;
    private JPanel panel;
    private JTable table;
    private JButton addButton, editButton, deleteButton;

    public ShowStorageInfo(String userId) {
        this.userId = userId;
        this.server = new Server();
        setTitle("저장고 내역");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        panel = new JPanel(new BorderLayout());
        add(panel);

        addButton = new JButton("추가");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StorageDialog(ShowStorageInfo.this, "저장고 추가", null, userId);
            }
        });

        editButton = new JButton("수정");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String[] storageData = new String[]{
                        userId,
                        table.getValueAt(selectedRow, 0).toString(),
                        table.getValueAt(selectedRow, 1).toString(),
                        table.getValueAt(selectedRow, 2).toString(),
                        table.getValueAt(selectedRow, 3).toString()
                    };
                    new StorageDialog(ShowStorageInfo.this, "저장고 수정", storageData, userId);
                } else {
                    JOptionPane.showMessageDialog(ShowStorageInfo.this, "수정할 항목을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        deleteButton = new JButton("삭제");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(ShowStorageInfo.this, "선택한 항목을 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String[] storageData = new String[]{
                            userId,
                            table.getValueAt(selectedRow, 0).toString(),
                            table.getValueAt(selectedRow, 1).toString(),
                            table.getValueAt(selectedRow, 2).toString(),
                            table.getValueAt(selectedRow, 3).toString()
                        };
                        server.deleteStorage(storageData);
                        refreshStorageData();
                    }
                } else {
                    JOptionPane.showMessageDialog(ShowStorageInfo.this, "삭제할 항목을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        refreshStorageData();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void refreshStorageData() {
        if (panel != null) {
            panel.removeAll();
            List<String[]> storageData = server.loadStorage();
            Object[] columnNames = {"농작물 명", "재고(kg)", "Kg 당 가격", "판매 여부"};
            Object[][] rowData = storageData.stream()
                .filter(data -> data[0].equals(userId))
                .map(data -> new Object[]{data[1], data[2], data[3], data[4]})
                .toArray(Object[][]::new);

            table = new JTable(rowData, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(addButton);
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);

            panel.add(buttonPanel, BorderLayout.SOUTH);

            panel.revalidate();
            panel.repaint();
        }
    }
}
