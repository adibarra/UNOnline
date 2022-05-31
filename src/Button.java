import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Button {
	
	String buttonStr;
	Rectangle button;
	Rectangle outline;

	Button(String str, int x, int y, int width, int height)
	{
		buttonStr = str;
		button = new Rectangle(x-width/2,y-height/2,width,height);
		outline = new Rectangle(x-width/2,y-height/2,width-1,height-1);
	}
	
	public void draw(Graphics g2)
	{
		int[] rgb = ImageLoader.getPixelColorAt((int)(button.x+button.getWidth()/2),(int)(button.y+button.getHeight()/2));
		
		g2.setColor(Color.LIGHT_GRAY);
		((Graphics2D) g2).fill(getButton());
		if(rgb[0] > 128 && rgb[1] > 128 && rgb[2] > 128)
			g2.setColor(Color.BLACK);
		else
			g2.setColor(Color.WHITE);
		((Graphics2D) g2).draw(outline);
		g2.setColor(Color.BLACK);
		drawCenteredString(g2,buttonStr,new Rectangle2D.Double(getButton().getX(),getButton().getY(),getButton().getWidth(),getButton().getHeight()),new Font("Ariel",Font.BOLD,18));
	}

	public Rectangle getButton()
	{
		return button;
	}
	
	public void setButtonStr(String newButtonStr)
	{
		buttonStr = newButtonStr;
	}
	
	public String getButtonStr()
	{
		return buttonStr;
	}
	
	public boolean contains(int mousex, int mousey)
	{
		return button.contains(mousex,mousey);
	}
	
	public static void drawCenteredString(Graphics g2, String text, Rectangle2D.Double rect, Font font) 
	{
	    FontMetrics metrics = g2.getFontMetrics(font);
	    int x = (int)(rect.getX()+(rect.getWidth()-metrics.stringWidth(text))/2);
	    int y = (int)(rect.getY()+((rect.getHeight()-metrics.getHeight())/2)+metrics.getAscent());
	    g2.setFont(font);
	    g2.drawString(text,x,y);
	}
}
