import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Arrow extends JPanel {
    private JLabel messageLabel;
    private JLabel descriptionLabel;
    private JButton startButton;
    private JLabel scoreLabel;
    private ArrowPanel arrowPanel;
    private int score;
    private int currentArrowIndex;
    private final int totalArrows = 20;
    private String[] arrowImages = {"Img/up.png", "Img/left.png", "Img/down.png", "Img/right.png"};
    private String[] arrowString = {"up", "left", "down", "right"};
    private int[] arrowSequence;
    private long startTime;
    private boolean[] correctAnswers;

    
    public boolean isGameCleared() {
        // 게임이 클리어된 경우, 즉 모든 화살표를 맞춘 경우
        for (boolean correct : correctAnswers) {
            if (!correct) {
                return false; // 하나라도 틀리면 게임이 끝난 것이 아님
            }
        }
        return true; // 모든 화살표가 맞았다면 게임 클리어
    }

    public Arrow() {
        setLayout(new BorderLayout());

        // 초기화
        score = 0;
        currentArrowIndex = 0;
        arrowSequence = new int[totalArrows];
        correctAnswers = new boolean[totalArrows];
        for (int i = 0; i < totalArrows; i++) {
            arrowSequence[i] = (int) (Math.random() * 4);
        }

        // 설명 라벨
        descriptionLabel = new JLabel("<html><center>로또 명당을 찾아가자!<br>순서는<br>"
                + "1&nbsp;&nbsp;&nbsp;2&nbsp;&nbsp;&nbsp;3&nbsp;&nbsp;&nbsp;4&nbsp;&nbsp;&nbsp;5<br>"
                + "6&nbsp;&nbsp;&nbsp;7&nbsp;&nbsp;&nbsp;8&nbsp;&nbsp;&nbsp;9&nbsp;&nbsp;10<br>"
                + "11&nbsp;12&nbsp;13&nbsp;14&nbsp;15<br>"
                + "16&nbsp;17&nbsp;18&nbsp;19&nbsp;20<br></center></html>");
        descriptionLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 점수 라벨
        scoreLabel = new JLabel("<html><br>Score: 0 / " + totalArrows + "</html>");
        scoreLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);


        // 설명 라벨과 점수 라벨을 하나의 패널에 넣기
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(descriptionLabel, BorderLayout.NORTH);
        topPanel.add(scoreLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // 메시지 라벨
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 시작 버튼
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        startButton.addActionListener(e -> startGame());

        // 컨트롤 패널 (하단)
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(startButton, BorderLayout.CENTER);
        controlPanel.add(messageLabel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);

        // 화살표 패널
        arrowPanel = new ArrowPanel();
        add(arrowPanel, BorderLayout.CENTER);

        // 키 이벤트 처리
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                checkAnswer(e.getKeyCode());
            }
        });
    }

    private void startGame() {
        score = 0;
        currentArrowIndex = 0;
        scoreLabel.setText("Score: 0 / " + totalArrows);
        scoreLabel.setVisible(true); // 점수 표시
        startButton.setVisible(false); // 시작 버튼 숨김
        messageLabel.setText("");
        
        // 설명 라벨 숨기기
        descriptionLabel.setVisible(false);

        startTime = System.currentTimeMillis();
        correctAnswers = new boolean[totalArrows];
        generateArrows();
    }


    private void generateArrows() {
        arrowPanel.setArrowSequence(arrowSequence);
        arrowPanel.repaint();
    }

    private void checkAnswer(int keyCode) {
        if (currentArrowIndex < totalArrows) {
            int correctIndex = arrowSequence[currentArrowIndex];
            if ((keyCode == KeyEvent.VK_UP && correctIndex == 0) ||
                (keyCode == KeyEvent.VK_LEFT && correctIndex == 1) ||
                (keyCode == KeyEvent.VK_DOWN && correctIndex == 2) ||
                (keyCode == KeyEvent.VK_RIGHT && correctIndex == 3)) {
                score++;
                messageLabel.setText("Correct!");
                correctAnswers[currentArrowIndex] = true;
                currentArrowIndex++;
            } else {
                messageLabel.setText("Wrong!");
            }

            scoreLabel.setText("Score: " + score + " / " + totalArrows);

            if (currentArrowIndex >= totalArrows) {
                gameOver();
            }
            arrowPanel.repaint();
        }
    }

    private void gameOver() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        JOptionPane optionPane = new JOptionPane(
            "<html><center>로또 명당을 찾았다!<br>소요 시간: " + (elapsedTime / 1000.0) + "초</center></html>",
            JOptionPane.PLAIN_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[]{}); 
        JDialog dialog = optionPane.createDialog(this, "SUCCEESS!");

        // 버튼 추가
        JButton closeButton = new JButton("로또 사러가기");
        closeButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        closeButton.addActionListener(e -> {
            dialog.dispose();
           
        });

        optionPane.setOptions(new Object[]{closeButton});
        dialog.setVisible(true);
    }

    private class ArrowPanel extends JPanel {
        private int[] arrowSequence;

        public void setArrowSequence(int[] arrowSequence) {
            this.arrowSequence = arrowSequence;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (arrowSequence != null) {
                int arrowWidth = 100;
                int arrowHeight = 100;
                int xOffset = 100;
                int yOffset = 100;

                for (int i = 0; i < totalArrows; i++) {
                    if (!correctAnswers[i]) {
                        int arrowIndex = arrowSequence[i];
                        ImageIcon arrowIcon = new ImageIcon(arrowImages[arrowIndex]);
                        g.drawImage(arrowIcon.getImage(), (i % 5) * (arrowWidth + xOffset), (i / 5) * (arrowHeight + yOffset), arrowWidth, arrowHeight, this);
                    }
                }
            }
        }
    }
}
