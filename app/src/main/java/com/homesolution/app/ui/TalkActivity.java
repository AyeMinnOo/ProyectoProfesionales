package com.homesolution.app.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.homesolution.app.Global;
import com.homesolution.app.domain.Category;
import com.homesolution.app.domain.Message;
import com.homesolution.app.domain.Talk;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.io.responses.ChatResponse;
import com.homesolution.app.io.responses.EnviarMsjeResponse;
import com.homesolution.app.io.responses.SimpleResponse;
import com.homesolution.app.ui.adapter.MessageAdapter;
import com.squareup.picasso.Picasso;
import com.youtube.sorcjc.proyectoprofesionales.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TalkActivity extends AppCompatActivity implements View.OnClickListener, View.OnLayoutChangeListener {

    private Button btnPerfil;
    private Button btnCalificar;
    private Button btnLlamar;

    // Views in fragment_busqueda.xml
    private RecyclerView recyclerView;

    // Used to render the messages
    private MessageAdapter adapter;
    private ProgressDialog progressDialog;

    // To send a message
    private ImageView btnSend;
    private EditText etMessage;

    // To send a picture
    private ImageView btnImage;

    // User authenticated data
    // Using static we just need one load
    private static String token;
    private static String uid;

    // User destination data
    private String toUid;
    private String pid;
    private String name;
    private String catstr;
    private String phoneNumber;

    // Custom action bar
    private ImageView ivPhoto;
    private TextView tvName;
    private TextView tvDescription;

    // Request codes
    private static final int REQUEST_CAMERA = 20;
    private static final int SELECT_FILE = 21;

    // Default extension for images (using camera)
    private final String defaultExtension = "jpg";

    // Location of the last photo taken
    private String currentPhotoPath;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        adapter = new MessageAdapter(this);

        // Setup the action bar
        setUpActionBar();

        // Get references to the views and controls
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnLayoutChangeListener(this);

        etMessage = (EditText) findViewById(R.id.etMessage);
        btnSend = (ImageView) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        btnImage = (ImageView) findViewById(R.id.btnImage);
        btnImage.setOnClickListener(this);

        // Bundle parameters from previous activity
        if (toUid == null) {
            Bundle b = getIntent().getExtras();
            toUid = b.getString("uid");
            pid = b.getString("pid");
            name = b.getString("name");
            catstr = b.getString("catstr");
            phoneNumber = b.getString("tel");
        }

        loadAuthenticatedUser();
        loadMessages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                // Get the thumbnail from extras
                /*
                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                */
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                postPicture(bitmap, defaultExtension);
                new File(currentPhotoPath).delete();

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection,
                        null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);

                // Get the extension from the full path
                final int lastDot = selectedImagePath.lastIndexOf(".");
                final String extension = selectedImagePath.substring(lastDot+1);

                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                postPicture(bitmap, extension);
            }
        }
    }

    private void loadAuthenticatedUser() {
        if (token == null) {
            final Global global = (Global) getApplicationContext();
            token = global.getToken();
            uid = global.getUid();
        }
    }

    private void setUpActionBar() {
        // Custom action bar
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_talk, null);
        tvName = (TextView) mCustomView.findViewById(R.id.tvName);
        ivPhoto = (ImageView) mCustomView.findViewById(R.id.ivPhoto);
        tvDescription = (TextView) mCustomView.findViewById(R.id.tvDescription);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setUpTabs(boolean esPrestador) {
        if (esPrestador) {
            btnPerfil = (Button) findViewById(R.id.btnPerfil);
            btnCalificar = (Button) findViewById(R.id.btnCalificar);
            btnLlamar = (Button) findViewById(R.id.btnLlamar);

            btnPerfil.setOnClickListener(this);
            btnCalificar.setOnClickListener(this);
            btnLlamar.setOnClickListener(this);
        } else {
            AppBarLayout topOptions = (AppBarLayout) findViewById(R.id.appbar);
            topOptions.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPerfil:
                Intent iProfile = new Intent(view.getContext(), ProfileActivity.class);
                Bundle bProfile = new Bundle();
                bProfile.putString("pid", pid);
                iProfile.putExtras(bProfile);
                startActivity(iProfile);
                break;

            case R.id.btnCalificar:
                Intent iScore = new Intent(view.getContext(), ScoreActivity.class);
                Bundle bScore = new Bundle();
                bScore.putString("pid", pid);
                iScore.putExtras(bScore);
                startActivity(iScore);
                break;

            case R.id.btnLlamar:
                makeCall();
                break;

            case R.id.btnSend:
                postMessage();
                break;

            case R.id.btnImage:
                selectPicture();
                break;
        }
    }

    private void makeCall() {
        if (phoneNumber.isEmpty()) {
            Toast.makeText(TalkActivity.this, "No es posible realizar esta acción", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(TalkActivity.this, "Usted no ha asignado los permisos para llamar", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);

        Call<SimpleResponse> call = HomeSolutionApiAdapter.getApiService().getRegistrarLlamada(toUid, pid, token);

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Response<SimpleResponse> response, Retrofit retrofit) {
                if (response != null && response.body().getStatus() == 1) {
                    Log.d("Test/Call", "phoneNumber => " + phoneNumber);
                    Log.d("Test/Call", "Llamada registrada al usuario => uid " + toUid + " | pid " + pid);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TalkActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postMessage() {
        // Hide the keyboard
        hideKeyboard();

        final String content = etMessage.getText().toString().trim();

        if (content.isEmpty())
            return;

        final String replyTo = adapter.getParentMid();

        Call<EnviarMsjeResponse> call = HomeSolutionApiAdapter.getApiService().getEnviarMensaje(token, toUid, replyTo, content);

        call.enqueue(new Callback<EnviarMsjeResponse>() {
            @Override
            public void onResponse(Response<EnviarMsjeResponse> response, Retrofit retrofit) {
                if (response.body() == null)
                    return;

                if (response.body().getStatus() == 0) {
                    Toast.makeText(TalkActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Message message = response.body().getResponse();
                    if (message != null) {
                        adapter.addItem(message);
                        etMessage.setText("");
                        scrollLastMessage();
                    } else {
                        Toast.makeText(TalkActivity.this, "No se ha enviado el mensaje", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TalkActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private File createDestinationFile() throws IOException {
        // Path for the temporary image and its name
        final File storageDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        final String imageFileName = "" + System.currentTimeMillis();

        File image = File.createTempFile(
                imageFileName,          // prefix
                "." + defaultExtension, // suffix
                storageDirectory              // directory
        );

        // Save a the file path
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void selectPicture() {
        // Options for the alert dialog
        final CharSequence[] items = {
                getResources().getString(R.string.picture_from_camera),
                getResources().getString(R.string.picture_from_gallery),
                getResources().getString(R.string.picture_cancel)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(TalkActivity.this);

        // Title
        builder.setTitle(getResources().getString(R.string.picture_title));

        // Actions
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {

                if (option == 0) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createDestinationFile();
                    } catch (IOException ex) {
                        return;
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                    }

                } else if (option == 1) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    final String chooserTitle = getResources().getString(R.string.picture_chooser_title);
                    startActivityForResult(Intent.createChooser(intent, chooserTitle), SELECT_FILE);
                } else if (option == 2) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void postPicture(Bitmap bitmap, final String extension) {
        final String base64 = getBase64FromBitmap(bitmap);
        final String replyTo = adapter.getParentMid();

        Call<EnviarMsjeResponse> call = HomeSolutionApiAdapter.getApiService().postPic(token, toUid, base64, extension, replyTo);

        call.enqueue(new Callback<EnviarMsjeResponse>() {
            @Override
            public void onResponse(Response<EnviarMsjeResponse> response, Retrofit retrofit) {
                if (response.body() == null)
                    return;

                if (response.body().getStatus() == 0) {
                    Toast.makeText(TalkActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Message message = response.body().getResponse();
                    if (message != null) {
                        adapter.addItem(message);
                        etMessage.setText("");
                        scrollLastMessage();
                    } else {
                        Toast.makeText(TalkActivity.this, "Ocurrió un problema al enviar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TalkActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void loadDummyMessages() {
        // Used for testing
        ArrayList<Message> examples = new ArrayList<>();
        examples.add(new Message("Hola, cómo estás?", "4.30 PM", false));
        examples.add(new Message("Estás ahí?", "4.45 PM", false));
        examples.add(new Message("Sí, dime ...", "4.50 PM", true));
        examples.add(new Message("Quería hacerte una consulta", "4.55 PM", false));
        adapter.addAll(examples);
    }

    private void loadMessages() {
        Log.d("Test/Talk", "Loading chat with the uid => " + toUid);
        Call<ChatResponse> call = HomeSolutionApiAdapter.getApiService().getChatResponse(token, toUid);

        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Response<ChatResponse> response, Retrofit retrofit) {
                if (response.body() == null)
                    return;

                if (response.body().getStatus() == 0) {
                    Toast.makeText(TalkActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Talk talk = response.body().getResponse();
                    setUpTabs(talk.isPrestador());
                    if (talk.isPrestador()) {
                        saveCategoriesGlobal(talk.getPrestador().getCategories());
                    }

                    // Set contact data
                    tvName.setText(name);
                    Picasso.with(getBaseContext())
                            .load(talk.getPicture())
                            .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                            .into(ivPhoto);
                    if (catstr.isEmpty())
                        tvDescription.setVisibility(View.GONE);
                    else
                        tvDescription.setText(catstr);

                    // Set messages
                    adapter.addAll(talk.getChat());
                    scrollLastMessage();
                    Log.d("Test/Talk", "Number of messages in chat => " + talk.getChat().size());
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TalkActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Test/Talk", "onFailure => " + t.getLocalizedMessage());
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando mensajes ...");
        progressDialog.show();
    }

    private void saveCategoriesGlobal(ArrayList<Category> categories) {
        // Save the categories for the last selected worker
        final Global global = (Global) getApplicationContext();
        global.setCategories(categories);
    }

    private void scrollLastMessage() {
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
    }

    @Override
    public void onLayoutChange(View view, int i, int i1, int i2, int bottom, int i4, int i5, int i6, int oldBottom) {
        if (bottom < oldBottom) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollLastMessage();
                }
            }, 100);
        }
    }

    public String getBase64FromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // Get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

}
