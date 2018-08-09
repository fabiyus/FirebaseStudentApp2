package sg.edu.rp.webservices.firebasestudentapp2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    EditText etName, etContactNumber, etHobbies;
    TextView tvEmail;
    Button btnUpdate;

    final String TAG = "EditProfileActivity";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userProfileRef;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = findViewById(R.id.editTextName);
        tvEmail = findViewById(R.id.textViewEmail);
        etContactNumber = findViewById(R.id.editTextContactNo);
        etHobbies = findViewById(R.id.editTextHobbies);
        btnUpdate = findViewById(R.id.buttonUpdate);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        tvEmail.setText(firebaseUser.getEmail());

        firebaseDatabase = FirebaseDatabase.getInstance();
        userProfileRef = firebaseDatabase.getReference("profiles/" + firebaseUser.getUid());

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "userProfileRef.addValueEventListener -- onDataChange()");
                UserProfile profile = dataSnapshot.getValue(UserProfile.class);
                if (profile != null) {
                    Log.i(TAG, "profile: " + profile.toString());
                    etName.setText(profile.getName());
                    etContactNumber.setText(profile.getContactNo());
                    etHobbies.setText(profile.getHobbies());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error occurred", databaseError.toException());
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = firebaseUser.getUid();
                String name = etName.getText().toString();
                String contactNo = etContactNumber.getText().toString();
                String hobbies = etHobbies.getText().toString();
                UserProfile user = new UserProfile(id, name,contactNo,hobbies);
                userProfileRef.setValue(user);

                Toast.makeText(getApplicationContext(), "User profile updated successfully", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}