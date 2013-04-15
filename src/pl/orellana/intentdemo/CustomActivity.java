package pl.orellana.intentdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class CustomActivity extends Activity {
	private SeekBar bar;
	private TextView rate;
	private Button done;

	private float ratenumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom);

		bar = (SeekBar) findViewById(R.id.seekbar);
		rate = (TextView) findViewById(R.id.text_grade);
		done = (Button) findViewById(R.id.button_done);

		bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				ratenumber = (progress / 2f) + 2f;
				rate.setText("" + ratenumber);

			}
		});

		done.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				Bundle counters = new Bundle();
				counters.putFloat("rate", ratenumber);
				i.putExtras(counters);
				setResult(Activity.RESULT_FIRST_USER, i);
				finish();
			}
		});
	}
}
