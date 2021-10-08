package maxmol.igp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import maxmol.igp.classes.Game;
import maxmol.igp.classes.SaveLoad;
import maxmol.igp.classes.SaveLoad.SaveLoad_NoFileSpecified;

/**
 * Buy/Upgrades menu activity. It's called 'GameActivity' only because initially I thought it would be the actual game activity with game canvas and all.
 * Now there's 'FlightActivity' for that.
 *
 * And here you can see how ship upgrades and laser buying work.
 *
 * Interface is mostly hardcoded here. It may not be the best variant but I wanted it that way. :^)
 */
public class GameActivity extends Activity {

    public static GameActivity context = null;

    public Button[] panelButtons;
    public ScrollView[] gamePanels;

    private ArrayList<LinearLayout> panel2_blocks = new ArrayList<>();
    private ArrayList<Button> panel2_btns = new ArrayList<>();
    private ArrayList<TextView> panel2_levels = new ArrayList<>();
    private ArrayList<TextView> panel2_costs = new ArrayList<>();

    private ArrayList<LinearLayout> panel3_blocks = new ArrayList<>();
    private ArrayList<Button> panel3_btns = new ArrayList<>();
    private ArrayList<TextView> panel3_counts = new ArrayList<>();
    private ArrayList<TextView> panel3_costs = new ArrayList<>();

    private Integer currentPanel = 0;
    public boolean pressedButton = false;
    private Typeface font;

    /**
     * change the focused panel after pressing "Upgrades", "Shop" buttons
     * @param id: panel number
     */
    public void setCurrentPanel(int id) {
        if (id == currentPanel) return;

        gamePanels[currentPanel].setVisibility(View.GONE);
        gamePanels[id].setVisibility(View.VISIBLE);

        currentPanel = id;
    }

