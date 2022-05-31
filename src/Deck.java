import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	
	ArrayList<Card> deck = new ArrayList<>();
	int x = 6*Logic.screenWidth/11;
	int y = Logic.screenHeight/2-125;
		
	public void draw(Graphics g2)
	{
		x = 6*Logic.screenWidth/11+10;
		y = Logic.screenHeight/2-135;
		
		int xx = x;
		int yy = y;
		
		for(int k = 0; k < 10; k++)
		{
			if(k%2==0)
				g2.setColor(Color.BLACK);
			else
				g2.setColor(Color.WHITE);
			g2.drawRect(xx,yy,100,199);
			xx++;
			yy--;
		}
		g2.drawImage(ImageLoader.getImage("unoBack"),xx,yy,null);
	}
	
	public ArrayList<Card> draw(int num)
	{
		ArrayList<Card> drawn = new ArrayList<>();
		if(num <= deck.size() && deck.size() > 0)
		{
			for(int k = 0; k < num; k++)
				drawn.add(deck.remove(0));
		}
		else
		{
			int size = deck.size();
			if(size > 0)
				for(int k = 0; k < size; k++)
					drawn.add(deck.remove(0));
			populate().shuffle();
			for(int k = 0; k < num-size; k++)
				drawn.add(deck.remove(0));
		}
		return drawn;
	}
	
	public Deck populate()
	{
		deck.clear();
		deck.add(new Card(0,CardColor.BLUE));
		deck.add(new Card(0,CardColor.RED));
		deck.add(new Card(0,CardColor.YELLOW));
		deck.add(new Card(0,CardColor.GREEN));
		
		deck.add(new Card(1,CardColor.BLUE));
		deck.add(new Card(1,CardColor.RED));
		deck.add(new Card(1,CardColor.YELLOW));
		deck.add(new Card(1,CardColor.GREEN));
		deck.add(new Card(1,CardColor.BLUE));
		deck.add(new Card(1,CardColor.RED));
		deck.add(new Card(1,CardColor.YELLOW));
		deck.add(new Card(1,CardColor.GREEN));
		
		deck.add(new Card(2,CardColor.BLUE));
		deck.add(new Card(2,CardColor.RED));
		deck.add(new Card(2,CardColor.YELLOW));
		deck.add(new Card(2,CardColor.GREEN));
		deck.add(new Card(2,CardColor.BLUE));
		deck.add(new Card(2,CardColor.RED));
		deck.add(new Card(2,CardColor.YELLOW));
		deck.add(new Card(2,CardColor.GREEN));
		
		deck.add(new Card(3,CardColor.BLUE));
		deck.add(new Card(3,CardColor.RED));
		deck.add(new Card(3,CardColor.YELLOW));
		deck.add(new Card(3,CardColor.GREEN));
		deck.add(new Card(3,CardColor.BLUE));
		deck.add(new Card(3,CardColor.RED));
		deck.add(new Card(3,CardColor.YELLOW));
		deck.add(new Card(3,CardColor.GREEN));
		
		deck.add(new Card(4,CardColor.BLUE));
		deck.add(new Card(4,CardColor.RED));
		deck.add(new Card(4,CardColor.YELLOW));
		deck.add(new Card(4,CardColor.GREEN));
		deck.add(new Card(4,CardColor.BLUE));
		deck.add(new Card(4,CardColor.RED));
		deck.add(new Card(4,CardColor.YELLOW));
		deck.add(new Card(4,CardColor.GREEN));
		
		deck.add(new Card(5,CardColor.BLUE));
		deck.add(new Card(5,CardColor.RED));
		deck.add(new Card(5,CardColor.YELLOW));
		deck.add(new Card(5,CardColor.GREEN));
		deck.add(new Card(5,CardColor.BLUE));
		deck.add(new Card(5,CardColor.RED));
		deck.add(new Card(5,CardColor.YELLOW));
		deck.add(new Card(5,CardColor.GREEN));
		
		deck.add(new Card(6,CardColor.BLUE));
		deck.add(new Card(6,CardColor.RED));
		deck.add(new Card(6,CardColor.YELLOW));
		deck.add(new Card(6,CardColor.GREEN));
		deck.add(new Card(6,CardColor.BLUE));
		deck.add(new Card(6,CardColor.RED));
		deck.add(new Card(6,CardColor.YELLOW));
		deck.add(new Card(6,CardColor.GREEN));
		
		deck.add(new Card(7,CardColor.BLUE));
		deck.add(new Card(7,CardColor.RED));
		deck.add(new Card(7,CardColor.YELLOW));
		deck.add(new Card(7,CardColor.GREEN));
		deck.add(new Card(7,CardColor.BLUE));
		deck.add(new Card(7,CardColor.RED));
		deck.add(new Card(7,CardColor.YELLOW));
		deck.add(new Card(7,CardColor.GREEN));
		
		deck.add(new Card(8,CardColor.BLUE));
		deck.add(new Card(8,CardColor.RED));
		deck.add(new Card(8,CardColor.YELLOW));
		deck.add(new Card(8,CardColor.GREEN));
		deck.add(new Card(8,CardColor.BLUE));
		deck.add(new Card(8,CardColor.RED));
		deck.add(new Card(8,CardColor.YELLOW));
		deck.add(new Card(8,CardColor.GREEN));
		
		deck.add(new Card(9,CardColor.BLUE));
		deck.add(new Card(9,CardColor.RED));
		deck.add(new Card(9,CardColor.YELLOW));
		deck.add(new Card(9,CardColor.GREEN));
		deck.add(new Card(9,CardColor.BLUE));
		deck.add(new Card(9,CardColor.RED));
		deck.add(new Card(9,CardColor.YELLOW));
		deck.add(new Card(9,CardColor.GREEN));
		
		deck.add(new Card(10,CardColor.BLUE));//SKIP
		deck.add(new Card(10,CardColor.RED));
		deck.add(new Card(10,CardColor.YELLOW));
		deck.add(new Card(10,CardColor.GREEN));
		deck.add(new Card(10,CardColor.BLUE));
		deck.add(new Card(10,CardColor.RED));
		deck.add(new Card(10,CardColor.YELLOW));
		deck.add(new Card(10,CardColor.GREEN));
		
		
		deck.add(new Card(11,CardColor.BLUE));//REVERSE
		deck.add(new Card(11,CardColor.RED));
		deck.add(new Card(11,CardColor.YELLOW));
		deck.add(new Card(11,CardColor.GREEN));
		deck.add(new Card(11,CardColor.BLUE));
		deck.add(new Card(11,CardColor.RED));
		deck.add(new Card(11,CardColor.YELLOW));
		deck.add(new Card(11,CardColor.GREEN));
		
		deck.add(new Card(12,CardColor.BLUE));//DRAW2
		deck.add(new Card(12,CardColor.RED));
		deck.add(new Card(12,CardColor.YELLOW));
		deck.add(new Card(12,CardColor.GREEN));
		deck.add(new Card(12,CardColor.BLUE));
		deck.add(new Card(12,CardColor.RED));
		deck.add(new Card(12,CardColor.YELLOW));
		deck.add(new Card(12,CardColor.GREEN));
		
		deck.add(new Card(13,CardColor.NONE));//WILD
		deck.add(new Card(13,CardColor.NONE));
		deck.add(new Card(13,CardColor.NONE));
		deck.add(new Card(13,CardColor.NONE));
		
		deck.add(new Card(14,CardColor.NONE));//DRAW4WILD
		deck.add(new Card(14,CardColor.NONE));
		deck.add(new Card(14,CardColor.NONE));
		deck.add(new Card(14,CardColor.NONE));
		
		return this;
	}
	
	public Deck shuffle()
	{
		Collections.shuffle(deck);
		return this;
	}
}
