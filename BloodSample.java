package com.gunjan.medihelp.api;

import com.gunjan.medihelp.database.pojo.Age;
import com.gunjan.medihelp.database.pojo.Gender;
import com.gunjan.medihelp.database.pojo.Range;
import com.gunjan.medihelp.database.util.Utility;
import java.util.HashMap;
                                                                                 
public class BloodSample {

    private String sampleName;
    private HashMap<Gender, HashMap<Age,Range>> gender_ageRangePair;
    private HashMap<Age,Range> ageRangePair;
    private String category;
    private String sample_range;

    public HashMap<Gender, HashMap<Age, Range>> getGender_ageRangePair() {
        return gender_ageRangePair;
    }

    public BloodSample(String sampleName, HashMap<Gender, HashMap<Age, Range>> gender_ageRangePair)
    {
        this.sampleName = sampleName;
        this.gender_ageRangePair = gender_ageRangePair;
    }

    public String getCategory(String gender, int age, double inputValue) {

        for(Gender tempGender :  gender_ageRangePair.keySet())
        {
            if(tempGender.getGender().equalsIgnoreCase(gender))
            {
                ageRangePair = gender_ageRangePair.get(tempGender);
                for(Age tempAge: ageRangePair.keySet())
                {
                    if(age >= tempAge.getLower_age() && age <= tempAge.getHigher_age())
                    {
                        Range localRange = ageRangePair.get(tempAge);

                        if( inputValue < (localRange.getLower_range()-10) )
                        {
                            return Utility.VERY_LOW;
                        }
                        else if(inputValue >= (localRange.getLower_range() - 10) && inputValue < localRange.getLower_range())
                        {
                            return Utility.LOW;
                        }
                        else if(inputValue >= localRange.getLower_range() && inputValue <= localRange.getHigher_range())
                                                                                                       
                        {
                            return Utility.NORMAL;
                        }
                        else if(inputValue > localRange.getHigher_range() && inputValue <= (localRange.getHigher_range() + 10))
                        {
                            return Utility.HIGH;
                        }

                        else if(inputValue > localRange.getHigher_range()+10)
                        {
                            return Utility.VERY_HIGH;
                        }
                    }
                }
            }
        }
        return "";
    }

    public String getSampleName() {

        return sampleName;
    }

}


