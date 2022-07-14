package com.gunjan.medihelp;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gunjan.medihelp.api.BloodSample;
import com.gunjan.medihelp.database.pojo.Age;
import com.gunjan.medihelp.database.pojo.Gender;
import com.gunjan.medihelp.database.pojo.Range;
import com.gunjan.medihelp.database.util.MediHelpDatabase;
import com.gunjan.medihelp.database.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText name_value,age_value,hb_value,rbc_value,pc_value,hct_value,mcv_value,mch_value,mchc_value,rdw_cv_value,blood_sugar_value;
    private Spinner gender_value;
    private ArrayList<BloodSample> bloodSamples;
    private HashMap<String, HashMap<Gender, HashMap<Age,Range>>> bloodSampleMap;
    private HashMap<String,String> bloodSample_categoryPair;
    
    private int ageInput;
    private String sampleName, nameInput, genderInput;
    private double hbInput, rbcInput, pcInput, hctInput, mcvInput, mchInput, mchcInput, rdw_cvInput, blood_sugarInput;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bloodSample_categoryPair = new HashMap<>();
        bloodSampleMap = new HashMap<>();

        setContentView(R.layout.activity_main);
        name_value = (EditText) findViewById(R.id.name_value);
        age_value = (EditText) findViewById(R.id.age_value);
        gender_value = (Spinner) findViewById(R.id.gender_value);

        hb_value = (EditText) findViewById(R.id.hb_value);
        rbc_value = (EditText) findViewById(R.id.rbc_value);
        pc_value = (EditText) findViewById(R.id.pc_value);
        hct_value = (EditText) findViewById(R.id.hct_value);
        mcv_value = (EditText) findViewById(R.id.mcv_value);
        mch_value = (EditText) findViewById(R.id.mch_value);
        mchc_value = (EditText) findViewById(R.id.mchc_value);
        rdw_cv_value = (EditText) findViewById(R.id.rdw_cv_value);
        blood_sugar_value = (EditText) findViewById(R.id.blood_sugar_value);




        MediHelpDatabase.getMediHelpDatabaseInstance().createDatabase();
        bloodSamples = MediHelpDatabase.getMediHelpDatabaseInstance().getBloodSamples();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    
                                                                        
