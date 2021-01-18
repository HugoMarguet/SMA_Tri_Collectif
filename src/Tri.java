import java.util.ArrayList;

public class Tri {

    public static void main(String[] args) {


        new MonteCarloModel(0.1, 0.3, 0.d);
        launch(50, 50, 50, 20);


    }

    private static void launch(int m, int nbA, int nbB, int nbAgents) {
        Environment env = new Environment(m,m, nbA, nbB, nbAgents, 1, 10);
        Agent agent;
        int counter = 0;
        int limit = 5000000;

        System.out.println(env.toString());
        while (counter < limit) {
            agent = env.next();
            agent.perception(env);
            agent.doAction(env);
            counter++;
        }
        System.out.println(env.toString());
    }
}
