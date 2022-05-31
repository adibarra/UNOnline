import java.awt.*;
import java.awt.geom.AffineTransform;

public class Card {

	int number;//0,1,2,3,4,5,6,7,8,9,skip,reverse,draw2,wild,draw4wild;(15)
	CardColor color;
	Shape hitbox;
	boolean hovered;
	
	Card()
	{
		number = 0;
		color = null;
		hitbox = null;
		hovered = false;
	}
	
	Card(int num, CardColor color)
	{
		this.number = num;
		this.color = color;
		this.hovered = false;
	}

	public void drawImage(Graphics2D g2, double x, double y)
	{
		AffineTransform at = new AffineTransform();
		at.translate(x,y);
		g2.setColor(getCardColorColor());
		g2.fill(new Rectangle((int)x,(int)y,100,200));
		g2.drawImage(ImageLoader.getImage(getAbility()),at,null);
		g2.setColor(Color.BLACK);
		g2.draw(new Rectangle((int)x,(int)y,100,200));
	}
	
	public boolean canPlay(Card previous)
	{
		if(number < 13)
			return (previous.getCardColor().equals(getCardColor()) || previous.getNumber() == getNumber());
		return true;
	}
	
	public void setHitbox(Shape newHitbox)
	{
		hitbox = newHitbox;
	}
	
	public Shape getHitBox()
	{
		return hitbox;
	}
	
	public void setHovered(boolean hovered)
	{
		this.hovered = hovered;
	}
	
	public boolean getHovered()
	{
		return hovered;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public String getAbility()
	{		
		if(number < 10)
			return ""+getNumber();
		else if(number == 10)
			return "SKIP";
		else if(number == 11)
			return "REVERSE";
		else if(number == 12)
			return "DRAW2";
		else if(number == 13)
			return "WILD";
		else if(number == 14)
			return "DRAW4WILD";
		return "No Special Ability";
	}
	
	public Color getCardColorColor()
	{
		if(color.equals(CardColor.RED))
			return new Color(255,0,0);
		else if(color.equals(CardColor.YELLOW))
			return new Color(255,255,0);
		else if(color.equals(CardColor.GREEN))
			return new Color(0,210,0);
		else if(color.equals(CardColor.BLUE))
			return new Color(0,0,255);
		return Color.WHITE;
	}
	
	public CardColor getCardColor()
	{
		return color;
	}
	
	public void setCardColor(CardColor cc)
	{
		color = cc;
	}
	
	public String toString()
	{
		return getCardColor()+" "+getAbility();
	}
}

enum CardColor
{
	RED,BLUE,YELLOW,GREEN,NONE
}
