import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Card extends JPanel implements ActionListener {
    private boolean gameCleared = false;  // 게임 클리어 상태 변수 추가
    static JPanel panelNorth;
    static JPanel panelCenter;
    static JLabel labelMessage;
    static JButton[] buttons = new JButton[16]; 
    static String[] images = { 
        "fruit01.png","fruit02.png","fruit03.png","fruit04.png",
        "fruit05.png","fruit06.png","fruit07.png","fruit08.png",
        "fruit01.png","fruit02.png","fruit03.png","fruit04.png",
        "fruit05.png","fruit06.png","fruit07.png","fruit08.png",
    };
    static int openCount = 0; // 열린카드 인덱스
    static int buttonIndexSave1 = 0; // 첫번째 카드 인덱스
    static int buttonIndexSave2 = 0; // 두번째 카드 인덱스
    static Timer timer;
    static int tryCount = 15; // 시도횟수
    static int successCount = 0; // 빙고 카운트
    
    // 이미지 변경 메소드
    static ImageIcon changeImage(String filename) {
        ImageIcon icon = new ImageIcon("./img/" + filename);
        Image originImage = icon.getImage();
        Image changedImage = originImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon icon_new = new ImageIcon(changedImage);
        return icon_new;
    }

    // 카드 섞기
    public void mixCard() {
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) { // 1000번 섞기
            int random = rand.nextInt(15) + 1; // 1~15까지만
            String temp = images[0];
            images[0] = images[random];
            images[random] = temp;
        }
    }

    public Card() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 500));

        mixCard(); // 카드 섞기
        
        // 상단 패널 (메시지)
        panelNorth = new JPanel();
        panelNorth.setPreferredSize(new Dimension(500, 100));
        panelNorth.setBackground(Color.BLUE);
        labelMessage = new JLabel("같은 네잎클로버를 찾아보자! 남은 횟수: 15번");
        labelMessage.setPreferredSize(new Dimension(500, 100));
        labelMessage.setForeground(Color.WHITE);
        labelMessage.setFont(new Font("맑은고딕", Font.BOLD, 20));
        labelMessage.setHorizontalAlignment(JLabel.CENTER);
        panelNorth.add(labelMessage);
        this.add(panelNorth, BorderLayout.NORTH);
        
        // 카드 버튼 생성
        panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayout(4, 4));
        panelCenter.setPreferredSize(new Dimension(400, 400));
        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(100, 100));
            buttons[i].setIcon(changeImage("question.png")); // 초기 이미지
            buttons[i].addActionListener(this);
            panelCenter.add(buttons[i]);
        }
        this.add(panelCenter, BorderLayout.CENTER);
    }

    // 버튼 인덱스 구하기
    public int getButtonIndex(JButton btn) {
        int index = 0;
        for (int i = 0; i < 16; i++) {
            if (buttons[i] == btn) { // 주소값 비교
                index = i;
            }
        }
        return index;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (openCount == 2)
            return; // 두 카드가 열린 상태면 클릭 무효
        
        JButton btn = (JButton) e.getSource();
        int index = getButtonIndex(btn);
        btn.setIcon(changeImage(images[index])); // 버튼에 해당 이미지 표시
        
        openCount++;
        if (openCount == 1) {
            buttonIndexSave1 = index;
        } else if (openCount == 2) {
            buttonIndexSave2 = index;
            tryCount--;
            labelMessage.setText("같은 네잎클로버를 찾아보자! 남은 횟수: " + tryCount + "번");

            // 남은 횟수가 0이 되면 게임 종료 처리
            if (tryCount < 0 && successCount < 8) {
                labelMessage.setText("GAME OVER!");
                for (JButton button : buttons) {
                    button.setEnabled(false); // 모든 버튼 비활성화
                }
            }

            // 카드가 일치하는지 확인
            boolean isBingo = checkCard(buttonIndexSave1, buttonIndexSave2);
            if (isBingo) { // 성공
                openCount = 0;
                successCount++;
                if (successCount == 8) {
                    labelMessage.setText("SUCCESS!");
                    gameCleared = true; // 게임 클리어 상태 변경
                    for (JButton button : buttons) {
                        button.setEnabled(false); // 모든 버튼 비활성화
                    }
                }
            } else { // 실패
                backToQuestion();
            }
        }
    }
    
 // 게임 클리어 여부 확인
    public boolean isGameCleared() {
        return gameCleared;
    }

    // 게임 클리어 시 상태 변경
    public void setGameCleared(boolean cleared) {
        this.gameCleared = cleared;
    }

    // 카드 뒤집기 실패 시
    public void backToQuestion() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCount = 0;
                buttons[buttonIndexSave1].setIcon(changeImage("question.png"));
                buttons[buttonIndexSave2].setIcon(changeImage("question.png"));
                timer.stop(); // 반복 종료
            }
        });
        timer.start();
    }

    // 카드 일치 여부 확인
    public boolean checkCard(int index1, int index2) {
        if (index1 == index2)
            return false;
        return images[index1].equals(images[index2]);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("네잎클로버 찾기");
        Card cardPanel = new Card();
        frame.add(cardPanel);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

