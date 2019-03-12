package controller;

import model.Die;
import model.Player;
import model.Room.Room;
import model.Room.Stage;
import model.Room.Trailer;
import model.Scene;
import resources.XmlParser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DeadwoodController implements ActionListener{

    private Trailer t;
    private ArrayList<Room> board;
    private ArrayList<Listener> listeners;
    private Queue<Player> order;
    private Stack<Scene> scenes;
    private Stack<Scene> discard;
    private Die die;
    private int rounds;
    private Player cur;
    private Executor executor;
    private final Object ob = new Object();

    public interface Listener {
        public void changed(Player p);
    }

    public DeadwoodController(int players) {
        this.rounds = 4;
        executor = Executors.newSingleThreadExecutor();
        listeners = new ArrayList<>();

        if (players < 4) {
            this.rounds = 3;
        } else if (players > 8) {
            System.out.println("DeadwoodController can handle a maximum of 8 players");
            System.exit(0);
        }

        initBoard();
        initPlayers(players);

    }

    public void startGame() {
        play(rounds);
    }

    public void printBoard() {
        for (int i = 0; i < board.size(); i++) {
            System.out.println(board.get(i));
        }
    }

    private void initBoard() {

        XmlParser roomParser = new XmlParser("board.xml");
        board = roomParser.parseRooms();

        if (board == null) {
            System.out.println("Error reading board.xml");
            System.exit(0);
        }


        for (Room r : board) {
            if (r instanceof Trailer) {
                t = (Trailer) r;
                break;
            }
        }

        XmlParser sceneParser = new XmlParser("cards.xml");
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
        for (int i = 0; i < rounds; i++) {
            while (numWrapped() < 9) {
                cur = order.poll();
                notifyListeners();
                try {
                    synchronized (ob) {
                        ob.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                notifyListeners();

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

    public void getInput(Player p) {
        Scanner input = new Scanner(System.in);
        System.out.println("What would you like do to, " + p.getName() + "?");
        String choice = input.nextLine().trim();
        input.close();
        takeTurn(p, choice);
    }

    public void takeTurn(Player p, String choice){
        String[] division = splitChoice(choice);
        String firstWord = division[0];
        String options = division[1];
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
            } else if (firstWord.equalsIgnoreCase("end")) {
                synchronized (ob) {
                    ob.notify();
                }
            } else {
                System.out.println("Invalid choice. Try again.");
            }

            notifyListeners();
        }


    private void move(Player p, String input) {
        Room dest = null;
        int i = 0;

        while (i < board.size() && dest == null) {
            Room r = board.get(i);
            if (r.getName().equalsIgnoreCase(input)) {
                dest = r;
            }
            i++;
        }

        if (dest == null) {
            System.out.println("Could not find specified Room " + input);
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
            if (options[0].contains("$")) {
                p.upgradeMoney(Integer.parseInt(options[1]));
            } else if (options[0].contains("cr")) {
                p.upgradeCr(Integer.parseInt(options[1]));
            }
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

        char[] imgs = {'b', 'c', 'g', 'o', 'p', 'r', 'v', 'y'};

        int loc = 0;

        final Class cls = XmlParser.class;

        for (int i = 0; i < players; i++) {
            char c = imgs[loc];
            String[] imgFiles = new String[6];
            for (int j = 0; j < imgFiles.length; j++) {
                String f = String.format("%c%d.png", c, j + 1);

                try {
                    URL u = cls.getResource("dice/" + f);
                    imgFiles[j] = u.getFile();
                } catch (Exception e) {
                    System.err.println(f);
                    e.printStackTrace();
                    System.exit(1);
                }


            }
            Player p = new Player(names[loc], t, imgFiles);
            order.add(p);
            loc++;
        }
    }


	public ArrayList<Room> getBoard() {
		return board;
	}

    public Player getCurrentPlayer() {
        return cur;
    }

    public Queue<Player> getPlayers() {
        return order;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println(actionEvent.getActionCommand());
        takeTurn(cur, actionEvent.getActionCommand());

    }

    public void addListener(Listener l){
        listeners.add(l);
    }

    public void notifyListeners(){
        for(Listener l : listeners){
            l.changed(cur);
        }
    }
}

