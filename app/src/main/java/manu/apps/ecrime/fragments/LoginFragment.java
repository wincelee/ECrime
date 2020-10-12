package manu.apps.ecrime.fragments;

import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import manu.apps.ecrime.R;
import manu.apps.ecrime.classes.Config;

public class LoginFragment extends Fragment implements View.OnClickListener, TextWatcher {

    TextInputLayout tilEmail, tilPassword;

    TextInputEditText etEmail, etPassword;

    TextView tvRegister;

    NavController navController;

    MaterialButton btnLogin;

    ProgressDialog pbLogin;

    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        tilEmail = view.findViewById(R.id.til_email);
        tilPassword = view.findViewById(R.id.til_password);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        tvRegister = view.findViewById(R.id.tv_register);
        btnLogin = view.findViewById(R.id.btn_login);


        firebaseAuth = FirebaseAuth.getInstance();
        pbLogin = new ProgressDialog(getActivity());


        // Setting on click listeners
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                checkCredentials();
                break;
            case R.id.tv_register:
                navController.navigate(R.id.action_login_to_register);
                break;
            default:
                break;
        }
    }

    private void checkCredentials() {

        final String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty()) {
            showError(tilEmail, "Email has not been entered");
        }
        if (password.isEmpty()) {
            showError(tilPassword, "Password has not been entered");
        } else {

            pbLogin.setMessage("Please wait ......");
            pbLogin.setCancelable(false);
            pbLogin.show();

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        navController.navigate(R.id.action_login_to_report_crime);

                        pbLogin.dismiss();

                    }else {

                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();;
                        alertDialog.setMessage("Error has occurred during login, please try again.... ");
                        alertDialog.show();

                        pbLogin.dismiss();
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tilEmail.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}