package lift;

import java.util.LinkedList;

public class Main {

	public static void main(String[] args) {
		LinkedList<Person> people;
		LiftData data;
		LiftHandler lift;
		
		int nFloors = 7;
		int maxLoad = 4;
		int nPeople = 5;
		
		people = new LinkedList<Person>();
		data = new LiftData(nFloors, maxLoad);
		lift = new LiftHandler(data);
		for(int i = 0; i < nPeople; i++){
			Person p = new Person(data);
			p.start();
			people.add(p);
		}
		lift.start();

	}

}
