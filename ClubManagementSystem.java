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
        loadUsersFromFile(); // 사용자 정보 로드
        loadClubsFromFile();
        new ClubManagementSystemGUI(clubManager, users, roles).showMainMenu(); // 로그인 화면 표시
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
     * @lastModified 2024-12-21
     */
    private void loadClubsFromFile() {
        String fileName = "clubs_data.txt";
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

    private Map<String, String> users = new HashMap<>(); // 아이디-비밀번호 저장
    private Map<String, String> roles = new HashMap<>(); // 아이디-역할 저장

    /**
     * 파일에서 사용자 정보를 로드합니다.
     * <p>
     * 파일 포맷: 아이디,비밀번호,역할
     * 예: admin,admin123,관리자
     * </p>
     *
     * @created 2024-12-23
     */
    private void loadUsersFromFile() {
        String fileName = "users.txt"; // 사용자 정보 파일 경로
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String id = parts[0].trim();
                    String password = parts[1].trim();
                    String role = parts[2].trim();
                    users.put(id, password);
                    roles.put(id, role);
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] 사용자 데이터를 로드할 수 없습니다: " + e.getMessage());
        }
    }
}

/**
 * GUI 관리 클래스
 * <p>
 * 청주대학교 동아리 관리 시스템의 메인 메뉴와 각 역할(관리자, 학생, 동아리 회장)에 따른 서브 메뉴를 제공하며,
 * 사용자 로그인 기능도 포함됩니다.
 * </p>
 *
 * <p>
 * 주요 기능:
 * <ul>
 *   <li>메인 메뉴 표시: {@link #showMainMenu()}</li>
 *   <li>로그인 창 표시: {@link #showLoginScreen(JFrame, String)}</li>
 *   <li>관리자, 학생, 동아리 회장 메뉴 표시</li>
 * </ul>
 * </p>
 *
 * @author Ji Woon Kim
 * @version 1.0
 * @since 2024-12-19
 */
class ClubManagementSystemGUI {
    private Map<String, String> users; // 사용자 ID와 비밀번호 저장
    private Map<String, String> roles; // 사용자 ID와 역할 저장

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
    public ClubManagementSystemGUI(ClubManager clubManager, Map<String, String> users, Map<String, String> roles) {
        this.clubManager = clubManager;
        this.users = users;
        this.roles = roles;
    }


    /**
     * 사용자 데이터를 파일에서 로드합니다.
     * <p>
     * 각 줄은 "ID,비밀번호,역할" 형식으로 작성되어야 합니다.
     * </p>
     *
     * @param filePath 사용자 데이터 파일 경로
     */
    private void loadUsersFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String id = parts[0].trim();
                    String password = parts[1].trim();
                    String role = parts[2].trim();

