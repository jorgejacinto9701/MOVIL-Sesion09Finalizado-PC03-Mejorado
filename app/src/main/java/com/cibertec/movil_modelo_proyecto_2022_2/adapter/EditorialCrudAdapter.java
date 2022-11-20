package com.cibertec.movil_modelo_proyecto_2022_2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Editorial;
import java.util.List;

public class EditorialCrudAdapter extends ArrayAdapter<Editorial> {

    private Context context;
    private List<Editorial> lista;

    public EditorialCrudAdapter(@NonNull Context context, int resource, @NonNull List<Editorial> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_editorial_crud_item, parent, false);

        Editorial obj = lista.get(position);

        TextView txtID = row.findViewById(R.id.idCrudEditorialItemID);
        txtID.setText(String.valueOf(obj.getIdEditorial()));

        TextView  txtRazSoc = row.findViewById(R.id.idCrudEditorialItemRazoSocial);
        txtRazSoc.setText(String.valueOf(obj.getRazonSocial()));

        if (position%2 ==0){
            txtRazSoc.setBackgroundColor(Color.rgb(204, 255, 204));
            txtID.setBackgroundColor(Color.rgb(204, 255, 204));
        }
        return row;
    }
}
