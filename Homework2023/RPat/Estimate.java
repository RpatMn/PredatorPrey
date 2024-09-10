package Homework2023.RPat;




import java.util.Random;

public class Estimate {

    public static void main(String[] args) {
        int startGrass = 8;
        int startSheep = 3;
        int totalTime = 100;

        Estimate simulation = new Estimate();
        simulation.runSimulation(startGrass, startSheep, totalTime);
    }

    public void runSimulation(int startGrass, int startSheep, int totalTime) {
        int grass = startGrass;
        int sheep = startSheep;
       Random r = new Random(14682);
        Boolean rr=r.nextInt(10)==0;
        for (int time = 1; time <= totalTime; time++) {
            // Grass doubles every 2 seconds
            if (time % 2 == 0) {
                grass *= 2;
            }

            // Sheep have a 50% chance of eating grass every 1.5 seconds
            if (time % 1.5 == 0) {
                for (int i = 0; i < sheep; i++) {
                    if (r.nextInt(3)==0) {
                        grass -= 1;
                    }
                }
            }

            // Sheep survival and reproduction
            if (time % 3 == 0) {
                int survivingSheep = 0;
                for (int i = 0; i < sheep; i++) {
                    if (r.nextInt(5)==0) {
                        // Sheep survives
                        survivingSheep++;
                    }
                }
                sheep = survivingSheep;
            }

            if (r.nextInt(7)==0) {
                // Sheep reproduces
                sheep += survivingSheep;
            }
            }
        }

        System.out.println("Final Grass: " + grass);
        System.out.println("Final Sheep: " + sheep);
    }
}

