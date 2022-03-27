package ma.emsi.tp_volley_image.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ma.emsi.tp_volley_image.EditStudentActivity;
import ma.emsi.tp_volley_image.R;
import ma.emsi.tp_volley_image.beans.Etudiant;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {

    private final String TAG = "EtudiantAdapter";
    private List<Etudiant> etudiants;
    private Context context;
    private String server = "https://10.0.2.2/projet/images/";

    public EtudiantAdapter(Context context, List<Etudiant> etudiants) {
        this.etudiants = etudiants;
        this.context = context;
        Log.d(TAG, etudiants.size()+"");
    }

    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.student_item, parent, false);
        final EtudiantViewHolder holder = new EtudiantViewHolder(v);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nom = view.findViewById(R.id.name);
                TextView sexe = view.findViewById(R.id.sexe);
                TextView ville = view.findViewById(R.id.ville);
                TextView ids = view.findViewById(R.id.ids);
                Bitmap bitmap =
                        ((BitmapDrawable)((ImageView)view.findViewById(R.id.image)).getDrawable()).getBitmap();
                Intent intent = new Intent(context, EditStudentActivity.class);

                intent.putExtra("nom",nom.getText().toString());
                intent.putExtra("sexe", sexe.getText().toString());
                intent.putExtra("ville", ville.getText().toString());
                intent.putExtra("ids",ids.getText().toString());
                intent.putExtra("image",bitmap);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder holder, int i) {
        Log.d(TAG, "onBindView call ! " + i);
        Glide.with(context)
                .asBitmap()
                .load(server + etudiants.get(i).getImage())
                .apply(new RequestOptions().override(100, 100))
                .into(holder.img);
        holder.name.setText(etudiants.get(i).getNom() + " " + etudiants.get(i).getPrenom());
        holder.sexe.setText(etudiants.get(i).getSexe());
        holder.ville.setText(etudiants.get(i).getVille());
        holder.idss.setText(etudiants.get(i).getId() + "");
    }

    @Override
    public int getItemCount() {
        return etudiants.size();
    }

    public class EtudiantViewHolder extends RecyclerView.ViewHolder {
        TextView idss;
        ImageView img;
        TextView name;
        TextView sexe;
        TextView ville;
        CardView parent;

        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            img = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            sexe = itemView.findViewById(R.id.sexe);
            ville = itemView.findViewById(R.id.ville);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
