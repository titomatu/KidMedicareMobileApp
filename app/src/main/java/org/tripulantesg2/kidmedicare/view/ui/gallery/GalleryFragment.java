package org.tripulantesg2.kidmedicare.view.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.tripulantesg2.kidmedicare.R;
import org.tripulantesg2.kidmedicare.databinding.FragmentGalleryBinding;
import org.tripulantesg2.kidmedicare.model.Solicitud;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private static final String TAG = "tester";

    private FirebaseFirestore db;
    private List<Solicitud> lista_solicitudes;
    private ArrayAdapter<Solicitud> adapter;
    private View root;

    private AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Log.d(TAG, "Se seleccionó: " + lista_solicitudes.get(position).toString());
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        lista_solicitudes = new ArrayList<Solicitud>();

        this.obtenerSolicitudes();


        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void obtenerSolicitudes(){
        db.collection("solicitudes_kidmedicare")
                .whereEqualTo("state", "A")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Solicitud solicitud = document.toObject(Solicitud.class);
                                lista_solicitudes.add(solicitud);
                                Log.d(TAG, "===> documento: " + solicitud.toString());
                            }

                            adapter = new ArrayAdapter<Solicitud>(getContext(), android.R.layout.simple_list_item_1, lista_solicitudes);
                            showListView(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void showListView(ArrayAdapter<Solicitud> adapter){
        ListView listaSolicitudes = (ListView) root.findViewById(R.id.id_lista_solicitudes);

        listaSolicitudes.setAdapter(adapter);
        listaSolicitudes.setOnItemClickListener(messageClickedHandler);
    }
}