package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Editorial;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceEditorial;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServicePais;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorialCrudFormularioActivity extends NewAppCompatActivity {


    Spinner spnPais;
    ArrayAdapter<String> adaptador;
    ArrayList<String> paises = new ArrayList<String>();

    //Servicio
    ServiceEditorial serviceEditorial;
    ServicePais servicePais;

    //Componentes
    TextView txtTitulo;
    EditText txtRaz, txtDir, txtRuc, txtFec;
    Button btnEnviar, btnRegresar;

    //Tipo señala si es registra o actualizar
    String tipo;

    //Objeto que contiene los datos de editorial seleccionado
    Editorial obj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_crud_formulario);

        txtTitulo = findViewById(R.id.idCrudEditorialFrmTitulo);
        txtRaz = findViewById(R.id.idCrudEditorialFrmRazonSocial);
        txtDir = findViewById(R.id.idCrudEditorialFrmDireccion);
        txtRuc = findViewById(R.id.idCrudEditorialFrmRUC);
        txtFec = findViewById(R.id.idCrudEditorialFrmFechaCreacion);
        btnEnviar = findViewById(R.id.idCrudEditorialFrmBtnEnviar);
        btnRegresar = findViewById(R.id.idCrudEditorialFrmBtnRegresar);

        //Para el adapatador
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.idCrudEditorialFrmPais);
        spnPais.setAdapter(adaptador);

        //para conectar al servicio rest
        serviceEditorial = ConnectionRest.getConnection().create(ServiceEditorial.class);
        servicePais = ConnectionRest.getConnection().create(ServicePais.class);

        //Se cargan el el Spiner los paises
        cargaPais();

        Bundle extras = getIntent().getExtras();
        tipo = extras.getString("var_tipo");


        if (tipo.equals("REGISTRAR")){
            txtTitulo.setText("Mantenimiento Editorial - REGISTRA");
            btnEnviar.setText("REGISTRA");

        }else if (tipo.equals("ACTUALIZAR")){
            txtTitulo.setText("Mantenimiento Editorial - ACTUALIZA");
            btnEnviar.setText("ACTUALIZA");

            obj = (Editorial) extras.get("var_item");

            txtRaz.setText(obj.getRazonSocial());
            txtDir.setText(obj.getDireccion());
            txtRuc.setText(obj.getRuc());
            txtFec.setText(obj.getFechaCreacion());

        }

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String raz = txtRaz.getText().toString();
                String dir = txtDir.getText().toString();
                String ruc = txtRuc.getText().toString();
                String fec = txtFec.getText().toString();

                if (!raz.matches(ValidacionUtil.TEXTO)){
                    mensajeAlert("La razón Social es de 2 a 20 caracteres");
                }else if (!dir.matches(ValidacionUtil.DIRECCION)){
                    mensajeAlert("La dirección es de 3 a 20 caracteres");
                }else if (!ruc.matches(ValidacionUtil.RUC)){
                    mensajeAlert("El ruc es de 11 dígitos");
                }else if (!fec.matches(ValidacionUtil.FECHA)){
                    mensajeAlert("La fecha es de formato YYYY-MM-dd");
                }else{
                    String pais = spnPais.getSelectedItem().toString();
                    String idPais = pais.split(":")[0];

                    Pais objPais = new Pais();
                    objPais.setIdPais(Integer.parseInt(idPais));

                    Editorial objEditorial = new Editorial();
                    objEditorial.setRazonSocial(raz);
                    objEditorial.setDireccion(dir);
                    objEditorial.setRuc(ruc);
                    objEditorial.setFechaCreacion(fec);
                    objEditorial.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objEditorial.setEstado(1);
                    objEditorial.setPais(objPais);

                    if("REGISTRAR".equals(tipo)){
                        registraEditorial(objEditorial);
                    }else if("ACTUALIZAR".equals(tipo)){
                        Editorial obj = (Editorial)  extras.get("var_item");
                        objEditorial.setIdEditorial(obj.getIdEditorial());
                        actualizaEditorial(objEditorial);
                    }
                }

            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditorialCrudFormularioActivity.this, EditorialCrudListaActivity.class);
                startActivity(intent);
            }
        });
    }

    public void registraEditorial(Editorial obj){
        Call<Editorial> call = serviceEditorial.insertaEditorial(obj);
        call.enqueue(new Callback<Editorial>() {
            @Override
            public void onResponse(Call<Editorial> call, Response<Editorial> response) {
                if (response.isSuccessful()){
                    Editorial objSalida =   response.body();
                    mensajeAlert("Se registro la Editorial " +
                            "\nID >> " + objSalida.getIdEditorial() +
                            "\nRazón Social >> " + objSalida.getRazonSocial() );
                }
            }
            @Override
            public void onFailure(Call<Editorial> call, Throwable t) {
                mensajeAlert("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }

    public void actualizaEditorial(Editorial obj){
        Call<Editorial> call = serviceEditorial.actualizaEditorial(obj);
        call.enqueue(new Callback<Editorial>() {
            @Override
            public void onResponse(Call<Editorial> call, Response<Editorial> response) {
                if (response.isSuccessful()){
                    Editorial objSalida =   response.body();
                    mensajeAlert("Se actualiza la Editorial " +
                            "\nID >> " + objSalida.getIdEditorial() +
                            "\nRazón Social >> " + objSalida.getRazonSocial() );
                }
            }
            @Override
            public void onFailure(Call<Editorial> call, Throwable t) {
                mensajeAlert("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }

    public void cargaPais(){
        Call<List<Pais>> call = servicePais.listaPais();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful()){
                    List<Pais> lstPaises =  response.body();
                    for(Pais obj: lstPaises){
                        paises.add(obj.getIdPais() +":"+ obj.getNombre());
                    }
                    adaptador.notifyDataSetChanged();

                    if (tipo.equals("ACTUALIZAR")){

                        int idPais = obj.getPais().getIdPais();
                        String nombrePais = obj.getPais().getNombre();

                        String itemPais = idPais+":"+nombrePais;
                        int posSeleccionada = -1;
                        for(int i=0; i< paises.size(); i++){
                            if (paises.get(i).equals(itemPais)){
                                posSeleccionada = i;
                                break;
                            }
                        }
                        spnPais.setSelection(posSeleccionada);
                    }

                }else{
                    mensajeToastLong("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                mensajeToastLong("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }


}