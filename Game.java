import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.net.URL;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel{

	int crx,cry;				//location of cross_road
	int car_x,car_y;			//location of car
	int speedX,speedY;	
	int nOpponent;
	String imageLoc[];
	int lx[],ly[];
	int score = 0;
	
	boolean isUp, isDown, isRight, isLeft;
	
	public Game(){
		crx = cry = -999;
		addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
				stopCar(e);
			}
			public void keyPressed(KeyEvent e) {
				moveCar(e);
			}
		});
		setFocusable(true);
		car_x = car_y = 300;
		isUp = isDown = isLeft = isRight = false;
		speedX = speedY = 0;
		nOpponent = 0;
		lx = new int[200];
		ly = new int[200];
		imageLoc = new String[200];
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D obj = (Graphics2D) g;
		obj.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		try{		
			obj.drawImage(getToolkit().getImage("images/st_road.png"), 0, 0 ,this);
			if(cry >= -499 && crx >= -499)
				obj.drawImage(getToolkit().getImage("images/cross_road.png"),crx,cry,this);
				
			obj.drawImage(getToolkit().getImage("images/car_self.png"),car_x,car_y,this);
		
			if(this.nOpponent > 0){
				for(int i=0;i<this.nOpponent;i++){
					obj.drawImage(getToolkit().getImage(this.imageLoc[i]),this.lx[i],this.ly[i],this);
				}
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		
	}

	void moveRoad(int count){
		if(crx == -999 && cry == -999){
			if(count%10 == 0){
				crx = 499;
				cry = 0;
			}
		}
		else{
			crx--;
		}
		if(crx == -499 && cry == 0){
			crx = cry = -999;
		}
		car_x += speedX;
		car_y += speedY;
		
		if(car_x < 0)
			car_x = 0;
		
		//also run opponents
		for(int i=0;i<this.nOpponent;i++){
			this.lx[i] -= 2;
		}
		
		int index[] = new int[nOpponent];
		for(int i=0;i<nOpponent;i++){
			if(lx[i] >= -127){
				index[i] = 1;
			}
		}
		int c = 0;
		for(int i=0;i<nOpponent;i++){
			if(index[i] == 1){
				imageLoc[c] = imageLoc[i];
				lx[c] = lx[i];
				ly[c] = ly[i];
				c++;
			}
		}
		score += nOpponent - c;
		nOpponent = c;
		//Check for collision
		int diff = 0;
		for(int i=0;i<nOpponent;i++){
			diff = car_y - ly[i];
			if((ly[i] >= car_y && ly[i] <= car_y+46) || (ly[i]+46 >= car_y && ly[i]+46 <= car_y+46)){
				//along same line (horizontal)
				if(car_x+87 >= lx[i] && !(car_x >= lx[i]+87)){
					//Collision hua h
					//ab game end karo
					System.out.println("My car : "+car_x+", "+car_y);
					System.out.println("Colliding car : "+lx[i]+", "+ly[i]);
					this.finish();
				}
			}
		}
	}
	
	
	void finish(){
		JOptionPane.showMessageDialog(this,"Game Over!!!\nScore : "+score, "Game Over", JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}
	
	public void moveCar(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_UP){
			isUp = true;
			speedX = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			isDown = true;
			speedX = -2;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			isRight = true;
			speedY = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			isLeft = true;
			speedY = -1;
		}
	}
	
	public void stopCar(KeyEvent e){
		
		//System.out.println(isUp + " " + isDown + " " + isLeft + " " + isRight);
		if(e.getKeyCode() == KeyEvent.VK_UP){
			isUp = false;
			speedX = 0;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			isDown = false;
			speedX = 0;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			isLeft = false;
			speedY = 0;
			//System.out.println("Left key released");
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			isRight = false;
			speedY = 0;
		}
	}
	
	public static void main(String args[]){
		JFrame frame = new JFrame("Car Racing Game");
		Game game = new Game();
		frame.add(game);			//Graphics2D components are added to JFrame Window
		frame.setSize(500,500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int count = 1, c = 1;
    	while(true){
    		game.moveRoad(count);
    		while(c <= 1){
				game.repaint();
				try{
					Thread.sleep(5);
				}
				catch(Exception e){
					System.out.println(e);
				}
				c++;
			}
			c = 1;
			count++;
			if(game.nOpponent < 4 && count % 200 == 0){
				game.imageLoc[game.nOpponent] = "images/car_left_"+((int)((Math.random()*100)%3)+1)+".png";
				game.lx[game.nOpponent] = 499;
				int p = (int)(Math.random()*100)%4;
				if(p == 0){
					p = 250;
				}
				else if(p == 1){
					p = 300;
				}
				else if(p == 2){
					p = 185;
				}
				else{
					p = 130;
				}
				game.ly[game.nOpponent] = p;
				game.nOpponent++;
			}
    	}
	}
}