    /**
     * update panel 2 block after game data was modified
     * @param id: block id
     * @param val: level
     */
    public void panel2_updateBlock(int id, int val) {
        panel2_levels.get(id).setText((val < 5) ? (getResources().getString(R.string.level) + " " + val) : "MAX");
        panel2_costs.get(id).setText((val < 5) ? Game.formatMoney(Game.getUpgradeCost(val)) : "LEVEL");

        if (Game.getMoney() < Game.getUpgradeCost(val)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                panel2_btns.get(id).setBackgroundTintList(ContextCompat.getColorStateList(GameActivity.this, R.color.colorAccent));
            }
            else {
                panel2_btns.get(id).setBackgroundColor(Color.rgb(216, 27, 96));
            }
            panel2_btns.get(id).setText("NOT ENOUGH");
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                panel2_btns.get(id).setBackgroundTintList(ContextCompat.getColorStateList(GameActivity.this, R.color.material_deep_teal_200));
            }
            else {
                panel2_btns.get(id).setBackgroundColor(Color.rgb(128, 203, 196));
            }
            panel2_btns.get(id).setText("UPGRADE");
        }
    }

    /**
     * update panel 3 block after game data was modified
     * @param id: block id
     * @param cost: price
     * @param count: how many we have
     */
    public void panel3_updateBlock(int id, int cost, Integer count) {
        if (count != null) panel3_counts.get(id).setText("You Have: " + " " + count);
        panel3_costs.get(id).setText(Game.formatMoney(cost));

        if (Game.getMoney() < cost) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                panel3_btns.get(id).setBackgroundTintList(ContextCompat.getColorStateList(GameActivity.this, R.color.colorAccent));
            }
            else {
                panel3_btns.get(id).setBackgroundColor(Color.rgb(196, 64, 96));
            }
            panel3_btns.get(id).setText("NOT ENOUGH");
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                panel3_btns.get(id).setBackgroundTintList(ContextCompat.getColorStateList(GameActivity.this, R.color.material_deep_teal_200));
            }
            else {
                panel3_btns.get(id).setBackgroundColor(Color.rgb(64, 196, 96));
            }
            panel3_btns.get(id).setText("BUY");
        }
    }

    /**
     * a simple method which we call to update all the info in the activity.
     */
    public void panel2_updateBlocks() {
        panel2_updateBlock(0, Game.maxHealthLevel);
        panel2_updateBlock(1, Game.attackLevel);
        panel2_updateBlock(2, Game.bombLevel);
        panel2_updateBlock(3, Game.critLevel);

        panel3_updateBlock(0, Game.laserAttackCost, Game.laserAttackCount);
    }

    /**
     * I used this function to make the app look same on different devices
     * @param dp
     * @return modified value of pixels
     */
    public static int dp(float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);
        //dp = dp * 2;
        //System.out.println(context.getResources().getSystem().getDisplayMetrics().density);
        //return (int) dp;

        return pixels;
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * we use that to get ids for our newly created UI elements. this method is borrowed from the internet!!!
     * @return new view id
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1;
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /**
     * unified format for textviews and buttons
     * @param view
     */
    public void uniformat(TextView view) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        view.setTypeface(font);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(Color.BLACK);
        view.setTextColor(Color.rgb(240, 240, 240));
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * create menu type 2
     * @param title
     * @param onClickListener
     */
    public void panel2_createBlock(String title, View.OnClickListener onClickListener) {
        LinearLayout game_panel_1_block_1 = new LinearLayout(this);
        game_panel_1_block_1.setOrientation(LinearLayout.VERTICAL);

        game_panel_1_block_1.setDividerPadding(dp(16));
        game_panel_1_block_1.setBackgroundColor(Color.rgb(0, 150, 136));
        game_panel_1_block_1.setId(generateViewId());

        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if (panel2_blocks.size() > 0) {
            rp.setMargins(0, dp(14), 0, 0);
            rp.addRule(RelativeLayout.BELOW, panel2_blocks.get(panel2_blocks.size() - 1).getId());
        }

        game_panel_1_block_1.setLayoutParams(rp);
        ((RelativeLayout) findViewById(R.id.game_panel_1_layout)).addView(game_panel_1_block_1);

        panel2_blocks.add(game_panel_1_block_1);

        TextView game_panel_1_block_1_title = new TextView(this);
        game_panel_1_block_1_title.setText(title);
        uniformat(game_panel_1_block_1_title);
        game_panel_1_block_1.addView(game_panel_1_block_1_title);

        TextView game_panel_1_block_1_level = new TextView(this);
        game_panel_1_block_1_level.setText("LEVEL 1");
        uniformat(game_panel_1_block_1_level);
        game_panel_1_block_1.addView(game_panel_1_block_1_level);

        panel2_levels.add(game_panel_1_block_1_level);

        TextView game_panel_1_block_1_cost = new TextView(this);
        game_panel_1_block_1_cost.setText("0");
        uniformat(game_panel_1_block_1_cost);
        game_panel_1_block_1.addView(game_panel_1_block_1_cost);

        panel2_costs.add(game_panel_1_block_1_cost);

        Button game_panel_1_block_1_btn = new Button(this);
        //game_panel_1_block_1_btn.setBackgroundColor(Color.rgb(64, 196, 96));
        uniformat(game_panel_1_block_1_btn);
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        //        ViewGroup.LayoutParams.MATCH_PARENT,
        //        dp(50));
        //lp.setMargins(0, dp(10), 0, 0);
        //game_panel_1_block_1_btn.setLayoutParams(lp);
        game_panel_1_block_1.addView(game_panel_1_block_1_btn);

        game_panel_1_block_1_btn.setOnClickListener(onClickListener);

        panel2_btns.add(game_panel_1_block_1_btn);
    }

    /**
     * create menu type 3
     * @param title
     * @param onClickListener
     */
    public void panel3_createBlock(String title, View.OnClickListener onClickListener) {
        LinearLayout game_panel_1_block_1 = new LinearLayout(this);
        game_panel_1_block_1.setOrientation(LinearLayout.VERTICAL);
        game_panel_1_block_1.setDividerPadding(dp(16));
        game_panel_1_block_1.setBackgroundColor(Color.rgb(0, 150, 136));
        game_panel_1_block_1.setId(generateViewId());

        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if (panel3_blocks.size() > 0) {
            rp.setMargins(0, dp(14), 0, 0);
            rp.addRule(RelativeLayout.BELOW, panel3_blocks.get(panel3_blocks.size() - 1).getId());
        }

        game_panel_1_block_1.setLayoutParams(rp);
        ((RelativeLayout) findViewById(R.id.game_panel_2_layout)).addView(game_panel_1_block_1);

        panel3_blocks.add(game_panel_1_block_1);

        TextView game_panel_1_block_1_title = new TextView(this);
        game_panel_1_block_1_title.setText(title);
        uniformat(game_panel_1_block_1_title);
        game_panel_1_block_1.addView(game_panel_1_block_1_title);

        TextView game_panel_1_block_1_count = new TextView(this);
        game_panel_1_block_1_count.setText("");
        uniformat(game_panel_1_block_1_count);
        game_panel_1_block_1.addView(game_panel_1_block_1_count);

        panel3_counts.add(game_panel_1_block_1_count);

        TextView game_panel_1_block_1_cost = new TextView(this);
        game_panel_1_block_1_cost.setText("0");
        uniformat(game_panel_1_block_1_cost);
        game_panel_1_block_1.addView(game_panel_1_block_1_cost);

        panel3_costs.add(game_panel_1_block_1_cost);

        Button game_panel_1_block_1_btn = new Button(this);
        uniformat(game_panel_1_block_1_btn);
        game_panel_1_block_1_btn.setText("BUY");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(50));
        lp.setMargins(0, dp(10), 0, 0);
        game_panel_1_block_1_btn.setLayoutParams(lp);
        game_panel_1_block_1.addView(game_panel_1_block_1_btn);

        game_panel_1_block_1_btn.setOnClickListener(onClickListener);

        panel3_btns.add(game_panel_1_block_1_btn);
    }

    /**
     * When the activity is initialized, we create all basic UI elements.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        font = ResourcesCompat.getFont(this, R.font.sabofilled);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;

        TextView money_text = (TextView) findViewById(R.id.gamestats_money);

        Game.moneyTextView = money_text;
        Game.context = getApplicationContext();

        money_text.setText(Game.formatMoney(Game.getMoney()));

        panelButtons = new Button[] {
                (Button) findViewById(R.id.game_panel_1_button),
                (Button) findViewById(R.id.game_panel_2_button),
                (Button) findViewById(R.id.game_panel_3_button),
        };

        gamePanels = new ScrollView[] {
                (ScrollView) findViewById(R.id.game_panel_1),
                (ScrollView) findViewById(R.id.game_panel_2),
        };


        panelButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.setCurrentPanel(0);
            }
        });

        panelButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.setCurrentPanel(1);
            }
        });

        panelButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (Game.getHealth() <= 0) {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Your ship is destoyed!")
                            .setMessage("You can't start when your HP is 0")
                            .setPositiveButton("Continue", null)
                            .show();
                }
                else {*/
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Stage " + Game.getStage())
                        .setMessage("Are you sure you want to start this stage?")
                        .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pressedButton = true;

                                Intent intent = new Intent(context, FlightActivity.class);
                                startActivityForResult(intent, 0);
                            }

                        })
                        .setNegativeButton("Close", null)
                        .show();
                //}
            }
        });

        // ----

        final SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        final int button10 = soundPool.load(getApplicationContext(), R.raw.button10, 1);
        final int blip1 = soundPool.load(getApplicationContext(), R.raw.blip1, 1);

        panel2_createBlock("Max Health", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Game.maxHealthLevel >= 5) {
                    soundPool.play(button10, 1, 1, 0, 0, 1);
                    return;
                }

                Integer needMoney = Game.getUpgradeCost(Game.maxHealthLevel);
                if (Game.getMoney() >= needMoney) {
                    Game.takeMoney(needMoney);
                    int lasthealth = Game.getMaxHealth();
                    Game.maxHealthLevel++;
                    Game.setHealth(Game.getHealth() + Game.getMaxHealth() - lasthealth);
                    soundPool.play(blip1, 1, 1, 0, 0, 1);
                }
                else {
                    soundPool.play(button10, 1, 1, 0, 0, 1);
                }

                panel2_updateBlocks();
            }
        });
        panel2_createBlock("Attack", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Game.attackLevel >= 5) {
                    soundPool.play(button10, 1, 1, 0, 0, 1);
                    return;
                }

                Integer needMoney = Game.getUpgradeCost(Game.attackLevel);
                if (Game.getMoney() >= needMoney) {
                    Game.takeMoney(needMoney);
                    Game.attackLevel++;
                    soundPool.play(blip1, 1, 1, 0, 0, 1);
                }
                else {
                    soundPool.play(button10, 1, 1, 0, 0, 1);
                }

                panel2_updateBlocks();
            }
        });
        panel2_createBlock("Laser Beam", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Game.bombLevel >= 5) {
                    soundPool.play(button10, 1, 1, 0, 0, 1);
                    return;
                }

                Integer needMoney = Game.getUpgradeCost(Game.bombLevel);
                if (Game.getMoney() >= needMoney) {
                    Game.takeMoney(needMoney);
                    Game.bombLevel++;
                    soundPool.play(blip1, 1, 1, 0, 0, 1);
                }
                else {
                    soundPool.play(button10, 1, 1, 0, 0, 1);
                }

                panel2_updateBlocks();
            }
        });
        panel2_createBlock("Energy Ball", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Game.critLevel >= 5) {
                    soundPool.play(button10, 1, 1, 0, 0, 1);
                    return;
                }

                Integer needMoney = Game.getUpgradeCost(Game.critLevel);
                if (Game.getMoney() >= needMoney) {
                    Game.takeMoney(needMoney);
                    Game.critLevel++;
                    soundPool.play(blip1, 1, 1, 0, 0, 1);
                }
                else {
                    soundPool.play(button10, 1, 1, 0, 0, 1);
                }

                panel2_updateBlocks();
            }
        });

        panel3_createBlock("Laser Attack", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Game.getMoney() >= Game.laserAttackCost) {
                    Game.laserAttackCount++;
                    Game.takeMoney(Game.laserAttackCost);
                    soundPool.play(blip1, 1, 1, 0, 0, 1);
                }
                else {
                    soundPool.play(button10, 1, 1, 0, 0, 1);
                }

                panel2_updateBlocks();
            }
        });

        panel2_updateBlocks();

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            for (Button b : panelButtons) {
                b.setTypeface(font);
            }
        }
    }

    /**
     * Play background music again and update money balance after the app was opened
     */
    @Override
    protected void onResume() {
        super.onResume();

        panel2_updateBlocks();

        if (MainMenu.menu_theme != null && !MainMenu.menu_theme.isPlaying()) {
            MainMenu.menu_theme.start();
        }

        Game.moneyTextView.setText(Game.formatMoney(Game.getMoney()));
        Game.moneyTextView.setText(Game.formatMoney(Game.getMoney()));
    }

    /**
     * This runs after the game is stopped. save progress!
     */
    @Override
    protected void onStop() {
        super.onStop();

        try {
            SaveLoad.save();
        } catch (SaveLoad_NoFileSpecified saveLoad_noFileSpecified) {
            saveLoad_noFileSpecified.printStackTrace();
        }
    }

    /**
     * If the application was minimized we pause the game and music
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (!this.isFinishing() && !pressedButton && MainMenu.menu_theme != null) {
            MainMenu.menu_theme.pause();
        }

        pressedButton = false;
    }
}
