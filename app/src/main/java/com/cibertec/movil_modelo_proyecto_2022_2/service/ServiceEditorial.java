package com.cibertec.movil_modelo_proyecto_2022_2.service;

import com.cibertec.movil_modelo_proyecto_2022_2.entity.Editorial;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceEditorial  {

    @POST("editorial")
    public Call<Editorial> insertaEditorial(@Body Editorial obj);
    @GET("editorial")
    public Call<List<Editorial>> listaEditorial();
    @GET("editorial/porNombre/{nombre}")
    public Call<List<Editorial>> listaEditorialPorNombre(@Path("nombre")String nombre);
    @PUT("editorial")
    public Call<Editorial> actualizaEditorial(@Body Editorial obj);
}