@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            // Reset
            case R.id.action_reset:
                reset();
                return true;

            // Show Result
            case R.id.action_send:
                if(!errorValidate()) {
                    showResult();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid Input!", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reset()
    {
        name_value.setText("");
        age_value.setText("");
        hb_value.setText("");
        rbc_value.setText("");
        pc_value.setText("");
        hct_value.setText("");
        mcv_value.setText("");
        mch_value.setText("");
        mchc_value.setText("");
        rdw_cv_value.setText("");
        blood_sugar_value.setText("");

                                                                        
 gender_value.setSelection(0);
        Toast.makeText(getApplicationContext(), "All fields reset to default.", Toast.LENGTH_SHORT).show();
    }

    private boolean errorValidate()
    {
        nameInput = name_value.getText().toString();

        boolean errorFlag = false;
        if(nameInput.length()==0 ){
            name_value.setError("Invalid FullName!");
            errorFlag = true;
        }
        if(age_value.getText().toString().length()==0 || Integer.parseInt(age_value.getText().toString()) <= 0 || Integer.parseInt(age_value.getText().toString()) >= 105){
            age_value.setError("Invalid Age!");
            errorFlag = true;
        }
        if(hb_value.getText().toString().length()==0 ){
            hb_value.setError("Invalid Hemoglobin input!");
            errorFlag = true;
        }
        if(rbc_value.getText().toString().length()==0 ){
            rbc_value.setError("Invalid Red Blood Cell input!");
            errorFlag = true;
        }
        if(pc_value.getText().toString().length()==0 ){
            pc_value.setError("Invalid Platelets Count input!");
            errorFlag = true;
        }
        if(hct_value.getText().toString().length()==0 || Double.parseDouble(hct_value.getText().toString()) < 0 || Double.parseDouble(hct_value.getText().toString()) > 100){
            hct_value.setError("Invalid Haematacrit input!");
            errorFlag = true;
        }
        if(mcv_value.getText().toString().length()==0 ){
            mcv_value.setError("Invalid Mean Corpuscular Volume input!");
            errorFlag = true;
        }
       

                                                                             
 if(mch_value.getText().toString().length()==0 ){
            mch_value.setError("Invalid Mean Corpuscular Hemoglobin input!");
            errorFlag = true;
        }
        if(mchc_value.getText().toString().length()==0 ){
            mchc_value.setError("Invalid Mean Corpuscular Hemoglobin Concentration input!");
            errorFlag = true;
        }
        if(rdw_cv_value.getText().toString().length()==0 || Double.parseDouble(rdw_cv_value.getText().toString()) < 0 || Double.parseDouble(rdw_cv_value.getText().toString()) > 100){
            rdw_cv_value.setError("Invalid RDW-CV input!");
            errorFlag = true;
        }
        if(blood_sugar_value.getText().toString().length()==0 ){
            blood_sugar_value.setError("Invalid Blood Sugar input!");
            errorFlag = true;
        }

        return errorFlag;
    }

    private void showResult() {
        processData();
        String result = fuzzyLogic();

        StringBuilder report = new StringBuilder();
        report.append("Name: ").append(nameInput).append(" Age:").append(ageInput).append("\n").append(result);

        new AlertDialog.Builder(this)
                .setTitle(Html.fromHtml("<font color='#303F9F'>Report:</font>"))
                .setMessage(report.toString())
                .setPositiveButton(getString(R.string.alert_ok),
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                )
                .show();
    }

                                                                                
private void processData()
    {
        genderInput = gender_value.getSelectedItem().toString();
        ageInput = Integer.parseInt(age_value.getText().toString());
        hbInput = Double.parseDouble(hb_value.getText().toString());
        rbcInput = Double.parseDouble(rbc_value.getText().toString());
        pcInput = Double.parseDouble(pc_value.getText().toString());
        hctInput = Double.parseDouble(hct_value.getText().toString());
        mcvInput = Double.parseDouble(mcv_value.getText().toString());
        mchInput = Double.parseDouble(mch_value.getText().toString());
        mchcInput = Double.parseDouble(mchc_value.getText().toString());
        rdw_cvInput = Double.parseDouble(rdw_cv_value.getText().toString());
        blood_sugarInput = Double.parseDouble(blood_sugar_value.getText().toString());

        for (BloodSample bloodSample : bloodSamples)
        {
            sampleName = bloodSample.getSampleName();
            switch (sampleName)
            {
                case Utility.HB:
                    bloodSample_categoryPair.put(sampleName,bloodSample.getCategory(genderInput,ageInput,hbInput));
                    break;
                case Utility.RBC:
                    bloodSample_categoryPair.put(sampleName,bloodSample.getCategory(genderInput,ageInput,rbcInput));
                    break;
                case Utility.PC:
                    bloodSample_categoryPair.put(sampleName,bloodSample.getCategory(genderInput,ageInput,pcInput));
                    break;
                case Utility.HCT:
                    bloodSample_categoryPair.put(sampleName,bloodSample.getCategory(genderInput,ageInput,hctInput));
                    break;
                case Utility.MCV:
                    bloodSample_categoryPair.put(sampleName,bloodSample.getCategory(genderInput,ageInput,mcvInput));
                   
                                                                             
 break;
                case Utility.MCH:
                    bloodSample_categoryPair.put(sampleName,bloodSample.getCategory(genderInput,ageInput,mchInput));
                    break;
                case Utility.MCHC:
                    bloodSample_categoryPair.put(sampleName,bloodSample.getCategory(genderInput,ageInput,mchcInput));
                    break;
                case Utility.RDW_CV:
                    bloodSample_categoryPair.put(sampleName,bloodSample.getCategory(genderInput,ageInput,rdw_cvInput));
                    break;
                case Utility.BS:
                    bloodSample_categoryPair.put(sampleName,bloodSample.getCategory(genderInput,ageInput,blood_sugarInput));
                    break;


            }
        }
    }

    private String fuzzyLogic()
    {
        String result;
        if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.VERY_HIGH) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = "Problem: Folate Deficiency Anemia Vitamin(B12)"+"\n"+ "Recommendation: Take Hydroxocobelamin (B12) injection"+"\n"+ "Expert=Verified";
        }
        
                                                                             
