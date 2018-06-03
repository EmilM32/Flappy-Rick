package flappyrick;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Main implements ActionListener, MouseListener, KeyListener
{

    public static Main main;
    public static final int WIDTH = 1200, HEIGHT = 800;
    
    public Render render;
    public Rectangle bird;
    public ArrayList<Rectangle> columns;
    public Random rand;
    public int ticks, yMontion, score;
    public boolean gameOver;
    public boolean started;
    public boolean shift = false;
    
    public Main()
    {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        
        render = new Render();
        rand = new Random();
        

        int szerokosc = Toolkit.getDefaultToolkit().getScreenSize().width;
        int wysokosc = Toolkit.getDefaultToolkit().getScreenSize().height;
        
        jframe.setIconImage(Toolkit.getDefaultToolkit().getImage("img" + File.separator + "icon.png"));
        jframe.setLocation((szerokosc-WIDTH)/2, (wysokosc-HEIGHT)/2);     
        jframe.add(render);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setTitle("Flappy Game!");
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setVisible(true);
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(3);
        
        bird = new Rectangle(WIDTH/2-10, HEIGHT/2-10, 20, 20);
        columns = new ArrayList<Rectangle>();
        

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        
        timer.start();
    }
    public void addColumn(boolean start)
    {
        int space = 350;
        int width = 78;
        int height = 50 + rand.nextInt(300); //minimum 50, maksimum 300
        
        if(start)
        {
           if(score <= 10)
           {
               columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
               columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
           }
           else if(score > 10 && score <= 20)
           {
               columns.add(new Rectangle(WIDTH + width + columns.size() * 200, HEIGHT - height - 120, width, height));
               columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 200, 0, width, HEIGHT - height - space));
           }
           else
           {
               columns.add(new Rectangle(WIDTH + width + columns.size() * 150, HEIGHT - height - 120, width, height));
               columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 150, 0, width, HEIGHT - height - space));
           }
        }
        else
        {
            if(score <= 10)
            {
                columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
                columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
            }
            else if(score > 10 && score <= 20)
            {
                columns.add(new Rectangle(columns.get(columns.size() - 1).x + 500, HEIGHT - height - 120, width, height));
                columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
            }
            else
            {
                columns.add(new Rectangle(columns.get(columns.size() - 1).x + 400, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
            }
            
        }
        
    }
    Image image_column = Toolkit.getDefaultToolkit().createImage("img" + File.separator + "pipe_part.png");   
    Image pipe = Toolkit.getDefaultToolkit().createImage("img" + File.separator + "pipe.png");   
    Image image_column2 = Toolkit.getDefaultToolkit().createImage("img" + File.separator + "pipe_part2.png");   
    Image pipe2 = Toolkit.getDefaultToolkit().createImage("img" + File.separator + "pipe2.png");   
    public void paintColumn(Graphics g, Rectangle column)
    {
        if(!shift)
        {
            g.drawImage(image_column, column.x, column.y, column.width, column.height, render);

            if(column.getBounds().y != 0)
            {
                g.drawImage(pipe, column.x, column.y, render); 
            }
            else
            {
                g.drawImage(pipe, column.x, column.y+column.height-32, render); //32 is a height of pipe
            }
        }
        else if(shift)
        {
            g.drawImage(image_column2, column.x, column.y, column.width, column.height, render);

            if(column.getBounds().y != 0)
            {
                g.drawImage(pipe2, column.x, column.y, render); 
            }
            else
            {
                g.drawImage(pipe2, column.x, column.y+column.height-32, render); //32 is a height of pipe
            }
        }
      
             
    }
    
    public void jump()
    {
        if(gameOver)
        {
            bird = new Rectangle(WIDTH/2-10, HEIGHT/2-10, 20, 20);
            columns.clear();
            yMontion = 0;
            score = 0;
                    
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            
            gameOver = false;
        }
        if(!started)
        {
            started = true;
        }
        else if(!gameOver)
        {
            if(yMontion > 0)
            {
                yMontion = 0;
            }
            yMontion -= 10;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        int speed = 10; //prędkość poruszania się ekranu w prawo
        ticks++;
        
        if(started)
        {
            for(int i = 0; i < columns.size(); i++)
            {
                Rectangle column = columns.get(i);
                column.x -= speed;
            }

            if(ticks%2 == 0 && yMontion < 15)
            {
                yMontion += 2; //prędkość spadania
            }

            for(int i = 0; i < columns.size(); i++)
            {
                Rectangle column = columns.get(i);

                if(column.x + column.width < 0) //cały czas nowe kolumny
                {
                    columns.remove(column);

                    if(column.y == 0)
                    {
                        addColumn(false);
                    }
                }
            }

            bird.y += yMontion;

            for(Rectangle column : columns)
            {
                if(column.y == 0 && bird.x + bird.width/2 > column.x + column.width/2 - 5 && bird.x + bird.width/2 < column.x + column.width/2 + 5) //czerwona kropka jest w centrum przejścia pomiędzy kolumnami
                {
                    score++;
                }
                if(column.intersects(bird))
                {
                    gameOver = true;
                    
                    if(bird.x <= column.x)
                    {
                        bird.x = column.x - bird.width;
                    }
                    else 
                    {
                        if(column.y != 0)
                        {
                            bird.y = column.y - bird.height;
                        }
                        else if(bird.y < column.height)
                        {
                            bird.y = column.height;
                        }
                    }
                    
                    
                }
            }
            if(bird.y > HEIGHT - 120 || bird.y < 0)
            {
                gameOver = true;
            }
            if(bird.y + yMontion >= HEIGHT - 120)
            {
                bird.y = HEIGHT - 120 - bird.height;              
            }
        }
        
        
        render.repaint();
    }
   Image background = Toolkit.getDefaultToolkit().createImage("img" + File.separator + "background.png");   
   Image background2 = Toolkit.getDefaultToolkit().createImage("img" + File.separator + "background2.png");   
   Image block = Toolkit.getDefaultToolkit().createImage("img" + File.separator + "block.png");   
   Image block2 = Toolkit.getDefaultToolkit().createImage("img" + File.separator + "block2.png");   
   Image rick = Toolkit.getDefaultToolkit().createImage("img" + File.separator + "rick.png");   
   Image red = Toolkit.getDefaultToolkit().createImage("img" + File.separator + "player_red.png");   
   Image img_score = Toolkit.getDefaultToolkit().createImage("img" + File.separator + "score.png");   
    public void repaint(Graphics g) 
    {
 
        background.getScaledInstance(WIDTH, HEIGHT, 0);
        background2.getScaledInstance(WIDTH, HEIGHT, 0);
        
        if(!shift)
            g.drawImage(background,0, 0, render);
        else if(shift)
            g.drawImage(background2,0, 0, render);
        
        for(int x = 0; x < WIDTH; x += 83)
        {
            if(!shift)
              g.drawImage(block, x, HEIGHT - 120, render);
            else if(shift)
              g.drawImage(block2, x, HEIGHT - 120, render);
        }

        if(!shift)
            g.drawImage(rick,bird.x, bird.y, 50, 60, render);
        else if(shift)
            g.drawImage(red,bird.x, bird.y, 48, 39, render);
        
        for(Rectangle column : columns)
        {
            paintColumn(g, column);
        }
        
        g.setColor(Color.black);
        g.setFont(new Font("Arial", 1, 100));
        
        if(!started)
        {
            g.drawString("Click to start!", WIDTH/2 - 300, HEIGHT/2 - 50);
        }
        if(gameOver)
        {
            g.drawString("Game Over", WIDTH/2 - 250, HEIGHT/2 - 50);
        }
        if(!gameOver && started)
        {
            g.setColor(Color.white);
            g.setFont(new Font("Verdana", 1, 30));
            g.drawImage(img_score, 1100, 0, render);
            if(score < 10)
                g.drawString(String.valueOf(score), 1120, 40);
            else
                g.drawString(String.valueOf(score), 1110, 40);
        }
        
        g.setFont(new Font("Arial", 1, 10));
        g.drawString("Press SHIFT to change design", 10, 10);
    }

    public static void main(String[] args) 
    {
        main =  new Main();        
    }

    @Override
    public void mouseClicked(MouseEvent me)
    {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent me) 
    {
        
    }

    @Override
    public void mouseReleased(MouseEvent me) 
    {
        
    }

    @Override
    public void mouseEntered(MouseEvent me) 
    {
       
    }

    @Override
    public void mouseExited(MouseEvent me) 
    {
        
    }

    @Override
    public void keyTyped(KeyEvent ke) 
    {

    }

    @Override
    public void keyPressed(KeyEvent ke)
    {

    }

    @Override
    public void keyReleased(KeyEvent ke)
    {
       if(ke.getKeyCode() == KeyEvent.VK_SPACE)
       {
           jump();
       }
       if(ke.getKeyCode() == KeyEvent.VK_ESCAPE)
       {
           System.exit(0);                  
       }
       if(ke.getKeyCode() == KeyEvent.VK_CONTROL)
       {
           score += 10;
       }
       if(ke.getKeyCode() == KeyEvent.VK_SHIFT)
       {
           shift = !shift;
       }
    }
                  
}

