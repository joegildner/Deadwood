package model;

import model.Board.*;
import model.Board.Room.*;
import model.Parsers.*;

import java.util.*;

public class Deadwood {

    private static Trailer t;
    private static ArrayList<Room> board;
    private Queue<Player> order;
    private Stack<Scene> scenes;
    private Stack<Scene> discard;
    private Die die;
    private Scanner input;
    private int rounds;

	/*
    public  void main(String[] args) {
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
	*/

    public Deadwood(int players) {
        this.rounds = 4;

        if (players < 4) {
            this.rounds = 3;
        } else if (players > 8) {
            System.out.println("Deadwood can handle a maximum of 8 players");
            System.exit(0);
        }
        input = new Scanner(System.in);
        initBoard();
        initPlayers(players);

    }

    public void startGame() {
        play(rounds);
        input.close();
    }

    public void printBoard() {
        for (int i = 0; i < board.size(); i++) {
            System.out.println(board.get(i));
        }
    }

    private void initBoard() {

        XmlParser roomParser = new XmlParser("../Deadwood/src/resources/board.xml");
        board = roomParser.parseRooms();

        if (board == null) {
            System.out.println("Error reading board.xml");
            System.exit(0);
            ;
        }


        for (Room r : board) {
            if (r instanceof Trailer) {
                t = (Trailer) r;
                break;
            }
        }

        XmlParser sceneParser = new XmlParser("../Deadwood/src/resources/cards.xml");
        scenes = sceneParser.parseScenes();
        if (scenes == null) {
            System.out.println("Error reading cards.xml");
            System.exit(0);
        }


        discard = new Stack<Scene>();

        Collections.shuffle(scenes);

        die = new Die();

        newDay();
    }

    public void play(int rounds) {
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

    public void finalScores() {
        int scores[] = new int[order.size()];
        int loc = 0;

        for (Player p : order) {
            scores[loc] = p.getScore();
            loc++;
        }

        Arrays.sort(scores);

        for (int i = 0; i < scores.length; i++) {
            Player p = matchScore(scores[i]);
            int place = i + 1;
            System.out.println(place + ": " + p.getName());
        }


    }

    private Player matchScore(int score) {
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

    public void newDay() {
        for (int i = 0; i < board.size(); i++) {
            Room r = board.get(i);
            r.newDay();
            if (r instanceof Stage) {
                r.newScene(scenes.peek());
                discard.push(scenes.pop());
            }
        }
    }

    public int numWrapped() {
        int count = 0;
        for (Room r : board) {
            if (r.isComplete())
                count++;
        }

        return count;
    }

    public void takeTurn(Player p) {
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
            } else if (firstWord.equalsIgnoreCase("add")) {
                p.addEarnings(Integer.parseInt(options));
            } else {
                System.out.println("Invalid choice. Try again.");
            }

            choice = input.nextLine().trim();
            division = splitChoice(choice);
            firstWord = division[0];
            options = division[1];
        }
    }

    private void move(Player p, String input) {
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

    private void upgrade(Player p, String input) {
        String[] options = input.split(" ");
        if (options.length < 2) {
            System.out.println("Could not parse upgrade request.");
        } else {
            p.upgrade(options[0], Integer.parseInt(options[1]));
        }
    }

    private String[] splitChoice(String str) {
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


    public void initPlayers(int players) {
        order = new LinkedList<Player>();
        String[] names = {"blue", "cyan", "green", "orange", "pink", "red", "violet", "yellow"};

        char[] imgs = {'b', 'c', 'g', 'p', 'r', 'v', 'y'};

        int loc = 0;

        for (int i = 0; i < players; i++) {
            char c = imgs[loc];
            String[] imgFiles = new String[6];
            for (int j = 0; j < imgFiles.length; j++) {
                imgFiles[j] = String.format("../Deadwood/src/resources/dice/%c%d.png", c, j + 1);

            }
            Player p = new Player(names[loc], t, imgFiles);
            order.add(p);
            loc++;
        }
    }

}
