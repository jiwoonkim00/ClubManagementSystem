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
 * 청주대학교 동아리 관리 시스템의 메인 메뉴와 각 모드의 서브 메뉴를 제공하는 GUI 클래스입니다.
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
     * @created 2024-12-23
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
     * @lastModified 2024-12-20
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
     *  * <p>
     *  * 주요 변경 사항(2024-12-23):
     *  * <ul>
     *      <li>{@code JTextArea} 구성:
     *  *     <ul>
     *  *       <li>{@code setLineWrap(true)}: 줄바꿈 활성화</li>
     *  *       <li>{@code setWrapStyleWord(true)}: 단어 단위로 줄바꿈</li>
     *  *     </ul>
     *  *   <li><b>JTextArea 사용</b>:
     *  *     <p>가입 신청서 작성 시, {@code JTextArea(10, 30)}로 텍스트 입력 크기를 설정하였습니다.
     *  *     여기서 10은 줄 수, 30은 열 수를 의미하며, 사용자가 더 긴 텍스트를 입력할 수 있는 공간을 제공합니다.</p>
     *  *   </li>
     *  *   <li><b>JScrollPane로 스크롤 가능 설정</b>:
     *  *     <p>{@code JScrollPane}를 추가하여 텍스트가 길어질 경우 스크롤을 통해 내용을 확인할 수 있도록 하였습니다.</p>
     *  *   </li>
     *  *   <li><b>입력 확인</b>:
     *  *     <p>{@code JOptionPane.showConfirmDialog}를 사용하여 OK/Cancel 버튼이 있는 입력 창을 표시하였습니다.
     *  *     사용자가 입력 후 확인 버튼을 누르면 동아리 가입 신청서가 저장됩니다.</p>
     *  *   </li>
     *  * </ul>
     *  * </p>
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
            tableModel.addRow(new Object[]{club.getName(), club.getPresident(), club.getDescription()});
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane);
        frame.setVisible(true);
    }

    /**
     * 가입 신청서를 표로 표시합니다.
     * <p>
     * 이 메서드는 특정 동아리에 제출된 가입 신청서를 JTable로 표시하며,
     * 신청자의 이름과 작성한 신청서를 포함합니다.
     * 신청 승인 버튼 추가
     * </p>
     *
     * <p>
     * 레이아웃:
     * <ul>
     *   <li>JFrame: 가입 신청 목록을 표시하는 창</li>
     *   <li>JTable: 신청 정보를 표시하는 테이블</li>
     *   <li>JScrollPane: 테이블이 스크롤 가능하도록 설정</li>
     * </ul>
     * </p>
     *
     * @param parentFrame 부모 프레임
     * @param club 가입 신청 목록을 표시할 동아리 객체
     *
     * @created 2024-12-20
     * @lastModified 2024-12-23
     */
    private void displayApplicationTable(JFrame parentFrame, Club club) {
        JFrame frame = new JFrame("가입 신청 목록");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"이름", "신청 내용"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Member member : club.getPendingApplications()) {
            tableModel.addRow(new Object[]{member.getName(), member.getApplicationText()});
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

         /** 2024-12-23 추가
         * 가입 신청서를 승인하는 버튼과 관련된 이벤트 핸들러를 정의합니다.
         * <p>
         * 이 버튼은 가입 신청 목록(JTable)에서 선택된 신청서를 승인하며, 승인된 신청서를 동아리 대기 목록에서 제거합니다.
         * 또한, 승인된 신청서는 JTable에서도 제거되며, 성공적으로 처리된 경우 사용자에게 확인 메시지가 표시됩니다.
         * </p>
         *
         * <p>
         * 주요 동작:
         * <ul>
         *   <li>JTable에서 현재 선택된 행의 데이터를 확인합니다.</li>
         *   <li>선택된 신청자의 이름을 기반으로 {@link Club#approveApplication(String)}을 호출하여 신청서를 승인합니다.</li>
         *   <li>승인된 신청서를 JTable 및 동아리 대기 목록에서 제거합니다.</li>
         *   <li>처리 결과에 따라 사용자에게 메시지를 표시합니다.</li>
         * </ul>
         * </p>
         *
         * <p>
         * 예외 처리:
         * <ul>
         *   <li>선택된 행이 없는 경우, 사용자에게 "승인할 신청서를 선택하세요." 메시지를 표시합니다.</li>
         *   <li>신청서 승인이 실패한 경우, "승인할 신청서를 찾을 수 없습니다." 메시지를 표시합니다.</li>
         * </ul>
         * </p>
         *
         * <p>
         * 레이아웃 구성:
         * <ul>
         *   <li>중앙 영역: 신청 목록을 보여주는 {@link JScrollPane}</li>
         *   <li>하단 영역: "신청 승인" 버튼</li>
         * </ul>
         * </p>
         *
         * @created 2024-12-23
         * @lastModified 2024-12-23
         */
        JButton approveButton = new JButton("신청 승인");
        approveButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String memberName = table.getValueAt(selectedRow, 0).toString();
                Member approvedMember = club.approveApplication(memberName);
                if (approvedMember != null) {
                    tableModel.removeRow(selectedRow); // 테이블에서 승인된 신청 제거
                    JOptionPane.showMessageDialog(frame, "신청 승인 완료: " + approvedMember.getName());
                } else {
                    JOptionPane.showMessageDialog(frame, "승인할 신청서를 찾을 수 없습니다.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "승인할 신청서를 선택하세요.");
            }
        });

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(approveButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    /**
     * 동아리 데이터를 파일에 저장합니다.
     * <p>
     * 현재 등록된 모든 동아리 정보를 "clubs_data.txt" 파일에 저장합니다.
     * 각 동아리 정보는 CSV 형식으로 저장되며, 파일은 UTF-8 형식으로 작성됩니다.
     * </p>
     *
     * <p>
     * 파일 형식:
     * <ul>
     *   <li>파일 이름: "clubs_data.txt"</li>
     *   <li>형식: "동아리 이름,회장 이름,소개"</li>
     *   <li>줄바꿈: 각 동아리마다 한 줄</li>
     * </ul>
     * </p>
     *
     * <p>
     * 주요 동작:
     * <ul>
     *   <li>{@link ClubManager#getAllClubs()} 메서드를 호출하여 모든 동아리 정보를 가져옵니다.</li>
     *   <li>{@link BufferedWriter}를 사용하여 파일을 작성합니다.</li>
     *   <li>파일 쓰기 중 문제가 발생할 경우, 예외를 잡아 에러 메시지를 출력합니다.</li>
     * </ul>
     * </p>
     *
     * <p>
     * 예외 처리:
     * <ul>
     *   <li>IOException: 파일 쓰기 중 오류가 발생한 경우, 콘솔에 오류 메시지를 출력합니다.</li>
     * </ul>
     * </p>
     *
     * <p>
     * 참고: 이 메서드는 동기적으로 작동하며, 실행 중 파일이 잠길 수 있으므로 호출 전에 동시성 문제를 고려해야 합니다.
     * </p>
     *
     * @created 2024-12-20
     * @lastModified 2024-12-20
     */
    private void saveClubsToFile() {
        String fileName = "clubs_data.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Club club : clubManager.getAllClubs()) {
                bw.write(String.format("%s,%s,%s%n", club.getName(), club.getPresident(), club.getDescription()));
            }
        } catch (IOException e) {
            System.out.println("[ERROR] 동아리 데이터를 저장하는 중 오류가 발생했습니다: " + e.getMessage());
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
    /**
     * 2024-12-23 수정
     * 특정 이름의 가입 신청서를 승인합니다.
     *
     * @param name 승인할 회원의 이름
     * @return 승인된 {@link Member} 객체, 없으면 null 반환
     */
    public Member approveApplication(String name) {
        for (Member member : pendingApplications) {
            if (member.getName().equals(name)) {
                pendingApplications.remove(member); // 대기 목록에서 제거
                return member; // 승인된 회원 반환
            }
        }
        return null; // 신청서를 찾지 못한 경우
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

