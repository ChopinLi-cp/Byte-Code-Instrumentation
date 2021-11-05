package agent;

public enum OPERATION{
WRITE(0), READ(1);
private int value;

    OPERATION(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}


