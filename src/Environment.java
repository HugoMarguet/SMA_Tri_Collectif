import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Environment {

    private Element[][] grid;
    private int m;
    private int n;
    private List<Element> agents;

    private int counterAgent;


    public Environment(int m, int n, int nbItemsA, int nbItemsB, int nbAgents, int range, int memorySize) {
        this.counterAgent = -1;
        this.m = m;
        this.n = n;
        grid = new Element[m][n];
        agents = new ArrayList<>();

        IntStream.range(0, nbItemsA + nbItemsB + nbAgents)
                .mapToObj(counter -> {
                    int x = (int) (Math.random() * m);
                    int y = (int) (Math.random() * n);
                    while (grid[x][y] != null) {
                        x = (int) (Math.random() * m);
                        y = (int) (Math.random() * n);
                    }
                    if (counter < nbItemsA)
                        return new Item(Item.ID.A, x, y);
                    else if (counter < nbItemsA + nbItemsB)
                        return new Item(Item.ID.B, x, y);
                    else {
                        Agent agent = new Agent(x, y, range, memorySize);
                        agents.add(agent);
                        return agent;
                    }
                }).forEach(element -> grid[element.getPosX()][element.getPosY()] = element);
    }

    public ArrayList<Element> neighborhood(int x, int y) {
        ArrayList<Element> neighbors = new ArrayList<>();
        if (x + 1 < m) neighbors.add(grid[x + 1][y]);
        if (x - 1 >= 0) neighbors.add(grid[x - 1][y]);
        if (y + 1 < n) neighbors.add(grid[x][y + 1]);
        if (y - 1 >= 0) neighbors.add(grid[x][y - 1]);
        return (ArrayList<Element>) neighbors.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Element getElement(int x, int y) {
        return grid[x][y];
    }

    public ArrayList<Integer[]> getAvailableSquares(int x, int y, int range) {
        ArrayList<Integer[]> availableSquares = new ArrayList<>();
        if (x + range < m && grid[x + range][y] == null) availableSquares.add(new Integer[]{x + range, y});
        if (x - range >= 0 && grid[x - range][y] == null) availableSquares.add(new Integer[]{x - range, y});
        if (y + range < n && grid[x][y + range] == null) availableSquares.add(new Integer[]{x, y + range});
        if (y - range >= 0 && grid[x][y - range] == null) availableSquares.add(new Integer[]{x, y - range});
        for(Integer[] pos : availableSquares) {
            if(grid[pos[0]][pos[1]] != null) {
                System.out.println("not null " + grid[pos[0]][pos[1]].toString() + " " + pos[0] + " " + pos[1]);
            }
        }
        return availableSquares;
    }

    public void addElement(int x, int y, Element element) {
        if (grid[x][y] != null)
            System.out.println("not null " + grid[x][y].toString() + " " + x + " " + y + " El : " + element.toString());
        grid[x][y] = element;
    }

    public void delElement(Element item) {
        final int x = item.getPosX();
        final int y = item.getPosY();
        grid[x][y] = null;
    }

    public Agent next() {
        counterAgent++;
        return (Agent) agents.get(counterAgent % agents.size());
    }

    public String toString() {
        StringBuilder gridVue = new StringBuilder();
        Element el;
        final int length = (m + 1) * 2 + 1;
        IntStream.range(0, length).forEach(i -> gridVue.append("-"));
        for (int i = 0; i < m; i++) {
            gridVue.append("\n|");
            for (int j = 0; j < n; j++) {
                el = grid[i][j];
                gridVue.append(" ").append(el != null ? el.toString() : ' ');
            }
            gridVue.append(" |");
        }
        gridVue.append("\n");
        IntStream.range(0, length).forEach(i -> gridVue.append("-"));
        return gridVue.toString();
    }

    public int getSize() {
        int counter = 0;
        for(int i = 0; i< m; i++) {
            for(int j = 0; j< n; j++) {
                counter += grid[i][j] != null ? 1 : 0;
            }
        }
        return counter;
    }
}
