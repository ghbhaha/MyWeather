package suda.sudamodweather.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import suda.sudamodweather.R;
import suda.sudamodweather.dao.greendao.Alarms;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_info);

        Alarms alarms = (Alarms) getIntent().getSerializableExtra("alarminfo");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(alarms.getAreaName());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((ImageView) findViewById(R.id.iv_gantanhao)).setColorFilter(getResources().getColor(R.color.colorPrimary));
        ((TextView) findViewById(R.id.tv_alarmInfo)).setText(alarms.getAlarmLevelNoDesc() + alarms.getAlarmTypeDesc());
        ((TextView) findViewById(R.id.tv_alarmDetails)).setText(alarms.getAlarmContent().trim());
        ((TextView) findViewById(R.id.tv_time)).setText(alarms.getPublishTime());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
