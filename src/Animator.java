import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Animator {
	
	float startx = 0;
	float starty = 0;
	float currentx = 0;
	float currenty = 0;
    double speed = 2.0;
	int lifespan = 0;
	int size = 72;
	boolean drawing = true;
	boolean finished = false;
	Card card = null;
	double duration = 0;
	String str = "";


    Animator(double duration, String string)
	{
		this.duration = duration/2;
		this.str = string;
		
		new Thread(() -> {
            for(int k = 0; k < duration; k++)
            {
                Logic.delay(2);
                lifespan++;
            }
            finished = true;
        }).start();
	}
	
	Animator(double duration, int size, String string)
	{
		this.duration = duration/2;
		this.str = string;
		this.size = size;
		
		new Thread(() -> {
            for(int k = 0; k < duration; k++)
            {
                Logic.delay(2);
                lifespan++;
            }
            finished = true;
        }).start();
	}
	
	Animator(double delay, int startx, int starty, int endx, int endy, Card drawn, Hand hand, boolean instantAdd)
	{
		this.startx = startx;
		this.starty = starty;
		this.currentx = startx;
		this.currenty = starty;

        if(instantAdd)
			hand.hand.add(drawn);
		
		new Thread(() -> {
            Logic.delay(delay);
            while(!moveTo(endx,endy))
            {
                Logic.delay(2);
                lifespan++;
            }
            finished = true;
            if(!instantAdd)
                hand.hand.add(drawn);
        }).start();
	}
	
	Animator(double delay, int startx, int starty, int endx, int endy, Card toPlay)
	{
		this.startx = startx;
		this.starty = starty;
		this.currentx = startx;
		this.currenty = starty;
		card = toPlay;
		drawing = false;
		
		new Thread(() -> {
            Logic.delay(delay);
            while(!moveTo(endx,endy))
            {
                Logic.delay(2);
                lifespan++;
            }
            finished = true;
            Logic.last = toPlay;
        }).start();
	}
	
	public void draw(Graphics2D g2)
	{
		if(duration != 0)
		{
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)(1.0f-(lifespan/(duration*2)))));
			g2.setColor(new Color(255,255,255,192));
			g2.fillRect(0,0,Logic.screenWidth,Logic.screenHeight);
			g2.setColor(Color.BLACK);
			Logic.drawCenteredString(g2,str,new Rectangle2D.Double(0,0,Logic.screenWidth,Logic.screenHeight),new Font("Ariel",Font.BOLD,size));
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
		}
		else
		{
			AffineTransform at = new AffineTransform();
			at.translate(currentx,currenty);
			if(drawing)
				g2.drawImage(ImageLoader.getImage("unoBack"),at,null);
			else
			{
				Shape temp = at.createTransformedShape(new Rectangle(0,0,100,200));
				g2.setColor(card.getCardColorColor());
				g2.fill(temp);
				g2.drawImage(ImageLoader.getImage(card.getAbility()),at,null);
				g2.setColor(Color.BLACK);
				g2.draw(temp);
			}
		}
	}
	
	public boolean moveTo(float targetX, float targetY)
	{	
		float xDistance = targetX - currentx;
		float yDistance = targetY - currenty;
		double direction = Math.atan2(yDistance,xDistance);
		
		currentx += speed*Math.cos(direction);
		currenty += speed*Math.sin(direction);
				
		if(Logic.getDistance(targetX,targetY,currentx,currenty) <= speed*2)
		{
			currentx += (speed/2)*Math.cos(direction);
			currenty += (speed/2)*Math.sin(direction);
					
			if(Logic.getDistance(targetX,targetY,currentx,currenty) <= speed)
			{
				currentx = targetX;
				currenty = targetY;
				return true;
			}
		}
		
		return false;
	}

}
