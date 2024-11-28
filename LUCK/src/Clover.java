import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.util.ArrayList;

class Entity {
    private Image image;
    private int x, y, maxX, maxY;
    private final int SPEED = 20, WIDTH = 150, HEIGHT = 150, MARGIN = 25;
    public static final int LEFT = -1, RIGHT = 1, UP = -1, DOWN = 1, STOP = 0;
    private boolean isClover; // 클로버 여부를 판단할 변수

    // 초기화 : 이미지, 위치, 이동 가능 범위
    public Entity(final String image, int x, int y, int bgWidth, int bgHeight) {
        this.image = new ImageIcon(image).getImage();
        this.x = x;
        this.y = y;
        this.maxX = bgWidth - WIDTH - MARGIN;
        this.maxY = bgHeight - HEIGHT - MARGIN;
    }

    // 클로버 객체 생성자를 추가
    public Entity(final String image, int bgWidth, int bgHeight, boolean isClover) {
        this(image, 0, 50, bgWidth, bgHeight);
        this.x = (int) (Math.random() * maxX);
        this.isClover = isClover; // 클로버일 경우 true로 설정
    }

    //  초기화 : 화면 위쪽의 임의의 x 위치에서 폭탄 투하
    public Entity(final String image, int bgWidth, int bgHeight) {
        this(image, 0, 50, bgWidth, bgHeight);
        this.x = (int) (Math.random() * maxX);
    }

    // 일정 속도로 위치 이동
    public void move(int x, int y) {
        this.x += SPEED * x;
        this.y += SPEED * y;
        this.x = (this.x > MARGIN) ? this.x : MARGIN;
        this.x = (this.x < maxX) ? this.x : maxX;
        this.y = (this.y > MARGIN) ? this.y : MARGIN;
        this.y = (this.y < maxY) ? this.y : maxY;
    }

    public static void move(ArrayList<Entity> bombs) {
        for (int i = 0; i < bombs.size(); i++) {
            Entity bomb = bombs.get(i);
            bomb.move(STOP, DOWN);
            // 화면 경계선 밖의 폭탄은 화면에 안 보이므로 삭제
            if ((bomb.collide() == true))
                bombs.remove(bomb);
        }
    }

    // 화면 경계선이나 폭탄과 충돌 여부 확인
    public boolean collide() {
        return (x < MARGIN) || (maxX <= x) || (y < MARGIN) || (maxY <= y);
    }

    public boolean collide(Entity bomb) {
        return (Math.abs(this.x - bomb.x) < MARGIN * 3) && (Math.abs(this.y - bomb.y) < MARGIN * 3);
    }

    public boolean collide(ArrayList<Entity> bombs) {
        for (Entity bomb : bombs) {
            if (this.collide(bomb) == true)
                return true;
        }
        return false;
    }

    private boolean isCollided; // 충돌 여부 플래그

    public boolean isCollided() {
        return isCollided;
    }

    public void setCollided(boolean collided) {
        isCollided = collided;
    }

    // 최신 위치를 기준으로 화면 업데이트
    public void paint(Graphics g) {
        g.drawImage(image, x, y, WIDTH, HEIGHT, null);
    }

    public static void paint(ArrayList<Entity> bombs, Graphics g) {
        for (Entity bomb : bombs)
            bomb.paint(g);
    }
}

// Clover 클래스 수정: JPanel을 상속받음
public class Clover extends JPanel {
    private Entity character;
    private int characterX;
    private int characterHP;
    private ArrayList<Entity> bombs;
    private ArrayList<Entity> clovers; // 클로버 리스트 추가
    private String imageBomb;
    private String imageClover;
    private int time; // 이벤트 주기 컨트롤 단위 변수
    private Timer timer;
    private final int WIDTH = 1820; // 패널 폭
    private final int HEIGHT = 800; // 패널 높이
    private int score; // 점수 변수 추가
    private boolean isGameOver = false; // 게임 오버 상태
    private boolean isSuccess = false; // 성공 상태
    private boolean gameCleared = false;// 게임 클리어 상태 추가

