package manu.apps.ecrime.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import manu.apps.ecrime.R;
import manu.apps.ecrime.classes.Config;
import manu.apps.ecrime.viewmodels.RegisterViewModel;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    TextInputLayout tilEmail, tilUsername, tilPassword;
    TextInputEditText etEmail, etUsername, etPassword;
    MaterialButton btnRegister;
    MaterialTextView tvLogin;
    ProgressDialog pbRegister;

    NavController navController;

    private FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RegisterViewModel registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        tilEmail = view.findViewById(R.id.til_email);
        tilUsername = view.findViewById(R.id.til_username);
        tilPassword = view.findViewById(R.id.til_password);
        etEmail = view.findViewById(R.id.et_email);
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        btnRegister = view.findViewById(R.id.btn_register);
        tvLogin = view.findViewById(R.id.tv_login);

        pbRegister = new ProgressDialog(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();

        // Setting on click listeners
        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilEmail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilUsername.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login:

                navController.navigate(R.id.action_register_to_login);

                break;
            case R.id.btn_register:

                checkCredentials();

                break;
            default:
                break;
        }
    }

    private void checkCredentials(){

        final String userName = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (userName.isEmpty()){
            showError(tilUsername, "Username has not been entered");
        }if (email.isEmpty()){
            showError(tilEmail, "Email has not been entered");
        }if (password.isEmpty()){
            showError(tilPassword, "Password has not been entered");
        }else {

            pbRegister.setMessage("Registering please wait .......");
            pbRegister.setCancelable(false);
            pbRegister.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        pbRegister.dismiss();

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.layout_register_dialog);
                        dialog.show();
                        dialog.setCancelable(false);

                        // Setting dialog background to transparent
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        // Setting size of the dialog
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                        TextView tvRegistrationMessage = dialog.findViewById(R.id.tv_registration_message);
                        MaterialButton btnProceed = dialog.findViewById(R.id.btn_proceed);

                        tvRegistrationMessage.setText("Your account was created successfully" + " " + userName);

                        btnProceed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                navController.navigate(R.id.action_register_to_home);
                                dialog.dismiss();
                            }
                        });

                    }else {

                        pbRegister.dismiss();

                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();;
                        alertDialog.setMessage("Error has occurred during registration, please try again.... ");
                        alertDialog.show();

                        Config.showSnackBar(getActivity(), task.getException().toString());

                    }
                }
            });

        }
    }

    private void showError(TextInputLayout til, String error){
        til.setError(error);
        til.requestFocus();

    }

}