import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client
{	
	static String MATCHMAKING_SERVER_IP = "localhost";
	static final int MATCHMAKING_PORT = 7777;
	
	static Thread clientThread = null;
	static boolean clientRunning = false;
	static boolean stopClient = false;
	static Socket connection = new Socket();
	static BufferedReader br = null;
	static BufferedWriter bw = null;
	static int TURNNUM = -1;
	
	static int turn = 0;
	static Hand h1 = new Hand();
	static Hand h2 = new Hand();
	static Hand h3 = new Hand();
	static ArrayList<Hand> hands = new ArrayList<>();
	
	static ArrayList<String> incommingCommandQueue = new ArrayList<>();
	static ArrayList<String> outGoingCommandQueue = new ArrayList<>();
	
	Client()
	{
	
	}
	
	public static void startClient()
	{
		resetClient();
		if(!clientRunning)
		{	
			try
			{
                MATCHMAKING_SERVER_IP = JOptionPane.showInputDialog("Enter the server IP Address.", "localhost");
                if(MATCHMAKING_SERVER_IP == null)
                	MATCHMAKING_SERVER_IP = "localhost";
				connection.connect(new InetSocketAddress(MATCHMAKING_SERVER_IP,MATCHMAKING_PORT),1000);

			} catch (IOException e) {

				Logic.reset();
				Tracker.menuAnimators.add(new Animator(3000,36,"Unable to connect to the server..."));
				return;
			}

			try
			{
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			}
			catch (IOException e1)
			{
				Logic.reset();
				Tracker.menuAnimators.add(new Animator(3000,36,"Unable to connect to the server..."));
				return;
			}
			
			clientThread = new Thread(() -> {
				try
				{
					Thread outputThread = new Thread(() -> {
						try
						{
							if(Logic.clientName.equals("Client"))
							{
								Logic.clientName = JOptionPane.showInputDialog("Enter your name.");
								if(Logic.clientName == null || Logic.clientName.equals(""))
									Logic.clientName = "Player "+Double.toHexString(Math.random()*100).replaceAll("\\.","");
							}
							bw.write("GAME "+Logic.PROJECT_NAME+"\n");
							bw.write("VERSION "+Logic.PROJECT_VERSION+"\n");
							bw.write("NAME "+Logic.clientName+"\n");
							bw.flush();

							while(!stopClient)
							{
								try
								{
									ArrayList<String> temp = new ArrayList<>(outGoingCommandQueue);
									for(String command : temp)
									{
										bw.write(command+"\n");
										bw.flush();
									}
									outGoingCommandQueue.removeAll(temp);
									Thread.sleep(250);
								}
								catch (InterruptedException ignored) {}
								catch (IOException e)
								{
									Logic.reset();
									Tracker.menuAnimators.add(new Animator(3000,"Disconnected..."));
								}
							}

							bw.write("DISCONNECT\n");
							bw.flush();
							bw.close();
						}
						catch (IOException e)
						{
							Logic.reset();
							Tracker.menuAnimators.add(new Animator(3000,"Disconnected..."));
						}
					});

					outputThread.start();

					while(!stopClient)
					{
						String temp;
						while((temp = br.readLine()) != null)
							incommingCommandQueue.add(temp);
					}
				}
				catch (IOException e)
				{
					Logic.reset();
					Tracker.menuAnimators.add(new Animator(3000,"Disconnected..."));
				}
			});
			
			clientThread.start();
		}
	}
	
	public static void stopClient()
	{
		stopClient = true;
	}
	
	public static void resetClient()
	{
		stopClient = false;
		clientThread = null;
		clientRunning = false;
		connection = new Socket();
		br = null;
		bw = null;
		TURNNUM = -1;
		turn = 0;
		h1 = new Hand();
		h2 = new Hand();
		h3 = new Hand();
		hands = new ArrayList<>();
		incommingCommandQueue = new ArrayList<>();
		outGoingCommandQueue = new ArrayList<>();
	}
	
	public static void exectuteCommand(String command)
	{
		String[] cmd = command.split(" ");
		
		if(cmd[0].equalsIgnoreCase("draw"))
			Logic.playerHand.addToHand(new Card(Integer.parseInt(cmd[1]),CardColor.valueOf(cmd[2])),true,false);
		else if(cmd[0].equalsIgnoreCase("drawna"))
			Logic.playerHand.addToHand(new Card(Integer.parseInt(cmd[1]),CardColor.valueOf(cmd[2])),false,false);
		else if(cmd[0].equalsIgnoreCase("drew"))
		{
			int num = Integer.parseInt(cmd[1]);
			if(TURNNUM != num)
				hands.get(num).addToHand(Logic.deck.populate().shuffle().draw(1),true,false);
		}
		else if(cmd[0].equalsIgnoreCase("animator"))
		{
			int num = Integer.parseInt(cmd[1]);
			Tracker.animators.add(new Animator(0,hands.get(num).xpos,hands.get(num).ypos,4*Logic.screenWidth/11,Logic.screenHeight/2-125,new Card(Integer.parseInt(cmd[2]),CardColor.valueOf(cmd[3]))));
			if(TURNNUM != num)
				hands.get(num).removeFromHand(1);
		}
		else if(cmd[0].equalsIgnoreCase("animator2"))
		{
			StringBuilder str = new StringBuilder();
			for(int k = 1; k < cmd.length; k++)
				str.append(cmd[k]).append(" ");
			Tracker.animators.add(new Animator(2000, str.toString()));
		}
		else if(cmd[0].equalsIgnoreCase("gameover"))
		{
			StringBuilder str = new StringBuilder();
			for(int k = 1; k < cmd.length; k++)
				str.append(cmd[k]).append(" ");
			Logic.gameOverStr = str.toString();
			Logic.gameOver = true;
		}
		else if(cmd[0].equalsIgnoreCase("getcolor"))
			Logic.drawChooser = true;
		else if(cmd[0].equalsIgnoreCase("setlast"))
			Logic.last = new Card(Integer.parseInt(cmd[1]),CardColor.valueOf(cmd[2]));
		else if(cmd[0].equalsIgnoreCase("lobbycount"))
			Logic.lobbycount = Integer.parseInt(cmd[1]);
		else if(cmd[0].equalsIgnoreCase("votecount"))
			Logic.votecount = Integer.parseInt(cmd[1]);
		else if(cmd[0].equalsIgnoreCase("gamestart"))
		{
			Logic.last = new Card(Integer.parseInt(cmd[1]),CardColor.valueOf(cmd[2]));
			
			if(TURNNUM == 0)
			{
				if(Logic.lobbycount > 0)
					hands.add(Logic.playerHand);
				if(Logic.lobbycount > 1)
					hands.add(h1);
				if(Logic.lobbycount > 2)
					hands.add(h2);
				if(Logic.lobbycount > 3)
					hands.add(h3);
			}
			else if(TURNNUM == 1)
			{
				if(Logic.lobbycount > 0)
					hands.add(h1);
				if(Logic.lobbycount > 1)
					hands.add(Logic.playerHand);
				if(Logic.lobbycount > 2)
					hands.add(h2);
				if(Logic.lobbycount > 3)
					hands.add(h3);
			}
			else if(TURNNUM == 2)
			{
				if(Logic.lobbycount > 0)
					hands.add(h1);
				if(Logic.lobbycount > 1)
					hands.add(h2);
				if(Logic.lobbycount > 2)
					hands.add(Logic.playerHand);
				if(Logic.lobbycount > 3)
					hands.add(h3);
			}
			else if(TURNNUM == 3)
			{
				if(Logic.lobbycount > 0)
					hands.add(h1);
				if(Logic.lobbycount > 1)
					hands.add(h2);
				if(Logic.lobbycount > 2)
					hands.add(h3);
				if(Logic.lobbycount > 3)
					hands.add(Logic.playerHand);
			}
			
			for(Hand hand : hands)
				if(!hand.equals(Logic.playerHand))
					hand.addToHand(Logic.deck.populate().shuffle().draw(7),true,false);
			
			StringBuilder nameCong = new StringBuilder();
			for(int k = 3; k < cmd.length; k++)
				nameCong.append(cmd[k]).append(" ");
			
			String[] names = nameCong.toString().split("%");
			if(names.length > 0 && hands.size() > 0)
				hands.get(0).name = names[0];
			if(names.length > 1 && hands.size() > 1)
				hands.get(1).name = names[1];
			if(names.length > 2 && hands.size() > 2)
				hands.get(2).name = names[2];
			if(names.length > 3 && hands.size() > 3)
				hands.get(3).name = names[3];
			
			Logic.mpReady = true;
		}
		else if(command.equalsIgnoreCase("dirswitch"))
			Logic.direction = !Logic.direction;
		else if(cmd[0].equalsIgnoreCase("turn"))
			turn = Integer.parseInt(cmd[1]);
		else if(cmd[0].equalsIgnoreCase("turnnum"))
			TURNNUM = Integer.parseInt(cmd[1]);
	}

	public static Hand whosTurn()
	{
		if(turn > hands.size()-1)
			turn = 0;
		else if(turn < 0)
			turn = hands.size()-1;
		
		return hands.get(turn);
	}
}
