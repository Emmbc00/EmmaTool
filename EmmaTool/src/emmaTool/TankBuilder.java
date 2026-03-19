package emmaTool;

public class TankBuilder {

	//all of the tank properties
	int tankX;
	int tankY;
	int tankZ;
	int valveCount;	//4 screws, 2 bars, 2 pipes
	String material; //current materials are iron, steel. all lowercase.
	
	// the breakdown
	int totalBlocks;//amount of blocks in the exterior
	int interiorAir;// how much air there is inside the tank
	int totalTanks; // 4 screws and 4 plates for 2
	int totalIngots;
	int totalRods;
	int tankPairs;
	int tankRemainder;
	int totalScrews; //1 ingot each
	int totalPlates; //2 ingots each, until i have electricity
	int pipePlates;
	int totalBars; //6 ingots for 2 bars
	int barRemainder;
	int totalPipes; //6 plates each
	//the pbhToogle
	boolean pbhToggle;
	String pipeType; //bronze pipes for iron valves smh its fucked up

	//material breakdowns

	public void displayBreakdown() {
		displayStats();
		System.out.println();
		System.out.println("======breakdown======");
		System.out.println("]=======start=======[");
		System.out.println("Total Blocks: " + totalTanks +" "+ material + " tanks");
		System.out.println("------------- " + valveCount +" "+ material + " valves");
		System.out.println("------------- " + interiorAir + " air");
		System.out.println("---------------------");
		System.out.println("Materials: " + totalScrews +" "+ material + (" screws"));		
		System.out.println("----------- " + totalPlates +" "+ material + " plates");
		if(pipeType == "bronze") {//check for bronze pipes
			System.out.println("----------- " + pipePlates +" "+ pipeType + " plates");
		}
		System.out.println("----------- " + totalBars +" "+ material + " bars");
		if(barRemainder > 0) {
		System.out.println("-----------[" + barRemainder + " extra bars]");
		}
		System.out.println("-----------[" + totalRods + " rods]");
		System.out.println("Total "+ material +" " + totalIngots);		
		System.out.println("]========end========[");
		System.out.println();
	}	
	
	public void updateBreakdown(boolean platesByHand) { //this is a mess
		pbhToggle = platesByHand;
		//biggest level
		totalBlocks = (tankX * tankY * tankZ) - ((tankX-2) * (tankY - 2) * (tankZ - 2));
		interiorAir = (tankX * tankY * tankZ) - totalBlocks;
		totalTanks = totalBlocks - valveCount;
		tankRemainder = totalTanks % 2;//if the number is odd, I need to have the amount of tanks it needs to be one more.
		if(tankRemainder == 1) {
			totalTanks = totalTanks + 1;
		}
		tankPairs = totalTanks/2; // shut up ii'm doing it this way 
		//tank parts
		totalScrews = tankPairs * 4;
		totalPlates = tankPairs * 4;
		//valve parts
			//pipes
		totalPipes = valveCount * 2;
		pipePlates = totalPipes * 6;
		if(material == "iron") {
			pipeType = "bronze";
		}
		if(material == "steel") {
			pipeType = "steel";
			totalPlates += pipePlates;
		}
			//bars
		totalBars = valveCount * 2;
		barRemainder = totalBars % 3;
		switch(barRemainder) {
		case 0:
			break;
		case 1:
			totalBars = totalBars + 2;
			break;
		case 2:
			totalBars = totalBars + 1;
			break;
		}
		totalRods = totalBars * 2;
		
		//ingots
		totalIngots = totalScrews;
		if(pbhToggle = true) {
			totalIngots += totalPlates * 2;
		} else {
			totalIngots += totalPlates;
		}
		totalIngots += totalRods;
		
		System.out.println("Finished calculating!");
	}
	

	
	
	// utilities -----------------------------------------------------
	public void displayStats() {
		System.out.println("Your Tank:");
		System.out.println("Dimensions: " + tankX + "x" + tankY + "x" + tankZ);
		System.out.println("Valve Count: " + valveCount);
		System.out.println("Material: " + material);
	}
	
	
	//getters and setters --------------------------------------------
	// dimensions 
	public void setX(int x) {
		tankX = x;
	}
	public int getX() {
		return tankX;
	}
	
	public void setY(int y) {
		tankY = y;
	}
	public int getY() {
		return tankY;
	}
	
	public void setZ(int z) {
		tankZ = z;
	}
	public int getZ() {
		return tankZ;
	}
	//valves
	public void setValves(int valves) {
		valveCount = valves;
	}
	public int getValves() {
		return valveCount;
	}
	//material
	public void setMaterial(String mat) {
		material = mat;
	}
	public String getMaterial() {
		return material;
	}
	//plates by hand toggle
	public void setPBH(boolean pbh) {
		pbhToggle = pbh;
	}
	public boolean getPBH() {
		return pbhToggle;
	}
	// end getters/setters ---------------------------------------------
	
}
