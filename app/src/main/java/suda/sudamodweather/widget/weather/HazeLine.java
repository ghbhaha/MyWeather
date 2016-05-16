package suda.sudamodweather.widget.weather;

/**
 * Created by ghbha on 2016/5/16.
 */
public class HazeLine extends BaseLine {

    public HazeLine(int maxX, int maxY) {
        super(maxX, maxY);
    }

    @Override
    protected void resetRandom() {
        startX = 0;
    }

    @Override
    protected void initRandom() {
        startX = random.nextInt(maxX);
        startY = random.nextInt(maxY);
        stopX = startX + deltaX;
    }

    public void rain() {
        if (outOfBounds())
            resetRandom();
        startX += deltaX;
        stopX += deltaX;
    }

    @Override
    protected boolean outOfBounds() {
        if (getStartX() >= maxX) {
            return true;
        }
        return false;
    }
}
