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

    // http://homesolution.com.ar/api/register?us=Hamilton&email=matias@celani.com.pe&ps=lolcats&accept=1
    @GET("register")
    Call<RegistroResponse> getRegistroResponse(@Query("us") String nombre, @Query("email") String email, @Query("ps") String password, @Query("accept") int accept, @Query("area") String zona);

    // http://homesolution.com.ar/api/categos
    @GET("categos")
    Call<CategoriasResponse> getCategoriasResponse();

    // http://homesolution.com.ar/api/getchats?tk=813abd218962ff966b54d26915388ecf
    @GET("getchats")
    Call<ChatResponse> getChatResponse(@Query("tk") String token);

    // http://homesolution.com.ar/api/getagenda?tk=813abd218962ff966b54d26915388ecf
    @GET("getagenda")
    Call<AgendaResponse> getAgendaResponse(@Query("tk") String token);

}