                    users.put(id, password);
                    roles.put(id, role);
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] 사용자 데이터를 로드할 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 로그인 화면을 표시합니다.
     * <p>
     * 사용자 아이디와 비밀번호를 입력받아 역할을 검증합니다.
     * 성공 시 역할에 따라 다른 메뉴를 표시합니다.
     * </p>
     *
     * <p>
     * 구성 요소:
     * <ul>
     *   <li>아이디 입력 필드</li>
     *   <li>비밀번호 입력 필드</li>
     *   <li>로그인 버튼</li>
     * </ul>
     * </p>
     *
     *  <p>
     *  동작 방식:
     *  <ul>
     *    <li>로그인 성공: {@link #showAdminMenu()}, {@link #showStudentMenu()}, {@link #showPresidentMenu()} 호출</li>
     *    <li>로그인 실패: 경고 메시지 표시</li>
     *    <li>닫기 버튼: 창만 닫히고 프로그램은 계속 실행</li>
     *  </ul>
     *  </p>
     *
     *  @param parentFrame 호출한 부모 JFrame (메인 메뉴)
     *  @param role 접근하려는 역할 ("관리자", "학생", "동아리 회장")
     * @created 2024-12-23
     * @lastModified 2024-12-23
     */


    public void showLoginScreen(JFrame parentFrame, String role) {

        JFrame frame = new JFrame("로그인");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));

        JLabel idLabel = new JLabel("아이디:");
        JTextField idField = new JTextField();

        JLabel passwordLabel = new JLabel("비밀번호:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(e -> {
            String id = idField.getText();
            String password = new String(passwordField.getPassword());

            // 사용자 검증
            if (users.containsKey(id) && users.get(id).equals(password)) {
                String userRole = roles.get(id);
                if (userRole != null && userRole.equals(role)) {
                    frame.dispose(); // 로그인 창 닫기
                    parentFrame.dispose(); // 메인 메뉴 닫기

                    // 역할에 따라 메뉴로 이동
                    switch (role) {
                        case "관리자":
                            showAdminMenu();
                            break;
                        case "학생":
                            showStudentMenu();
                            break;
                        case "동아리 회장":
                            showPresidentMenu();
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "해당 역할로 접근할 권한이 없습니다.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "로그인 정보가 잘못되었습니다.");
            }
        });

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창 닫기 시 프로그램 종료하지 않음
        frame.add(idLabel);
        frame.add(idField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);


        frame.setVisible(true);
    }

    /**
     * 메인 메뉴를 표시합니다.
     * <p>
     * 이 메서드는 프로그램의 첫 화면인 메인 메뉴를 생성하고 표시합니다.
     * 메인 메뉴에서는 관리자, 학생, 동아리 회장 역할에 따라 로그인 창을 표시하며, 프로그램 종료 버튼도 포함됩니다.
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
     *   <li>관리자 모드 버튼: {@link #showLoginScreen(JFrame, String)} 호출</li>
     *   <li>학생 모드 버튼: {@link #showLoginScreen(JFrame, String)} 호출</li>
     *   <li>동아리 회장 모드 버튼: {@link #showLoginScreen(JFrame, String)} 호출</li>
     *   <li>종료 버튼: 프로그램 종료</li>
     * </ul>
     * </p>
     *
     * @created 2024-12-19
     * @lastModified 2024-12-23
     */
    public void showMainMenu() {
        JFrame frame = new JFrame("청주대학교 동아리 관리 시스템");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 1));

        JLabel label = new JLabel("청주대학교 동아리 관리 시스템", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        // 관리자 모드 버튼
        JButton adminButton = new JButton("관리자 모드");
        adminButton.addActionListener(e -> {
            showLoginScreen(frame, "관리자");
        });

        // 학생 모드 버튼
        JButton studentButton = new JButton("학생 모드");
        studentButton.addActionListener(e -> {
            showLoginScreen(frame, "학생");
        });

        // 동아리 회장 모드 버튼
        JButton presidentButton = new JButton("동아리 회장 모드");
        presidentButton.addActionListener(e -> {
            showLoginScreen(frame, "동아리 회장");
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
     * @lastModified 2024-12-20
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
     * 학생 메뉴를 표시합니다.
     * <p>
     * 이 메서드는 학생 전용 메뉴를 생성하고 표시합니다.
     * 학생 메뉴는 동아리 조회 및 동아리 가입 신청 기능을 제공합니다.
     * 학생은 동아리 목록을 확인하거나 특정 동아리에 가입 신청서를 제출할 수 있습니다.
     * </p>
     *
     *  <p>
     *  주요 변경 사항(2024-12-23):
     *  <ul>
     *    <li>{@code JTextArea} 구성:
     *      <ul>
     *        <li>{@code setLineWrap(true)}: 줄바꿈 활성화</li>
     *        <li>{@code setWrapStyleWord(true)}: 단어 단위로 줄바꿈</li>
     *      </ul>
     *    <li><b>JTextArea 사용</b>:
     *      <p>가입 신청서 작성 시, {@code JTextArea(10, 30)}로 텍스트 입력 크기를 설정하였습니다.
     *      여기서 10은 줄 수, 30은 열 수를 의미하며, 사용자가 더 긴 텍스트를 입력할 수 있는 공간을 제공합니다.</p>
     *    </li>
     *    <li><b>JScrollPane로 스크롤 가능 설정</b>:
     *      <p>{@code JScrollPane}를 추가하여 텍스트가 길어질 경우 스크롤을 통해 내용을 확인할 수 있도록 하였습니다.</p>
     *    </li>
     *    <li><b>입력 확인</b>:
     *      <p>{@code JOptionPane.showConfirmDialog}를 사용하여 OK/Cancel 버튼이 있는 입력 창을 표시하였습니다.
     *      사용자가 입력 후 확인 버튼을 누르면 동아리 가입 신청서가 저장됩니다.</p>
     *    </li>
     *  </ul>
     *  </p>
     * <p>
     * 구성 요소:
     * <ul>
     *   <li>동아리 목록 조회 버튼: 등록된 모든 동아리를 표로 확인</li>
     *   <li>동아리 가입 신청 버튼: 동아리 이름과 가입 신청서를 작성 후 제출</li>
     *   <li>이전으로 돌아가기 버튼: 메인 메뉴로 돌아가기</li>
     * </ul>
     * </p>
     *
     * <p>
     * 레이아웃:
     * <ul>
     *   <li>JFrame: 학생 메뉴를 담는 창</li>
     *   <li>GridLayout: 버튼들이 세로로 정렬되도록 설정</li>
     * </ul>
     * </p>
     *
     * <p>
     * 버튼 동작:
     * <ul>
     *   <li>동아리 목록 조회 버튼: {@link #displayClubTable(JFrame, List)} 호출</li>
     *   <li>동아리 가입 신청 버튼: {@link Club#addPendingApplication(Member)} 호출</li>
     *   <li>이전으로 돌아가기 버튼: 메인 메뉴로 이동</li>
     * </ul>
     * </p>
     *
     * @created 2024-12-20
     * @lastModified 2024-12-23
     */
    private void showStudentMenu() {
        JFrame frame = new JFrame("학생 모드");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(3, 1));

        JButton listClubsButton = new JButton("동아리 목록 조회");
        listClubsButton.addActionListener(e -> displayClubTable(frame, clubManager.getAllClubs()));

        JButton joinClubButton = new JButton("동아리 가입 신청");
        joinClubButton.addActionListener(e -> {
            String studentName = JOptionPane.showInputDialog(frame, "학생 이름:");
            if (studentName == null || studentName.trim().isEmpty()) return;

            String clubName = JOptionPane.showInputDialog(frame, "가입할 동아리 이름:");
            if (clubName == null || clubName.trim().isEmpty()) return;

            Club club = clubManager.getClub(clubName);
            if (club != null) {
                JTextArea applicationTextArea = new JTextArea(10, 30); // 텍스트 박스 크기 설정 (행 x 열)
                applicationTextArea.setLineWrap(true); // 줄바꿈을 활성화
                applicationTextArea.setWrapStyleWord(true); // 단어 단위로 줄바꿈
                JScrollPane scrollPane = new JScrollPane(applicationTextArea); // 스크롤 가능하도록 설정

                int result = JOptionPane.showConfirmDialog(
                        frame,
                        scrollPane,
                        "가입 신청서 작성",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    String applicationText = applicationTextArea.getText().trim();
                    if (!applicationText.isEmpty()) {
                        club.addPendingApplication(new Member(studentName, applicationText));
                        JOptionPane.showMessageDialog(frame, "가입 신청이 제출되었습니다.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "가입 신청서가 비어 있습니다. 다시 작성해주세요.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "해당 동아리가 존재하지 않습니다.");
            }
        });

        JButton backButton = new JButton("이전으로 돌아가기");
        backButton.addActionListener(e -> {
            frame.dispose();
            showMainMenu();
        });

        frame.add(listClubsButton);
        frame.add(joinClubButton);
        frame.add(backButton);

        frame.setVisible(true);
    }


    /**
     * 동아리 회장 메뉴를 표시합니다.
     * <p>
     * 이 메서드는 동아리 회장 전용 메뉴를 생성하고 표시합니다.
     * 회장은 동아리에 제출된 가입 신청서를 확인하고 승인할 수 있습니다.
     * </p>
     *
     * <p>
     * 구성 요소:
     * <ul>
     *   <li>가입 신청서 확인 및 승인 버튼: 특정 동아리에 제출된 가입 신청서를 조회 및 승인</li>
     *   <li>이전으로 돌아가기 버튼: 메인 메뉴로 돌아가기</li>
     * </ul>
     * </p>
     *
     * <p>
     * 레이아웃:
     * <ul>
     *   <li>JFrame: 동아리 회장 메뉴를 담는 창</li>
     *   <li>GridLayout: 버튼들이 세로로 정렬되도록 설정</li>
     * </ul>
     * </p>
     *
     * <p>
     * 버튼 동작:
     * <ul>
     *   <li>가입 신청서 확인 및 승인 버튼: {@link #displayApplicationTable(JFrame, Club)} 호출</li>
     *   <li>이전으로 돌아가기 버튼: 메인 메뉴로 이동</li>
     * </ul>
     * </p>
     *
     * @created 2024-12-20
     * @lastModified 2024-12-20
     */
    private void showPresidentMenu() {
        JFrame frame = new JFrame("동아리 회장 모드");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(2, 1));

        JButton viewApplicationsButton = new JButton("가입 신청서 확인 및 승인");
        viewApplicationsButton.addActionListener(e -> {
            String clubName = JOptionPane.showInputDialog(frame, "동아리 이름:");
            if (clubName == null || clubName.trim().isEmpty()) return;

            Club club = clubManager.getClub(clubName);
            if (club != null) {
                displayApplicationTable(frame, club);
            } else {
                JOptionPane.showMessageDialog(frame, "해당 동아리가 존재하지 않습니다.");
            }
        });

        JButton backButton = new JButton("이전으로 돌아가기");
        backButton.addActionListener(e -> {
            frame.dispose();
            showMainMenu();
        });

        frame.add(viewApplicationsButton);
        frame.add(backButton);

        frame.setVisible(true);
    }


    /**
     * 동아리 목록을 표로 표시합니다.
     * <p>
     * 이 메서드는 등록된 동아리 목록을 JTable로 표시하며, 동아리 이름, 회장, 설명 정보를 포함합니다.
     * </p>
     *
     * <p>
     * 레이아웃:
     * <ul>
     *   <li>JFrame: 동아리 목록을 표시하는 창</li>
     *   <li>JTable: 동아리 정보를 표시하는 테이블</li>
     *   <li>JScrollPane: 테이블이 스크롤 가능하도록 설정</li>
     * </ul>
     * </p>
     *
     * @param parentFrame 부모 프레임
     * @param clubs 표시할 동아리 목록
     *
     * @created 2024-12-20
     * @lastModified 2024-12-20
     */
    private void displayClubTable(JFrame parentFrame, List<Club> clubs) {
        JFrame frame = new JFrame("동아리 목록");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"동아리 이름", "회장", "소개"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Club club : clubs) {
            tableModel.addRow(new Object[]{club.getName(), club