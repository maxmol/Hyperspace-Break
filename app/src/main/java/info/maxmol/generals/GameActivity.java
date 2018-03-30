package info.maxmol.generals;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import info.maxmol.generals.classes.Game;
import info.maxmol.generals.classes.SaveLoad;
import info.maxmol.generals.classes.SaveLoad.SaveLoad_NoFileSpecified;

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
    private ArrayList<TextView> panel3_costs = new ArrayList<>();

    private Integer currentPanel = 0;

    public void setCurrentPanel(int id) {
        if (id == currentPanel) return;

        gamePanels[currentPanel].setVisibility(View.GONE);
        gamePanels[id].setVisibility(View.VISIBLE);

        currentPanel = id;
    }

    public void panel2_updateBlock(int id, int val) {
        panel2_levels.get(id).setText(getResources().getString(R.string.level) + " " + val);
        panel2_costs.get(id).setText(Game.formatMoney(Game.getUpgradeCost(val)));

        if (Game.getMoney() < Game.getUpgradeCost(val)) {
            panel2_btns.get(id).setBackgroundColor(Color.rgb(196, 64, 96));
            panel2_btns.get(id).setText("NOT ENOUGH");
        }
        else {
            panel2_btns.get(id).setBackgroundColor(Color.rgb(64, 196, 96));
            panel2_btns.get(id).setText("UPGRADE");
        }
    }

    public void panel3_updateBlock(int id, int val) {
        panel3_costs.get(id).setText(Game.formatMoney(val));

        if (Game.getMoney() < val) {
            panel3_btns.get(id).setBackgroundColor(Color.rgb(196, 64, 96));
            panel3_btns.get(id).setText("NOT ENOUGH");
        }
        else {
            panel3_btns.get(id).setBackgroundColor(Color.rgb(64, 196, 96));
            panel3_btns.get(id).setText("BUY");
        }
    }

    public void panel2_updateBlocks() {
        panel2_updateBlock(0, Game.MaxHealthLevel);
        panel2_updateBlock(1, Game.AttackLevel);
        panel2_updateBlock(2, Game.DefenceLevel);
        panel2_updateBlock(3, Game.CritLevel);

        panel3_updateBlock(0, Game.getMaxHealth());
    }

    public static int dp(float dp) {
        /*DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);*/
        dp = dp * 2;
        //System.out.println(context.getResources().getSystem().getDisplayMetrics().density);
        return (int) dp;

        //return pixels;
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public void panel2_createBlock(String title, View.OnClickListener onClickListener) {
        LinearLayout game_panel_1_block_1 = new LinearLayout(this);
        game_panel_1_block_1.setOrientation(LinearLayout.VERTICAL);

        game_panel_1_block_1.setDividerPadding(dp(16));
        game_panel_1_block_1.setBackgroundColor(Color.rgb(64, 96, 200));
        game_panel_1_block_1.setId(generateViewId());

        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if (panel2_blocks.size() > 0) {
            rp.setMargins(0, dp(14), 0, 0);
            rp.addRule(RelativeLayout.BELOW, panel2_blocks.get(panel2_blocks.size() - 1).getId());
            System.out.println(panel2_blocks.get(panel2_blocks.size() - 1).getId());
        }

        game_panel_1_block_1.setLayoutParams(rp);
        ((RelativeLayout) findViewById(R.id.game_panel_1_layout)).addView(game_panel_1_block_1);

        panel2_blocks.add(game_panel_1_block_1);

        TextView game_panel_1_block_1_title = new TextView(this);
        game_panel_1_block_1_title.setText(title);
        game_panel_1_block_1_title.setTextSize(dp(12));
        game_panel_1_block_1_title.setGravity(Gravity.CENTER);
        game_panel_1_block_1_title.setTextColor(Color.WHITE);
        game_panel_1_block_1_title.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        game_panel_1_block_1.addView(game_panel_1_block_1_title);

        TextView game_panel_1_block_1_level = new TextView(this);
        game_panel_1_block_1_level.setText("LEVEL 1");
        game_panel_1_block_1_level.setTextSize(dp(8));
        game_panel_1_block_1_level.setGravity(Gravity.CENTER);
        game_panel_1_block_1_level.setTextColor(Color.WHITE);
        game_panel_1_block_1_level.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        game_panel_1_block_1.addView(game_panel_1_block_1_level);

        panel2_levels.add(game_panel_1_block_1_level);

        TextView game_panel_1_block_1_cost = new TextView(this);
        game_panel_1_block_1_cost.setText("0");
        game_panel_1_block_1_cost.setTextSize(dp(8));
        game_panel_1_block_1_cost.setGravity(Gravity.CENTER);
        game_panel_1_block_1_cost.setTextColor(Color.WHITE);
        game_panel_1_block_1_cost.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        game_panel_1_block_1.addView(game_panel_1_block_1_cost);

        panel2_costs.add(game_panel_1_block_1_cost);

        Button game_panel_1_block_1_btn = new Button(this);
        game_panel_1_block_1_btn.setBackgroundColor(Color.rgb(64, 196, 96));
        game_panel_1_block_1_btn.setTextSize(dp(5));
        game_panel_1_block_1_btn.setTextColor(Color.WHITE);
        game_panel_1_block_1_btn.setText("UPGRADE");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(40));
        lp.setMargins(0, dp(10), 0, 0);
        game_panel_1_block_1_btn.setLayoutParams(lp);
        game_panel_1_block_1.addView(game_panel_1_block_1_btn);

        game_panel_1_block_1_btn.setOnClickListener(onClickListener);

        panel2_btns.add(game_panel_1_block_1_btn);
    }

    public void panel3_createBlock(String title, View.OnClickListener onClickListener) {
        LinearLayout game_panel_1_block_1 = new LinearLayout(this);
        game_panel_1_block_1.setOrientation(LinearLayout.VERTICAL);

        game_panel_1_block_1.setDividerPadding(dp(16));
        game_panel_1_block_1.setBackgroundColor(Color.rgb(64, 96, 200));
        game_panel_1_block_1.setId(generateViewId());

        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if (panel3_blocks.size() > 0) {
            rp.setMargins(0, dp(14), 0, 0);
            rp.addRule(RelativeLayout.BELOW, panel3_blocks.get(panel3_blocks.size() - 1).getId());
            System.out.println(panel3_blocks.get(panel3_blocks.size() - 1).getId());
        }

        game_panel_1_block_1.setLayoutParams(rp);
        ((RelativeLayout) findViewById(R.id.game_panel_2_layout)).addView(game_panel_1_block_1);

        panel3_blocks.add(game_panel_1_block_1);

        TextView game_panel_1_block_1_title = new TextView(this);
        game_panel_1_block_1_title.setText(title);
        game_panel_1_block_1_title.setTextSize(dp(12));
        game_panel_1_block_1_title.setGravity(Gravity.CENTER);
        game_panel_1_block_1_title.setTextColor(Color.WHITE);
        game_panel_1_block_1_title.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        game_panel_1_block_1.addView(game_panel_1_block_1_title);

        TextView game_panel_1_block_1_cost = new TextView(this);
        game_panel_1_block_1_cost.setText("0");
        game_panel_1_block_1_cost.setTextSize(dp(8));
        game_panel_1_block_1_cost.setGravity(Gravity.CENTER);
        game_panel_1_block_1_cost.setTextColor(Color.WHITE);
        game_panel_1_block_1_cost.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        game_panel_1_block_1.addView(game_panel_1_block_1_cost);

        panel3_costs.add(game_panel_1_block_1_cost);

        Button game_panel_1_block_1_btn = new Button(this);
        game_panel_1_block_1_btn.setBackgroundColor(Color.rgb(64, 196, 96));
        game_panel_1_block_1_btn.setTextSize(dp(5));
        game_panel_1_block_1_btn.setTextColor(Color.WHITE);
        game_panel_1_block_1_btn.setText("BUY");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(40));
        lp.setMargins(0, dp(10), 0, 0);
        game_panel_1_block_1_btn.setLayoutParams(lp);
        game_panel_1_block_1.addView(game_panel_1_block_1_btn);

        game_panel_1_block_1_btn.setOnClickListener(onClickListener);

        panel3_btns.add(game_panel_1_block_1_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
                if (Game.getHealth() <= 0) {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Your ship is destoyed!")
                            .setMessage("You can't start when your HP is 0")
                            .setPositiveButton("Continue", null)
                            .show();
                }
                else {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Stage " + Game.getStep())
                            .setMessage("Are you sure you want to start this stage?")
                            .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, FightActivity.class);
                                    startActivityForResult(intent, 0);
                                }

                            })
                            .setNegativeButton("Close", null)
                            .show();
                }
            }
        });

        // ----

        panel2_createBlock("Max Health", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer needMoney = Game.getUpgradeCost(Game.MaxHealthLevel);
                if (Game.getMoney() >= needMoney) {
                    Game.takeMoney(needMoney);
                    int lasthealth = Game.getMaxHealth();
                    Game.MaxHealthLevel++;
                    Game.setHealth(Game.getHealth() + Game.getMaxHealth() - lasthealth);
                }

                panel2_updateBlocks();
            }
        });
        panel2_createBlock("Attack", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer needMoney = Game.getUpgradeCost(Game.AttackLevel);
                if (Game.getMoney() >= needMoney) {
                    Game.takeMoney(needMoney);
                    Game.AttackLevel++;
                }

                panel2_updateBlocks();
            }
        });
        panel2_createBlock("Defence", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer needMoney = Game.getUpgradeCost(Game.DefenceLevel);
                if (Game.getMoney() >= needMoney) {
                    Game.takeMoney(needMoney);
                    Game.DefenceLevel++;
                }

                panel2_updateBlocks();
            }
        });
        panel2_createBlock("Crit Chance", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer needMoney = Game.getUpgradeCost(Game.CritLevel);
                if (Game.getMoney() >= needMoney) {
                    Game.takeMoney(needMoney);
                    Game.CritLevel++;
                }

                panel2_updateBlocks();
            }
        });

        panel3_createBlock("Full Health", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer needMoney = Game.getMaxHealth();
                if (Game.getMoney() >= needMoney) {
                    Game.takeMoney(needMoney);
                    Game.setHealth(Game.getMaxHealth());
                }

                panel2_updateBlocks();
            }
        });

        panel2_updateBlocks();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Game.setMoney(Game.getMoney());
        panel2_updateBlocks();
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            SaveLoad.Save();
        } catch (SaveLoad_NoFileSpecified saveLoad_noFileSpecified) {
            saveLoad_noFileSpecified.printStackTrace();
        }
    }
}
