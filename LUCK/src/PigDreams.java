import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class PigDreams extends JPanel {
    
    private static boolean gameCleared = false;

    public static boolean isGameCleared() {
        return gameCleared;
    }

    public static void setGameCleared(boolean gameCleared) {
        PigDreams.gameCleared = gameCleared;
    }
    
    // 캐릭터 패널
    static class CharacterMovementPanel extends JPanel {
        private int x, y, maxX, maxY;
        private int[][] map;
        private Image[] image;
        private JFrame frame;
        private final int SIZE = 60, SCOPE = 2, CHARACTER = 2, WALL = 1, EXIT = 5, PIG = 6;
        static boolean pig = false;
        static boolean exitReached = false;

        // 캐릭터 패널 준비
        public CharacterMovementPanel(int[][] map, String imagePath, JFrame frame) {
            this.x = 5;
            this.y = 5;
            this.map = map;
            this.frame = frame;
            this.maxX = map[0].length - 1;
            this.maxY = map.length - 1;
            this.image = new Image[8];
            this.image[0] = new ImageIcon(imagePath + "path.png").getImage();
            this.image[1] = new ImageIcon(imagePath + "wall.png").getImage();
            this.image[2] = new ImageIcon(imagePath + "character.png").getImage();
            this.image[5] = new ImageIcon(imagePath + "exit.png").getImage();
            this.image[6] = new ImageIcon(imagePath + "pig.png").getImage();

            // 맵 크기 비율에 맞춰 화면 크기 조정
            int panelWidth = frame.getWidth();
            int panelHeight = frame.getHeight();
            int mapWidth = map[0].length * SIZE;
            int mapHeight = map.length * SIZE;

            // 화면 크기에 맞춰 비율 계산
            double scaleX = (double) panelWidth / mapWidth;
            double scaleY = (double) panelHeight / mapHeight;
            double scale = Math.min(scaleX, scaleY);

            // 화면 크기를 맵 크기에 맞게 조정
            setPreferredSize(new Dimension((int)(mapWidth * scale), (int)(mapHeight * scale)));
            
            addKeyListener(new ArrowKeyListener());
            setFocusable(true);
        }
        
        private class ArrowKeyListener extends KeyAdapter {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                    case KeyEvent.VK_LEFT:
                    case 'A':
                    case 'a':
                        move(-1, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                    case 'D':
                    case 'd':
                        move(+1, 0);
                        break;
                    case KeyEvent.VK_UP:
                    case 'W':
                    case 'w':
                        move(0, -1);
                        break;
                    case KeyEvent.VK_DOWN:
                    case 'S':
                    case 's':
                        move(0, +1);
                        break;
                }
                repaint();
            }
        }

        public void move(int x, int y) {
            this.x += x;
            this.y += y;
            this.x = (this.x <= 0) ? 0 : this.x;
            this.y = (this.y <= 0) ? 0 : this.y;
            this.x = (this.x >= maxX) ? maxX : this.x;
            this.y = (this.y >= maxY) ? maxY : this.y;
            if (map[this.y][this.x] == WALL) {
                this.x -= x;
                this.y -= y;
            } else if (map[this.y][this.x] == PIG) {
                pig = true;
                // 메시지 창 띄우기
                JOptionPane.showMessageDialog(frame, "당신은 돼지의 행운을 얻었습니다!", "확인", JOptionPane.INFORMATION_MESSAGE);
                map[this.y][this.x] = 1;
            } else if (map[this.y][this.x] == EXIT) {
                if (pig) {
                    // "SUCCESS!" 메시지 창 띄우기
                    JOptionPane.showMessageDialog(frame, "SUCCESS!");
                    gameCleared = true;
                    exitReached = true;

                    // 2초 후에 창을 닫는 타이머 설정
                    Timer timer = new Timer(2000, e -> frame.dispose());
                    timer.setRepeats(false); // 타이머가 한 번만 실행되도록 설정
                    timer.start(); // 타이머 시작
                } else {
                    showMessageDialog("뭔가 빨려들어갈 것만 같은 곳이다. 어떡할까?", "경고", JOptionPane.WARNING_MESSAGE, new String[] { "가보자!", "아직은 가면 안될 것 같다" });
                }
            }
        }

        public void showMessageDialog(String message, String title, int messageType, String[] options) {
            JOptionPane optionPane = new JOptionPane(message, messageType, JOptionPane.DEFAULT_OPTION, null, options, options[0]);
            JDialog dialog = optionPane.createDialog(frame, title);
            dialog.setVisible(true);

            String selectedOption = (String) optionPane.getValue();
            if ("가보자!".equals(selectedOption)) {
                JOptionPane.showMessageDialog(frame, "꿈에 돼지는 나오지 않았지만 잘 잤다! 힘내서 행운을 찾아보자!");
                Timer timer = new Timer(2000, e -> frame.dispose());
                timer.setRepeats(false);
                timer.start();
            } else if ("아직은 가면 안될 것 같다".equals(selectedOption)) {
                dialog.dispose();
            }
        }

        public void paint(Graphics g) {
            super.paint(g);
            for (int y = this.y - SCOPE * 2, y2 = 0; y <= this.y + SCOPE * 2; y++, y2++) {
                for (int x = this.x - SCOPE * 4, x2 = 0; x <= this.x + SCOPE * 4; x++, x2++) {
                    int index = WALL;
                    if ((this.x == x) && (this.y == y))
                        index = CHARACTER;
                    else if ((0 <= x) && (x <= maxX) && (0 <= y) && (y <= maxY))
                        index = map[y][x];
                    g.drawImage(this.image[index], x2 * SIZE, y2 * SIZE, SIZE, SIZE, null);
                }
            }
        }
    }

    public PigDreams() {
        // PigDreams 클래스에서 JFrame의 setTitle을 설정
        JFrame frame = new JFrame("Pig Dreams");
        frame.setTitle("Pig Dreams");
        frame.setSize(1000,1000);

        // CardLayout을 사용하여 화면 전환
        JPanel cardPanel = new JPanel(new CardLayout());
        add(cardPanel);

        // 시작 화면
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());

        // 텍스트 추가
        JLabel titleLabel = new JLabel("<html>돼지가 꿈에 나오면 좋은 일이 있다는데,<br>내 꿈에도 돼지가 나와줄까?<br>일단 오늘은 돼지인형을 안고 자보자!</html>", JLabel.CENTER);  // 중앙 정렬
        titleLabel.setFont(new Font("맑은고딕", Font.BOLD, 24));  // 글꼴과 크기 설정
        startPanel.add(titleLabel, BorderLayout.NORTH);  // BorderLayout.NORTH에 텍스트 추가

        // 시작 버튼
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) (cardPanel.getLayout());
            cl.show(cardPanel, "gamePanel"); // 게임 화면으로 전환
            // 게임 화면으로 이동 후 포커스 요청
            Component gamePanel = cardPanel.getComponent(1);
            gamePanel.requestFocusInWindow();
        });
        startPanel.add(startButton, BorderLayout.CENTER);

        cardPanel.add(startPanel, "startPanel");

        // 게임 화면
        int[][] map = { { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1 },
                { 1,0,1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,0,1,1,1,1,1,1,0,1,0,1,1,1,1,1,0,1 },
                { 1,0,1,0,0,0,0,0,1,0,1,0,1,0,0,0,1,0,1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1 },
                { 1,0,1,1,1,1,0,1,1,1,1,0,0,0,1,0,1,0,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,1 },
                { 1,0,0,0,0,0,0,0,0,0,1,0,1,1,1,0,1,0,1,0,0,1,0,1,0,0,1,0,0,0,0,0,0,5 },
                { 1,1,1,1,1,1,0,1,1,0,1,1,1,0,1,0,1,0,1,1,0,1,0,1,0,1,1,0,1,1,0,1,1,1 },
                { 1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0,1,0,0,0,0,1,0,0,0,1,0,0,0,1,0,1,0,1 },
                { 1,0,1,0,1,0,1,1,1,0,1,0,1,0,1,0,1,0,1,1,1,1,1,1,1,1,1,1,0,1,0,0,0,1 },
                { 1,0,1,0,0,0,1,0,0,0,0,0,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,1 },
                { 1,6,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 }
              };
        String imagePath = "Img/"; 
        CharacterMovementPanel gamePanel = new CharacterMovementPanel(map, imagePath, frame);
        cardPanel.add(gamePanel, "gamePanel");

        
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pig Dreams");
        PigDreams game = new PigDreams();
        frame.add(game);
        frame.setSize(1000,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
