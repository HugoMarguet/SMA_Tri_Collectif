public class Tri {

    private static final int LIMIT = 1000000;

    private static void launch(int m, int nbA, int nbB, int nbAgents, int range, int memorySize) {
        Environment env = new Environment(m,m, nbA, nbB, nbAgents, range, memorySize);
        displayInfo(env);
        System.out.println(env);
        start(env, LIMIT);
        System.out.println(env);
    }

    private static void launchKeepCI(Environment environment, int nbRun) {
        displayInfo(environment);
        System.out.println(environment);
        Environment env;
        for (int run = 0; run < nbRun; run++) {
            env = environment.copy();
            start(env, LIMIT);
            System.out.println(env);
        }
    }

    private static void launchEvaluateError(Environment environment, float errorMin, float errorMax, float step) {
        System.out.println(environment.info());
        System.out.println(environment);
        Environment env;
        float e = errorMin;
        while (e <= errorMax) {
            new MonteCarloModel(0.1, 0.3, e);
            System.out.println(MonteCarloModel.info());
            env = environment.copy();
            start(env, LIMIT);
            System.out.println(env);
            e += step;
        }
    }

    private static void start(Environment env, int limit) {
        Agent agent;
        int counter = 0;
        while (counter < limit) {
            agent = env.next();
            agent.perception(env);
            agent.doAction(env);
            counter++;
        }
    }

    private static void displayInfo(Environment environment) {
        System.out.println(String.format("%s %s", environment.info(), MonteCarloModel.info()));
    }

    public static void main(String[] args) {

        // Question 1 : erreur = 0
        new MonteCarloModel(0.1, 0.3, 0.d);
        launch(50, 50, 50, 20, 1, 10);

        Environment env = new Environment(15, 15, 15, 15, 5, 1, 5);

        // Compare les fluctuations aléatoires avec les même CI
        new MonteCarloModel(0.1, 0.3, 0d);
        launchKeepCI(env, 5);

        // Question 2 : facteur discriminant évalué selon le degré d'erreur
        new MonteCarloModel(0.1, 0.3, 0d);
        launchEvaluateError(env, 0f, 1.1f, 0.1f);
    }


}
