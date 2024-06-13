import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Server {
    private final String userDataPath = "MemberInfo/member.txt";
    private final String farmDataPath = "FarmData/farms.txt";
    private final String farmerCheckDataPath = "FarmData/farmerCheck.txt";
    private final String cropDataPath = "FarmCropData/crops.txt";
    private final String reservationDataPath = "FarmReservationData/reservations.txt";
    private final String storageDataPath = "FarmCropData/storage.txt";

    // 회원 정보 저장 및 불러오기
    public void saveUser(String[] userData) {
        writeFile(userDataPath, userData);
    }

    public List<String[]> loadUsers() {
        return readFile(userDataPath);
    }

    // 유효성 검사 메서드 수정
    public String[] validateUser(String id, String password, String role) {
        List<String[]> users = loadUsers();
        for (String[] user : users) {
            if (user[0].equals(id) && user[1].equals(password) && user[3].equals(role)) {
                return user;
            }
        }
        return null;
    }

    public boolean isUserIdTaken(String id) {
        List<String[]> users = loadUsers();
        for (String[] user : users) {
            if (user[0].equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void removeFarmerCheck(String[] farmerData) {
        List<String[]> farmers = readFile(farmerCheckDataPath);
        farmers.removeIf(data -> Arrays.equals(data, farmerData));
        writeFile(farmerCheckDataPath, farmers, false);
    }

    private void writeFile(String filePath, List<String[]> data, boolean append) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, append))) {
            for (String[] line : data) {
                bw.write(String.join(",", line));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> loadFarms() {
        return readFile(farmDataPath);
    }

    public List<String[]> loadCrops() {
        return readFile(cropDataPath);
    }

    public List<String[]> loadReservations() {
        return readFile(reservationDataPath);
    }

    public List<String[]> loadStorage() {
        return readFile(storageDataPath);
    }

    public List<String[]> loadFarmerChecker(){
        return readFile(farmerCheckDataPath);
    }
    public void checkFarmer(String[] farmerData){
        writeFile(farmerCheckDataPath, farmerData);
    }
    public void saveFarm(String[] farmData) {
        List<String[]> farms = readFile(farmDataPath);
        boolean updated = false;

        for (int i = 0; i < farms.size(); i++) {
            String[] farm = farms.get(i);
            if (farm[farm.length - 1].equals(farmData[farmData.length - 1])) { // Compare userId
                farms.set(i, farmData); // Update the farm data
                updated = true;
                break;
            }
        }

        if (!updated) {
            farms.add(farmData); // Add new farm data if no match found
        }

        writeFile(farmDataPath, farms, false); // Overwrite the file with updated data
    }


    public void saveCrops(String userId, List<String[]> crops) {
        List<String[]> allCrops = loadCrops();
        allCrops.removeIf(crop -> crop[0].equals(userId));
        allCrops.addAll(crops);
        writeCropsToFile(cropDataPath, allCrops);
    }

    public void saveStorage(String userId, List<String[]> storage) {
        List<String[]> allCrops = loadCrops();
        allCrops.removeIf(crop -> crop[0].equals(userId));
        allCrops.addAll(storage);
        writeCropsToFile(storageDataPath, allCrops);
    }

    public void deleteCrop(String[] cropData) {
        List<String[]> allCrops = loadCrops();
        allCrops.removeIf(crop -> crop[0].equals(cropData[0]) && crop[1].equals(cropData[1]) && crop[2].equals(cropData[2]));
        writeCropsToFile(cropDataPath, allCrops);
    }

    public void addToStorage(String[] storageData) {
        writeFile(storageDataPath, storageData);
    }

    public void deleteStorage(String[] storageData) {
        List<String[]> allStorage = loadStorage();
        allStorage.removeIf(data -> data[0].equals(storageData[0]) && data[1].equals(storageData[1]));
        writeCropsToFile(storageDataPath, allStorage);
    }

    public void updateStorage(String[] oldData, String[] newData) {
        List<String[]> storageData = loadStorage();
        storageData.removeIf(data -> data[0].equals(oldData[0]) && data[1].equals(oldData[1]));
        storageData.add(newData);
        writeCropsToFile(storageDataPath, storageData);
    }

    private void writeCropsToFile(String filePath, List<String[]> crops) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] crop : crops) {
                bw.write(String.join(",", crop));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String[]> readFile(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }



    private void writeFile(String filePath, String[] data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(String.join(",", data));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void saveReservations(List<String[]> reservations) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(reservationDataPath))) {
            for (String[] reservation : reservations) {
                bw.write(String.join(",", reservation));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public List<String> searchFarmsByName(String keyword) {
        List<String> results = new ArrayList<>();
        for (String[] farm : loadFarms()) {
            if (farm.length > 0 && farm[0].contains(keyword)) {
                results.add(farm[0]);
            }
        }
        return results;
    }

    public String farmNameToId(String farmName) {
        for (String[] farm : loadFarms()) {
            if (farm.length > 0 && farm[0].equals(farmName)) {
                return farm[3];
            }
        }
        return null;
    }

    public String getFarmNameById(String farmId) {
        for (String[] farm : loadFarms()) {
            if (farm.length > 0 && farm[3].equals(farmId)) {
                return farm[0]; // 농장 이름이 있는 인덱스로 수정
            }
        }
        return null;
    }

    public List<String> searchFarmsByCrop(String keyword) {
        List<String> results = new ArrayList<>();
        for (String[] farm : loadFarms()) {
            if (farm.length > 2) {
                String[] crops = farm[2].split(";");
                for (String crop : crops) {
                    if (crop.trim().contains(keyword)) {
                        results.add(farm[0]);
                        break;
                    }
                }
            }
        }
        return results;
    }


    public void updateCropQuantity(String userId, String cropName, int newQuantity) {
        List<String[]> crops = loadCrops();
        for (String[] crop : crops) {
            if (crop[0].equals(userId) && crop[1].equals(cropName)) {
                crop[3] = String.valueOf(newQuantity);
                break;
            }
        }
        saveCrops(userId, crops);
    }

    public void updateCropQuantityMinus(String farmId, String cropName, int quantityToCancel) {
        List<String[]> crops = loadCrops();
        for (String[] crop : crops) {
            if (crop[0].equals(farmId) && crop[1].equals(cropName)) {
                int currentQuantity = Integer.parseInt(crop[3]);
                int updatedQuantity = currentQuantity + quantityToCancel;
                crop[3] = String.valueOf(updatedQuantity);
                break;
            }
        }
        writeCropsToFile(cropDataPath, crops);
    }

    public void updateCropQuantityBuy(String userId, String cropName, int newQuantity) {
        List<String[]> crops = loadStorage();
        for (String[] crop : crops) {
            if (crop[0].equals(userId) && crop[1].equals(cropName)) {
                crop[2] = String.valueOf(newQuantity);
                break;
            }
        }
        saveStorage(userId, crops);
    }



}
