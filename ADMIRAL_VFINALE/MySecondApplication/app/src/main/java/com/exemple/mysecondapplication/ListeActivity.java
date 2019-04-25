package com.exemple.mysecondapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class ListeActivity extends AppCompatActivity {

    RecyclerView list_view;
    Button btnPresent;
    Button btnEnvoyer;
    ArrayList<Student> students = new ArrayList();
    StudentAdapter adapter;
    int order = 0;
    Intent i1 = new Intent();
    Bundle b1 = new Bundle();
    String time;
    Intent i2 = new Intent();
    Bundle b2 = new Bundle();
    String time1;
    Date CurDate = new Date();
    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
    String date;
    String creneau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        list_view = (RecyclerView)findViewById(R.id.list_view);
        btnPresent = (Button)findViewById(R.id.btnPresent);
        btnEnvoyer = (Button)findViewById(R.id.btnEnvoyer);

        date = f.format(CurDate);

        i1 = getIntent();
        b1 = i1.getBundleExtra("timeb");
        time = b1.getString("string");
        i2 = getIntent();
        b2 = i1.getBundleExtra("time");
        time1 = b2.getString("string");
        creneau = date + "-" + time + "--" + time1;

        new SendGetRequest().execute();

        btnPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                for(int i=0;i<students.size();i++){
                    students.get(i).status = Student.Status.PRESENT;
                }
                adapter.notifyDataSetChanged();
            }
        });

        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendPostRequest().execute();

            }
        });

        //Toast.makeText(ListeActivity.this, time + " " + time1, Toast.LENGTH_SHORT).show();
        //Toast.makeText(ListeActivity.this, students.size(), Toast.LENGTH_SHORT).show();


    }

    public class SendGetRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://265.projet3il.fr/webservice/liste_etu2.php?creneau="+creneau); // here is your URL path

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            int statut;
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray array = new JSONArray(obj.getString("records"));
                for(int i = 0; i < array.length();i++){
                    JSONObject o = array.getJSONObject(i);
                    if(o.length() == 3) {
                        Student student = new Student(o.getString("nom"), o.getString("prenom"), o.getInt("statut"));
                        students.add(student);
                    }
                    if(o.length() == 2){
                        Student student = new Student(o.getString("nom"), o.getString("prenom"));
                        students.add(student);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).statut == 1) {
                    students.get(i).status = Student.Status.PRESENT;
                }
                if (students.get(i).statut == 2) {
                    students.get(i).status = Student.Status.ABSENT;
                }
                if (students.get(i).statut == 3) {
                    students.get(i).status = Student.Status.LATE;
                }
            }

            Collections.sort(students);
            adapter = new StudentAdapter(students);
            list_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            list_view.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            int statut=0;
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).status == Student.Status.PRESENT) {
                    statut = 1;
                }
                if (students.get(i).status == Student.Status.ABSENT) {
                    statut = 2;
                }
                if (students.get(i).status == Student.Status.LATE) {
                    statut = 3;
                }

                String nom = students.get(i).name;
                String prenom = students.get(i).prenom;

                try {

                    URL url = new URL("http://265.projet3il.fr/webservice/insert.php?nom="+nom+"&prenom="+prenom+"&creneau="+creneau+"&statut="+statut);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    //writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader in = new BufferedReader(new
                                InputStreamReader(
                                conn.getInputStream()));

                        StringBuffer sb = new StringBuffer("");
                        String line = "";

                        while ((line = in.readLine()) != null) {

                            sb.append(line);
                            break;
                        }

                        in.close();
                        //return sb.toString();

                    }
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            return "0";
        }
}

}
