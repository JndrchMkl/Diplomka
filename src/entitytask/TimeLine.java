package entitytask;

public class TimeLine {
    int actualTick = 0;
    int endTick = SystemCore.END_TICK;

    public void timeShift(int actualTime) {
        if (actualTime < 0 || actualTime > endTick) {
            return;
        }
        actualTick = actualTime;
    }

    public void timeIncrement() {
        actualTick++;
    }

    public int getActualTick() {
        return actualTick;
    }
}
