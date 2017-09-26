package lift;

import java.util.LinkedList;

public class Main {

	public static void main(String[] args) {
		LinkedList people;
		LiftData data;
		LiftHandler lift;
		
		int nFloors = 6;
		int maxLoad = 4;
		
		people = new LinkedList();
		data = new LiftData(nFloors, maxLoad);
		lift = new LiftHandler(data);
		for(int i = 0; i < 10; i++){
			int entryLevel = (int)(Math.random()*(nFloors));
			int exitLevel;
			do {
				exitLevel = (int)(Math.random()*(nFloors));
			}
			while(exitLevel == entryLevel);
			Person p = new Person(entryLevel, exitLevel, data);
			p.start();
			people.add(p);
		}
		
		lift.start();

	}

}
