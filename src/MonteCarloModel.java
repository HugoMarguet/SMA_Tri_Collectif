import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MonteCarloModel {

    public static double kMinus;
    public static double kPlus;
    public static double error;

    public MonteCarloModel(double k_plus, double k_minus, double e)  {
        kMinus = k_minus;
        kPlus = k_plus;
        error = e;
    }

    public static double putDown(double f) {
        return Math.pow(f / (kMinus + f), 2);
    }

    public static double pickUp(double f) {
        return Math.pow(kPlus / (kPlus + f), 2);
    }

    protected static Map<Item.ID, Integer> getOccurrences(String memory) {
        final int nbA = (int) memory.chars().filter(ch -> ch == 'A').count();
        final int nbB = (int) memory.chars().filter(ch -> ch == 'B').count();
        final int nb_ = (int) memory.chars().filter(ch -> ch == '0').count();
        final Map<Item.ID, Integer> occurrences = new HashMap<>();

        occurrences.put(Item.ID.A, nbA);
        occurrences.put(Item.ID.B, nbB);
        occurrences.put(null, nb_);
        return occurrences;
    }

    public static Map<Item.ID, Double> getF(ArrayList<Element> neighbors, Item item) {
        final Map<Item.ID, Double> f = new HashMap<>();
        f.put(Item.ID.A, 0d);
        f.put(Item.ID.B, 0d);
        final double occurrences = neighbors.size() > 0 ? (double) neighbors.stream()
                .filter(element -> element instanceof Item && ((Item) element).getId() == item.getId())
                .count() : 0;
        f.put(item.getId(), occurrences / 4.d);
        return f;
    }

    public static Map<Item.ID, Double> getF(String memory) {
        final Map<Item.ID, Integer> occurrences = getOccurrences(memory);
        final Map<Item.ID, Double> f = new HashMap<>();
        final double fA = (occurrences.get(Item.ID.A) + error * occurrences.get(Item.ID.B))
                / occurrences.values().stream().reduce(0, Integer::sum);
        final double fB = (occurrences.get(Item.ID.B) + error * occurrences.get(Item.ID.A))
                / occurrences.values().stream().reduce(0, Integer::sum);

        f.put(Item.ID.A, fA);
        f.put(Item.ID.B, fB);
        return f;
    }

    public static String info() {
        return String.format("MCM { k+=%.2f, k-=%.2f, e=%.2f }", kPlus, kMinus, error);
    }

}