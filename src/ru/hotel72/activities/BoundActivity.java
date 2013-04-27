package ru.hotel72.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ru.hotel72.R;
import ru.hotel72.accessData.BoundTask;
import ru.hotel72.utils.Validations;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 20.02.13
 * Time: 23:51
 * To change this template use File | Settings | File Templates.
 */
public class BoundActivity extends BaseHeaderActivity {
    private String android_id;
    private View mActivityLevelView;
    private boolean mIsUserBounded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsUserBounded = getIntent().getBooleanExtra(getString(R.string.is_user_bound), true);
        setContent(mIsUserBounded);

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void setContent(boolean mIsUserBounded) {
        LayoutInflater inflate = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        mActivityLevelView = inflate.inflate(R.layout.bound, null);
        contentLayout.addView(mActivityLevelView);

        Button btn = (Button) mActivityLevelView.findViewById(R.id.button);
        btn.setOnClickListener(this);

        if (mIsUserBounded) {
            fillUserData(mActivityLevelView);
            btn.setText(R.string.update_bound);
        } else {
            btn.setText(R.string.bound_number);
        }
    }

    public void dataBounded(HashMap<String, String> mData, String msg){
        Button btn = (Button) mActivityLevelView.findViewById(R.id.button);
        btn.setText(R.string.update_bound);

        SharedPreferences pref = getSharedPreferences(getString(R.string.userDataCache), MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(getString(R.string.user_name), mData.get(getString(R.string.user_name)));
        ed.putString(getString(R.string.phone_number), mData.get(getString(R.string.phone_number)));
        ed.putString(getString(R.string.email), mData.get(getString(R.string.email)));
        ed.putBoolean(getString(R.string.is_user_bound), true);
        ed.commit();

        Intent intent = new Intent();
        intent.putExtra(getString(R.string.showDialog), true);
        intent.putExtra(getString(R.string.dialogMessage), msg);
        setResult(RESULT_OK, intent);

        finish();
    }

    public void fillUserData(View view) {
        SharedPreferences pref = getSharedPreferences(getString(R.string.userDataCache), MODE_PRIVATE);

        String userName = pref.getString(getString(R.string.user_name), "");
        EditText userNameEdit = (EditText) view.findViewById(R.id.user_name);
        userNameEdit.setText(userName);

        String phoneNumber = pref.getString(getString(R.string.phone_number), "");
        EditText phoneNumberEdit = (EditText) view.findViewById(R.id.phone_number);
        phoneNumberEdit.setText(phoneNumber);

        String email = pref.getString(getString(R.string.email), "");
        EditText emailEdit = (EditText) view.findViewById(R.id.email);
        emailEdit.setText(email);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if(view.getId() == R.id.button){

            String phone = ((EditText) mActivityLevelView.findViewById(R.id.phone_number)).getText().toString().trim();
            String name = ((EditText) mActivityLevelView.findViewById(R.id.user_name)).getText().toString().trim();
            String email = ((EditText) mActivityLevelView.findViewById(R.id.email)).getText().toString().trim();

            // номер телефона и имя обязательны для заполнения
            if(phone == null || phone.length() == 0 || name == null || name.length() == 0){
                Toast.makeText(this, getString(R.string.required), 0).show();
                return;
            }

//            if((email != "" || email != null) && !Validations.isValidEmail(email)){
//                Toast.makeText(this, getString(R.string.incorrectEmail), 0).show();
//                return;
//            }

            new BoundTask(phone, android_id, name, email, this, mIsUserBounded).execute();
        }
    }
}
