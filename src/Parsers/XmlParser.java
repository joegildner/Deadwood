package Parsers;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.util.ArrayList;
import java.util.Stack;

import Board.Scene;
import Board.Role.*;
import Board.Room.Office;
import Board.Room.Room;
import Board.Room.Stage;
import Board.Room.Trailer;

//TODO: add image and sizes to parser

public class XmlParser {
	private String file;
	private ArrayList<Room> board;
	private Stack<Scene> scenes;
	
	public XmlParser(String file) {
		this.file = file;
		this.board = new ArrayList<>();
		this.scenes = new Stack<>();
	}
	
	public void setFile(String file) {
		this.file = file;
		this.board.clear();
		this.scenes.clear();
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
			System.out.println(inputFile.getCanonicalPath());

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
		for_each(doc.getElementsByTagName("set"), (Element card) -> buildBoard(card));
		for_each(doc.getElementsByTagName("set"), (Element card) -> buildStage(card));
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

	private void buildBoard(Element card) {
		Room r = null;
		String name = getName(card);
		if (findRoom(name) == null) {
			if (name.equals("office")) {
				r = new Office();
			} else if (name.equals("trailer")) {
				r = new Trailer();
			} else {
				r = new Stage(name);
			}
			board.add(r);
			
			for_each(card.getElementsByTagName("neighbor"), 
					(Element neighbor) -> buildBoard(neighbor));

		}
			
	}
	
	private void buildScene(Element card) {
		String name = card.getAttribute("name");
		int budget = Integer.parseInt(card.getAttribute("budget"));
		String desc = card.getElementsByTagName("scene").item(0).getTextContent();
		int num = Integer.parseInt(
				((Element) card.getElementsByTagName("scene").item(0)).getAttribute("number"));
		
		Scene s = new Scene(name, desc, budget, num);
		for_each(card.getElementsByTagName("part"),
				(Element part) -> buildRole(part, s));
		
	
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
		
		
		for_each(card.getElementsByTagName("neighbor"), 
				(Element neighbor) -> {
					Room n = findRoom(getName(neighbor));
					if (n != null && !r.isAdjacent(n))
						r.addConnection(n);
					
				});
	}
	
	private void buildStage(Element card) {
		Room r = findRoom(getName(card));
		if (!(r instanceof Stage))
			return;
		
		for_each(card.getElementsByTagName("part"),
				(Element part) -> buildRole(part, r));
		
		r.setTakes(card.getElementsByTagName("take").getLength());
	}
	
	private Room findRoom(String name) {
		for (Room r : this.board) {
			if (r.getName().equalsIgnoreCase(name)) 
				return r;
		}
		
		return null;
	}
	
	private void buildRole(Element part, Room r) {
		String name = part.getAttribute("name");
		int rank = Integer.parseInt(part.getAttribute("level"));
		String line = part.getElementsByTagName("line").item(0).getTextContent();
		
		Extra e = new Extra(name, line, rank);
		r.addRole(e);
	}
	
	private void buildRole(Element part, Scene s) {
		String name = part.getAttribute("name");
		int rank = Integer.parseInt(part.getAttribute("level"));
		String line = part.getElementsByTagName("line").item(0).getTextContent();
		
		Lead l = new Lead(name, line, rank);
		s.addRole(l);
	}
	

}
