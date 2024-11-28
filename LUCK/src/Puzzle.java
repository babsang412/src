

import javax.swing.*;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

class DraggableImage {
    BufferedImage image;
    int x, y,corx,cory;
    boolean correct;
    public DraggableImage(BufferedImage image, int x, int y,int corx,int cory) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.corx=corx;
        this.cory=cory;
        
        this.correct=(corx==x)&&(y==cory);
    }
}

public class Puzzle extends JPanel {
    private ArrayList<DraggableImage> images = new ArrayList<>();
    private DraggableImage selectedImage = null;
    private int mouseX, mouseY;
    private int miceX,miceY;
    private boolean isOverlapping = false;
    private boolean isHorizontal;
    private JButton clearbutton;
    private boolean correct[] = new boolean[25];
    private boolean end=false;
    private int p=0;
    
    
    
    public Puzzle() {
    	setLayout(null);
    	clearbutton = new JButton("편지 붙이기!");
    	clearbutton.setBounds(150,40,550,30);
    	clearbutton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			Check();
    			if(correct[24]==true) {
    				end=true;
    				try {
    					Thread.sleep(3000);
    				}catch(Exception e3) {
    					
    				}
    				JOptionPane.showMessageDialog(null, "편지를 다 맞췄어요!", "제목", JOptionPane.WARNING_MESSAGE);
    				
    				try {
    					
    				}catch (Exception e1) {
    		            e1.printStackTrace();
    		        }
    			
    				
    			}
    			else {
    				JOptionPane.showMessageDialog(null, "편지를 붙이기엔 부족해요", "제목", JOptionPane.WARNING_MESSAGE);
    				
    			}
    		}
    	});
    	this.add(clearbutton);
        // 이미지 로드
        try {
        	final String imagepath="C:\\joonmin\\2024\\KakaoTalk_20241128_005747322.png_5x5 (1)\\";
        	int[] h=new int[25];
        	int[] ux = new int[25];
        	int[] uy = new int[25];
        	for(int i = 0 ; i<h.length;i++) {
        		h[i]=(int)(Math.random()*25+1);
        		for(int j = 0 ; j<i;j++) {
        			if(h[i]==h[j]) {
        				i--;
        				break;
        			}
        		}
        	}
        	int l=0;
        	for(int i = 0 ; i< 5 ; i++) {
        		for(int j = 0 ; j < 5 ; j++) {
        			ux[l]=j*160+50;
            		uy[l]=i*160+80;
            		l++;
        		}
        	}
        	l=0;
            for(int i = 0 ; i < 5 ; i++) {
            	
            	for(int j = 0 ; j< 5 ; j++) {
            		String imagename=Integer.toString(h[l]);
            		
            		images.add(new DraggableImage(ImageIO.read(new File(imagepath+imagename+".png")), j*160+50, i*160+80,ux[h[l]-1],uy[h[l]-1]));
            		
            		
            		l++;
            		
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 마우스 리스너 추가
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                
                // 클릭한 위치에 있는 이미지 찾기
                for (int i = images.size() - 1 ; i >= 0 ; i--) {
                	DraggableImage img = images.get(i);
                    if (mouseX >= img.x && mouseX <= img.x + img.image.getWidth() &&
                        mouseY >= img.y && mouseY <= img.y + img.image.getHeight()) {
                        selectedImage = img;
                        miceX=img.x;
                        miceY=img.y;
                        images.remove(i);
                        images.add(img);
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	if(selectedImage!=null) {
            		for(DraggableImage img : images) {
            			if(img != selectedImage && isOverlapping(selectedImage, img)) {
            				isOverlapping = true;
            				break;
            			}
            		}
            		if(isOverlapping) {
            			for(DraggableImage img : images) {
            				if (img != selectedImage && isOverlapping(selectedImage, img)) {
                                // 위치 교환
                                
                                selectedImage.x = img.x;
                                selectedImage.y = img.y;
                                img.x = miceX;
                                img.y = miceY;
                                CheckCorrect(selectedImage);
                                CheckCorrect(img);
                                repaint();
                                isOverlapping=false;
                                break;
                            }
            			}
            		}
            		else {
            			selectedImage.x=miceX;
            			selectedImage.y=miceY;
            			repaint();
            		}
            	}
            	
            	
            	
                selectedImage = null; // 드래그 종료
            }
        });

        // 마우스 모션 리스너 추가
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
            	if (selectedImage != null) {
            		

                    // 이동 처리
                    
                    selectedImage.x += e.getX() - mouseX; // x좌표만 변경
                    
                    selectedImage.y += e.getY() - mouseY; // y좌표만 변경
                    

                    mouseX = e.getX(); // 현재 마우스 위치 업데이트
                    mouseY = e.getY(); // 현재 마우스 위치 업데이트
                    repaint(); // 패널을 다시 그리기
                }
            }
        });
        /*
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (selectedImage == null) {
                    // 이동 방향 결정
                	try {
                	    Thread.sleep(75);
                	} catch(InterruptedException e1) {
                	    // interrupt() 메소드가 호출되면 실행
                	}
                    int dx = Math.abs(e.getX() - mouseX);
                    int dy = Math.abs(e.getY() - mouseY);
                    
                    if (dx > dy) {
                        isHorizontal = true; // 수평 이동
                    } else {
                        isHorizontal = false; // 수직 이동
                    }
                }
            }
        });
        */
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 이미지 그리기
        for (DraggableImage img : images) {
            g.drawImage(img.image, img.x, img.y, this);
        }
     // 텍스트 그리기
        
    }
    
    private boolean isOverlapping(DraggableImage img1, DraggableImage img2) {
    	return img1.x< img2.x + img2.image.getWidth() &&
    		   img1.x + img1.image.getWidth()> img2.x &&
    		   img1.y< img2.y + img2.image.getHeight() &&
    		   img1.y + img1.image.getHeight() > img2.y;
    }
    void CheckCorrect(DraggableImage img) {
    	
		if(img.x==img.corx&&img.y==img.cory) {
			img.correct=true;
			
		}
		else {
			img.correct=false;
			
		}
    	
    }
    void Check() {
    	for(DraggableImage img : images) {
    		if(img.correct==false) {
    			return;
    		}
    		correct[24]=true;
    		
    		
    	}
    }
    boolean isGameCleared() {
    	return end;
    }
}