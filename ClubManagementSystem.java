import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
/**
 * 청주대학교 동아리 관리 시스템
 * <p>
 * 관리자, 학생, 동아리 회장을 위한 GUI 기반의 동아리 관리 시스템입니다.
 * </p>
 *
 * @author Ji Woon Kim
 * @version 1.0
 * @since 2024-12-18
 */
public class ClubManagementSystem {

    /**
     * 청주대학교 동아리 관리 시스템을 초기화합니다.
     * <p>
     * 파일에서 동아리 데이터를 로드하고 GUI를 시작합니다.
     * </p>
     *
     * @created 2024-12-18
     * @lastModified 2024-12-18
     *
     */

    public ClubManagementSystem() {
        this.clubManager = new ClubManager();
        loadClubsFromFile();
        new ClubManagementSystemGUI(clubManager);
    }

    public static void main(String[] args) {
        new ClubManagementSystem();
    }
    private void loadClubsFromFile() {
        String fileName = "src/clubs_data.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String name = parts[0].trim();
                    String president = parts[1].trim();
                    String description = parts[2].trim();
                    clubManager.addClub(new Club(name, president, description));
                }
            }
        } catch (IOException e) {
            System.out.println("[INFO] 초기 동아리 데이터를 로드할 수 없습니다: " + e.getMessage());
        }
    }
}