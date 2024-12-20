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
    private ClubManager clubManager;
    /**
     * 청주대학교 동아리 관리 시스템을 초기화합니다.
     * <p>
     * 파일에서 동아리 데이터를 로드하고 GUI를 시작합니다.
     * </p>
     *
     * @created 2024-12-18
     * @lastModified 2024-12-19
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

    /**
     * 파일에서 동아리 데이터를 읽어옵니다.
     * <p>
     * 동아리 데이터는 "src/clubs_data.txt" 파일에서 ,(컴마)를 기준으로 로드되며,
     * 각 라인은 "동아리 이름,회장 이름,소개" 형식으로 작성되어야 합니다.
     * 파일이 없거나 읽기에 실패할 경우 에러 메시지를 출력합니다.
     * </p>
     *
     * @created 2024-12-18
     * @lastModified 2024-12-18
     */
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
 * GUI 관리 클래스
 * <p>
 * 청주대학교 동아리 관리 시스템의 메인 메뉴와 각 모드의 서브 메뉴를 제공하는 GUI 클래스입니다.
 * </p>
 *
 * @author Ji Woon Kim
 * @version 1.0
 * @since 2024-12-19
 */
class ClubManagementSystemGUI {
    private ClubManager clubManager;

    /**
     * {@link ClubManagementSystemGUI} 생성자.
     * <p>
     * 주어진 {@link ClubManager}를 사용하여 GUI를 초기화하고 메인 메뉴를 표시합니다.
     * </p>
     *
     * @param clubManager 동아리 데이터를 관리하는 {@link ClubManager} 인스턴스
     * @created 2024-12-19
     * @lastModified 2024-12-19
     */
    public ClubManagementSystemGUI(ClubManager clubManager) {
        this.clubManager = clubManager;
        showMainMenu();
    }

