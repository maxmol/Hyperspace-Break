package info.maxmol.generals.classes;

import android.content.Context;
import android.widget.TextView;

// Static class for storing game variables
public class Game {
    private static Integer Step;
    private static Integer Money;
    private static Integer Health;
    public static Integer MaxHealthLevel;
    public static Integer BombLevel;
    public static Integer AttackLevel;
    public static Integer CritLevel;

    public static Integer LaserAttackCost = 50;
    public static Integer LaserAttackCount;

    private static Integer Stage;

    public static TextView moneyTextView = null;

    public static Context context = null;

    public static void reset() {
        Step = 1;
        Money = 100;
        Health = 100;
        MaxHealthLevel = 0;
        BombLevel = 0;
        AttackLevel = 0;
        CritLevel = 0;

        LaserAttackCount = 0;
    }

    public static Object[] GetTable() {
        return new Object[] {Step, Money, MaxHealthLevel, BombLevel, AttackLevel, CritLevel, LaserAttackCount};
    }

    public static void SetTable(String[] tbl) {
        MaxHealthLevel = Integer.parseInt(tbl[2]);
        BombLevel = Integer.parseInt(tbl[3]);
        AttackLevel = Integer.parseInt(tbl[4]);
        CritLevel = Integer.parseInt(tbl[5]);
        LaserAttackCount = Integer.parseInt(tbl[6]);

        setStep(Integer.parseInt(tbl[0]));
        setMoney(Integer.parseInt(tbl[1]));
    }

    public static int getUpgradeCost(Integer curLevel) {
        int res = (int) (75 * (Math.pow(1.25, (curLevel + 1))));
        res = res - res % 10;
        return res;
    }

    public static Integer getStep() {
        return Step;
    }

    public static void setStep(Integer step) {
        Step = step;
    }

    public static void nextStep() {
        setStep(getStep() + 1);
    }

    public static Integer getStage() {
        return Stage;
    }

    public static void setStage(Integer stage) {
        Stage = stage;
    }

    public static Integer getMoney() {
        return Money;
    }

    public static void setMoney(Integer money) {
        Money = money;

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
        return Health;
    }

    public static void setHealth(Integer hp) {
        Health = MUtil.Clamp(hp, 0, getMaxHealth());
    }

    public static void takeDamage(Integer hp) {
        setHealth(getHealth() - MUtil.Clamp(hp, 0));
    }

    public static Integer getMaxHealth() {
        return 100 + MaxHealthLevel * 25;
    }
}
