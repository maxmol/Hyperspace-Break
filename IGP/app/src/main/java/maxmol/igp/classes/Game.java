package maxmol.igp.classes;

import android.content.Context;
import android.widget.TextView;

// @ Static class used for storing game variables
public class Game {
    private static Integer Step;
    private static Integer Money;
    private static Integer Health;
    public static Integer MaxHealthLevel;
    public static Integer BombLevel;
    public static Integer AttackLevel;
    public static Integer CritLevel;
    public static Integer Difficulty;
    public static Integer HighScore = 0;

    public static CharSequence[] Difficulties = new CharSequence[]{"Easy", "Normal", "Hard"};

    public static Integer LaserAttackCost = 50;
    public static Integer LaserAttackCount;

    private static Integer Stage;

    public static TextView moneyTextView = null;

    public static Context context = null;

    public static void reset() {
        Step = 1;
        Money = 30;
        MaxHealthLevel = 0;
        BombLevel = 0;
        AttackLevel = 0;
        CritLevel = 0;
        LaserAttackCount = 0;
        Difficulty = 0;
        Health = Game.getMaxHealth();
    }

    public static String[] GetTable() {
        return new String[] {Step.toString(), Money.toString(), MaxHealthLevel.toString(), BombLevel.toString(), AttackLevel.toString(), CritLevel.toString(), LaserAttackCount.toString(), Difficulty.toString(), HighScore.toString()};
    }


    /*
    @ Parse saved table to static vars
    @params
        tbl: Source table
     */
    public static void SetTable(String[] tbl) {
        MaxHealthLevel = Integer.parseInt(tbl[2]);
        BombLevel = Integer.parseInt(tbl[3]);
        AttackLevel = Integer.parseInt(tbl[4]);
        CritLevel = Integer.parseInt(tbl[5]);
        LaserAttackCount = Integer.parseInt(tbl[6]);
        Difficulty = Integer.parseInt(tbl[7]);
        HighScore = Integer.parseInt(tbl[8]);

        setStep(Integer.parseInt(tbl[0]));
        setMoney(Integer.parseInt(tbl[1]));
    }

    /*
    @ Calculate the price of an upgrade
    @params
        curLevel: Level for which the price is calculated
     */
    public static int getUpgradeCost(Integer curLevel) {
        int res = (int) (25 * (Math.pow(1.5, (curLevel + 1))));
        res = res - res % 10;
        return res;
    }

    /*
    @ Stages methods (getters, setters, etc.)
     */
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


    /*
    @ Money methods (get - return, set - setter, etc )
     */
    public static Integer getMoney() {
        return Money;
    }

    /*
    @ update UI after money amount changes
     */
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
        return 50 + MaxHealthLevel * 20;
    }
}
