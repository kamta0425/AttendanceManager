package attendance.Manager;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class Record extends Activity {

	TextView total, percent, attended;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);

		Intent i = getIntent();
		int[] x = (int[]) i.getIntArrayExtra("MyData");

		total = (TextView) findViewById(R.id.totalclass);
		percent = (TextView) findViewById(R.id.percent);
		attended = (TextView) findViewById(R.id.classattend);

		total.setText(x[1] + "");
		attended.setText(x[0] + "");

		total.setTextColor(Color.RED);
		attended.setTextColor(Color.RED);

		double d=0.0;
		if (x[1] == 0) {
			percent.setText("0%");
			percent.setTextColor(Color.BLACK);
		} else {
			d = (x[0] * 100.0) / (x[1] * 1.0);
			String f = String.format(Locale.US, "%.2f", d);
			percent.setText(f + "%");
			if (d < 75.0)
				percent.setTextColor(Color.RED);
			else
				percent.setTextColor(Color.GREEN);
		}
	}

	@Override
	protected void onDestroy() {
		finish();
		super.onDestroy();
	}
}