else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = nameInput+ "is Healthy."+"\n"+"Expert=Verified";

        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = "Problem: Iron Deficiency"+"\n"+ "Recommendation: Take Iron supplements & make changes to diet"+"\n"+ "Expert=Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC ).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = "Problem: Folate Deficiency Anemia Vitamin(B12)"+"\n"+ "Recommendation: Take Hydroxocobelamin (B12) injection";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.VERY_LOW) && 


                                                                            
bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.LOW) && 
bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = "Problem: Anemia (Bleeding)"+"\n"+ "Recommendation: Stop Bleeding"+"\n"+ "Expert=Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = nameInput+ "is Healthy."+"\n"+ "Expert= Not Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = "Problem: Polycythemia (Blood Cancer)"+"\n"+ "Recommendation: Chemotherapy"+"\n"+ "Expert=Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
       
                                                                                
 {
           
 result = "Problem: Cancer"+"\n"+ "Recommendation: Chemotherapy"+"\n"+ "Expert=Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = nameInput+ "is Healthy."+"\n"+ "Expert=Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.LOW))
        {
            result = "Problem: Dengue Fever" + "\n" + "Recommendation: Use pain relivers with Acetaminophen" + "\n" + "Expert= Not Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.VERY_HIGH) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = "Problem: Dengue"+"\n"+ "Recommendation: Use pain Relivers"+"\n"+ "Expert=Verified";
        }
       

else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = "Problem: Cold and Fever "+"\n"+ "Recommendation: Use Paracetamol and Crocin"+"\n"+"Expert=Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.LOW))
        {
            result = "Problem: Cold and Viral "+"\n"+ "Recommendation: Stay away from Infections"+"\n"+ "Expert= Not Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC ).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.LOW))
        {
            result = "Problem: Chickengunya"+"\n"+ "Recommendation: No medicine, No Vaccine Only Prevent mosquito bites"+"\n"+" Expert= Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.HIGH) && 



                                                                               
bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = "Problem: Malaria "+"\n"+ "Recommendation: Protective clothes"+"\n"+ "Expert=Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.VERY_HIGH) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.VERY_HIGH) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.VERY_HIGH) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.VERY_HIGH) && bloodSample_categoryPair.get(Utility.BsS).equalsIgnoreCase(Utility.VERY_LOW))
        {
            result = "Problem: Dengue and Malaria "+"\n"+ "Recommendation: Reduce Mosquito habitats "+"\n"+ "Expert=Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.VERY_HIGH) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.VERY_HIGH) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.VERY_HIGH) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.VERY_HIGH) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.VERY_HIGH))
        {
            result = "Problem: Chickengunya and Malaria "+"\n"+ "Recommendation: Reduce Mosquito Habitat and Protective clothes"+"\n"+ "Expert=Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.LOW) && 


                                                                               
bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.HIGH))
        {
            result = "Problem: Polycythemia "+"\n"+ "Recommendation: Primary polycythemia can't be prevent but proper treat can prevent and delay symptoms"+"\n"+ "Expert=Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result =  "Problem: Jaundice" + "\n" + "Recommendation: Phototherapy or Healthy &  Balance Diet "+"\n"+ "Expert=Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RBC).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.HIGH) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.HIGH))
        {
            result = "Problem: Chickenpox" + "\n" + "Recommendation: Chickenpox Vaccine " + "\n" + "Expert=  Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.RBC ).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.VERY_LOW) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.LOW) && 

                                                                             
bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.LOW))
        
{
            result = "Problem: Fever and Chickengunya"+"\n"+ "Recommendation: No medicine, No Vaccine Only Prevent mosquito bites"+"\n"+" Expert= Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RBC ).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.LOW))
        {
            result = "Problem: Fever and Sickness"+"\n"+ "Recommendation: Sumo and Paracetamol"+"\n"+" Expert= Verified";
        }
        else if( bloodSample_categoryPair.get(Utility.HB).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RBC ).equalsIgnoreCase(Utility.LOW) && bloodSample_categoryPair.get(Utility.PC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.HCT).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCH).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.MCHC).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.RDW_CV).equalsIgnoreCase(Utility.NORMAL) && bloodSample_categoryPair.get(Utility.BS).equalsIgnoreCase(Utility.NORMAL))
        {
            result = "Problem: Fever "+"\n"+ "Recommendation: Sumo and Paracetamol"+"\n"+" Expert= Verified";
        }
        else
        {
            result = "No Problems found in database as per received inputs."+"\n"+ "Expert= Not Verified";
        }
        return result;
    }
}


