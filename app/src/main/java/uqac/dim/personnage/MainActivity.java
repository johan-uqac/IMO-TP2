package uqac.dim.personnage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private Personnage mage;
    private Personnage guerrier;
    private Personnage currentCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guerrier = new Guerrier();
        mage = new Mage();
        currentCharacter = guerrier;
        loadCharacter();

        EditText characterName = findViewById(R.id.character_name);
        EditText characterHP = findViewById(R.id.character_hp);
        EditText characterCA = findViewById(R.id.character_ca);
        EditText characterDMG = findViewById(R.id.character_dmg);
        EditText characterMagicPoints = findViewById(R.id.character_magic_points);
        TextView characterClass = findViewById(R.id.character_type);
        LinearLayout characterMagicPointsContainer = findViewById(R.id.magic_points_container);
        SwitchCompat characterGood = findViewById(R.id.character_good);

        CheckBox modificationCheckBox = findViewById(R.id.enable_modifications);
        modificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i("MainActivity", "onCheckedChanged: " + b);
                characterName.setEnabled(b);
                characterHP.setEnabled(b);
                characterCA.setEnabled(b);
                characterDMG.setEnabled(b);
                characterGood.setEnabled(b);
                characterMagicPoints.setEnabled(b);
            }
        });

        RadioButton characterMage = findViewById(R.id.radiobutton_mage);
        RadioButton characterWarrior = findViewById(R.id.radiobutton_warrior);
        ImageView characterImage = findViewById(R.id.character_image);

        characterMage.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                currentCharacter = mage;
                characterMage.setChecked(true);
                characterWarrior.setChecked(false);
                characterMagicPointsContainer.setVisibility(LinearLayout.VISIBLE);
                characterClass.setText(currentCharacter.getClasse());
                characterImage.setImageDrawable(getDrawable(R.drawable.mage));
                loadCharacter();
            }
        });

        characterWarrior.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                currentCharacter = guerrier;
                characterMage.setChecked(false);
                characterWarrior.setChecked(true);
                characterMagicPointsContainer.setVisibility(LinearLayout.GONE);
                characterClass.setText(currentCharacter.getClasse());
                characterImage.setImageDrawable(getDrawable(R.drawable.guerrier));
                loadCharacter();
            }
        });

        characterGood.setOnCheckedChangeListener((compoundButton, b) -> {
            TextView characterGoodText = findViewById(R.id.character_good_text);
            if (b) {
                currentCharacter.setAlignement(Personnage.Alignement.MAUVAIS);
                characterGoodText.setText(R.string.bad_character);
            } else {
                currentCharacter.setAlignement(Personnage.Alignement.BON);
                characterGoodText.setText(R.string.good_character);
            }
        });
    }

    protected void loadCharacter() {
        EditText characterName = findViewById(R.id.character_name);
        EditText characterHP = findViewById(R.id.character_hp);
        EditText characterCA = findViewById(R.id.character_ca);
        EditText characterDMG = findViewById(R.id.character_dmg);

        SharedPreferences sharedPreferences = getSharedPreferences("Personnage" + currentCharacter.getClasse(), Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String hp = sharedPreferences.getString("hp", "5");
        String ca = sharedPreferences.getString("ca", "5");
        String dmg = sharedPreferences.getString("dmg", "5");
        String pm = "";
        if (currentCharacter instanceof Mage) {
            pm = sharedPreferences.getString("pm", "5");
        }

        currentCharacter.setNom(name);
        currentCharacter.setPV(Integer.parseInt(hp));
        currentCharacter.setCA(Integer.parseInt(ca));
        currentCharacter.setDMG(Integer.parseInt(dmg));
        if (currentCharacter instanceof Mage) {
            ((Mage) currentCharacter).setPM(Integer.parseInt(pm));
        }

        Log.i("loading class", "Personnage" + currentCharacter.getClasse());
        Log.i("loading", "loadCharacter: " + name + " " + hp + " " + ca + " " + dmg + " " + pm);

        characterName.setText(name);
        characterHP.setText(hp);
        characterCA.setText(ca);
        characterDMG.setText(dmg);
        if (currentCharacter instanceof Mage) {
            EditText characterMagicPoints = findViewById(R.id.character_magic_points);
            characterMagicPoints.setText(pm);
        }
    }

    public void saveCharacter(View v) {
        EditText characterName = findViewById(R.id.character_name);
        EditText characterHP = findViewById(R.id.character_hp);
        EditText characterCA = findViewById(R.id.character_ca);
        EditText characterDMG = findViewById(R.id.character_dmg);
        EditText characterMagicPoints = findViewById(R.id.character_magic_points);

        SharedPreferences sharedPreferences = getSharedPreferences("Personnage" + currentCharacter.getClasse(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", characterName.getText().toString());
        editor.putString("hp", characterHP.getText().toString());
        editor.putString("ca", characterCA.getText().toString());
        editor.putString("dmg", characterDMG.getText().toString());
        if (currentCharacter instanceof Mage) {
            editor.putString("pm", characterMagicPoints.getText().toString());
        }
        editor.apply();
        Log.i("SelectedCharacterClass", currentCharacter.getClasse());
        Log.i("SelectedCharacterName", characterName.getText().toString());
        Log.i("SelectedCharacterPv", characterHP.getText().toString());
        Log.i("SelectedCharacterCa", characterCA.getText().toString());
        Log.i("SelectedCharacterDMG", characterDMG.getText().toString());
        Log.i("Editor", editor.toString());
    }

    public void newCharacter(View v) {
        EditText characterName = findViewById(R.id.character_name);
        EditText characterHP = findViewById(R.id.character_hp);
        EditText characterCA = findViewById(R.id.character_ca);
        EditText characterDMG = findViewById(R.id.character_dmg);
        EditText characterMagicPoints = findViewById(R.id.character_magic_points);
        SwitchCompat characterGood = findViewById(R.id.character_good);
        CheckBox modificationCheckBox = findViewById(R.id.enable_modifications);

        characterName.setText("");
        characterHP.setText("5");
        characterCA.setText("5");
        characterDMG.setText("5");
        characterMagicPoints.setText(currentCharacter instanceof Mage ? "5" : "0");
        characterGood.setChecked(true);
        modificationCheckBox.setChecked(true);
    }

    public void resetForm(View v) {
        currentCharacter = (currentCharacter instanceof Mage) ? new Mage() : new Guerrier();
        loadCharacter();
    }
}