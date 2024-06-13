import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FarmSearchWindow extends JFrame implements Navigable {
    private JPanel panel;
    private JTextField searchField;
    private JButton searchButton, viewFarmButton, backButton;
    private JRadioButton nameRadioButton, cropRadioButton;
    private JList<String> resultList;
    private DefaultListModel<String> listModel;
    private Server server;
    private Navigable previousPage;
    private String selectedFarm;

    public FarmSearchWindow(Navigable previousPage, String userId) {
        this.previousPage = previousPage;
        this.server = new Server();

        setTitle("농장 검색");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.6);
        int height = (int) (screenSize.height * 0.6);
        setSize(width, height);

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        searchField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(searchField, gbc);

        searchButton = new JButton("검색");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(searchButton, gbc);

        nameRadioButton = new JRadioButton("농장 이름 기준");
        nameRadioButton.setSelected(true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameRadioButton, gbc);

        cropRadioButton = new JRadioButton("농작물 이름 기준");
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(cropRadioButton, gbc);

        ButtonGroup group = new ButtonGroup();
        group.add(nameRadioButton);
        group.add(cropRadioButton);

        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedFarm = resultList.getSelectedValue();
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultList);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        viewFarmButton = new JButton("농장 보기");
        viewFarmButton.setPreferredSize(new Dimension(100, 30));  // 크기 조정
        viewFarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFarm != null) {
                    new ShowFarmInfo(FarmSearchWindow.this, userId, selectedFarm, "Customer");
                    dispose();
                }
            }
        });
        buttonPanel.add(viewFarmButton);

        backButton = new JButton("뒤로 가기");
        backButton.setPreferredSize(new Dimension(100, 30));  // 크기 조정
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousPage.goBack();
                dispose();
            }
        });
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        panel.add(buttonPanel, gbc);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        List<String> results;

        if (nameRadioButton.isSelected()) {
            results = server.searchFarmsByName(keyword);
        } else {
            results = server.searchFarmsByCrop(keyword);
        }

        listModel.clear();
        if (results.isEmpty()) {
            listModel.addElement("검색 결과가 없습니다.");
        } else {
            for (String farm : results) {
                listModel.addElement(farm);
            }
        }
    }

    @Override
    public void goBack() {
        previousPage.goBack();
    }
}
