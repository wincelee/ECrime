package manu.apps.ecrime.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import manu.apps.ecrime.R;
import manu.apps.ecrime.classes.Config;

public class ReportCrimeFragment extends Fragment implements View.OnClickListener {


    TextInputLayout tilCrimeCounty, tilSubCounty, tilCrimeDate, tilCrimeDescription, tilPhoneNumber;
    TextInputEditText etCrimeCounty, etSubCounty, etCrimeDate, etCrimeDescription, etPhoneNumber;
    MaterialButton btnReportCrime;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.report_crime_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();

        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilCrimeCounty = view.findViewById(R.id.til_crime_county);
        tilSubCounty = view.findViewById(R.id.til_sub_county);
        tilCrimeDate = view.findViewById(R.id.til_crime_date);
        tilCrimeDescription = view.findViewById(R.id.til_crime_description);
        tilPhoneNumber = view.findViewById(R.id.til_phone_number);

        etCrimeCounty = view.findViewById(R.id.et_crime_county);
        etSubCounty = view.findViewById(R.id.et_sub_county);
        etCrimeDate = view.findViewById(R.id.et_crime_date);
        etCrimeDescription = view.findViewById(R.id.et_crime_description);
        etPhoneNumber = view.findViewById(R.id.et_phone_number);
        btnReportCrime = view.findViewById(R.id.btn_report_crime);

        btnReportCrime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_report_crime) {

            final String county = etCrimeCounty.getText().toString().trim();
            final String subCounty = etSubCounty.getText().toString().trim();
            final String crimeDate = etCrimeDate.getText().toString().trim();
            final String crimeDescription = etCrimeDescription.getText().toString().trim();
            final String phoneNumber = etPhoneNumber.getText().toString().trim();

            if (TextUtils.isEmpty(county)) {
                tilCrimeCounty.setError("County of crime is required");
                return;
            }

            if (TextUtils.isEmpty(subCounty)) {
                tilSubCounty.setError("Sub county of crime is required");
                return;
            }

            if (TextUtils.isEmpty(crimeDate)) {
                tilCrimeDate.setError("Date of crime is required");
                return;
            }

            if (TextUtils.isEmpty(crimeDescription)) {
                tilCrimeDescription.setError("Description of crime is required");
                return;

            }
            if (TextUtils.isEmpty(phoneNumber)) {
                tilCrimeDescription.setError("Phone Number is required");
                return;

            }else {

                final ProgressDialog pdReportCrime = new ProgressDialog(getActivity());
                pdReportCrime.setMessage("Loading.....");
                pdReportCrime.setCancelable(false);
                pdReportCrime.show();


                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                assert firebaseUser != null;
                String userId = firebaseUser.getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference("Crimes").child(userId);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userId", userId);
                hashMap.put("county", county);
                hashMap.put("subCounty", subCounty);
                hashMap.put("crimeDate", crimeDate);
                hashMap.put("crimeDescription", crimeDescription);
                hashMap.put("phoneNumber", phoneNumber);


                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            pdReportCrime.dismiss();

                            final Dialog registerDialog = new Dialog(getActivity());
                            registerDialog.setContentView(R.layout.layout_register_dialog);
                            registerDialog.show();
                            registerDialog.setCancelable(false);

                            // Setting dialog background to transparent
                            registerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            // Setting size of the dialog
                            registerDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);

                            TextView tvRegistrationMessage = registerDialog.findViewById(R.id.tv_registration_message);
                            TextView tvTitle = registerDialog.findViewById(R.id.tv_title);
                            MaterialButton btnProceed = registerDialog.findViewById(R.id.btn_proceed);

                            tvTitle.setText("Thank You");
                            tvRegistrationMessage.setText("Your crime was successfully reported");

                            btnProceed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    registerDialog.dismiss();
                                }
                            });

                        }else {
                            Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                            Config.showSnackBar(getActivity(), "We encountered an error while reporting your crime");
                        }
                    }
                });

            }
        }
    }
}