package com.homesolution.app.io;

import com.homesolution.app.io.response.AgendaResponse;
import com.homesolution.app.io.response.CategoriasResponse;
import com.homesolution.app.io.response.ChatResponse;
import com.homesolution.app.io.response.ChatsResponse;
import com.homesolution.app.io.response.EnviarMsjeResponse;
import com.homesolution.app.io.response.LoginResponse;
import com.homesolution.app.io.response.PrestadorResponse;
import com.homesolution.app.io.response.RecuperarResponse;
import com.homesolution.app.io.response.ZonasResponse;
import com.homesolution.app.io.response.SimpleResponse;
import com.homesolution.app.io.response.CalificarResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;

public interface HomeSolutionApiService {

    // /api/areas
    @GET("areas")
    Call<ZonasResponse> getZonasResponse();

    // /api/login?us=info@latrampera.com&ps=lolcats
    @GET("login")
    Call<LoginResponse> getLoginResponse(@Query("us") String email, @Query("ps") String password, @Query("gcm_id") String gcmId);

    // /api/fbconnect
    @GET("fbconnect")
    Call<LoginResponse> getFbConnect(@Query("fbtk") String facebookToken, @Query("gcm_id") String gcmId, @Query("area") String area);

    // /api/logout?tk=813abd218962ff966b54d26915388ecf
    @GET("logout")
    Call<SimpleResponse> getLogoutResponse(@Query("tk") String token);

    // /api/register?us=Hamilton&email=matias@celani.com.pe&ps=lolcats&accept=1
    @GET("register")
    Call<LoginResponse> getRegistroResponse(@Query("us") String nombre, @Query("email") String email, @Query("ps") String password, @Query("accept") int accept, @Query("area") String zona, @Query("gcm_id") String gcm_id);

    // /api/categos
    @GET("categos")
    Call<CategoriasResponse> getCategoriasResponse();

    // /api/getchats?tk={token}
    @GET("getchats")
    Call<ChatsResponse> getChatsResponse(@Query("tk") String token);

    // /api/getagenda?tk={token}
    @GET("getagenda")
    Call<AgendaResponse> getAgendaResponse(@Query("tk") String token);

    // /api/getchat?tk=813abd218962ff966b54d26915388ecf&touid=3
    @GET("getchat")
    Call<ChatResponse> getChatResponse(@Query("tk") String token, @Query("touid") String touid);

    // /api/sendmsg?tk={token}&touid=3&replyto=2521&msg=Puede%20ser
    @GET("sendmsg")
    Call<EnviarMsjeResponse> getEnviarMensaje(@Query("tk") String token, @Query("touid") String toUid, @Query("replyto") String replyTo, @Query("msg") String message);

    // /api/sendpic/
    @Multipart
    @POST("sendpic")
    Call<EnviarMsjeResponse> postPic(@Query("tk") String token, @Query("touid") String toUid, @Part("image") String base64, @Query("extension") String extension, @Query("replyto") String replyTo);

    // /api/closechat/
    @GET("closechat")
    Call<SimpleResponse> getCloseChatResponse(@Query("tk") String token, @Query("touid") String toUid);

    // /api/getprestadores?cid=36&tk={token}
    @GET("getprestadores")
    Call<AgendaResponse> getPrestadores(@Query("cid") String categoryId, @Query("tk") String token);

    // /api/getprestador?tk={token}&pid=26
    @GET("getprestador")
    Call<PrestadorResponse> getPrestador(@Query("tk") String token, @Query("pid") String prestadorId);

    // /api/agendar?tk={token}&pid={pid}
    @GET("agendar")
    Call<SimpleResponse> getAgendar(@Query("tk") String token, @Query("pid") String pid);

    // /api/desagendar?tk={token}&pid={pid}
    @GET("desagendar")
    Call<SimpleResponse> getDesagendar(@Query("tk") String token, @Query("pid") String pid);

    // /api/forgot?email={email}
    @GET("forgot")
    Call<RecuperarResponse> getRecuperarContra(@Query("email") String email);

    // /api/calificar?tk={token}&pid=26&puntualidad=2&profesionalismo=3&cumplimiento=3&recomendaria=3&precio=2&catego=28&comentarios={comentarios}
    @GET("calificar")
    Call<CalificarResponse> getCalificar(@Query("tk") String token, @Query("pid") String pId, @Query("puntualidad") int puntualidad, @Query("profesionalismo") int profesionalismo, @Query("cumplimiento") int cumplimiento, @Query("recomendaria") int recomendaria, @Query("precio") int precio, @Query("catego") String catego, @Query("comentarios") String comentarios);

    // /api/updateuser
    @GET("updateuser")
    Call<SimpleResponse> getModificarDatos(@Query("tk") String token, @Query("area") String area, @Query("gcm_id") String gcm_id, @Query("pass") String password, @Query("username") String username, @Query("email") String email);

    // /api/trackcall?uid={uid}&pid={pid}&tk={token}
    @GET("trackcall")
    Call<SimpleResponse> getRegistrarLlamada(@Query("uid") String uid, @Query("pid") String pid, @Query("tk") String token);

    // /api/buscar?term=gonzalez&tk={token}
    @GET("buscar")
    Call<AgendaResponse> getBuscar(@Query("term") String term, @Query("tk") String token);

    // http://dev.homesolution.net/api/setlatlng
    @GET("setlatlng")
    Call<SimpleResponse> getLatLng(@Query("tk") String token, @Query("lat") String lag, @Query("lng") String lng);

    // http://dev.homesolution.net/api/setgeooff
    @GET("setgeooff")
    Call<SimpleResponse> getGeoOff(@Query("tk") String token);

    // Accept message
    @GET("acceptmsg")
    Call<ChatResponse> getAcceptMessage(@Query("tk") String token, @Query("touid") String toUid, @Query("mid") String mId);
    // Reject message
    @GET("rejectmsg")
    Call<ChatsResponse> getRejectMessage(@Query("tk") String token, @Query("mid") String mId);
}
