import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.net.URL;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Instance of the game
public class Game extends JPanel{
    
    int crx,cry;	//location of the crossing
    int car_x,car_y;    //x and y location of user's car
    int speedX,speedY;	//the movement values of the user's car
    int nOpponent;      //the number of opponent vehicles in the game
    String imageLoc[]; //array used to store oponnent car images
    int lx[],ly[];  //integer arrays used to store the x and y values of the oncoming vehicles
    int score;      //intger variable used to store the current score of the player
    int highScore;  //integer variable used to store the high score of the player
    int speedOpponent[]; //integer array used to store the spped value of each opponent vehicle in the game
    boolean isFinished; //boolean that will be used the end the game when a colision occurs
    boolean isUp, isDown, isRight, isLeft;  //boolean values that show when a user clicks the corresponding arrow key
    
    public Game(){
        crx = cry = -999;   //initialing setting the location of the crossing to (-999,-999)
        //Listener to get input from user when a key is pressed and released
        addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) { //when a key is released
                stopCar(e); //stop movement of car
            }
            public void keyPressed(KeyEvent e) { //when a key is pressed
                moveCar(e); //move the car in the direction given by the key
            }
        });
        setFocusable(true); //indicates the JPanel can be focused
        car_x = car_y = 300;    //initialling setting the user's car location to (300,300)
        isUp = isDown = isLeft = isRight = false;   //initial arrow key values set to false, meaning user has not pressed any arrow keys
        speedX = speedY = 0;    //movement of the car in the x and y direction initially set to 0 (starting position)
        nOpponent = 0;  //set the number of opponent cars initially to zero
        lx = new int[20]; //array to be used to store the x position of all enemy cars
        ly = new int[20]; //array to be used to store the y position of all enemy cars
        imageLoc = new String[20];
        speedOpponent = new int[20]; //integer array used to store the spped value of each opponent vehicle in the game
        isFinished = false; //when false, game is running, when true, game has ended
        score = highScore = 0;  //initialling setting the current score and the highscore to zero
    }
    
    //function that paints all graphic images to the screen at specified locations
    //scene is repainted everytime the scene settings change
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D obj = (Graphics2D) g;
        obj.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try{
            obj.drawImage(getToolkit().getImage("images/st_road.png"), 0, 0 ,this); //draw road on window
            if(cry >= -499 && crx >= -499) //if a road crossing has passed the window view
                obj.drawImage(getToolkit().getImage("images/cross_road.png"),crx,cry,this); //draw another road crossing on window
            
            obj.drawImage(getToolkit().getImage("images/car_self.png"),car_x,car_y,this);   //draw car on window
            
            if(isFinished){ //if collision occurs
                obj.drawImage(getToolkit().getImage("images/boom.png"),car_x-30,car_y-30,this); //draw explosion image on window at collision to indicate the collision has occured
            }
            
            if(this.nOpponent > 0){ //if there is more than one opponent car in the game
                for(int i=0;i<this.nOpponent;i++){ //for every opponent car
                    obj.drawImage(getToolkit().getImage(this.imageLoc[i]),this.lx[i],this.ly[i],this); //draw onto window
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        
    }
    
    //function that moves the road scene across the window to make it seem like the car is driving
    void moveRoad(int count){
        if(crx == -999 && cry == -999){ //if the road crossing has passed by
            if(count%10 == 0){  //after a certain time
                crx = 499;      //send crossing location back at the beginning
                cry = 0;
            }
        }
        else{   //otherwise
            crx--; //keep moving the crossing across the window
        }
        if(crx == -499 && cry == 0){ //if the opponent car passes the user without colliding
            crx = cry = -999;   //reset opponent car position to beginning to start over
        }
        car_x += speedX; //update car x position
        car_y += speedY; //update car y position
        
        //case analysis to restrict car from going outside the left side of the screen
        if(car_x < 0)   //if the car has reached or gone under its min x axis value
            car_x = 0;  //keep it at its min x axis value
        
        //case analysis to restrict car from going outside the right side of the screen
        if(car_x+93 >= 500) //if the car has reached or gone over its max x axis value
            car_x = 500-93; //keep it at its max x axis value
        
        //case analysis to restrict car from going outside the right side of the road
        if(car_y <= 124)    //if the car has reached the the right side of the road or is trying to go further
            car_y = 124;    //keep the car where it is
        
        //case analysis to restrict car from going outside the left side of the road
        if(car_y >= 364-50) //if the car has reached the the left side of the road or is trying to go further
            car_y = 364-50; //keep the car where it is
        
        
        for(int i=0;i<this.nOpponent;i++){ //for all opponent cars
            this.lx[i] -= speedOpponent[i]; //move across the screen based on already calculated speed values
        }
        
        //next 16 lines unknown
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
                speedOpponent[c] = speedOpponent[i];
                c++;
            }
        }
        
        score += nOpponent - c; //score is incremented everytime an opponent car passes
        
        if(score > highScore)   //if the current score is higher than the high score
            highScore = score;  //update high score to the current score
        
        nOpponent = c;
        
        //Check for collision
        int diff = 0; //difference between users car and opponents car initially set to zero
        for(int i=0;i<nOpponent;i++){ //for all opponent cars
            diff = car_y - ly[i]; //diff is the distance between the user's car and the opponent car
            if((ly[i] >= car_y && ly[i] <= car_y+46) || (ly[i]+46 >= car_y && ly[i]+46 <= car_y+46)){   //if the cars collide vertically
                if(car_x+87 >= lx[i] && !(car_x >= lx[i]+87)){  //and if the cars collide horizontally
                    System.out.println("My car : "+car_x+", "+car_y);
                    System.out.println("Colliding car : "+lx[i]+", "+ly[i]);
                    this.finish(); //end game and print end message
                }
            }
        }
    }
    
    //function that will display message after user has lost the game
    void finish(){
        String str = "";    //create empty string that will be used for a congratulations method
        isFinished = true;  //indicates that game has finished to the rest of the program
        this.repaint();     //tells the window manager that the component has to be redrawn
        if(score == highScore && score != 0) //if the user scores a new high score, or the same high score
            str = "\nCongratulations!!! Its a high score";  //create a congratulations message
        JOptionPane.showMessageDialog(this,"Game Over!!!\nYour Score : "+score+"\nHigh Score : "+highScore+str,     "Game Over", JOptionPane.YES_NO_OPTION);    //displays the congratulations message and a message saying game over and the users score and the high score
        System.exit(ABORT); //terminate game
    }
    
    
    //function that handles input by user to move the user's car up, left, down and right
    public void moveCar(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){   //if user clicks on the up arrow key
            isUp = true;
            speedX = 1;     //moves car foward
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){ //if user clicks on the down arrow key
            isDown = true;
            speedX = -2;    //moves car backwards
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){ //if user clicks on the right arrow key
            isRight = true;
            speedY = 1;     //moves car to the right
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){ //if user clicks on the left arrow key
            isLeft = true;
            speedY = -1;    //moves car to the left
        }
    }
    
    //function that handles user input when the car is supposed to be stopped
    public void stopCar(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){   //if user clicks on the up arrow key
            isUp = false;
            speedX = 0; //set speed of car to zero
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){    //if user clicks on the down arrow key
            isDown = false;
            speedX = 0; //set speed of car to zero
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){    //if user clicks on the left arrow key
            isLeft = false;
            speedY = 0; //set speed of car to zero
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){   //if user clicks on the right arrow key
            isRight = false;
            speedY = 0; //set speed of car to zero
        }
    }
    
    //main method where the java application begins processing
    public static void main(String args[]){
        JFrame frame = new JFrame("Car Racing Game");   //creating a new JFrame window to display the game
        Game game = new Game(); //creating a new instance of a Game
        frame.add(game);		//Graphics2D components are added to JFrame Window
        frame.setSize(500,500); //setting size of screen to 500x500
        frame.setVisible(true); //allows the JFrame and its children to displayed on the screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int count = 1, c = 1;
        while(true){
            game.moveRoad(count);   //move the road
            while(c <= 1){
                game.repaint();     //redraw road to match new locations
                try{
                    Thread.sleep(5);    //wait so that the road appears to be moving continously
                }
                catch(Exception e){
                    System.out.println(e);
                }
                c++;
            }
            c = 1;
            count++; //increment count value
            if(game.nOpponent < 4 && count % 200 == 0){ //if there is less than 4 cars and count timer reaches 200
                game.imageLoc[game.nOpponent] = "images/car_left_"+((int)((Math.random()*100)%3)+1)+".png"; //assign images to the opponent cars
                game.lx[game.nOpponent] = 499; //set opponent cars start positions
                int p = (int)(Math.random()*100)%4; //create a random number that is the remainder of a number between 0 and 100 is divided by 4.
                if(p == 0){     //if the remainder is 0
                    p = 250;    //place the car in the fourth lane
                }
                else if(p == 1){ //if the remainder is 1
                    p = 300;    //place the car in the second lane
                }
                else if(p == 2){ //if the remainder is 2
                    p = 185;    //place the car in the third lane
                }
                else{           //otherwise
                    p = 130;    //place the car in the fourth  lane
                }
                game.ly[game.nOpponent] = p; //assign lane position to car
                game.speedOpponent[game.nOpponent] = (int)(Math.random()*100)%2 + 2; //sets the speed of the new opponent car to a random number that is the remainder of a number between 0 and 100, plus 2
                game.nOpponent++; //add the car to the game
            }
        }
    }
}
