package maxmol.igp.Classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;


/**
 * Static class used for storing game variables
 */
public class Game {
    private static Integer step;
    private static Integer money;
    private static Integer health;
    public static Integer maxHealthLevel;
    public static Integer bombLevel;
    public static Integer attackLevel;
    public static Integer critLevel;
    public static Integer difficulty;
    public static Integer highScore = 0;

    public static CharSequence[] difficulties = new CharSequence[]{"Easy", "Normal", "Hard"};

    public static Integer laserAttackCost = 50;
    public static Integer laserAttackCount;

    private static Integer stage;

    public static TextView moneyTextView = null;

    public static Context context = null;

    /**
     * Resets all game progress
     */
    public static void reset() {
        step = 0;
        money = 30;
        maxHealthLevel = 0;
        bombLevel = 0;
        attackLevel = 0;
        critLevel = 0;
        laserAttackCount = 0;
        difficulty = 0;
        health = Game.getMaxHealth();
    }

    /**
     * @return a table containing all saved (permanently to a file) variables
     */
    @NonNull
    public static String[] getTable() {
        return new String[] {step.toString(), money.toString(), maxHealthLevel.toString(), bombLevel.toString(), attackLevel.toString(), critLevel.toString(), laserAttackCount.toString(), difficulty.toString(), highScore.toString()};
    }


    /**
     * Parse saved table to static vars
     * @param tbl:Source table
     */
    public static void setTable(String[] tbl) {
        maxHealthLevel = Integer.parseInt(tbl[2]);
        bombLevel = Integer.parseInt(tbl[3]);
        attackLevel = Integer.parseInt(tbl[4]);
        critLevel = Integer.parseInt(tbl[5]);
        laserAttackCount = Integer.parseInt(tbl[6]);
        difficulty = Integer.parseInt(tbl[7]);
        highScore = Integer.parseInt(tbl[8]);

        setStep(Integer.parseInt(tbl[0]));
        setMoney(Integer.parseInt(tbl[1]));
    }

    /**
     * @param curLevel: Level for which the price is calculated
     * @return calculated price of an upgrade
     */
    public static int getUpgradeCost(Integer curLevel) {
        int res = (int) (25 * (Math.pow(1.5, (curLevel + 1))));
        res = res - res % 10;
        return res;
    }

    /**
     * below are simple getters, setters, etc
     */
    public static Integer getStep() {
        return step;
    }

    public static void setStep(Integer step) {
        Game.step = step;
    }

    public static void nextStep() {
        setStep(getStep() + 1);
    }

    public static Integer getStage() {
        return stage;
    }

    public static void setStage(Integer stage) {
        Game.stage = stage;
    }

    public static Integer getMoney() {
        return money;
    }

    public static void setMoney(Integer money) {
        Game.money = money;

        if (moneyTextView != null) {
            try {
                System.out.println(Game.formatMoney(money));
                moneyTextView.setText(Game.formatMoney(money));
                System.out.println(moneyTextView.getText());
            }
            catch (Exception e) {

            }
        }
    }

    public static void addMoney(Integer money) {
        setMoney(getMoney() + money);
    }

    public static void takeMoney(Integer money) {
        setMoney(getMoney() - money);
    }

    public static String formatMoney(Integer money) {
        return "¤" + money;
    }

    public static Integer getHealth() {
        return health;
    }

    public static void setHealth(Integer hp) {
        health = MUtil.clamp(hp, 0, getMaxHealth());
    }

    public static void takeDamage(Integer hp) {
        setHealth(getHealth() - MUtil.clamp(hp, 0));
    }

    public static Integer getMaxHealth() {
        return 50 + maxHealthLevel * 20;
    }
}