    public Clover() {
        // 폭탄 및 캐릭터 초기화
        bombs = new ArrayList<Entity>();
        clovers = new ArrayList<Entity>(); // 클로버 리스트 초기화
        imageBomb = "Img/bomb.png";
        imageClover = "Img/clover.png";
        character = new Entity("Img/character_clover.png", WIDTH / 2, HEIGHT - 60, WIDTH, HEIGHT);
        characterX = character.STOP;
        characterHP = 5;
        score = 0; // 점수 초기화
        setBackground(Color.white);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // 키보드 입력 시 반응할 리스너 등록
        addKeyListener(new MyKeyListener());
        setFocusable(true);
        requestFocus();
        // 일정 시간마다 반응할 타이머 시작
        timer = new Timer(40, this.new TimerListener());
        timer.start();
        this.time = 300;
    }

    // 키보드 입력 시 반응
    private class MyKeyListener extends KeyAdapter {
        // 키를 눌렀을 때 캐릭터 좌우 이동방향 설정
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
                case KeyEvent.VK_LEFT:
                case 'A':
                case 'a':
                    characterX = character.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                case 'D':
                case 'd':
                    characterX = character.RIGHT;
                    break;
            }
        }

        // 키를 뗐을 때 캐릭터 멈춤
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case 'A':
                case 'a':
                case KeyEvent.VK_RIGHT:
                case 'D':
                case 'd':
                    characterX = character.STOP;
                    break;
            }
        }
    }

    // 일정 시간마다 반응
    protected class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // 모든 폭탄과 클로버 이동
            Entity.move(bombs);
            Entity.move(clovers);

            // 일정 주기마다 폭탄과 클로버 생성
            if ((time-- % 15) == 0) {
                bombs.add(new Entity(imageBomb, WIDTH, HEIGHT));
                clovers.add(new Entity(imageClover, WIDTH, HEIGHT, true));
            }

            // 캐릭터 이동
            character.move(characterX, character.STOP);

            // 폭탄 충돌 처리
            if (character.collide(bombs)) {
                bombs.removeIf(b -> {
                    if (character.collide(b)) {
                        characterHP--; // 체력 감소
                        return true; // 충돌한 폭탄 제거
                    }
                    return false;
                });
            }

            // 클로버 충돌 처리
            if (character.collide(clovers)) {
                clovers.removeIf(c -> {
                    if (character.collide(c)) {
                        score++; // 점수 증가
                        return true; // 충돌한 클로버 제거
                    }
                    return false;
                });
            }

            // 게임 종료 및 성공 여부 확인
            if (score >= 7 && characterHP > 0) {
                isSuccess = true;
                gameCleared = true; // 게임 클리어 상태 변경
                
            } else if (characterHP <= 0) {
                isGameOver = true;
                
            }

            // 화면 업데이트
            repaint();
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

    // 화면 업데이트
    public void paint(Graphics g) {
        super.paint(g);

        if (!isGameOver && !isSuccess) {
            g.setColor(Color.black);
            g.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            g.drawString("좌우 방향키로 이동해서 별똥별을 7개 모아 소원을 빌어보자!", WIDTH / 2 - 400, 50);
            g.drawString("Tip. 정가운데로 받을 수록 잘 받을 수 있어!", WIDTH / 2 - 300, 100);
        }

        // 화면 첫줄에 HP 출력
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("HP : " + hp(), 20, 50);
        g.drawString("Score: " + score, 20, 100);

        // 제한시간동안 폭탄을 피해 살아남으면 성공 종료 화면
        if (score >= 7 && characterHP > 0) {
            g.drawString("Success!", WIDTH / 2 - 100, HEIGHT / 2);
            timer.stop();
        }
        // HP가 더 이상 없으면 실패 종료 화면
        else if (this.characterHP <= 0) {
            g.drawString("Game Over!", WIDTH / 2 - 100, HEIGHT / 2);
            timer.stop();
        }
        // 폭탄 및 캐릭터의 현재 위치를 바탕으로 게임 진행 화면
        else {
            Entity.paint(bombs, g);
            Entity.paint(clovers, g); // 클로버도 화면에 그리기
            character.paint(g);
        }
    }

    // HP를 문자열로 표현
    public String hp() {
        switch (this.characterHP) {
            case 5:
                return "● ● ● ● ●";
            case 4:
                return "● ● ● ● ○";
            case 3:
                return "● ● ● ○ ○";
            case 2:
                return "● ● ○ ○ ○";
            case 1:
                return "● ○ ○ ○ ○";
            default:
                return "○ ○ ○ ○ ○";
        }
    }

    // 게임 실행을 위한 main 함수
    public static void main(String[] args) {
        // JFrame 설정
        JFrame frame = new JFrame("폭탄 피하기 게임");
        Clover panel = new Clover(); // Clover 클래스를 JPanel로 사용
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
