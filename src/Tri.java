public class Tri {

    private static final int LIMIT = 3000000;

    private static void launch(int m, int nbA, int nbB, int nbAgents, int range, int memorySize) {
        Environment env = new Environment(m,m, nbA, nbB, nbAgents, range, memorySize);
        displayInfo(env);
        System.out.println(env);
        start(env);
        System.out.println(env);
    }

    private static void launchMultipleTimesKeepingCI(Environment environment, int nbRun) {
        displayInfo(environment);
        System.out.println(environment);
        Environment env;
        for (int run = 0; run < nbRun; run++) {
            env = environment.copy();
            start(env);
            System.out.println(env);
        }
    }

    private static void launchMultipleTimes(int m, int nbA, int nbB, int nbAgents, int range, int memorySize, int nbRun) {
        Environment env = new Environment(m, m, nbA, nbB, nbAgents, range, memorySize);
        displayInfo(env);
        for (int run = 0; run < nbRun; run++) {
            start(env);
            System.out.println(env);
            env = new Environment(m, m, nbA, nbB, nbAgents, range, memorySize);
        }
    }

    private static void launchEvaluateParam(Environment environment, float min, float max, float step, String param_id,
                                            float kPlus, float kMinus, float error) {
        System.out.println(environment.info());
        System.out.println(environment);
        Environment env;
        float param = min;
        while (param <= max) {
            switch (param_id) {
                case "k_plus":
                    new MonteCarloModel(param, kMinus, error);
                    break;
                case "k_minus":
                    new MonteCarloModel(kPlus, param, error);
                    break;
                case "error" :
                    new MonteCarloModel(kPlus, kMinus, param);
                    break;
            }
            System.out.println(MonteCarloModel.info());
            env = environment.copy();
            start(env);
            System.out.println(env);
            param += step;
        }
    }

    private static void start(Environment env) {
        Agent agent;
        int counter = 0;
        while (counter < LIMIT) {
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

        Environment env = new Environment(15, 15, 10, 10, 5, 1, 5);

        // Question 1 : erreur = 0
        new MonteCarloModel(0.1, 0.3, 0.d);
        launch(50, 50, 50, 20, 1, 10);

        // Question 2 : facteur discriminant évalué selon le degré d'erreur
        launchEvaluateParam(env, 0f, 1.1f, 0.1f, "error", 0.1f, 0.3f, 0.0f);

        // Compare les fluctuations aléatoires avec les même CI
        new MonteCarloModel(0.1, 0.3, 0d);
        launchMultipleTimesKeepingCI(env, 5);

        // Compare les fluctuations aléatoires avec différentes CI
        new MonteCarloModel(0.1, 0.3, 0d);
        launchMultipleTimes(15,  10, 10, 5, 1, 5, 5);

        // Compare l'influence de k+ et k-
        launchEvaluateParam(env, 0.0f, 0.1f, 0.01f, "k_plus", 0.1f, 0.3f, 0f);
        launchEvaluateParam(env, 0.05f, 1.1f, 0.1f, "k_minus", 0.1f, 0.3f, 0f);

        new MonteCarloModel(0.1, 0.3, 0.d);
        launch(15,  10, 10, 5, 1, 2);
        launch(15,  10, 10, 5, 1, 5);
        launch(15,  10, 10, 5, 1, 10);
        launch(15,  10, 10, 5, 1, 20);

    }


}
