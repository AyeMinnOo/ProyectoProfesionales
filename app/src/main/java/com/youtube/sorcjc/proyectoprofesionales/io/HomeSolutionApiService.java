package com.youtube.sorcjc.proyectoprofesionales.io;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface HomeSolutionApiService {

    // http://dev.homesolution.com.ar/api/areas
    @GET("areas")
    Call<ZonasResponse> getZonasResponse();

    // http://dev.homesolution.com.ar/api/login?us=info@latrampera.com&ps=lolcats
    // @GET("login?us={email}&ps={password}") I was using @Path, but @Query is better
    @GET("login")
    Call<LoginResponse> getLoginResponse(@Query("us") String email, @Query("ps") String password);

    // http://dev.homesolution.com.ar/api/register?us=Hamilton&email=matias@celani.com.pe&ps=lolcats&accept=1
    @GET("register")
    Call<RegistroResponse> getRegistroResponse(@Query("us") String nombre, @Query("email") String email, @Query("ps") String password, @Query("accept") int accept, @Query("area") String zona);

    // http://dev.homesolution.com.ar/api/categos
    @GET("categos")
    Call<CategoriasResponse> getCategoriasResponse();

    // http://dev.homesolution.com.ar/api/getchats?tk={token}
    @GET("getchats")
    Call<ChatsResponse> getChatsResponse(@Query("tk") String token);

    // http://dev.homesolution.com.ar/api/getagenda?tk={token}
    @GET("getagenda")
    Call<AgendaResponse> getAgendaResponse(@Query("tk") String token);

    // http://dev.homesolution.com.ar/api/getchat/?tk=813abd218962ff966b54d26915388ecf&touid=3
    @GET("getchat")
    Call<ChatResponse> getChatResponse(@Query("tk") String token, @Query("touid") String touid);

    // http://dev.homesolution.com.ar/api/sendmsg?tk={token}&touid=3&replyto=2521&msg=Puede%20ser
    @GET("sendmsg")
    Call<EnviarMsjeResponse> getEnviarMensaje(@Query("tk") String token, @Query("touid") String toUid, @Query("replyto") String replyTo, @Query("msg") String message);

    // http://dev.homesolution.com.ar/api/getprestadores?cid=36&tk={token}
    @GET("getprestadores")
    Call<AgendaResponse> getPrestadores(@Query("cid") String categoryId, @Query("tk") String token);

    // http://dev.homesolution.com.ar/api/getprestador?tk={token}&pid=26
    @GET("getprestador")
    Call<PrestadorResponse> getPrestador(@Query("tk") String token, @Query("pid") String prestadorId);

    // http://dev.homesolution.com.ar/api/agendar?tk={token}&pid=26
    @GET("agendar")
    Call<AgendarResponse> getAgendar(@Query("tk") String token, @Query("pid") String pid);

    // http://dev.homesolution.com.ar/api/forgot?email={email}
    @GET("forgot")
    Call<RecuperarResponse> getRecuperarContra(@Query("email") String email);

    // http://dev.homesolution.com.ar/api/calificar?tk={token}&pid=26&puntualidad=2&profesionalismo=3&cumplimiento=3&recomendaria=3&precio=2&catego=28&comentarios={comentarios}
    @GET("calificar")
    Call<CalificarResponse> getCalificar(@Query("tk") String token, @Query("pid") String pId, @Query("puntualidad") int puntualidad, @Query("profesionalismo") int profesionalismo, @Query("cumplimiento") int cumplimiento, @Query("recomendaria") int recomendaria, @Query("precio") int precio, @Query("catego") String catego, @Query("comentarios") String comentarios);
}
