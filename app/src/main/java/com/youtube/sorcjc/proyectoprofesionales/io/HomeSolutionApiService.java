package com.youtube.sorcjc.proyectoprofesionales.io;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface HomeSolutionApiService {

    // http://homesolution.com.ar/api/areas
    @GET("areas")
    Call<ZonasResponse> getZonasResponse();

    // http://homesolution.com.ar/api/login?us=info@latrampera.com&ps=lolcats
    // @GET("login?us={email}&ps={password}") I was using @Path, but @Query is better
    @GET("login")
    Call<LoginResponse> getLoginResponse(@Query("us") String email, @Query("ps") String password);

/*
    @FormUrlEncoded
    @POST("/windpower/WebService_Write_APP.php")
    Call<Respuesta> getRespuesta(@Field("energiaObjetivo") Float energiaObjetivo, @Field("horaSimulacion") int horas, @Field("planta1") boolean p1, @Field("planta2") boolean p2, @Field("planta3") boolean p3);
*/
}
