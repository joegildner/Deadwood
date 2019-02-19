import Board.*;
import Board.Room.*;
import Parsers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Deadwood {

	private static Trailer t;
	private static ArrayList<Room> board;
	private static Queue<Player> order;
	private static Stack<Scene> scenes;
	private static Stack<Scene> discard;
	private static Die die;
	private static Scanner input;

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Must enter number of players when calling Deadwood.");
			System.out.println("Example: java Deadwood <num players>");
			System.exit(0);
		}

		int players = Integer.parseInt(args[0]);
		int rounds = 4;

		if (players < 4) {
			rounds = 3;
		} else if (players > 8) {
			System.out.println("Deadwood can handle a maximum of 8 players");
			System.exit(0);
		}


		input = new Scanner(System.in);
		initBoard();
		initPlayers(players);
		
		play(rounds);
		input.close();

	}
	
	public static void printBoard() {
		for (int i = 0; i < board.size(); i++) {
			System.out.println(board.get(i));
		}
	}

	private static void initBoard() {
		
		XmlParser roomParser = new XmlParser("../Deadwood/resources/board.xml");
		board = roomParser.parseRooms();
		
		if (board == null) {
			System.out.println("Error reading board.xml");
			System.exit(0);;
		}
		
		
		for (Room r : board) {
			if (r instanceof Trailer) {
				t = (Trailer) r;
				break;
			}
		}
		
		XmlParser sceneParser = new XmlParser("../Deadwood/resources/cards.xml");
		scenes = sceneParser.parseScenes();
		if (scenes == null) {
			System.out.println("Error reading cards.xml");
			System.exit(0);
		}



		discard = new Stack<Scene>();
		shuffle();

		die = new Die();

		newDay();
	}

	public static void play(int rounds) {
		Player cur;
		for (int i = 0; i < rounds; i++) {
			while (numWrapped() < 9) {
				cur = order.poll();

				takeTurn(cur);

				order.offer(cur);
			}
			newDay();
		}
		
		System.out.println("Game ended! Point values:");
		finalScores();
	}
	
	public static void finalScores() {
		int scores[] = new int[order.size()];
		int loc = 0;
		
		for (Player p : order) {
			scores[loc] = p.getScore();
			loc++;
		}
		
		Arrays.sort(scores);
		
		for (int i = 0; i < scores.length; i++) {
			Player p = matchScore(scores[i]);
			int place = i+1;
			System.out.println(place + ": " + p.getName());
		}
		
		
	}
	
	private static Player matchScore(int score) {
		Player ret;
		for (Player p : order) {
			if (p.getScore() == score) {
				ret = p;
				order.remove(p);
				return ret;
			}
		}
		
		return null;
	}
	
	public static void newDay() {
		for (int i = 0; i < board.size(); i++) {
			Room r = board.get(i);
			r.newDay();
			if (r instanceof Stage) {
				r.newScene(scenes.peek());
				discard.push(scenes.pop());
			}
		}
	}

	public static int numWrapped() {
		int count = 0;
		for (Room r : board) {
			if (r.isComplete())
				count++;
		}

		return count;
	}

	// Shuffles the scene deck by swapping the positions of two random
	// Scene cards 1000 times
	public static void shuffle() {
		
		// make sure discard pile is returned to scene deck
		while (!discard.isEmpty()) {
			scenes.push(discard.pop());
		}
			
		
		int size = scenes.size();
		Scene[] deck = new Scene[size];
		scenes.copyInto(deck);

		Scene temp;
		int loc1;
		int loc2;

		for (int i = 0; i < 1000; i++) {
			loc1 = (int) (Math.random() * (size - 1));
			loc2 = (int) (Math.random() * (size - 1));
			temp = deck[loc1];
			deck[loc1] = deck[loc2];
			deck[loc2] = temp;
		}

		scenes.clear();

		for (int i = 0; i < deck.length; i++)
			scenes.push(deck[i]);

	}

	public static void takeTurn(Player p) {
		System.out.println("What would you like do to, " + p.getName() + "?");
		String choice = input.nextLine().trim();
		String[] division = splitChoice(choice);
		String firstWord = division[0];
		String options = division[1];

		while (!firstWord.equalsIgnoreCase("end")) {
			if (firstWord.equalsIgnoreCase("who")) {
				System.out.println(p);
			} else if (firstWord.equalsIgnoreCase("where")) {
				System.out.println(p.getRoom());
			} else if (firstWord.equalsIgnoreCase("move")) {
				move(p, options);
			} else if (firstWord.equalsIgnoreCase("work")) {
				p.commit(options);
			} else if (firstWord.equalsIgnoreCase("upgrade")) {
				upgrade(p, options);
			} else if (firstWord.equalsIgnoreCase("rehearse")) {
				p.rehearse();
			} else if (firstWord.equalsIgnoreCase("act")) {
				p.act(die.roll());
			} else {
				System.out.println("Invalid choice. Try again.");
			}

			choice = input.nextLine().trim();
			division = splitChoice(choice);
			firstWord = division[0];
			options = division[1];
		}
	}

	private static void move(Player p, String input) {
		Room dest = null;

		for (int i = 0; i < board.size(); i++) {
			Room r = board.get(i);
			if (r.getName().equalsIgnoreCase(input)) {
				dest = r;
				break;
			}
		}

		if (dest == null) {
			System.out.println("Could not find specfied Room " + input);
		} else {
			if (p.move(dest))
				System.out.println("Moved to " + input);
		}
	}

	private static void upgrade(Player p, String input) {
		String[] options = input.split(" ");
		if (options.length < 2) {
			System.out.println("Could not parse upgrade request.");
		} else {
			p.upgrade(options[0], Integer.parseInt(options[1]));
		}
	}

	private static String[] splitChoice(String str) {
		String[] division = new String[2];
		int endFirstWord = str.indexOf(" ");
		if (endFirstWord == -1) {
			division[0] = str;
			return division;
		}
		division[0] = str.substring(0, endFirstWord);
		division[1] = str.substring(endFirstWord + 1);

		return division;

	}



	public static void initPlayers(int players) {
		order = new LinkedList<Player>();
		String[] names = { "blue", "cyan", "green", "orange", "pink", "red", "purple", "yellow" };
		int loc = 0;

		for (int i = 0; i < players; i++) {
			Player p = new Player(names[loc], t);
			order.add(p);
			loc++;
		}
	}

}
