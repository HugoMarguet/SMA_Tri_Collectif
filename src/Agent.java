import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Agent extends Element {

    private static int ID_COUNTER = 0;
    private static MonteCarloModel model;

    private final int id;
    private final int range;
    private String memory;

    private Map<Item.ID, Double> f;
    private Item item;
    private ArrayList<Element> neighbors;
    private ArrayList<Integer[]> emptySquares;
    private ArrayList<Integer[]> availableSquares;
    private boolean occupiedAround;

    public Agent(int posX, int posY, int range, int memorySize) {

        super(posX, posY);

        id = ID_COUNTER;
        ID_COUNTER++;

        this.range = range;

        item = null;
        emptySquares = new ArrayList<>();
        availableSquares = new ArrayList<>();
        f = new HashMap<>();
        f.put(Item.ID.A, 0d);
        f.put(Item.ID.B, 0d);

        memory = "";
        IntStream.range(0, memorySize).forEach(i -> this.memory += "0");
    }

    /**
     * Percevoir l'environnement pour mettre à jour les informations nécessaires à l'agent sur l'environnement
     * @param environment
     */
    public void perception(Environment environment) {

        neighbors = environment.neighborhood(posX, posY);
        emptySquares = environment.getAvailableSquares(posX, posY);
        occupiedAround = emptySquares.isEmpty();
        availableSquares = environment.getAvailableSquares(posX, posY);

        if (item != null)
            f = MonteCarloModel.getF(neighbors, item);
        else
            f = MonteCarloModel.getF(memory);
    }

    /**
     * Interagir avec l'environnement grâce aux informations récupérés par la perception
     * @param environment
     */
    public void doAction(Environment environment) {
        double p = Math.random();
        Integer [] itemDroppedPosition = null;
        if (item != null) {
            if (!occupiedAround && p > MonteCarloModel.pickDown(f.get(item.getId()))) {
                itemDroppedPosition = drop_Item(environment);
            }
        }
        else {
            double pA = MonteCarloModel.pickUp(f.get(Item.ID.A));
            double pB = MonteCarloModel.pickUp(f.get(Item.ID.B));

            final boolean AIsAround = neighbors.stream()
                    .anyMatch(element -> element instanceof Item && ((Item) element).getId() == Item.ID.A);
            final boolean BIsAround = neighbors.stream()
                    .anyMatch(element -> element instanceof Item && ((Item) element).getId() == Item.ID.B);

            if (AIsAround && !BIsAround) {
                addMemory('A');
                if (p < pA) take_item(Item.ID.A, environment);
            } else if (!AIsAround && BIsAround) {
                addMemory('B');
                if (p < pB) take_item(Item.ID.B, environment);
            } else if (AIsAround && BIsAround)
                if (p < pA && pB < pA) {
                    addMemory('A');
                    take_item(Item.ID.A, environment);
                }
                else if (p < pB && pA < pB) {
                    addMemory('B');
                    take_item(Item.ID.B, environment);
                }
                else if (p < pA && pA == pB) {
                    if (Math.random() > 0.5) {
                        addMemory('A');
                        take_item(Item.ID.A, environment);
                    }
                    else {
                        addMemory('B');
                        take_item(Item.ID.B, environment);
                    }
                }
                else
                    addMemory(pA == pB  ? (Math.random() > 0.5 ? 'A' : 'B') : (pA > pB ? 'A' : 'B'));
            else
                addMemory('0');
        }
        for(int i = 0; i < range; i++)
            move(environment, itemDroppedPosition);
    }

    private void move(Environment environment, Integer[] itemDroppedPosition) {
        final ArrayList<Integer[]> positions = (ArrayList<Integer[]>) availableSquares.stream()
                .filter(position ->
                        itemDroppedPosition == null
                        || !Objects.equals(position[0], itemDroppedPosition[0])
                        || !Objects.equals(position[1], itemDroppedPosition[1]))
                .collect(Collectors.toList());
        if(positions.size() != 0) {
            Integer[] position = positions.get((int) (Math.random() * positions.size()));
            environment.delElement(this);
            environment.addElement(position[0], position[1], this);
            posX = position[0];
            posY = position[1];
        }
    }

    private void take_item(Item.ID id, Environment environment) {
        ArrayList<Element> items = (ArrayList<Element>) neighbors.stream()
                       .filter(element -> element instanceof Item && ((Item) element).getId() == id)
                       .collect(Collectors.toList());
        item = (Item) items.get((int) (Math.random() * items.size()));
        environment.delElement(item);
    }

    private Integer[] drop_Item(Environment environment) {
        final Integer[] position = emptySquares.get((int) (Math.random() * emptySquares.size()));
        item.setPosX(position[0]);
        item.setPosY(position[1]);
        environment.addElement(position[0], position[1], item);
        item = null;
        return position;
    }

    public void addMemory(char c) {
        memory = memory.substring(1);
        memory += c;
    }

    public String toString() {
        return " "; //"#"; //String.valueOf(id);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return id == agent.id;
    }

    public int hashCode() {
        return Objects.hash(id);
    }
}
