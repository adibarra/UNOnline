import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Hand {
	
	ArrayList<Card> hand = new ArrayList<>();
	int xpos = 0;
	int ypos = 0;
	String name = "";
	
	Hand()
	{
		
	}
	
	public void draw(Graphics2D g2, String location)
	{
		if(Logic.singleplayer)
		{
			switch (location) {
				case "bottom":
					xpos = Logic.screenWidth / 2 - 50;
					ypos = Logic.screenHeight - 230;

					if (Tracker.whosTurn().equals(Logic.playerHand)) {
						g2.setColor(new Color(255, 255, 255, 128));
						g2.fill(new Rectangle(5, Logic.screenHeight - 250, Logic.screenWidth - 10, 220));
					}

					for (int k = 0; k < hand.size(); k++) {
						Card temp = hand.get(k);

						int buffer;

						if (hand.size() > 54)
							buffer = -44;
						else if (hand.size() > 44)
							buffer = -42;
						else if (hand.size() > 37)
							buffer = -40;
						else if (hand.size() > 29)
							buffer = -38;
						else if (hand.size() > 22)
							buffer = -35;
						else if (hand.size() > 18)
							buffer = -30;
						else if (hand.size() > 15)
							buffer = -25;
						else if (hand.size() > 13)
							buffer = -20;
						else if (hand.size() > 10)
							buffer = -15;
						else if (hand.size() > 8)
							buffer = -5;
						else
							buffer = 5;

						int x = (Logic.screenWidth / 2) - ((50 + buffer) * hand.size()) + (k * (100 + (buffer * 2))) - (hand.size());

						AffineTransform at = new AffineTransform();
						at.translate(x, Logic.screenHeight - 240);
						temp.setHitbox(at.createTransformedShape(new Rectangle(0, 0, 100, 200)));
						g2.setColor(temp.getCardColorColor());
						g2.fill(temp.getHitBox());
						g2.drawImage(ImageLoader.getImage(temp.getAbility()), at, null);
						g2.setColor(Color.BLACK);
						g2.draw(temp.getHitBox());

						if (temp.getHovered()) {
							g2.setColor(new Color(0, 0, 0, 30));
							g2.fill(hand.get(k).getHitBox());
							g2.setColor(Color.WHITE);
							Stroke oldStroke = g2.getStroke();
							g2.setStroke(new BasicStroke(4));
							g2.draw(temp.getHitBox());
							g2.setStroke(oldStroke);
						}
					}

					if (Tracker.whosTurn().equals(Logic.playerHand)) {
						g2.setColor(Color.GRAY);
						Stroke oldStroke = g2.getStroke();
						g2.setStroke(new BasicStroke(6));
						g2.draw(new Rectangle(5, Logic.screenHeight - 250, Logic.screenWidth - 10, 220));
						g2.setColor(Color.ORANGE);
						g2.setStroke(new BasicStroke(2));
						g2.draw(new Rectangle(5, Logic.screenHeight - 250, Logic.screenWidth - 10, 220));
						g2.setStroke(oldStroke);
					}
					break;
				case "left":
					xpos = Logic.screenWidth / 3 - 125;
					ypos = 0;

					if (Tracker.whosTurn().equals(Logic.comp3Hand)) {
						g2.setColor(new Color(255, 255, 255, 80));
						g2.fill(new Rectangle(Logic.screenWidth / 3 - 275, 0, 300, 165));
					}

					for (int k = 0; k < hand.size(); k++) {
						Card temp = hand.get(k);
						float scale = 0.75f;

						AffineTransform at = new AffineTransform();
						at.scale(scale, scale);
						at.translate((10 + ((float) k * ((Logic.screenWidth * (1 - scale) - 120) / (float) (hand.size())))) + 1.5 * (Logic.screenWidth / 10.0), 10);
						temp.setHitbox(at.createTransformedShape(new Rectangle(0, 0, 100, 200)));
						g2.drawImage(ImageLoader.getImage("unoBack"), at, null);
					}

					if (Tracker.whosTurn().equals(Logic.comp3Hand)) {
						g2.setColor(Color.GRAY);
						Stroke oldStroke = g2.getStroke();
						g2.setStroke(new BasicStroke(6));
						g2.draw(new Rectangle(Logic.screenWidth / 3 - 275, 0, 300, 165));
						g2.setColor(Color.ORANGE);
						g2.setStroke(new BasicStroke(2));
						g2.draw(new Rectangle(Logic.screenWidth / 3 - 275, 0, 300, 165));
						g2.setStroke(oldStroke);
					}
					break;
				case "middle":
					xpos = Logic.screenWidth / 2 - 50;
					ypos = 0;

					if (Tracker.whosTurn().equals(Logic.comp2Hand)) {
						g2.setColor(new Color(255, 255, 255, 80));
						g2.fill(new Rectangle(2 * Logic.screenWidth / 3 - 310, 0, 300, 165));
					}

					for (int k = 0; k < hand.size(); k++) {
						Card temp = hand.get(k);
						float scale = 0.75f;

						AffineTransform at = new AffineTransform();
						at.scale(scale, scale);
						at.translate((10 + ((float) k * ((Logic.screenWidth * (1 - scale) - 120) / (float) (hand.size())))) + 5.5 * (Logic.screenWidth / 10.0), 10);
						temp.setHitbox(at.createTransformedShape(new Rectangle(0, 0, 100, 200)));
						g2.drawImage(ImageLoader.getImage("unoBack"), at, null);
					}

					if (Tracker.whosTurn().equals(Logic.comp2Hand)) {
						g2.setColor(Color.GRAY);
						Stroke oldStroke = g2.getStroke();
						g2.setStroke(new BasicStroke(6));
						g2.draw(new Rectangle(2 * Logic.screenWidth / 3 - 310, 0, 300, 165));
						g2.setColor(Color.ORANGE);
						g2.setStroke(new BasicStroke(2));
						g2.draw(new Rectangle(2 * Logic.screenWidth / 3 - 310, 0, 300, 165));
						g2.setStroke(oldStroke);
					}
					break;
				case "right":
					xpos = 3 * Logic.screenWidth / 3 - 195;
					ypos = 0;

					if (Tracker.whosTurn().equals(Logic.comp1Hand)) {
						g2.setColor(new Color(255, 255, 255, 80));
						g2.fill(new Rectangle(3 * Logic.screenWidth / 3 - 345, 0, 300, 165));
					}

					for (int k = 0; k < hand.size(); k++) {
						Card temp = hand.get(k);
						float scale = 0.75f;

						AffineTransform at = new AffineTransform();
						at.scale(scale, scale);
						at.translate((10 + ((float) k * ((Logic.screenWidth * (1 - scale) - 120) / (float) (hand.size())))) + 9.5 * (Logic.screenWidth / 10.0), 10);
						temp.setHitbox(at.createTransformedShape(new Rectangle(0, 0, 100, 200)));
						g2.drawImage(ImageLoader.getImage("unoBack"), at, null);
					}

					if (Tracker.whosTurn().equals(Logic.comp1Hand)) {
						g2.setColor(Color.GRAY);
						Stroke oldStroke = g2.getStroke();
						g2.setStroke(new BasicStroke(6));
						g2.draw(new Rectangle(3 * Logic.screenWidth / 3 - 345, 0, 300, 165));
						g2.setColor(Color.ORANGE);
						g2.setStroke(new BasicStroke(2));
						g2.draw(new Rectangle(3 * Logic.screenWidth / 3 - 345, 0, 300, 165));
						g2.setStroke(oldStroke);
					}
					break;
			}
		}
		else
		{
			switch (location) {
				case "bottom":
					xpos = Logic.screenWidth / 2 - 50;
					ypos = Logic.screenHeight - 230;

					if (Client.whosTurn().equals(Logic.playerHand)) {
						g2.setColor(new Color(255, 255, 255, 128));
						g2.fill(new Rectangle(5, Logic.screenHeight - 250, Logic.screenWidth - 10, 220));
					}

					for (int k = 0; k < hand.size(); k++) {
						Card temp = hand.get(k);

						int buffer;

						if (hand.size() > 54)
							buffer = -44;
						else if (hand.size() > 44)
							buffer = -42;
						else if (hand.size() > 37)
							buffer = -40;
						else if (hand.size() > 29)
							buffer = -38;
						else if (hand.size() > 22)
							buffer = -35;
						else if (hand.size() > 18)
							buffer = -30;
						else if (hand.size() > 15)
							buffer = -25;
						else if (hand.size() > 13)
							buffer = -20;
						else if (hand.size() > 10)
							buffer = -15;
						else if (hand.size() > 8)
							buffer = -5;
						else
							buffer = 5;

						int x = (Logic.screenWidth / 2) - ((50 + buffer) * hand.size()) + (k * (100 + (buffer * 2))) - (hand.size());

						AffineTransform at = new AffineTransform();
						at.translate(x, Logic.screenHeight - 240);
						temp.setHitbox(at.createTransformedShape(new Rectangle(0, 0, 100, 200)));
						g2.setColor(temp.getCardColorColor());
						g2.fill(temp.getHitBox());
						g2.drawImage(ImageLoader.getImage(temp.getAbility()), at, null);
						g2.setColor(Color.BLACK);
						g2.draw(temp.getHitBox());

						if (temp.getHovered()) {
							g2.setColor(new Color(0, 0, 0, 30));
							g2.fill(hand.get(k).getHitBox());
							g2.setColor(Color.WHITE);
							Stroke oldStroke = g2.getStroke();
							g2.setStroke(new BasicStroke(4));
							g2.draw(temp.getHitBox());
							g2.setStroke(oldStroke);
						}
					}

					if (Client.whosTurn().equals(Logic.playerHand)) {
						g2.setColor(Color.GRAY);
						Stroke oldStroke = g2.getStroke();
						g2.setStroke(new BasicStroke(6));
						g2.draw(new Rectangle(5, Logic.screenHeight - 250, Logic.screenWidth - 10, 220));
						g2.setColor(Color.ORANGE);
						g2.setStroke(new BasicStroke(2));
						g2.draw(new Rectangle(5, Logic.screenHeight - 250, Logic.screenWidth - 10, 220));
						g2.setStroke(oldStroke);
					}

					g2.setColor(Color.BLACK);
					drawCenteredString(g2, name, new Rectangle2D.Double(5, Logic.screenHeight - 275, Logic.screenWidth / 3.0, 20), new Font("Ariel", Font.BOLD, 18));
					break;
				case "left":
					xpos = Logic.screenWidth / 3 - 125;
					ypos = 0;

					if (Client.whosTurn().equals(Client.h3)) {
						g2.setColor(new Color(255, 255, 255, 80));
						g2.fill(new Rectangle(Logic.screenWidth / 3 - 275, 0, 300, 165));
					}

					for (int k = 0; k < hand.size(); k++) {
						Card temp = hand.get(k);
						float scale = 0.75f;

						AffineTransform at = new AffineTransform();
						at.scale(scale, scale);
						at.translate((10 + ((float) k * ((Logic.screenWidth * (1 - scale) - 120) / (float) (hand.size())))) + 1.5 * (Logic.screenWidth / 10.0), 10);
						temp.setHitbox(at.createTransformedShape(new Rectangle(0, 0, 100, 200)));
						g2.drawImage(ImageLoader.getImage("unoBack"), at, null);
					}

					if (Client.whosTurn().equals(Client.h3)) {
						g2.setColor(Color.GRAY);
						Stroke oldStroke = g2.getStroke();
						g2.setStroke(new BasicStroke(6));
						g2.draw(new Rectangle(Logic.screenWidth / 3 - 275, 0, 300, 165));
						g2.setColor(Color.ORANGE);
						g2.setStroke(new BasicStroke(2));
						g2.draw(new Rectangle(Logic.screenWidth / 3 - 275, 0, 300, 165));
						g2.setStroke(oldStroke);
					}

					g2.setColor(Color.BLACK);
					drawCenteredString(g2, name, new Rectangle2D.Double(Logic.screenWidth / 3.0 - 275, 170, 300, 20), new Font("Ariel", Font.BOLD, 18));
					break;
				case "middle":
					xpos = Logic.screenWidth / 2 - 50;
					ypos = 0;

					if (Client.whosTurn().equals(Client.h2)) {
						g2.setColor(new Color(255, 255, 255, 80));
						g2.fill(new Rectangle(2 * Logic.screenWidth / 3 - 310, 0, 300, 165));
					}

					for (int k = 0; k < hand.size(); k++) {
						Card temp = hand.get(k);
						float scale = 0.75f;

						AffineTransform at = new AffineTransform();
						at.scale(scale, scale);
						at.translate((10 + ((float) k * ((Logic.screenWidth * (1 - scale) - 120) / (float) (hand.size())))) + 5.5 * (Logic.screenWidth / 10.0), 10);
						temp.setHitbox(at.createTransformedShape(new Rectangle(0, 0, 100, 200)));
						g2.drawImage(ImageLoader.getImage("unoBack"), at, null);
					}

					if (Client.whosTurn().equals(Client.h2)) {
						g2.setColor(Color.GRAY);
						Stroke oldStroke = g2.getStroke();
						g2.setStroke(new BasicStroke(6));
						g2.draw(new Rectangle(2 * Logic.screenWidth / 3 - 310, 0, 300, 165));
						g2.setColor(Color.ORANGE);
						g2.setStroke(new BasicStroke(2));
						g2.draw(new Rectangle(2 * Logic.screenWidth / 3 - 310, 0, 300, 165));
						g2.setStroke(oldStroke);
					}

					g2.setColor(Color.BLACK);
					drawCenteredString(g2, name, new Rectangle2D.Double(2 * Logic.screenWidth / 3.0 - 310, 170, 300, 20), new Font("Ariel", Font.BOLD, 18));
					break;
				case "right":
					xpos = 3 * Logic.screenWidth / 3 - 195;
					ypos = 0;

					if (Client.whosTurn().equals(Client.h1)) {
						g2.setColor(new Color(255, 255, 255, 80));
						g2.fill(new Rectangle(3 * Logic.screenWidth / 3 - 345, 0, 300, 165));
					}

					for (int k = 0; k < hand.size(); k++) {
						Card temp = hand.get(k);
						float scale = 0.75f;

						AffineTransform at = new AffineTransform();
						at.scale(scale, scale);
						at.translate((10 + ((float) k * ((Logic.screenWidth * (1 - scale) - 120) / (float) (hand.size())))) + 9.5 * (Logic.screenWidth / 10.0), 10);
						temp.setHitbox(at.createTransformedShape(new Rectangle(0, 0, 100, 200)));
						g2.drawImage(ImageLoader.getImage("unoBack"), at, null);
					}

					if (Client.whosTurn().equals(Client.h1)) {
						g2.setColor(Color.GRAY);
						Stroke oldStroke = g2.getStroke();
						g2.setStroke(new BasicStroke(6));
						g2.draw(new Rectangle(3 * Logic.screenWidth / 3 - 345, 0, 300, 165));
						g2.setColor(Color.ORANGE);
						g2.setStroke(new BasicStroke(2));
						g2.draw(new Rectangle(3 * Logic.screenWidth / 3 - 345, 0, 300, 165));
						g2.setStroke(oldStroke);
					}

					g2.setColor(Color.BLACK);
					drawCenteredString(g2, name, new Rectangle2D.Double(3 * Logic.screenWidth / 3.0 - 345, 170, 300, 20), new Font("Ariel", Font.BOLD, 18));
					break;
			}
		}	
	}
	
	public CardColor isPureColor()
	{
		int green = 0;
		int blue = 0;
		int yellow = 0;
		int red = 0;

		for (Card card : hand) {
			if (card.getCardColor().equals(CardColor.GREEN))
				green++;
			else if (card.getCardColor().equals(CardColor.BLUE))
				blue++;
			else if (card.getCardColor().equals(CardColor.YELLOW))
				yellow++;
			else if (card.getCardColor().equals(CardColor.RED))
				red++;
		}
		
		if(green != 0 && blue == 0 && yellow == 0 && red == 0)
			return CardColor.GREEN;
		if(blue != 0 && green == 0 && yellow == 0 && red == 0)
			return CardColor.BLUE;
		if(yellow != 0 && blue == 0 && green == 0 && red == 0)
			return CardColor.YELLOW;
		if(red != 0 && blue == 0 && yellow == 0 && green == 0)
			return CardColor.RED;
		return CardColor.NONE;
	}
	
	public CardColor getMostColor()
	{
		int green = 0;
		int blue = 0;
		int yellow = 0;
		int red = 0;

		for (Card card : hand) {
			if (card.getCardColor().equals(CardColor.GREEN))
				green++;
			else if (card.getCardColor().equals(CardColor.BLUE))
				blue++;
			else if (card.getCardColor().equals(CardColor.YELLOW))
				yellow++;
			else if (card.getCardColor().equals(CardColor.RED))
				red++;
		}
		
		if(green >= blue && green >= yellow && green >= red)
			return CardColor.GREEN;
		if(blue >= green && blue >= yellow && blue >= red)
			return CardColor.BLUE;
		if(yellow >= blue && yellow >= green && yellow >= red)
			return CardColor.YELLOW;
		return CardColor.RED;
	}

	public ArrayList<Card> getNumberCards()
	{
		ArrayList<Card> numbers = new ArrayList<>();
		for (Card card : hand) {
			if (card.getNumber() < 10)
				numbers.add(card);
		}
		return numbers;
	}
	
	public ArrayList<Card> getSymbolCards()
	{
		ArrayList<Card> symbols = new ArrayList<>();
		for (Card card : hand) {
			if (card.getNumber() > 9 && card.getNumber() < 13)
				symbols.add(card);
		}
		return symbols;
	}
	
	public ArrayList<Card> getWildCards()
	{
		ArrayList<Card> wilds = new ArrayList<>();
		for (Card card : hand) {
			if (card.getNumber() > 12)
				wilds.add(card);
		}
		return wilds;
	}
	
	public Card findBestPlay()
	{
		Card toPlay;
		Card last = Logic.last;
		ArrayList<Card> numbers = getNumberCards();
		ArrayList<Card> symbols = getSymbolCards();
		ArrayList<Card> wilds = getWildCards();
		
		if(!isPureColor().equals(CardColor.NONE))//check for wild if only 1 color remains && not already needed color
			if(wilds.size() > 0)
				if(!last.getCardColor().equals(isPureColor()))
				{
					toPlay = wilds.get(0);
					toPlay.setCardColor(isPureColor());
					//System.out.println("Only one color remains must use wild...");
					return toPlay;
				}

		//Check for same color (number cards)
		for (Card number : numbers)
			if (number.getCardColor().equals(last.getCardColor())) {
				//System.out.println("Found number card with same color");
				return number;
			}

		//Check for same number (number cards)
		for (Card number : numbers)
			if (number.getNumber() == last.getNumber()) {
				//System.out.println("Found number card with same number");
				return number;
			}

		//Check for same color (symbol cards)
		for (Card symbol : symbols)
			if (symbol.getCardColor().equals(last.getCardColor())) {
				//System.out.println("Found symbol card with same color");
				return symbol;
			}

		//Check for same number (symbol cards)
		for (Card symbol : symbols)
			if (symbol.getNumber() == last.getNumber()) {
				//System.out.println("Found symbol card with same symbol");
				return symbol;
			}
		
		if(wilds.size() > 0)//check for wilds and if so set to most needed color
		{
			toPlay = wilds.get(0);
			toPlay.setCardColor(getMostColor());
			//System.out.println("Using a wild card to set most needed color");
			return toPlay;
		}

		//last ditch effort
		for (Card card : hand)
			if (card.canPlay(last)) {
				//System.out.println("Found this during the last ditch effort");
				return card;
			}
		
		//draw a card and try again
		//System.out.println("Drawing a new card...");
		addToHand(Logic.deck.draw(1),true,true);
		toPlay = findBestPlay();
		return toPlay;
	}
	
	public void playCard(Card card)
	{	
		if(Logic.singleplayer)
		{
			Tracker.animators.add(new Animator(0,Tracker.whosTurn().xpos,Tracker.whosTurn().ypos,4*Logic.screenWidth/11,Logic.screenHeight/2-125,card));
			switch (card.getAbility()) {
				case "SKIP":
					Tracker.nextTurn();
					Tracker.nextTurn();
					break;
				case "REVERSE":
					Logic.direction = !Logic.direction;
					Tracker.nextTurn();
					break;
				case "WILD":
					if (card.getCardColor().equals(CardColor.NONE))
						Logic.drawChooser = true;
					else
						Tracker.nextTurn();
					break;
				case "DRAW2":
					Tracker.whosNext().addToHand(Logic.deck.draw(2), true, false);
					Tracker.nextTurn();
					Tracker.nextTurn();
					break;
				case "DRAW4WILD":
					Tracker.whosNext().addToHand(Logic.deck.draw(4), true, false);
					if (card.getCardColor().equals(CardColor.NONE))
						Logic.drawChooser = true;
					else {
						Tracker.nextTurn();
						Tracker.nextTurn();
					}
					break;
				default:
					Tracker.nextTurn();
					break;
			}
			
			hand.remove(card);
			Logic.tickTimer = Logic.tickTimerFull;
			Tracker.checkForWinners();
		}
	}
	
	public void addToHand(ArrayList<Card> cards, boolean animate, boolean instantAdd)
	{	
		if(animate)
		{
			for(int k = 0; k < cards.size(); k++)
			Tracker.animators.add(new Animator((k+1),Logic.deck.x,Logic.deck.y,xpos,ypos,cards.get(k),this,instantAdd));
		}
		else
			hand.addAll(cards);
	}
	
	public void addToHand(Card card, boolean animate, boolean instantAdd)
	{	
		if(animate)
			Tracker.animators.add(new Animator(1,Logic.deck.x,Logic.deck.y,xpos,ypos,card,this,instantAdd));
		else
			hand.add(card);
	}
	
	public void removeFromHand(int number)
	{	
		if(hand.size() > 0)
			if (number > 0) {
				hand.subList(0, number).clear();
			}
	}
	
	public boolean checkForWin()
	{
		return hand.size() == 0;
	}
	
	public static void drawCenteredString(Graphics g2, String text, Rectangle2D.Double rect, Font font) 
	{
	    FontMetrics metrics = g2.getFontMetrics(font);
	    int x = (int)(rect.getX()+(rect.getWidth()-metrics.stringWidth(text))/2);
	    int y = (int)(rect.getY()+((rect.getHeight()-metrics.getHeight())/2)+metrics.getAscent());
	    g2.setFont(font);
	    g2.drawString(text,x,y);
	}
	
	public void hoverCheck(int mousex, int mousey)
	{
		for (Card card : hand) card.setHovered(false);
			
		for(int k = hand.size()-1; k > -1; k--)
		{
			if(hand.get(k).getHitBox() != null)
				if(hand.get(k).getHitBox().contains(new Point(mousex,mousey)))
				{
					hand.get(k).setHovered(true);
					return;
				}
		}
	}
	
	public Card clickCheck(int clickx, int clicky)
	{
		for(int k = hand.size()-1; k > -1; k--)
			if(hand.get(k).getHitBox() != null)
				if(hand.get(k).getHitBox().contains(new Point(clickx,clicky)))
					return hand.get(k);
		return null;
	}
}
