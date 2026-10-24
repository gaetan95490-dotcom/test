package fr.philips.minimal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private LinearLayout root, appsContainer;
    private TextView status;
    private final String[] labels = {"YouTube", "Netflix", "Prime Video", "Disney+"};
    private final String[] packages = {"com.google.android.youtube.tv", "com.netflix.ninja", "com.amazon.amazonvideo.livingroom", "com.disney.disneyplus"};
    private final int[] wallpaperResources = {R.drawable.fond_1, R.drawable.fond_2, R.drawable.fond_3, R.drawable.fond_4, R.drawable.fond_5, R.drawable.fond_6};
    private final String[] wallpaperNames = {"Fond 1", "Fond 2", "Fond 3", "Fond 4", "Fond 5", "Fond 6"};
    private SharedPreferences prefs;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("philips_minimal", MODE_PRIVATE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        buildUi();
    }

    private void buildUi() {
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(42), dp(20), dp(42), dp(20));
        root.setBackgroundResource(wallpaperResources[Math.max(0, Math.min(5, prefs.getInt("wallpaper", 0)))]); setContentView(root);
        LinearLayout top = new LinearLayout(this); top.setGravity(Gravity.CENTER_VERTICAL);
        TextView brand = text("PHILIPS MINIMAL", 40, 0xFFDDDDDD, true); brand.setLetterSpacing(.16f); top.addView(brand, new LinearLayout.LayoutParams(0, dp(48), 1));
        TextClock clock = new TextClock(this); clock.setFormat24Hour("HH:mm"); clock.setTextColor(Color.WHITE); clock.setTextSize(35); clock.setTypeface(Typeface.create("sans-serif-light", 0)); top.addView(clock);
        top.addView(spacer(22));
        TextView customize = text("⚙", 28, Color.WHITE, true); customize.setContentDescription("Personnaliser"); focusable(customize); customize.setOnClickListener(v -> showCustomization()); top.addView(customize);
        top.addView(spacer(24));
        TextView home = text("⌂", 32, Color.WHITE, true); home.setContentDescription("Quitter Philips Minimal"); focusable(home); home.setOnClickListener(v -> finish()); top.addView(home);
        root.addView(top);
        TextView heading = text("Que souhaitez-vous regarder ?", 26, Color.WHITE, false); heading.setPadding(0, dp(35), 0, dp(20)); root.addView(heading);
        ScrollView scroll = new ScrollView(this); appsContainer = new LinearLayout(this); appsContainer.setOrientation(LinearLayout.VERTICAL); scroll.addView(appsContainer); root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        status = text("Utilisez les flèches et OK de la télécommande.", 13, 0xFFBBBBBB, false); status.setPadding(0, dp(12), 0, 0); root.addView(status);
        buildApps();
    }

    private void buildApps() {
        appsContainer.removeAllViews(); boolean list = prefs.getBoolean("list", false);
        if (list) {
            for (int i=0; i<labels.length; i++) { final int ix=i; LinearLayout item = new LinearLayout(this); item.setGravity(Gravity.CENTER_VERTICAL); item.setPadding(dp(20), dp(8), dp(20), dp(8)); item.setBackground(tileBg(false)); item.setFocusable(true); item.setClickable(true); item.setOnFocusChangeListener((v,f)->v.setBackground(tileBg(f))); item.setOnClickListener(v->launch(ix));
                ImageView icon = appIcon(ix); item.addView(icon, new LinearLayout.LayoutParams(dp(54), dp(54))); TextView label=text(labels[i],18,Color.WHITE,true); label.setPadding(dp(20),0,0,0); item.addView(label); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,dp(72)); lp.bottomMargin=dp(8); appsContainer.addView(item,lp); }
        } else {
            LinearLayout row = null;
            for(int i=0;i<labels.length;i++){ if(i%4==0){row=new LinearLayout(this); row.setGravity(Gravity.CENTER); appsContainer.addView(row,new LinearLayout.LayoutParams(-1,dp(150)));}
                final int ix=i; LinearLayout tile=new LinearLayout(this); tile.setGravity(Gravity.CENTER); tile.setPadding(dp(10),dp(10),dp(10),dp(10)); tile.setBackground(tileBg(false)); tile.setFocusable(true); tile.setClickable(true); tile.setOnFocusChangeListener((v,f)->v.setBackground(tileBg(f))); tile.setOnClickListener(v->launch(ix)); tile.addView(appIcon(i),new LinearLayout.LayoutParams(dp(88),dp(88))); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(120),1); lp.setMargins(dp(7),dp(6),dp(7),dp(6)); row.addView(tile,lp); }
        }
    }
    private ImageView appIcon(int i){ ImageView iv=new ImageView(this); iv.setScaleType(ImageView.ScaleType.FIT_CENTER); try { Drawable d=getPackageManager().getApplicationIcon(packages[i]); iv.setImageDrawable(d); } catch(Exception e){ TextView fallback=text(labels[i].substring(0,1),36,Color.WHITE,true); fallback.setGravity(Gravity.CENTER); iv.setImageDrawable(fallback.getBackground()); iv.setContentDescription(labels[i]); } iv.setContentDescription(labels[i]); return iv; }
    private void launch(int i){ Intent intent=getPackageManager().getLaunchIntentForPackage(packages[i]); if(intent==null){status.setText(labels[i]+" n'est pas trouvée sur la télévision."); Toast.makeText(this,"Application non installée ou incompatible",Toast.LENGTH_LONG).show();return;} try{startActivity(intent);}catch(Exception e){status.setText("Impossible d'ouvrir "+labels[i]+".");} }
    private void showCustomization(){ final String[] options={"Présentation : gros logos / liste", "Choisir le fond d'écran", "Paramètres Android", "Retour"}; new AlertDialog.Builder(this).setTitle("Personnaliser Philips Minimal").setItems(options,(d,which)->{if(which==0){prefs.edit().putBoolean("list",!prefs.getBoolean("list",false)).apply();buildUi();}else if(which==1)showWallpapers();else if(which==2){try{startActivity(new Intent(Settings.ACTION_SETTINGS));}catch(Exception e){Toast.makeText(this,"Paramètres indisponibles",Toast.LENGTH_SHORT).show();}}}).show(); }
    private void showWallpapers(){new AlertDialog.Builder(this).setTitle("Choisir un fond d'écran").setSingleChoiceItems(wallpaperNames,prefs.getInt("wallpaper",0),(d,which)->{prefs.edit().putInt("wallpaper",which).apply(); d.dismiss(); buildUi();}).setNegativeButton("Annuler",null).show();}
    private void focusable(View v){v.setFocusable(true);v.setClickable(true);v.setPadding(dp(8),dp(4),dp(8),dp(4));v.setBackground(tileBg(false));v.setOnFocusChangeListener((view,focused)->view.setBackground(tileBg(focused)));}
    private GradientDrawable tileBg(boolean focused){GradientDrawable d=new GradientDrawable();d.setColor(focused?0xFF3A3A3A:0x33111111);d.setCornerRadius(dp(10));d.setStroke(dp(focused?3:1),focused?Color.WHITE:0x55777777);return d;}
    private TextView text(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setTypeface(Typeface.create("sans-serif",bold?Typeface.BOLD:Typeface.NORMAL));return t;}
    private View spacer(int w){View v=new View(this);v.setLayoutParams(new LinearLayout.LayoutParams(dp(w),1));return v;}
    private int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
    @Override public boolean onKeyDown(int keyCode,KeyEvent event){if(keyCode==KeyEvent.KEYCODE_MENU){showCustomization();return true;}return super.onKeyDown(keyCode,event);}
}
