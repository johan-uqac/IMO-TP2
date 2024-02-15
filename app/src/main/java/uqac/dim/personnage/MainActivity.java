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

        SharedPreferences sharedPreferences = getSharedPreferences(R.string.app_name + currentCharacter.getClasse(), Context.MODE_PRIVATE);
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

        characterName.setText(name);
        characterHP.setText(hp);
        characterCA.setText(ca);
        characterDMG.setText(dmg);
        if (currentCharacter instanceof Mage) {
            EditText characterMagicPoints = findViewById(R.id.character_magic_points);
            characterMagicPoints.setText(pm);
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void saveCharacter(View v) {
        EditText characterName = findViewById(R.id.character_name);
        EditText characterHP = findViewById(R.id.character_hp);
        EditText characterCA = findViewById(R.id.character_ca);
        EditText characterDMG = findViewById(R.id.character_dmg);
        EditText characterMagicPoints = findViewById(R.id.character_magic_points);

        String name = characterName.getText().toString();
        boolean error = false;
        if (name.isEmpty()) {
            characterName.setError(getString(R.string.name_error));
            error = true;
        }

        String hp = characterHP.getText().toString();
        if (hp.isEmpty() || !isInteger(hp)) {
            characterHP.setError(getString(R.string.hp_error));
            error = true;
        }
        String ca = characterCA.getText().toString();
        if (ca.isEmpty() || !isInteger(ca)) {
            characterCA.setError(getString(R.string.ca_error));
            error = true;
        }
        String dmg = characterDMG.getText().toString();
        if (dmg.isEmpty() || !isInteger(dmg)) {
            characterDMG.setError(getString(R.string.damage_error));
            error = true;
        }
        String pm = "";
        if (currentCharacter instanceof Mage) {
            pm = characterMagicPoints.getText().toString();
            if (pm.isEmpty() || !isInteger(pm)) {
                characterMagicPoints.setError(getString(R.string.magic_points_error));
                error = true;
            }
        }
        if (error) {
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(R.string.app_name + currentCharacter.getClasse(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", characterName.getText().toString());
        editor.putString("hp", characterHP.getText().toString());
        editor.putString("ca", characterCA.getText().toString());
        editor.putString("dmg", characterDMG.getText().toString());
        if (currentCharacter instanceof Mage) {
            editor.putString("pm", characterMagicPoints.getText().toString());
        }
        editor.apply();
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