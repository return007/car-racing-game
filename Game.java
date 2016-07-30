import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.net.URL;

public class Game extends JPanel{

	int crx,cry;
	public Game(){
		crx = cry = -999;
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D obj = (Graphics2D) g;
		obj.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		try{		
			obj.drawImage(getToolkit().getImage("images/st_road.png"), 0, 0 ,this);
			if(cry >= -499 && crx >= -499)
				obj.drawImage(getToolkit().getImage("images/cross_road.png"),crx,cry,this);
		}
		catch(Exception e){
			System.out.println(e);
		}
		
			obj.drawImage(getToolkit().getImage("images/car.png"),300,300,this);
	}

	void moveCar(int count){
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
	}
	
	public static void main(String args[]){
		JFrame frame = new JFrame("Car Racing Game");
		Game game = new Game();
		frame.add(game);			//Graphics2D components are added to JFrame Window
		frame.setSize(500,500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int count = 1;
    	while(true){
    		game.moveCar(count);
    		game.repaint();
    		try{
    			Thread.sleep(5);
    		}
    		catch(Exception e){
    			System.out.println(e);
    		}
    		count++;
    	}
	}
}
