package ma.emsi.tp_volley_image;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.emsi.tp_volley_image.adapter.EtudiantAdapter;
import ma.emsi.tp_volley_image.beans.Etudiant;

public class StudentsListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EtudiantAdapter etudiantAdapter = null;
    private List<Etudiant> etudiants;
    private static final String TAG = "StudentsListActivity";
    private static final String deleteUrl = "https://10.0.2.2/projet/ws/deleteEtudiant.php";

    public List<Etudiant> getAll() {
        List<Etudiant> etudiants = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(StudentsListActivity.this);
        String url = "https://10.0.2.2/projet/ws/loadEtudiant.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type type = new TypeToken<List<Etudiant>>() {
                        }.getType();
                        List<Etudiant> students = new Gson().fromJson(response, type);
                        etudiants.addAll(students);
                        for (Etudiant e : etudiants) {
                            Log.d(TAG, e.toString());
                        }
                        etudiantAdapter = new EtudiantAdapter(StudentsListActivity.this, etudiants);
                        recyclerView.setAdapter(etudiantAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(StudentsListActivity.this));

                        if(students.size() == 0){
                            Toast.makeText(StudentsListActivity.this, "Aucun etudiant trouver", Toast.LENGTH_LONG).show();
                            Toast.makeText(StudentsListActivity.this, "Ajouter un etudiant", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(StudentsListActivity.this, AddStudentActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        queue.add(stringRequest);
        return etudiants;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        recyclerView = findViewById(R.id.recycle_view);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        getAll();
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
            TextView ids = viewHolder.itemView.findViewById(R.id.ids);
            Toast.makeText(StudentsListActivity.this, "Bien supprimer", Toast.LENGTH_SHORT).show();
            deleteStudent(ids.getText().toString());
        }
    };

    private void deleteStudent(String ids) {
        StringRequest request = new StringRequest(Request.Method.POST, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Bien supprimer", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentsListActivity.this, StudentsListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                Log.d("EditStudentActivity", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", ids);
                map.put("nom", "");
                map.put("prenom", "");
                map.put("sexe", "");
                map.put("ville", "");
                map.put("image", "");
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}