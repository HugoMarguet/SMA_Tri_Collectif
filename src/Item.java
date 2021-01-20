public class Item extends Element{

    public enum ID {
        A, B;
        public String toString() {
            return this == ID.A ?
                    LoggerUtil.coloredString("A", LoggerUtil.Color.ANSI_RED)
                    : LoggerUtil.coloredString("B", LoggerUtil.Color.ANSI_BLUE);
        }
    }
    private ID id;

    public Item(ID id, int posX, int posY) {

        super(posX, posY);

        this.id = id;
    }

    public String toString() {
        return id.toString();
    }

    public Item copy() {
        return new Item(id, posX, posY);
    }

    public ID getId() {
        return id;
    }

}

class LoggerUtil {

    public static String coloredString(CharSequence stringToColor, LoggerUtil.Color color) {
        return String.format("%s%s%s", color.getValue(), stringToColor, Color.ANSI_RESET.getValue());
    }

    public enum Color {
        ANSI_RED("\u001B[31m"),
        ANSI_BLUE("\u001B[34m"),
        ANSI_RESET("\u001B[0m");

        private String value;

        Color(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