    /**
     * 메인 메뉴를 표시합니다.
     * <p>
     * 이 메서드는 프로그램의 첫 화면인 메인 메뉴를 생성하고 표시합니다.
     * 메인 메뉴에는 네 개의 주요 버튼이 포함되어 있으며, 각각 관리자 모드, 학생 모드, 동아리 회장 모드로의
     * 접근과 프로그램 종료 기능을 제공합니다. 버튼 클릭 시 해당 모드의 서브 메뉴로 이동하거나 프로그램을 종료합니다.
     * </p>
     *
     * <p>
     * 구성 요소:
     * <ul>
     *   <li>제목 라벨: "청주대학교 동아리 관리 시스템" 텍스트를 중앙에 굵은 글씨로 표시</li>
     *   <li>관리자 모드 버튼: 관리자 전용 기능(동아리 추가/삭제 등)에 접근</li>
     *   <li>학생 모드 버튼: 학생 전용 기능(동아리 목록 조회, 가입 신청 등)에 접근</li>
     *   <li>동아리 회장 모드 버튼: 가입 신청 관리 및 기타 회장 전용 기능에 접근</li>
     *   <li>종료 버튼: 프로그램을 종료</li>
     * </ul>
     * </p>
     *
     * <p>
     * 레이아웃:
     * <ul>
     *   <li>JFrame: 전체 메인 메뉴를 담는 창</li>
     *   <li>GridLayout: 메뉴 버튼들이 세로로 정렬되도록 설정</li>
     * </ul>
     * </p>
     *
     * <p>
     * 버튼 동작:
     * <ul>
     *   <li>관리자 모드 버튼: {@link #showAdminMenu()} 호출</li>
     *   <li>학생 모드 버튼: {@link #showStudentMenu()} 호출</li>
     *   <li>동아리 회장 모드 버튼: {@link #showPresidentMenu()} 호출</li>
     *   <li>종료 버튼: 프로그램 종료</li>
     * </ul>
     * </p>
     *
     * @created 2024-12-19
     * @lastModified 2024-12-19
     */
    private void showMainMenu() {
        JFrame frame = new JFrame("청주대학교 동아리 관리 시스템");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 1));

        JLabel label = new JLabel("청주대학교 동아리 관리 시스템", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        JButton adminButton = new JButton("관리자 모드");
        adminButton.addActionListener(e -> {
            frame.dispose();
            showAdminMenu();
        });

        JButton studentButton = new JButton("학생 모드");
        studentButton.addActionListener(e -> {
            frame.dispose();
            showStudentMenu();
        });

        JButton presidentButton = new JButton("동아리 회장 모드");
        presidentButton.addActionListener(e -> {
            frame.dispose();
            showPresidentMenu();
        });

        JButton exitButton = new JButton("종료");
        exitButton.addActionListener(e -> System.exit(0));

        frame.add(label);
        frame.add(adminButton);
        frame.add(studentButton);
        frame.add(presidentButton);
        frame.add(exitButton);

        frame.setVisible(true);
    }

    /**
     * 관리자 메뉴를 표시합니다.
     * <p>
     * 이 메서드는 관리자 전용 메뉴를 생성하고 표시합니다.
     * 관리자 메뉴는 동아리 관리 기능을 수행할 수 있는 네 개의 버튼으로 구성되어 있습니다.
     * 동아리를 추가하거나 삭제할 수 있으며, 등록된 동아리 목록을 조회하거나 메인 메뉴로 돌아갈 수 있습니다.
     * </p>
     *
     * <p>
     * 구성 요소:
     * <ul>
     *   <li>동아리 추가 버튼: 새로운 동아리를 추가할 수 있는 입력 폼을 표시</li>
     *   <li>동아리 삭제 버튼: 삭제할 동아리 이름을 입력받아 동아리를 삭제</li>
     *   <li>동아리 목록 조회 버튼: 등록된 모든 동아리를 표로 표시</li>
     *   <li>이전으로 돌아가기 버튼: 메인 메뉴로 돌아가기</li>
     * </ul>
     * </p>
     *
     * <p>
     * 레이아웃:
     * <ul>
     *   <li>JFrame: 전체 관리자 메뉴를 담는 창</li>
     *   <li>GridLayout: 버튼들이 세로로 정렬되도록 설정</li>
     * </ul>
     * </p>
     *
     * <p>
     * 버튼 동작:
     * <ul>
     *   <li>동아리 추가 버튼: {@link ClubManager#addClub(Club)} 호출</li>
     *   <li>동아리 삭제 버튼: {@link ClubManager#removeClub(String)} 호출</li>
     *   <li>동아리 목록 조회 버튼: {@link #displayClubTable(JFrame, List)} 호출</li>
     *   <li>이전으로 돌아가기 버튼: 메인 메뉴로 이동</li>
     * </ul>
     * </p>
     *
     * @created 2024-12-19
     * @lastModified 2024-12-19
     */
    private void showAdminMenu() {
        JFrame frame = new JFrame("관리자 모드");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 1));

        JButton addClubButton = new JButton("동아리 추가");
        addClubButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "동아리 이름:");
            if (name == null || name.trim().isEmpty()) return;

            String president = JOptionPane.showInputDialog(frame, "회장 이름:");
            if (president == null || president.trim().isEmpty()) return;

            String description = JOptionPane.showInputDialog(frame, "동아리 소개:");
            if (description == null || description.trim().isEmpty()) return;

            clubManager.addClub(new Club(name, president, description));
            saveClubsToFile();
            JOptionPane.showMessageDialog(frame, "동아리가 추가되었습니다.");
        });

        JButton removeClubButton = new JButton("동아리 삭제");
        removeClubButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "삭제할 동아리 이름:");
            if (name == null || name.trim().isEmpty()) return;

            if (clubManager.removeClub(name)) {
                saveClubsToFile();
                JOptionPane.showMessageDialog(frame, "동아리가 삭제되었습니다.");
            } else {
                JOptionPane.showMessageDialog(frame, "해당 동아리를 찾을 수 없습니다.");
            }
        });

        JButton listClubsButton = new JButton("동아리 목록 조회");
        listClubsButton.addActionListener(e -> displayClubTable(frame, clubManager.getAllClubs()));

        JButton backButton = new JButton("이전으로 돌아가기");
        backButton.addActionListener(e -> {
            frame.dispose();
            showMainMenu();
        });

        frame.add(addClubButton);
        frame.add(removeClubButton);
        frame.add(listClubsButton);
        frame.add(backButton);

        frame.setVisible(true);
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

