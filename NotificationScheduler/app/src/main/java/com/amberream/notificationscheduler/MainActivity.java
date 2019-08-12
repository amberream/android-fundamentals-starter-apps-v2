package com.amberream.notificationscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int JOB_ID = 0;

    // initialized in scheduleJob
    private JobScheduler mJobScheduler;

    private Switch mSwitchDeviceIdle;
    private Switch mSwitchDeviceCharging;
    private SeekBar mSeekBar;
    private TextView mTextViewProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwitchDeviceCharging = findViewById(R.id.chargingSwitch);
        mSwitchDeviceIdle = findViewById(R.id.idleSwitch);
        mSeekBar = findViewById(R.id.seekBar);
        mTextViewProgress = findViewById(R.id.seekBarProgress);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 0)
                {
                    mTextViewProgress.setText(progress + " s");
                }
                else
                {
                    mTextViewProgress.setText("Not Set");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void scheduleJob(View view) {

        RadioGroup networkOptions = findViewById(R.id.networkOptions);
        int checkedButton = networkOptions.getCheckedRadioButtonId();

        // used to override the deadline
        int seekBarProgress = mSeekBar.getProgress();
        boolean seekBarSet = seekBarProgress > 0;

        // default
        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;

        switch (checkedButton) {
            case R.id.noNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                break;
            case R.id.wifiNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                break;
            case R.id.anyNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                break;
        }

        boolean constraintSet = selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE ||
                mSwitchDeviceIdle.isChecked() || mSwitchDeviceCharging.isChecked() || seekBarSet;

        // to run a job at least one constraint must be set (network type none doesn't count!)
        if (constraintSet) {
            mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

            ComponentName componentName = new ComponentName(getPackageName(), NotificationJobService.class.getName());
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
            builder.setRequiredNetworkType(selectedNetworkOption);
            builder.setRequiresCharging(mSwitchDeviceCharging.isChecked());
            builder.setRequiresDeviceIdle(mSwitchDeviceIdle.isChecked());

            // only set a deadline if it's > 0 sec
            if (seekBarSet)
            {
                builder.setOverrideDeadline(seekBarProgress * 1000);
            }

            mJobScheduler.schedule(builder.build());
            Toast.makeText(this, "Job Scheduled, job will run when the constraints are met", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please set at least one constraint", Toast.LENGTH_SHORT);
        }
    }

    public void cancelJob (View view)
    {
        // mJobScheduler is initialized in scheduleJob
        // so if it's null, no jobs have been scheduled
        if (mJobScheduler != null)
        {
            mJobScheduler.cancelAll();
            mJobScheduler = null;
            Toast.makeText(this, "Job cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
