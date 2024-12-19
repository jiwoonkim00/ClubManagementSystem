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
    /**
     * 프로그램의 메인 진입점입니다.
     * <p>
     * {@link ClubManagementSystem} 인스턴스를 생성하여 실행합니다.
     * </p>
     *
     * @param args 명령줄 인자 (사용하지 않음)
     *
     */
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

/**
 * 동아리를 나타내는 클래스
 * <p>
 * 동아리 이름, 회장, 설명, 가입 신청서를 관리합니다.
 * </p>
 *
 * @author Ji Woon Kim
 * @version 1.0
 * @since 2024-12-18
 */
class Club {
    private String name;
    private String president;
    private String description;
    private List<Member> pendingApplications;

    /**
     * {@link Club} 객체를 초기화합니다.
     *
     * @param name 동아리 이름
     * @param president 동아리 회장 이름
     * @param description 동아리 설명
     * @created 2024-12-18
     * @lastModified 2024-12-18
     */
    public Club(String name, String president, String description) {
        this.name = name;
        this.president = president;
        this.description = description;
        this.pendingApplications = new ArrayList<>();
    }

    /**
     * 동아리 이름을 반환합니다.
     *
     * @return 동아리 이름
     */
    public String getName() {
        return name;
    }

    /**
     * 동아리 회장의 이름을 반환합니다.
     *
     * @return 동아리 회장 이름
     */
    public String getPresident() {
        return president;
    }

    /**
     * 동아리 설명을 반환합니다.
     *
     * @return 동아리 설명
     */
    public String getDescription() {
        return description;
    }

    /**
     * 가입 신청서를 추가합니다.
     *
     * @param member 가입 신청을 한 회원 객체
     */
    public void addPendingApplication(Member member) {
        pendingApplications.add(member);
    }

    /**
     * 가입 신청 목록을 반환합니다.
     *
     * @return 가입 신청 중인 {@link Member} 객체의 리스트
     */
    public List<Member> getPendingApplications() {
        return pendingApplications;
    }
}

/**
 * 동아리 회원을 나타내는 클래스
 * <p>
 * 회원 이름과 가입 신청서를 포함합니다.
 * </p>
 *
 * @author Ji Woon Kim
 * @version 1.0
 * @since 2024-12-18
 */
class Member {
    private String name;
    private String applicationText;

    /**
     * {@link Member} 객체를 초기화합니다.
     *
     * @param name 회원 이름
     * @param applicationText 가입 신청서 내용
     * @created 2024-12-18
     * @lastModified 2024-12-18
     */
    public Member(String name, String applicationText) {
        this.name = name;
        this.applicationText = applicationText;
    }

    /**
     * 회원 이름을 반환합니다.
     *
     * @return 회원 이름
     */
    public String getName() {
        return name;
    }

    /**
     * 회원이 작성한 가입 신청서 내용을 반환합니다.
     *
     * @return 가입 신청서 내용
     */
    public String getApplicationText() {
        return applicationText;
    }
}

/**
 * 동아리 관리를 담당하는 클래스
 * <p>
 * 동아리의 추가, 삭제, 검색 및 목록 관리를 제공합니다.
 * </p>
 *
 * @author Ji Woo Park
 * @version 1.0
 * @since 2024-12-18
 */
class ClubManager {
    private Map<String, Club> clubs;

    /**
     * {@link ClubManager} 객체를 초기화합니다.
     * <p>
     * 내부적으로 빈 동아리 맵을 생성합니다.
     * </p>
     *
     * @created 2024-12-18
     * @lastModified 2024-12-18
     */
    public ClubManager() {
        this.clubs = new HashMap<>();
    }

    /**
     * 새로운 동아리를 추가합니다.
     * <p>
     * 동아리 이름을 키로 사용하여 내부 맵에 저장합니다.
     * </p>
     *
     * @param club 추가할 동아리 객체
     */
    public void addClub(Club club) {
        clubs.put(club.getName(), club);
    }

    /**
     * 동아리를 삭제합니다.
     *
     * @param name 삭제할 동아리의 이름
     * @return 삭제가 성공하면 true, 아니면 false
     */
    public boolean removeClub(String name) {
        return clubs.remove(name) != null;
    }

    /**
     * 동아리를 이름으로 검색합니다.
     *
     * @param name 검색할 동아리의 이름
     * @return 해당 이름의 동아리 객체. 없으면 null 반환
     */
    public Club getClub(String name) {
        return clubs.get(name);
    }

    /**
     * 모든 동아리 목록을 반환합니다.
     *
     * @return {@link Club} 객체의 리스트
     */
    public List<Club> getAllClubs() {
        return new ArrayList<>(clubs.values());
    }
}