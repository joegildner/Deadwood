package resources;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import model.Role.Extra;
import model.Role.Lead;
import model.Room.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

import model.Scene;

public class XmlParser {
	private String file;
	private ArrayList<Room> board;
	private Stack<Scene> scenes;
	private final Class cls = XmlParser.class;
	
	public XmlParser(String type) {
		String file = "";

		try {
			URL u = cls.getResource(type);
			file = u.getFile();


		} catch (Exception e) {
			System.err.println(type);
			e.printStackTrace();
			System.exit(1);
		}

		this.file = file;
		this.board = new ArrayList<>();
		this.scenes = new Stack<>();
	}
	

/* ------------THE FOLLOWING CODE IS ADAPTED FROM XmlDemo.java PROVIDED BY ARAN CLAUSON -----------*/

	
	/*
	 * For_each node This interface and for_each function iterator over the nodes in
	 * a node list.
	 */
	public interface Runnable_Node {
		public void run(Node n);
	}

	public void for_each(NodeList list, Runnable_Node run) {
		for (int i = 0; i < list.getLength(); ++i)
			run.run(list.item(i));
	}

	/*
	 * For_each element This interface and the following for_each function iterate
	 * over the elements in a node list.
	 */
	public interface Runnable_Element {
		public void run(Element n);
	}

	public void for_each(NodeList list, Runnable_Element run) {
		for_each(list, (Node n) -> {
			if (n.getNodeType() == Node.ELEMENT_NODE)
				run.run((Element) n);
		});
	}

	/*
	 * Parse Document
	 *
	 * Reads, parses, and returns the root element of the indicated file.
	 */
	public Element parse_doc(String filename) {
		Element root = null;

		try {

			File inputFile = new File(filename);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);

			doc.getDocumentElement().normalize();
			root = doc.getDocumentElement();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return root;
	}
	
	
/* -------------------------------------END ADAPTED CODE-------------------------------------------*/
	
	public ArrayList<Room> parseRooms() {
		if (!this.file.contains("board"))
			return null;

		Element doc = parse_doc(this.file);
		for_each(doc.getElementsByTagName("set"), (Element card) -> buildBoard(card)); // initilize all rooms
		for_each(doc.getElementsByTagName("set"), (Element card) -> buildStage(card)); // find all stages
		for_each(doc.getElementsByTagName("set"), (Element card) -> addNeighbors(card));
		for_each(doc.getElementsByTagName("office"), (Element card) -> addNeighbors(card));
		for_each(doc.getElementsByTagName("trailer"), (Element card) -> addNeighbors(card));
		
		return this.board;

	}
	
	public Stack<Scene> parseScenes() {
		if (!this.file.contains("cards"))
			return null;
		
		Element doc = parse_doc(this.file);
		for_each(doc.getElementsByTagName("card"), (Element card) -> buildScene(card));
		
		return this.scenes;
	}

	private void buildBoard(Element room) {
		Room r = null;
		String name = getName(room);
		if (findRoom(name) == null) {
			if (name.equals("office")) {
				r = new Office();
			} else if (name.equals("trailer")) {
				r = new Trailer();
			} else {
				r = new Stage(name);
			}
			board.add(r);
			
			for_each(room.getElementsByTagName("neighbor"),
					(Element neighbor) -> buildBoard(neighbor));

		}
			
	}
	
	private void buildScene(Element card) {
		String name = card.getAttribute("name");
		int budget = Integer.parseInt(card.getAttribute("budget"));
		String desc = card.getElementsByTagName("scene").item(0).getTextContent();
		int num = Integer.parseInt(
				((Element) card.getElementsByTagName("scene").item(0)).getAttribute("number"));


		String img = card.getAttribute("img");
		String urlName = "";
		try {
			URL u = cls.getResource("cards/" + img);
			urlName = u.getFile();
		} catch (Exception e) {
			System.err.println("cards/" + img);
			e.printStackTrace();
			System.exit(1);
		}

		Scene s = new Scene(name, desc, budget, num, urlName);
		for_each(card.getElementsByTagName("part"),
				(Element part) -> buildRole(part, s, null));
		
	
		this.scenes.push(s);
		
	}
	
	private String getName(Element card) {
		String name;
		if (card.hasAttribute("name")) {
			name = card.getAttribute("name");
		} else {
			name = card.getTagName();
		}
		return name;
	}
	
	

	/*
	 *  for each neighbor in this element, find it's name/room and add it
	 *  to the current "room's" (represented by the Element card) 
	 *  adjacency list.
	 */
 
	private void addNeighbors(Element card) {
		Room r = findRoom(getName(card));

		int[] area = getArea(card);
		r.setArea(area);
		
		
		for_each(card.getElementsByTagName("neighbor"), 
				(Element neighbor) -> {
					Room n = findRoom(getName(neighbor));
					if (n != null && !r.isAdjacent(n))
						r.addConnection(n);
				});

		if (r instanceof Office) {
			buildOffice(card, r);
		}
	}

	private void buildOffice(Element card, Room r) {
		for_each(card.getElementsByTagName("upgrade"),
				(Element upgrade) -> {
					Upgrade u = buildUpgrade(upgrade);
					r.addUpgrade(u);
				});
	}

	private Upgrade buildUpgrade(Element upgrade) {
		int level = Integer.parseInt(upgrade.getAttribute("level"));
		String currency = upgrade.getAttribute("currency");
		int amt = Integer.parseInt(upgrade.getAttribute("amt"));
		int[] area = getArea(upgrade);

		Upgrade u = new Upgrade(area, level, amt, currency);
		return u;
	}
	
	private void buildStage(Element card) {
		Room r = findRoom(getName(card));
		if (!(r instanceof Stage))
			return;

		for_each(card.getElementsByTagName("part"),
				(Element part) -> buildRole(part, null, r));
		
		r.setTakes(card.getElementsByTagName("take").getLength());

		for_each(card.getElementsByTagName("take"),
				(Element take) -> {
					int[] area = getArea(take);
					Take t = new Take(area);
					r.addTake(t);
				});
	}
	
	private Room findRoom(String name) {
		for (Room r : this.board) {
			if (r.getName().equalsIgnoreCase(name)) 
				return r;
		}
		
		return null;
	}


	private int[] getArea(Element e) {
		int[] area = new int[4];
		NodeList nlArea = e.getElementsByTagName("area");
		Node nArea = nlArea.item(0);
		Element eArea = (Element) nArea;

		area[0] = Integer.parseInt(eArea.getAttribute("x"));
		area[1] = Integer.parseInt(eArea.getAttribute("y"));
		area[2] = Integer.parseInt(eArea.getAttribute("h"));
		area[3] = Integer.parseInt(eArea.getAttribute("w"));
		return area;
	}

	private void buildRole(Element part, Scene s, Room r) {
		String name = part.getAttribute("name");
		int rank = Integer.parseInt(part.getAttribute("level"));
		String line = part.getElementsByTagName("line").item(0).getTextContent();
		int[] area = getArea(part);

		if (s == null) {
			Extra e = new Extra(name, line, rank, area);
			r.addRole(e);
		} else if (r == null){
			Lead l = new Lead(name, line, rank, area);
			s.addRole(l);
		}
	}

}
