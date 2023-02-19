import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;

public class panel extends JPanel implements ActionListener {

    static int width = 1200;
    static int height = 600;
    static int unit = 50;
    Timer timer;
    static int delay = 160;
    Random random;

    //food coordinates
    int fx,fy;

    //body length of the snake initially
    int body = 3;
    char dir = 'R';
    int score ;

    //to keep a check on the state of the game
    boolean flag = false;

    int siz = (height/unit)*(width/unit);
    //288 CHECK
    int xSnake[] = new int[siz];
    int ySnake[] = new int [siz];

    panel(){
        //size of panel
        this.setPreferredSize(new Dimension(width,height));

        this.setBackground(Color.BLACK);
        //to enable keyboard input
        this.setFocusable(true);
        //adding a keylistner
        this.addKeyListener(new myKey());

        random = new Random();
        gameStart();



    }
    public void gameStart(){
        flag = true;
        spawnFood();
        timer = new Timer(delay,this);
        timer.start();
    }

    public void spawnFood(){
        fx = random.nextInt((int) width/unit)*unit;
        fy = random.nextInt((int) height/unit)*unit;
    }

    public void paintComponent(Graphics graphic){
        super.paintComponent(graphic);
        draw(graphic);
    }

    public void draw(Graphics graphic){
        if(flag==true){
            graphic.setColor(Color.orange);
            graphic.fillOval(fx,fy,unit,unit);

            for(int i=0;i<body;i++){
                if(i==0){
                    graphic.setColor(Color.red);
                    graphic.fillRect(xSnake[i],ySnake[i],unit,unit);
                }
                else{
                    graphic.setColor(Color.green);
                    graphic.fillRect(xSnake[i],ySnake[i],unit,unit);
                }
            }
            //spawning the score display
            graphic.setColor(Color.CYAN);
            //setting font
            graphic.setFont(new Font("comic sans",Font.BOLD,40));
            FontMetrics fme = getFontMetrics(graphic.getFont());
            graphic.drawString("Score : "+score,(width-fme.stringWidth("Score"+score))/2, graphic.getFont().getSize());

        }
        else{
            gameover(graphic);
        }
    }

    public void gameover(Graphics graphic){
        //drawing the score
        graphic.setColor(Color.CYAN);
        //setting font
        graphic.setFont(new Font("comic sans",Font.BOLD,40));
        FontMetrics fme = getFontMetrics(graphic.getFont());
        graphic.drawString("Score : "+score,(width-fme.stringWidth("Score"+score))/2, graphic.getFont().getSize());

        //drawing the gameover text
        graphic.setColor(Color.red);
        //setting font
        graphic.setFont(new Font("comic sans",Font.BOLD,80));
        FontMetrics fme1 = getFontMetrics(graphic.getFont());
        graphic.drawString("GAME OVER",(width-fme1.stringWidth("GAME OVER"))/2, height/2);

        //drawing the replay prompt
        graphic.setColor(Color.green);
        //setting font
        graphic.setFont(new Font("comic sans",Font.BOLD,40));
        FontMetrics fme2 = getFontMetrics(graphic.getFont());
        graphic.drawString("Press R to replay",(width - fme2.stringWidth("Press R to replay"))/2, height/2 + 150);

    }

    public void move(){
        //for upcoming the rest of the body except the head
        for(int i=body;i>0;i--){
            xSnake[i] = xSnake[i-1];
            ySnake[i] = ySnake[i-1];
        }

        //updating the head of the snake
        switch(dir){
            case 'R':
                xSnake[0] = xSnake[0] + unit;
                break;
            case 'L':
                xSnake[0] = xSnake[0] - unit;
                break;
            case 'U':
                ySnake[0] = ySnake[0] - unit;
                break;
            case 'D':
                ySnake[0] = ySnake[0] + unit;
                break;
        }
    }

    public void check(){
        //checking out of bound
        if(xSnake[0]<0){
            flag = false;
        }
        else if(xSnake[0]>width){
            flag = false;
        }
        else if(ySnake[0]<0){
            flag = false;
        }
        else if(ySnake[0]>height){
            flag = false;
        }

        //checking hit with body
        for(int i=body;i>0;i--){
            if((xSnake[0]==xSnake[i]) && (ySnake[0]==ySnake[i])){
                flag = false;
            }
        }
        if(flag == false){
            timer.stop();
        }
    }

    public void eat(){
        if((xSnake[0]==fx) && (ySnake[0]==fy)){
            body++;
            score++;
            spawnFood();
        }
    }
























    public class myKey extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_UP:
                    if(dir!='D')
                        dir = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if(dir!='U')
                        dir = 'D';
                    break;
                case KeyEvent.VK_RIGHT:
                    if(dir!='L')
                        dir = 'R';
                    break;
                case KeyEvent.VK_LEFT:
                    if(dir!='R')
                        dir = 'L';
                    break;
                case KeyEvent.VK_R:
                    if(!flag){
                        score = 0;
                        body = 3;
                        dir = 'R';
                        Arrays.fill(xSnake,0);
                        Arrays.fill(ySnake,0);
                        gameStart();
                    }
                    break;
            }
        }


    }
    public void actionPerformed(ActionEvent e){
        if(flag){
            move();
            eat();
            check();
        }
        repaint();

    }
}
