package ru.hotel72.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import ru.hotel72.R;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 20.02.13
 * Time: 23:51
 * To change this template use File | Settings | File Templates.
 */
public class BaseHeaderActivity extends Activity implements View.OnClickListener {

    protected RelativeLayout contentLayout;
    protected View mActivityLevelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_layout);

        final View mainLayout = findViewById(R.id.scrollView).findViewById(R.id.mainLayout);

        contentLayout = (RelativeLayout) mainLayout.findViewById(R.id.content);

        View returnBtn = mainLayout.findViewById(R.id.headerLayout).findViewById(R.id.returnBtn);

        returnBtn.setOnClickListener(this);
    }

    public void setActivityView(int id){
        View view = getView(id);
        contentLayout.addView(view);
        mActivityLevelView = view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.returnBtn){
            Intent intent = new Intent(this, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public View getView(int id){
        LayoutInflater inflate = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        return inflate.inflate(id, null);
    }
}
