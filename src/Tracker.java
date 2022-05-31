import java.util.ArrayList;

//Alec Ibarra
public class Tracker
{

	public static ArrayList<Button> buttons = new ArrayList<>();
	public static ArrayList<Hand> hands = new ArrayList<>();
	public static ArrayList<Animator> animators = new ArrayList<>();
	public static ArrayList<Animator> menuAnimators = new ArrayList<>();
	public static ArrayList<Animator> effects = new ArrayList<>();
	public static int turn = 0;
	
	public static void prepare()
	{
		buttons.clear();
		buttons.add(Logic.singleplayerButton);
		buttons.add(Logic.multiplayerButton);
	}
	
	public static void checkForWinners()
	{
		for(int k = 0; k < hands.size(); k++)
		{
			if(hands.get(k).equals(Logic.playerHand))
			{
				if(Logic.playerHand.checkForWin())
				{			
					hands.remove(Logic.playerHand);
					Logic.gameOverStr = "Congratulations you win!";
					Logic.gameOver = true;
					break;
				}
			}
			else if(hands.get(k).equals(Logic.comp1Hand))
			{
				if(Logic.comp1Hand.checkForWin())
				{			
					hands.remove(Logic.comp1Hand);
					effects.add(new Animator(2000,"Computer 1 has won!"));
					break;
				}
			} 
			else if(hands.get(k).equals(Logic.comp2Hand))
			{
				if(Logic.comp2Hand.checkForWin())
				{
					hands.remove(Logic.comp2Hand);
					effects.add(new Animator(2000,"Computer 2 has won!"));
					break;
				}
			}
			else if(hands.get(k).equals(Logic.comp3Hand))
			{
				if(Logic.comp3Hand.checkForWin())
				{
					hands.remove(Logic.comp3Hand);
					effects.add(new Animator(2000,"Computer 3 has won!"));
					break;
				}
			}
		}
		
		if(hands.size() == 1)
		{
			Logic.gameOver = true;
			if(hands.get(0).equals(Logic.playerHand))
				Logic.gameOverStr = "You lose!";
		}
		
		whosTurn();//fix value for turn
	}
	
	public static String whosTurnStr()
	{	
		if(hands.get(turn).equals(Logic.playerHand))
			return "Player's Turn";
		else if(hands.get(turn).equals(Logic.comp1Hand))
			return "Right Comp's Turn";
		else if(hands.get(turn).equals(Logic.comp2Hand))
			return "Middle Comp's Turn";
		else if(hands.get(turn).equals(Logic.comp3Hand))
			return "Left Comp's Turn";
		return "";
	}
	
	public static Hand whosTurn()
	{
		if(turn > hands.size()-1)
			turn = 0;
		else if(turn < 0)
			turn = hands.size()-1;
		
		return hands.get(turn);
	}
	
	public static Hand whosNext()
	{
		if(Logic.direction)
		{
			if(turn+1 > hands.size()-1)
				return hands.get(0);
			else
				return hands.get(turn+1);
		}
		else
		{
			if(turn-1 < 0)
				return hands.get(hands.size()-1);
			else
				return hands.get(turn-1);
		}
	}
	
	public static void nextTurn()
	{
		if(Logic.direction)
		{
			turn++;
			if(turn > hands.size()-1)
				turn = 0;
		}
		else
		{
			turn--;
			if(turn < 0)
				turn = hands.size()-1;
		}
	}
}
