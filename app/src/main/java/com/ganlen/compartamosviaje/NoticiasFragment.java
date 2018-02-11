package com.ganlen.compartamosviaje;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ganlen.compartamosviaje.Objects.AdapterPosts;
import com.ganlen.compartamosviaje.Objects.Posts;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoticiasFragment extends Fragment {

    List<Posts> ListPosts;
    RecyclerView recyclerView;
    private AdapterPosts adapter;
    private LinearLayoutManager layoutManager;

    // variable for login fb
    private CallbackManager callbackManager;
    private List<String> permissions;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        if(AccessToken.getCurrentAccessToken() == null) {
            alertDialogForLoginFb();
        }
        if(AccessToken.getCurrentAccessToken() != null){
            getNews();
        }
        return view;
    }

    public void getNews(){
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_posts);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ListPosts = new ArrayList<Posts>();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "UNAM.MX.Oficial/posts",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        String enumPost = null;
                        TextView postDescription = getActivity().findViewById(R.id.post_description);

                        try {
                            JSONObject json = graphResponse.getJSONObject();
                            JSONArray jarray = json.getJSONArray("data");
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject onePost= jarray.getJSONObject(i);
                                enumPost += onePost.getString("message")+"\n";
                                if(onePost.has("full_picture")) {
                                    enumPost += onePost.getString("full_picture") + "\n";
                                }
                                if(onePost.has("full_picture")) {
                                    ListPosts.add(new Posts(onePost.getString("message"),
                                            onePost.getString("full_picture")));
                                }else{
                                    ListPosts.add(new Posts(onePost.getString("message"), ""));
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        TextView post = getActivity().findViewById(R.id.post);
                        post.setText("\n\n\n");
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "full_picture,id,created_time,message,permalink_url,object_id");
        parameters.putString("limit", "50");
        request.setParameters(parameters);
        request.executeAsync();
        adapter = new AdapterPosts(ListPosts);
        recyclerView.setAdapter(adapter);
    }

    public void alertDialogForLoginFb() {
        callbackManager = CallbackManager.Factory.create();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alerta");
        builder.setMessage("Necesitas iniciar sesión en Facebook para poder usar la sección de noticias.");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                permissions = Arrays.asList("public_profile");
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), permissions);
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getNews();
                    }
                    @Override
                    public void onCancel() {
                        Toast.makeText(getActivity(), "Inicio de Facebook cancelado", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getActivity(), "Error al iniciar Facebook", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }
}