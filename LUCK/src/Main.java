import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


// 게임의 첫 번째 화면 표시
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartScreen();
        });
    }
}

// StartScreen.java
class StartScreen extends JFrame {
    private JButton startButton;
    
    private ArrayList<String> playerNumbers = new ArrayList<>();
    

    public StartScreen() {
        setTitle("행운모험");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 제목 라벨
        JLabel titleLabel = new JLabel("행운 모험", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은고딕", Font.BOLD, 24));

        // Start 버튼
        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 20));
        startButton.addActionListener(e -> {
            dispose(); // 현재 화면 닫기
            SwingUtilities.invokeLater(() -> new StoryScreen()); // 게임 스토리 화면으로 이동
        });

        // 레이아웃 설정
        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
        setVisible(true);
    }
}

// StoryScreen.java
class StoryScreen extends JFrame {
    private JButton nextButton;

    public StoryScreen() {
        setTitle("게임 스토리");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 게임 스토리 텍스트
        JLabel storyLabel = new JLabel("<html><div style='font-size:15px; font-family: 맑은고딕;'>"
                + "<p>대학 졸업 후 취업준비만 <strong>N년</strong> 째,</p>"
                + "<p>현실의 차가운 벽에 가로막혀 좌절하고 있는 <strong>이수뭉</strong>.</p>"
                + "<br>"
                + "<p>면접은 커녕, 서류단계에서 전부 <strong>광탈</strong>...</p>"
                + "<p>집안에서도 <strong>금쪽이</strong> 취급, 스스로가 점점 초라해지기만 한다.</p>"
                + "<br>"
                + "<p>눈칫밥을 견디지 못하고 집을 나와 떠돌던 어느 날,</p>"
                + "<p>새똥 맞기, 개똥 밟기, 잘나가는 친구를 우연히 만나기 ...</p>"
                + "<p>온갖 불운한 상황이 연속된다.</p>"
                + "<br>"
                + "<p><em>\"<strong> 내 인생에 행운이 없다면,</em></p>"
                + "<p><em><strong>직접 만들어내고야 말겠어 !</strong> \"</em></p>"
                + "<br>"
                + "<p>결심한 <strong>이수뭉</strong>은 운에 좋다는 모든 행동을 하며 노력한다.</p>"
                + "<p>네잎클로버 찾기, 돼지꿈 꾸기 ···</p>"
                + "<br>"
                + "<p>수뭉이는 과연 <strong>로또 당첨</strong>의 꿈을 이룰 수 있을까?</p>"
                + "<p>혹은 ··· ?</p>"
                + "</div></html>", JLabel.CENTER);
        storyLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        // Next 버튼
        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.PLAIN, 20));
        nextButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new StageSelectScreen()); // 스테이지 선택 화면으로 이동
        });

        // 레이아웃 설정
        setLayout(new BorderLayout());
        add(storyLabel, BorderLayout.CENTER);
        add(nextButton, BorderLayout.SOUTH); // Next 버튼만 하단에 추가
        setVisible(true);
    }
}

//StageSelectScreen.java
class StageSelectScreen extends JFrame {
	private int completedStages = 0; // 완료된 스테이지 수 추적
	private JButton[] stageButtons;
    private JLabel[] stageNumbers;  // 각 스테이지에 랜덤 번호를 표시할 라벨 추가
    private Set<Integer> usedNumbers;  // 이미 사용된 번호를 추적하기 위한 Set

