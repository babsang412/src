import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class Cat extends JPanel {
    static int coinsCollected = 0; // 동전 개수
    private JFrame frame;
    private JLabel player;
    private JLabel coin;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private int xOffset, yOffset;
    private boolean gameCleared = false;  // 게임 완료 상태 변수
    
    // isGameCleared() 메서드 정의
    public boolean isGameCleared() {
        return gameCleared;
    }
    
    // makeDraggable 메서드 추가
    public void makeDraggable(JLabel foodLabel, JLabel collisionArea, JLabel heartLabel, JFrame parentFrame) {
        foodLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // 드래그 시작 위치 기록
                int xOffset = e.getX();
                int yOffset = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                // 충돌 영역 확인
                if (foodLabel.getBounds().intersects(collisionArea.getBounds())) {
                    heartLabel.setVisible(true);
                    showCatMessage(parentFrame);
                    gameCleared = true;
                } else {
                    heartLabel.setVisible(false);
                }
            }
        });

        foodLabel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                // 드래그 중 위치 업데이트
                foodLabel.setLocation(foodLabel.getX() + e.getX() - xOffset, foodLabel.getY() + e.getY() - yOffset);
            }
        });
    }

    public Cat startCoinGame(JFrame parentFrame) {
        // 부모 프레임의 레이아웃 설정
        parentFrame.setLayout(null);

        // 게임 설명 텍스트 추가
        JLabel instructionLabel = new JLabel("방향키로 이동해서 동전을 모으자!");
        instructionLabel.setBounds(170, 10, 500, 30);
        instructionLabel.setFont(new Font("맑은고딕", Font.BOLD, 20));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        parentFrame.add(instructionLabel);

        // 캐릭터 이미지 설정
        ImageIcon playerIcon = new ImageIcon("Img/character.png");
        player = new JLabel(resizeImage(playerIcon, 50, 50));
        player.setBounds(350, 500, 50, 50);
        parentFrame.add(player);

        // 동전 이미지 설정
        ImageIcon coinIcon = new ImageIcon("Img/coin.png");
        coin = new JLabel(resizeImage(coinIcon, 50, 50));
        coin.setBounds((int) (Math.random() * 700), (int) (Math.random() * 400), 50, 50);
        parentFrame.add(coin);

        // 타이머 및 점수 라벨 추가
        timerLabel = new JLabel("Time: 60s");
        timerLabel.setBounds(10, 10, 200, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        parentFrame.add(timerLabel);

        scoreLabel = new JLabel("coin: 0/5");
        scoreLabel.setBounds(10, 40, 200, 30);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        parentFrame.add(scoreLabel);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int timeLeft = 60;

            @Override
            public void run() {
                timerLabel.setText("Time: " + (--timeLeft) + "s");
                if (timeLeft <= 0 || coinsCollected >= 5) {
                    timer.cancel();
                    if (coinsCollected < 5) {
                        JOptionPane.showMessageDialog(parentFrame, "시간 내에 동전을 다 모으지 못했어요. 게임 종료!");
                    }

                    // 부모 창을 닫지 않고, 오직 게임 창만 닫기
                    if (coinsCollected >= 5) {
                        showVendingMachine(parentFrame); // 자판기 화면으로 전환
                    } else {
                    	parentFrame.dispose(); // 게임 종료
                    }
                }
            }
        }, 0, 1000);

        parentFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int x = player.getX();
                int y = player.getY();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> player.setLocation(x - 10, y);
                    case KeyEvent.VK_RIGHT -> player.setLocation(x + 10, y);
                    case KeyEvent.VK_UP -> player.setLocation(x, y - 10);
                    case KeyEvent.VK_DOWN -> player.setLocation(x, y + 10);
                }

                // 동전 충돌 확인
                if (player.getBounds().intersects(coin.getBounds())) {
                    coinsCollected++;
                    scoreLabel.setText("coin: " + coinsCollected + "/5");
                    coin.setBounds((int) (Math.random() * 700), (int) (Math.random() * 400), 50, 50);
                }
            }
        });

        parentFrame.setFocusable(true);
        parentFrame.setVisible(true);
        
        return this; // 현재 객체를 반환
    }

    // 자판기 화면
    public void showVendingMachine(JFrame parentFrame) {
        JFrame frame = new JFrame("자판기");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 700);
        frame.setLayout(null);

        JLabel vendingLabel = new JLabel(new ImageIcon("Img/vendingMachine.png"));
        vendingLabel.setBounds(200, 50, 400, 500);
        frame.add(vendingLabel);

        // 자판기 화면 설명 추가
        JLabel vendingInstructionLabel = new JLabel("모은 동전으로 뭘 살까?");
        vendingInstructionLabel.setBounds(300, 10, 300, 30);
        vendingInstructionLabel.setFont(new Font("맑은고딕", Font.BOLD, 20));
        frame.add(vendingInstructionLabel);

        // 커피 버튼
        JButton coffeeButton = new JButton("커피");
        coffeeButton.setBounds(250, 560, 100, 50);
        frame.add(coffeeButton);

        // 츄르 버튼
        JButton churuButton = new JButton("츄르");
        churuButton.setBounds(450, 560, 100, 50);
        frame.add(churuButton);

        // 커피 버튼 이벤트 처리
        coffeeButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "카페인 충전! 열심히 행운을 모아보자!");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    frame.dispose();
                    
                }
            }, 2000); // 2초 후 종료
        });

        // 츄르 버튼 이벤트 처리
        churuButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "고양이를 만났다!");
            frame.dispose();
            startFeedingGame(parentFrame); // 고양이 먹이 주기 게임 시작
        });

        frame.setVisible(true);
    }

    // 고양이 먹이 주기 게임
    public void startFeedingGame(JFrame parentFrame) {
        JFrame frame = new JFrame("행운의 고양이");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1600, 1000);
        frame.setLayout(null);

        // 먹이 이미지
        ImageIcon foodIcon = new ImageIcon("Img/food.png");
        Image foodImage = foodIcon.getImage();
        Image scaledFoodImage = foodImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel foodLabel = new JLabel(new ImageIcon(scaledFoodImage));
        foodLabel.setBounds(0, 0, 300, 300);
        frame.add(foodLabel);

        // 고양이 충돌 범위 레이블 추가
        JLabel collisionArea = new JLabel();
        collisionArea.setBounds(1160, 550, 180, 125);
        frame.add(collisionArea);

        // 고양이 GIF 이미지
        JLabel catLabel = new JLabel(new ImageIcon("Img/cat.gif"));
        int labelWidth = 400;
        int labelHeight = 400;
        int centerX = 1000;
        int centerY = 500;
        catLabel.setBounds(centerX, centerY, labelWidth, labelHeight);
        frame.add(catLabel);

        // 하트 GIF
        JLabel heartLabel = new JLabel(new ImageIcon("Img/heart.gif"));
        heartLabel.setBounds(-250, -900, 3000, 3000);
        heartLabel.setVisible(false);
        frame.add(heartLabel);

        // 드래그 앤 드롭 활성화
        makeDraggable(foodLabel, collisionArea, heartLabel, frame);

        frame.setVisible(true);
       
    }

    // 고양이 메시지
    public void showCatMessage(JFrame parentFrame) {
        JFrame messageFrame = new JFrame("고양이 목걸이");
        messageFrame.setSize(400, 300);
        messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messageFrame.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel("어? 고양이 목에 뭔가 반짝이는게 있다!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("맑은고딕", Font.BOLD, 18));
        messageFrame.add(messageLabel, BorderLayout.CENTER);

        JButton clickButton = new JButton("뭐지?");
        clickButton.setFont(new Font("맑은고딕", Font.PLAIN, 16));

        // 버튼 클릭 시 2초 후 창을 닫기 위한 ActionListener 추가
        clickButton.addActionListener(e -> {
            // 2초 후에 창 닫기
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    messageFrame.dispose();
                }
            }, 1000);
        });

        messageFrame.add(clickButton, BorderLayout.SOUTH);
        messageFrame.setVisible(true);
    }
    
    // 이미지 크기 변경
    public ImageIcon resizeImage(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
}
