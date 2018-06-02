package com.example.guru.studentattendance;

import java.util.ArrayList;

/**
 * Created by Guru on 4/11/2018.
 */

public class Daysummary {


        //property basics
        private String checkin_date;
        private String leaves;
        private ArrayList<String> session_rows;



        //constructor
        public Daysummary(String checkin_date, String leaves, ArrayList<String> session_rows){
            this.checkin_date = checkin_date;
            this.leaves = leaves;
            this.session_rows = new ArrayList<>();
            for (int i = 0; i < session_rows.size(); i++) {
                this.session_rows.add(session_rows.get(i));
            }
        }

        public String getCheckin_date() {
            return checkin_date;
        }

        public String getLeaves() {
            return leaves;
        }

        public ArrayList<String> getSession_rows() {
            return session_rows;
        }
}



