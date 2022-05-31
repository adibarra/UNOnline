import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class UNOnlineServer
{
	static final int PORT = 7777;
	static ArrayList<ConnectionThread> availablePlayers = new ArrayList<>();
	static ArrayList<LobbyManager> lobbyManagers = new ArrayList<>();
	static MatchMaker mm = new MatchMaker();
	static boolean serverRunning = true;
	static int playersConnected = 0;

	static final double PROJECT_VERSION = 1.0;
	static final String PROJECT_NAME = "UNO";

    static final String ANSI_GREEN = "";//"\033[0;32m";
    static final String ANSI_YELLOW = "";//"\033[0;33m";
    static final String ANSI_RED = "";//"\033[0;31m";
    static final String ANSI_BLUE = "";//"\033[0;34m";
    static final String ANSI_RESET = "";//"\033[0m";


    public static void main(String[] args)
	{
		ServerSocket serverSocket = null;
		Socket socket = null;

		try
        {

			System.out.println();
			System.out.println("[INFO] "+ANSI_GREEN+"UNOnlineServer is now running on port "+PORT+"."+ANSI_RESET);
			serverSocket = new ServerSocket(PORT);
			System.out.println("[INFO] Starting matchmaking thread.");
			mm.start();
			System.out.println("[INFO] Spinning up a lobby.");
			UNOnlineServer.lobbyManagers.add(new LobbyManager());
			System.out.println("[INFO] Waiting for players...");

			//noinspection AnonymousHasLambdaAlternative
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run()
				{
					System.out.println("\n[INFO] "+ANSI_RED+"Server shutting down."+ANSI_RESET);
					UNOnlineServer.serverRunning = false;

					for (ConnectionThread availablePlayer : availablePlayers) availablePlayer.kick();

					for (LobbyManager lobbyManager : lobbyManagers) lobbyManager.killLobby();

					mm.kill();
					Thread.currentThread().interrupt();

					if(UNOnlineServer.playersConnected == 0)
						System.out.println("[INFO] Server shutdown successful.");
				}
			});
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		while(serverRunning)
		{
			try
			{
				assert serverSocket != null;
				socket = serverSocket.accept();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			ConnectionThread newConnection = new ConnectionThread(socket);
			newConnection.start();
			availablePlayers.add(newConnection);
			playersConnected++;
			System.out.println("[GAME] A player has connected to the server. ("+playersConnected+" connected)");
		}

		try
		{
			assert socket != null;
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void shutdownServer()
	{
		System.exit(0);
	}

}

class ConnectionThread extends Thread
{
	Socket socket;
	int turnnum = 0;
	String clientname = "";
	BufferedReader br = null;
	BufferedWriter bw = null;
	boolean connected = true;
	boolean killOutputThread = false;
	ArrayList<String> incommingCommandQueue = new ArrayList<>();
	ArrayList<String> outGoingCommandQueue = new ArrayList<>();

	public ConnectionThread(Socket clientSocket)
	{
		this.socket = clientSocket;
	}

	public void kick()
	{
		int num = 0;
		LobbyManager temp = null;

		for(LobbyManager lm : UNOnlineServer.lobbyManagers)
		{
			num++;
			for(ConnectionThread ct : lm.connectedPlayers)
				if(ct.equals(this))
				{
					temp = lm;
					break;
				}
		}

		if(temp != null)
		{
			temp.removePlayer(this);
			System.out.println("[GAME] A player has been kicked from lobby #"+num+". ("+temp.connectedPlayers.size()+"/"+temp.MAX_PLAYERS+" players)");
			System.out.println("[GAME] A player has been kicked from the server. ("+UNOnlineServer.playersConnected+" connected)");
		}
		else
		{
			System.out.println("[GAME] A player waiting for a lobby has been kicked from the server.");
		}

		UNOnlineServer.playersConnected--;
		UNOnlineServer.availablePlayers.remove(this);
		killOutputThread = true;
		connected = false;

		try
		{
			sleep(500);
			socket.close();
		}
		catch (InterruptedException ignored){}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		Thread.currentThread().interrupt();
	}

	public void disconnect()
	{
		int num = 0;
		LobbyManager temp = null;

		for(LobbyManager lm : UNOnlineServer.lobbyManagers)
		{
			num++;
			for(ConnectionThread ct : lm.connectedPlayers)
				if(ct.equals(this))
				{
					temp = lm;
					break;
				}
		}

		if(temp != null)
		{
			temp.removePlayer(this);
			UNOnlineServer.playersConnected--;
			System.out.println("[GAME] "+UNOnlineServer.ANSI_YELLOW+"A player has left lobby #"+num+". ("+temp.connectedPlayers.size()+"/"+temp.MAX_PLAYERS+" players)"+UNOnlineServer.ANSI_RESET);
			System.out.println("[GAME] "+UNOnlineServer.ANSI_YELLOW+"A player has disconnected from the server. ("+UNOnlineServer.playersConnected+" connected)"+UNOnlineServer.ANSI_RESET);
		}
		else
		{
			UNOnlineServer.playersConnected--;
			UNOnlineServer.availablePlayers.remove(this);
			System.out.println("[GAME] "+UNOnlineServer.ANSI_YELLOW+"A player waiting for a lobby has disconnected from the server."+UNOnlineServer.ANSI_RESET);
		}

		killOutputThread = true;
		connected = false;

		try
		{
			sleep(500);
			socket.close();
		}
		catch (InterruptedException ignored){}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		Thread.currentThread().interrupt();
	}

	public void run()
	{
		try
		{
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		}
		catch (IOException e)
		{
			return;
		}

		//noinspection AnonymousHasLambdaAlternative
		new Thread()//output processing loop thread
		{
			public void run()
			{
				try
				{
					while(!killOutputThread && UNOnlineServer.serverRunning)
					{
						if((int)(Math.random()*200) == 0)
							outGoingCommandQueue.add("checkalive");

						ArrayList<String> temp = new ArrayList<>(outGoingCommandQueue);
						for(String command : temp)
						{
							bw.write(command);
							bw.newLine();
							bw.flush();
						}

						outGoingCommandQueue.removeAll(temp);
						sleep(500);
					}
					Thread.currentThread().interrupt();
				}
				catch (IOException | InterruptedException e)
				{
				    //Normal disconnect
					//System.err.println("[WARN] An output thread has encountered an error. Likely an improper disconnect.");
					disconnect();
				}
			}

		}.start();

		String line;
		while(UNOnlineServer.serverRunning)//input proccessing loop
		{
			try
			{
				line = br.readLine();
				if(line == null)
				{
					disconnect();
					return;
				}
				else
				{
					if(line.split(" ")[0].equalsIgnoreCase("GAME"))
					{
						String[] cmd = line.split(" ");
						StringBuilder str = new StringBuilder();
						for(int k = 1; k < cmd.length; k++)
							str.append(cmd[k]).append(" ");
						str.setLength(str.length() - 1);
						String clientProjectName = str.toString();

						if(!clientProjectName.equalsIgnoreCase(UNOnlineServer.PROJECT_NAME))
						{
							System.out.println("[GAME] "+UNOnlineServer.ANSI_RED+"A Player was kicked for using an incompatible client("+clientProjectName+")."+UNOnlineServer.ANSI_RESET);
							disconnect();
						}
					}
					else if(line.split(" ")[0].equalsIgnoreCase("VERSION"))
					{
						String[] cmd = line.split(" ");
						StringBuilder str = new StringBuilder();
						for(int k = 1; k < cmd.length; k++)
							str.append(cmd[k]).append(" ");
						str.setLength(str.length() - 1);
						String clientProjectVersion = str.toString();
						String serverProjectVersion = String.valueOf(UNOnlineServer.PROJECT_VERSION);

						if(!clientProjectVersion.equalsIgnoreCase(serverProjectVersion))
						{
							System.out.println("[GAME] "+UNOnlineServer.ANSI_RED+"A Player was kicked for using an incompatible client version("+clientProjectVersion+")."+UNOnlineServer.ANSI_RESET);
							disconnect();
						}
					}
					else if(line.split(" ")[0].equalsIgnoreCase("NAME"))
					{
						String[] cmd = line.split(" ");
						StringBuilder str = new StringBuilder();
						for(int k = 1; k < cmd.length; k++)
							str.append(cmd[k]).append(" ");
						str.setLength(str.length() - 1);
						clientname = str.toString();
					}
					else if(line.equalsIgnoreCase("DISCONNECT"))
						disconnect();
					else if(line.equalsIgnoreCase("SHUTDOWN"))
						UNOnlineServer.shutdownServer();
					else if(line.equalsIgnoreCase("INPUT"))
						outGoingCommandQueue.add(JOptionPane.showInputDialog("Input a message to send to the client."));
					else
						incommingCommandQueue.add(line);
				}
			}
			catch (IOException e)
			{
                //Normal disconnect
                //System.err.println("[WARN] An output thread has encountered an error. Likely an improper disconnect.");
				disconnect();
				return;
			}
		}
	}
}

class MatchMaker extends Thread
{
	public MatchMaker()
	{

	}

	public void kill()
	{
		Thread.currentThread().interrupt();
	}

	public void run()
	{
		while(UNOnlineServer.serverRunning)
		{
			if(UNOnlineServer.availablePlayers.size() > 0)
			{
				boolean lobbyFound = false;
				for(int k = 0; k < UNOnlineServer.lobbyManagers.size(); k++)
				{
					LobbyManager temp = UNOnlineServer.lobbyManagers.get(k);
					if(temp.delLobby)
					{
						UNOnlineServer.lobbyManagers.remove(k);
						k--;
					}

					if(!temp.started && !temp.isFull())
					{
						lobbyFound = true;
						temp.addPlayer(UNOnlineServer.availablePlayers.get(0));
						System.out.println("[GAME] A player has joined lobby #"+(k+1)+". ("+temp.connectedPlayers.size()+"/"+temp.MAX_PLAYERS+" players)");
						break;
					}
				}

				if(!lobbyFound)
				{
					System.out.println("[GAME] "+UNOnlineServer.ANSI_BLUE+"Spinning up another lobby. (#"+(UNOnlineServer.lobbyManagers.size()+1)+")"+UNOnlineServer.ANSI_RESET);
					UNOnlineServer.lobbyManagers.add(new LobbyManager());
				}
			}
			else
			{
				try
				{
					sleep(1000);
				}
				catch (InterruptedException ignored) {}
			}
		}
	}
}

class LobbyManager extends Thread
{
	int turn = 0;
	boolean direction;

	Deck deck = new Deck();
	int MAX_PLAYERS = 4;
	int votestart = 0;
	boolean started = false;
	boolean lobbyActive = true;
	boolean delLobby = false;
	ArrayList<ConnectionThread> connectedPlayers = new ArrayList<>();

	public LobbyManager()
	{
		this.start();
	}

	public void killLobby()
	{
		delLobby = true;
		lobbyActive = false;
		System.out.println("[GAME] "+UNOnlineServer.ANSI_RED+"Shutting down a lobby."+UNOnlineServer.ANSI_RESET);
		ArrayList<ConnectionThread> temp = new ArrayList<>(connectedPlayers);
		for(ConnectionThread player : temp)
			player.kick();
		Thread.currentThread().interrupt();
	}

	public void prepLobby()
	{
		deck.populate().shuffle();
		Card card = deck.draw(1).get(0);

		while(card.getAbility().equals("WILD") || card.getAbility().equals("DRAW4WILD"))
			card = deck.draw(1).get(0);

		StringBuilder names = new StringBuilder();
		for(ConnectionThread pl : connectedPlayers)
			names.append(pl.clientname.replaceAll("%", "")).append("%");

		for(ConnectionThread player : connectedPlayers)
		{
			player.outGoingCommandQueue.add("gamestart "+card.getNumber()+" "+card.getCardColor()+" "+names);

			for(int k = 0; k < 7; k++)
			{
				Card temp = deck.draw(1).get(0);
				player.outGoingCommandQueue.add("drawna "+temp.getNumber()+" "+temp.getCardColor());
			}
		}
	}

	public void run()
	{
		while(UNOnlineServer.serverRunning && lobbyActive)
		{
			if(!started)
			{
				if(votestart >= connectedPlayers.size() && connectedPlayers.size() > 0)
				{
					started = true;
					int lobbynum = 0;
					for(LobbyManager lm : UNOnlineServer.lobbyManagers)
					{
						lobbynum++;
						if(lm.equals(this))
						{
							prepLobby();
							System.out.println("[GAME] "+UNOnlineServer.ANSI_BLUE+"Lobby #"+lobbynum+" is now starting."+UNOnlineServer.ANSI_RESET);
							break;
						}
					}
				}

				for(int k = 0; k < connectedPlayers.size(); k++)
				{
					ArrayList<String> temp = new ArrayList<>(connectedPlayers.get(k).incommingCommandQueue);

					for(String command : temp)
						parseCommand(connectedPlayers.get(k),command);

					connectedPlayers.get(k).incommingCommandQueue.removeAll(temp);
					connectedPlayers.get(k).outGoingCommandQueue.add("lobbycount "+connectedPlayers.size());
					connectedPlayers.get(k).outGoingCommandQueue.add("votecount "+votestart);
				}

				try
				{
					sleep(500);
				}
				catch (InterruptedException ignored) {}
			}
			else
			{
				if(connectedPlayers.size() > 0)
				{
					for (ConnectionThread connectedPlayer : connectedPlayers) {
						ArrayList<String> temp = new ArrayList<>(connectedPlayer.incommingCommandQueue);

						for (String command : temp)
							parseCommand(connectedPlayer, command);

						connectedPlayer.incommingCommandQueue.removeAll(temp);
					}
				}
				else
				{
					if(!delLobby)
						killLobby();
				}

				try
				{
					sleep(100);
				}
				catch (InterruptedException ignored) {}
			}
		}

		while(UNOnlineServer.serverRunning && connectedPlayers.size() > 0)
			Logic.delay(250);
		if(!delLobby)
			killLobby();
	}

	public void parseCommand(ConnectionThread player, String command)
	{
		String[] cmd = command.split(" ");

		if(command.equalsIgnoreCase("draw"))
		{
			Card temp = deck.draw(1).get(0);
			player.outGoingCommandQueue.add("draw "+temp.getNumber()+" "+temp.getCardColor());

			for(ConnectionThread pl : connectedPlayers)
				if(!pl.equals(player))
					pl.outGoingCommandQueue.add("drew "+turn);
		}
		else if(cmd[0].equalsIgnoreCase("play"))
		{
			if(turn == player.turnnum)
				playCard(player,new Card(Integer.parseInt(cmd[1]),CardColor.valueOf(cmd[2])));
			else
				player.outGoingCommandQueue.add("drawna "+cmd[1]+" "+cmd[2]);
		}
		else if(cmd[0].equalsIgnoreCase("colorchosen"))
		{
			for(ConnectionThread pl : connectedPlayers)
				pl.outGoingCommandQueue.add("setlast "+cmd[1]+" "+cmd[2]);

			if(Integer.parseInt(cmd[1]) == 14)//draw4wild
				nextTurn();
			nextTurn();
		}
		else if(command.equalsIgnoreCase("win"))
		{
			for(ConnectionThread pl : connectedPlayers)
			{
				if(pl.equals(player))
					pl.outGoingCommandQueue.add("gameover You Win!");
				else
					pl.outGoingCommandQueue.add("gameover You Lose!");
			}
			lobbyActive = false;
		}
		else if(command.equalsIgnoreCase("getLobbyCount"))
		{
			player.outGoingCommandQueue.add("lobbycount "+connectedPlayers.size());
		}
		else if(command.equalsIgnoreCase("voteStart"))
		{
			votestart++;
			int lobbynum = 0;
			for(LobbyManager lm : UNOnlineServer.lobbyManagers)
			{
				lobbynum++;
				for(ConnectionThread ct : lm.connectedPlayers)
					if(ct.equals(player))
					{
						System.out.println("[GAME] A player has voted to start lobby #"+lobbynum+". ("+votestart+"/"+connectedPlayers.size()+" votes)");
						return;
					}
			}
		}
	}

	public void playCard(ConnectionThread player, Card card)
	{
		for(ConnectionThread pl : connectedPlayers)
			pl.outGoingCommandQueue.add("animator "+turn+" "+card.getNumber()+" "+card.getCardColor());

		switch (card.getAbility()) {
			case "SKIP":
				nextTurn();
				nextTurn();
				break;
			case "REVERSE":
				direction = !direction;
				nextTurn();

				for (ConnectionThread pl : connectedPlayers)
					pl.outGoingCommandQueue.add("dirswitch");
				break;
			case "WILD":
				if (card.getCardColor().equals(CardColor.NONE))
					player.outGoingCommandQueue.add("getcolor");
				else
					nextTurn();
				break;
			case "DRAW2":
				for (ConnectionThread pl : connectedPlayers)
					if (!pl.equals(connectedPlayers.get(whosNext()))) {
						pl.outGoingCommandQueue.add("drew " + whosNext());
						pl.outGoingCommandQueue.add("drew " + whosNext());
					} else {
						for (int k = 0; k < 2; k++) {
							Card temp = deck.draw(1).get(0);
							pl.outGoingCommandQueue.add("draw " + temp.getNumber() + " " + temp.getCardColor());
						}
					}

				nextTurn();
				nextTurn();
				break;
			case "DRAW4WILD":
				for (ConnectionThread pl : connectedPlayers)
					if (!pl.equals(connectedPlayers.get(whosNext()))) {
						pl.outGoingCommandQueue.add("drew " + whosNext());
						pl.outGoingCommandQueue.add("drew " + whosNext());
						pl.outGoingCommandQueue.add("drew " + whosNext());
						pl.outGoingCommandQueue.add("drew " + whosNext());
					} else {
						for (int k = 0; k < 4; k++) {
							Card temp = deck.draw(1).get(0);
							pl.outGoingCommandQueue.add("draw " + temp.getNumber() + " " + temp.getCardColor());
						}
					}

				if (card.getCardColor().equals(CardColor.NONE))
					player.outGoingCommandQueue.add("getcolor");
				else {
					nextTurn();
					nextTurn();
				}
				break;
			default:
				nextTurn();
				break;
		}

		Logic.tickTimer = Logic.tickTimerFull;
		whosNext();
	}

	public int whosNext()
	{
		if(direction)
		{
			if(turn+1 > connectedPlayers.size()-1)
				return 0;
			else
				return turn+1;
		}
		else
		{
			if(turn-1 < 0)
				return connectedPlayers.size()-1;
			else
				return turn-1;
		}
	}

	public void nextTurn()
	{
		if(direction)
		{
			turn++;
			if(turn > connectedPlayers.size()-1)
				turn = 0;
		}
		else
		{
			turn--;
			if(turn < 0)
				turn = connectedPlayers.size()-1;
		}

		for(ConnectionThread player : connectedPlayers)
			player.outGoingCommandQueue.add("turn "+turn);
	}

	public void addPlayer(ConnectionThread newPlayer)
	{
		newPlayer.turnnum = connectedPlayers.size();
		newPlayer.outGoingCommandQueue.add("turnnum "+newPlayer.turnnum);
		UNOnlineServer.availablePlayers.remove(newPlayer);
		connectedPlayers.add(newPlayer);
		votestart = 0;
	}

	public void removePlayer(ConnectionThread player)
	{
		connectedPlayers.remove(player);
		votestart = 0;

		if(connectedPlayers.size() == 0 && UNOnlineServer.lobbyManagers.size() > 1)
		{
			UNOnlineServer.lobbyManagers.remove(this);
			killLobby();
		}
	}

	public boolean isFull()
	{
		return connectedPlayers.size() >= MAX_PLAYERS;
	}

}
