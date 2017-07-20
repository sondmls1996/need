package com.needfood.kh.SupportClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Vi on 2/2/2017.
 */

public class ChangeDatetoTimestamp

{

        public static String main(String datetime )
        {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date date = new java.util.Date();
            try {
                date = formatter.parse(datetime);
                //  bbdate = formatter.parse(birthbb2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            java.sql.Date date2 = new  java.sql.Date(date.getTime());
            //  java.sql.Date date3 = new  java.sql.Date(bbdate.getTime());
            String finaldate = (date2.getTime()/1000)+"";
            return finaldate;
        }

}
