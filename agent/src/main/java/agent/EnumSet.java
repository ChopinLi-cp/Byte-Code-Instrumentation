package agent;

public enum EnumSet{
ADD(1),CONTAINS(2);
private int value;

    EnumSet(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}