    public StageSelectScreen() {
        setTitle("스테이지 선택");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
     // 랜덤 번호 중복 방지를 위한 Set 초기화
        usedNumbers = new HashSet<>();

        // 스테이지 버튼들
        stageButtons = new JButton[6];
        stageNumbers = new JLabel[6]; // 각 스테이지에 랜덤 번호를 표시할 라벨 추가
        JPanel panel = new JPanel(new GridLayout(7, 1));  // 한 줄에 하나씩 표시되게 설정

        for (int i = 0; i < 6; i++) {
            // 스테이지 버튼 생성
            stageButtons[i] = new JButton("Stage " + (i + 1));
            stageNumbers[i] = new JLabel("");  // 각 스테이지에 대한 번호를 표시할 라벨 초기화
            JPanel stagePanel = new JPanel();  // 버튼과 번호 라벨을 하나의 패널에 포함시킴

            final int stageIndex = i;  // 람다식에서 사용할 변수를 final로 선언

            // 각 스테이지 버튼에 클릭 이벤트 추가
            stageButtons[i].addActionListener(e -> {
                // 스테이지 인덱스에 따라 실행할 게임을 결정
                switch (stageIndex) {
                    case 0: // 1번째 스테이지
                        SwingUtilities.invokeLater(() -> {
                        	 completedStages++;
                        	 JFrame cardFrame = new JFrame("네잎클로버 찾기");
                             cardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                             cardFrame.setSize(600, 600);
                             Card cardPanel = new Card(); // 게임 패널
                             cardFrame.add(cardPanel);
                             cardFrame.setVisible(true);
                            
                          // 카드 게임이 끝난 후 gameCleared 상태 확인
                             cardFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                                 public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                                     if (cardPanel.isGameCleared()) {
                                    	 
                                         String randomNumber = generateRandomNumber();
                                         stageNumbers[stageIndex].setText("번호: " + randomNumber);
                                     }
                                 }
                             });
                         });
                         break;

                    case 1:
                        SwingUtilities.invokeLater(() -> {
                        	 completedStages++;
                            JFrame cloverFrame = new JFrame("별똥별에 소원빌기");
                            cloverFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            cloverFrame.setSize(1820, 800);
                            Clover cloverPanel = new Clover();
                            cloverFrame.add(cloverPanel);
                            cloverFrame.setVisible(true);

                         // 카드 게임이 끝난 후 gameCleared 상태 확인
                            cloverFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                                    if (cloverPanel.isGameCleared()) {
                                        String randomNumber = generateRandomNumber();
                                        stageNumbers[stageIndex].setText("번호: " + randomNumber);
                                    }
                                }
                            });
                        });
                        break;

                    case 2: // 3번째 스테이지
                        SwingUtilities.invokeLater(() -> {
                        	 completedStages++;
                            JFrame puzzleFrame = new JFrame("행운의 편지");
                            puzzleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            puzzleFrame.setSize(900, 900); // 화면 크기 조정
                            Puzzle puzzlePanel = new Puzzle();
                            puzzleFrame.add(puzzlePanel); // Puzzle 클래스의 게임 화면을 추가
                            puzzleFrame.setVisible(true); // 게임 화면을 띄움

                         // 카드 게임이 끝난 후 gameCleared 상태 확인
                            puzzleFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                                    if (puzzlePanel.isGameCleared()) {
                                        String randomNumber = generateRandomNumber();
                                        stageNumbers[stageIndex].setText("번호: " + randomNumber);
                                    }
                                }
                            });
                        });
                        break;

                    case 3: // 4번째 스테이지
                        SwingUtilities.invokeLater(() -> {
                        	 completedStages++;
                            JFrame bonusFrame = new JFrame("행운의 고양이");
                            bonusFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            bonusFrame.setSize(800, 600); // 화면 크기 조정
                            
                         // startCoinGame이 Cat 객체를 반환한다고 가정
                            Cat bonusPanel = new Cat().startCoinGame(bonusFrame);  // bonusFrame을 전달하고, Cat 객체를 반환 받음
                            bonusFrame.add(bonusPanel); // Puzzle 클래스의 게임 화면을 추가
                            bonusFrame.setVisible(true); // 게임 화면을 띄움

                            // 카드 게임이 끝난 후 gameCleared 상태 확인
                            bonusFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                                    if (bonusPanel.isGameCleared()) {
                                        String randomNumber = generateRandomNumber();
                                        stageNumbers[stageIndex].setText("번호: " + randomNumber);
                                    }
                                }
                            });
                        });
                        break;

                    case 4: // 5번째 스테이지
                        SwingUtilities.invokeLater(() -> {
                        	 completedStages++;
                            // 새로운 JFrame 생성
                            JFrame pigFrame = new JFrame("돼지 꿈 꾸기");
                            pigFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            pigFrame.setSize(1000, 370);

                            // PigDreams 패널을 생성하여 JFrame에 추가
                            PigDreams pigPanel = new PigDreams();
                            pigFrame.add(pigPanel);  // JFrame에 PigDreams 패널 추가

                            // JFrame을 화면에 표시
                            pigFrame.setVisible(true);

                            // 카드 게임이 끝난 후 gameCleared 상태 확인
                            pigFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                                    // gameCleared 상태 확인
                                    if (pigPanel.isGameCleared()) {
                                        String randomNumber = generateRandomNumber();
                                        stageNumbers[stageIndex].setText("번호: " + randomNumber);
                                    }
                                }
                            });
                        });
                        break;



                    case 5: // 6번째 스테이지
                        SwingUtilities.invokeLater(() -> {
                        	 completedStages++;
                            JFrame arrowFrame = new JFrame("로또명당찾기");
                            arrowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            arrowFrame.setSize(1000, 900); // 화면 크기 조정
                            Arrow arrowPanel = new Arrow(); // Arrow 클래스의 게임 화면을 추가
                            arrowFrame.add(arrowPanel); 

                            // 게임 화면을 띄움
                            arrowFrame.setVisible(true);

                            // 카드 게임이 끝난 후 gameCleared 상태 확인
                            arrowFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                                    if (arrowPanel.isGameCleared()) {
                                        String randomNumber = generateRandomNumber(); // 랜덤 번호 생성
                                        stageNumbers[stageIndex].setText("번호: " + randomNumber);
                                        checkAllStagesCompleted();// 랜덤 번호 표시
                                    }
                                }
                            });
                        });
                        break;

                }

                // 스테이지가 완료되었을 경우, 버튼 비활성화 및 다음 스테이지 활성화
                stageButtons[stageIndex].setEnabled(false);
                
                
                // 다음 스테이지 활성화 (5번까지)
                if (stageIndex < 5) {
                    stageButtons[stageIndex + 1].setEnabled(true);
                }
            });

            // 처음에는 첫 번째 버튼만 활성화되고 나머지는 비활성화
            if (i > 0) {
                stageButtons[i].setEnabled(false);  // 첫 번째 버튼 외의 모든 버튼 비활성화
            }

         // 버튼과 번호 라벨을 하나의 패널에 추가
            stagePanel.add(stageButtons[i]);
            stagePanel.add(stageNumbers[i]);
            panel.add(stagePanel);  // 해당 패널을 전체 레이아웃에 추가
        }

        // 첫 번째 스테이지 버튼만 활성화
        stageButtons[0].setEnabled(true);

        // 레이아웃 설정
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
    
 // 랜덤 번호 생성 함수
    private String generateRandomNumber() {
        Random rand = new Random();
        int randomNum;
        // 중복되지 않도록 랜덤 번호 생성
        do {
            randomNum = rand.nextInt(45) + 1;  // 1~45 중 랜덤 번호 생성
        } while (usedNumbers.contains(randomNum));  // 이미 사용된 번호는 다시 생성하지 않음
        usedNumbers.add(randomNum);  // 사용된 번호로 추가
        return String.valueOf(randomNum);  // 번호를 문자열로 반환
    }
    
    private void checkAllStagesCompleted() {
        if (completedStages == 6) {
            // 모든 스테이지가 완료되었을 때 로또 결과 표시
            SwingUtilities.invokeLater(() -> displayLottoResult());
        }
    }
    
    
 // StageSelectScreen.java
    private void displayLottoResult() {
    	 ArrayList<String> sortedNumbers = new ArrayList<>(usedNumbers.stream().map(String::valueOf).toList());
    	    Collections.sort(sortedNumbers);
        String message = "축하합니다! \n";

        if (usedNumbers.size() == 6) {
            message += "1등 당첨! 번호: " + String.join(", ", usedNumbers.stream().map(String::valueOf).toList());
        } else {
            message += "로또 1등의 꿈은 물건너갔군요. 다시 도전해볼까요?";
        }

        JFrame resultFrame = new JFrame("로또 결과");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setSize(500, 300);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());

        JLabel resultLabel = new JLabel("<html><div style='font-size: 20px; font-family: Arial;'>" + message + "</div></html>", JLabel.CENTER);
        resultPanel.add(resultLabel, BorderLayout.CENTER);

        JButton restartButton = new JButton("다시 도전");
        restartButton.addActionListener(e -> {
            // 게임을 처음부터 다시 시작할 수 있는 버튼 추가
            usedNumbers.clear();
            // 다른 초기화 코드 추가 (예: 스테이지 다시 활성화)
            dispose();
            SwingUtilities.invokeLater(() -> new StartScreen());
        });
        resultPanel.add(restartButton, BorderLayout.SOUTH);

        resultFrame.add(resultPanel);
        resultFrame.setVisible(true);
    }

}

