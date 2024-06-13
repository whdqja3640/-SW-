import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShowSellingCrops extends JDialog {
    private String userId, CId;
    private JPanel listPanel;
    private JList<String> cropList;
    private JButton buyButton;
    private JButton reserveButton;
    private List<String[]> reservableCropsData;
    private List<String[]> purchasableCropsData;


    public ShowSellingCrops(JFrame parentFrame, String userId, String CusomterId) {
        super(parentFrame, "판매 중인 농작물", true);
        this.userId = userId;
        this.CId = CusomterId;
        reservableCropsData = new ArrayList<>();
        purchasableCropsData = new ArrayList<>();

        JPanel modalPanel = new JPanel(new BorderLayout());

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton reservableButton = new JButton("예약 가능 리스트");
        JButton purchasableButton = new JButton("구매 가능 리스트");

        reservableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadReservableCrops();
                buyButton.setEnabled(false);
                reserveButton.setEnabled(true);
            }
        });

        purchasableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPurchasableCrops();
                buyButton.setEnabled(true);
                reserveButton.setEnabled(false);
            }
        });

        buttonPanel.add(reservableButton);
        buttonPanel.add(purchasableButton);

        // 리스트 패널
        listPanel = new JPanel(new BorderLayout());
        cropList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(cropList);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        modalPanel.add(buttonPanel, BorderLayout.NORTH);
        modalPanel.add(listPanel, BorderLayout.CENTER);

        // 구매와 예약 버튼 추가
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buyButton = new JButton("구매");
        reserveButton = new JButton("예약");

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBuyDialog();
            }
        });

        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showReserveDialog();
            }
        });

        actionPanel.add(buyButton);
        actionPanel.add(reserveButton);

        modalPanel.add(actionPanel, BorderLayout.SOUTH);

        add(modalPanel);
        setSize(800, 600); // 모달 크기를 키움
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }

    private void loadReservableCrops() {
        List<String> crops = new ArrayList<>();
        reservableCropsData.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("FarmCropData/crops.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7 && parts[0].equals(userId) && parts[4].equals("예")) {
                    String cropInfo = String.format("농작물 명: %s, 예상 수확 시간: %s, 예상 수확량: %sKg, Kg당 예상 가격: %s원",
                            parts[1], parts[2], parts[3], parts[6]);
                    crops.add(cropInfo);
                    reservableCropsData.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cropList.setListData(crops.toArray(new String[0]));
    }

    private void loadPurchasableCrops() {
        List<String> crops = new ArrayList<>();
        purchasableCropsData.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("FarmCropData/storage.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[0].equals(userId) && parts[4].trim().equals("예")) {
                    String cropInfo = String.format("농작물 명: %s, 재고: %sKg, Kg당 가격: %s원",
                            parts[1], parts[2], parts[3]);
                    crops.add(cropInfo);
                    purchasableCropsData.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cropList.setListData(crops.toArray(new String[0]));
    }

    private void showBuyDialog() {
    int selectedIndex = cropList.getSelectedIndex();
    if (selectedIndex != -1) {
        String[] cropData = purchasableCropsData.get(selectedIndex);
        // 농작물 명, 농장 ID, 재고량, 가격을 BuyDialog로 전달
        new BuyDialog(this, userId, cropData[0], cropData[1], Integer.parseInt(cropData[2]), Integer.parseInt(cropData[3]));
    } else {
        JOptionPane.showMessageDialog(this, "구매할 농작물을 선택하세요.");
    }
}

    private void showReserveDialog() {
        int selectedIndex = cropList.getSelectedIndex();
        if (selectedIndex != -1) {
            String[] cropData = reservableCropsData.get(selectedIndex);
            new ReserveDialog(this, cropData[1], Integer.parseInt(cropData[3]), Integer.parseInt(cropData[6]), userId, CId);
        } else {
            JOptionPane.showMessageDialog(this, "예약할 농작물을 선택하세요.");
        }
    }

}
