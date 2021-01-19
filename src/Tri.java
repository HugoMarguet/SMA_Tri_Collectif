import java.util.ArrayList;

public class Tri {

    private static void launch(int m, int nbA, int nbB, int nbAgents, int range, int memorySize) {
        Environment env = new Environment(m,m, nbA, nbB, nbAgents, range, memorySize);
        Agent agent;
        int counter = 0;
        int limit = 100000000;

        System.out.println(env.toString());
        while (counter < limit) {
            agent = env.next();
            agent.perception(env);
            agent.doAction(env);
            counter++;
        }
        System.out.println(env.toString());
    }

    public static void main(String[] args) {


        // Question 1 :
        new MonteCarloModel(0.1, 0.3, 0.d);
        launch(25, 50, 50, 20, 1, 10);

        // Question 2 :
        new MonteCarloModel(0.1, 0.3, 1d);
        launch(25, 50, 50, 20,1, 10);

        //
        new MonteCarloModel(0.1, 0.3, 0d);
        launch(40, 25, 25, 20,1, 10);
    }


}
