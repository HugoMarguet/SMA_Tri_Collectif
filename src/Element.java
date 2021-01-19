public abstract class Element {

    public Element(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    protected int posX, posY;

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
