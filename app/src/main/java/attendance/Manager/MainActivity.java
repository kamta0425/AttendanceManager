package attendance.Manager;

import java.util.ArrayList;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

public class MainActivity extends Activity implements AdapterView.OnItemLongClickListener {

	ListView listView;
	SubjectAdapter adapter;
	ArrayList<Data> list;
	MyhelperAdapter helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		helper = new MyhelperAdapter(this);
		list = helper.getAllData();

		listView = (ListView) findViewById(R.id.listView);
		adapter = new SubjectAdapter(this, list);
		listView.setAdapter(adapter);

		listView.setOnItemLongClickListener(this);
	}

	public void close(View v) {
		finish();
		System.exit(0);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

		final String[] array = { "Edit Subject name", "Edit Attendance", "Delete" };
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, array);
		final int id1 = position;
		final Data data = helper.getData(position);
		builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int index) {
				if (index == 0) {
					AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
					builder1.setTitle("Change Subject name");
					LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view_Et = inflater.inflate(R.drawable.custome_edittext, null);
					final EditText editText = (EditText) view_Et.findViewById(R.id.custome_edittext);
					editText.setText(data.name);
					editText.setHeight(40);
					editText.setSelection(data.name.length());
					builder1.setView(view_Et);
					builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							String str = editText.getEditableText().toString();
							adapter.update(id1,-1,-1,str);
							listView.setAdapter(adapter);
							dialog.dismiss();
						}
					});
					builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override public void onClick(DialogInterface dialog, int id) {dialog.dismiss();}});
					final AlertDialog dialog1 =builder1.create();
					dialog1.show();
					editText.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
						@Override
						public void afterTextChanged(Editable editable) {
							if (editable.toString().length() == 0) {
								dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
							} else {
								dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
							}
						}
					});
					
				} else if (index == 1) {
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					try{
					builder.setTitle("Change Attendance");
					LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view_et = inflater.inflate(R.layout.modify_attendance, null);
					final EditText et_a = (EditText) view_et.findViewById(R.id.et_a);
					final EditText et_t = (EditText) view_et .findViewById(R.id.et_t);
					et_a.setText(String.valueOf(data.a));
					et_t.setText(String.valueOf(data.t));
					builder.setView(view_et);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							int a = Integer.parseInt(et_a.getText().toString());
							int t = Integer.parseInt(et_t.getText().toString());
							if(a>t){
								Toast toast=Toast.makeText(getApplicationContext(), "Attendance cannot be greater then Total Attendance", Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
							}else{
								adapter.update(id1,a,t,null);
								listView.setAdapter(adapter);
								dialog.dismiss();
							}
						}
					});
					builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override public void onClick(DialogInterface dialog, int id) {dialog.dismiss();}});
					final AlertDialog dialog1=builder.create();
					dialog1.show();
					et_a.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
						@Override
						public void afterTextChanged(Editable editable) {
							if (editable.toString().length() == 0) {
								dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
							} else {
								dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
							}
						}
					});
					et_t.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
						@Override
						public void afterTextChanged(Editable editable) {
							if (editable.toString().length() == 0) {
								dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
							} else {
								dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
							}
						}
					});
					}catch(Exception e){Toast.makeText(getApplicationContext(), e+"", Toast.LENGTH_SHORT).show();}
				} else {
					adapter.removeOne(id1);
					listView.setAdapter(adapter);
				}
			}
		});
		builder.show();
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.report:
			Intent i = new Intent(MainActivity.this, Record.class);
			ArrayList<Data> list=adapter.list;
			int a=0,t=0;
			for(int j=0;j<list.size();j++){
				a+=list.get(j).a;
				t+=list.get(j).t;
			}
			i.putExtra("MyData", new int[]{a,t});
			startActivity(i);
			return true;

		case R.id.delete:
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("Are You Sure Delete All");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					adapter.removeAll();
					listView.setAdapter(adapter);
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int id) {dialog.dismiss();}});
			builder.create().show();
			return true;

		case R.id.bunkAll:
			adapter.bunkAll();
			listView.setAdapter(adapter);
			return true;
		
		case R.id.attendAll:
			adapter.attendAll();
			listView.setAdapter(adapter);
			return true;
			
		}
		return super.onOptionsItemSelected(item);
	}

	public void showAlertBox(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Add Subject");
		View view_Et = getLayoutInflater().inflate(R.drawable.custome_edittext, null);
		final EditText editText = (EditText) view_Et.findViewById(R.id.custome_edittext);
		builder.setView(view_Et);
		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				String str = editText.getEditableText().toString();
				Data data = new Data(str, 0, 0);
				adapter.add(data);
				listView.setAdapter(adapter);
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		final AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.toString().length() == 0) {
					dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
				} else {
					dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
				}
			}
		});
	}

	class SubjectAdapter extends BaseAdapter {

		Context context;
		ArrayList<Data> list;

		public SubjectAdapter(Context context, ArrayList<Data> list) {
			this.context = context;
			this.list = list;
		}

		public void update(int id ,int a,int t,String name){
			if(name!=null){
				list.get(id).name=name;
			}
			if(a>-1)list.get(id).a=a;
			if(t>-1)list.get(id).t=t;
			helper.update(id, a,t,name);
		}
		public void add(Data data) {
			list.add(data);
			helper.storeData(data);
		}

		public void removeOne(int id) {
			list.remove(id);
			helper.remove(id); // problem here delete single row
		}

		public void removeAll() {
			list.clear();
			helper.removeAll();
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void bunkAll(){
			for(int i=0;i<list.size();i++)
						list.get(i).t+=1;
			helper.bunkAll();
		}
		public void attendAll(){
			for(int i=0;i<list.size();i++){
				list.get(i).a+=1;
				list.get(i).t+=1;
			}
			helper.attendAll();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.subject, listView, false);
			}
			final Data data = list.get(position);

			TextView tv_sub = (TextView) row.findViewById(R.id.tvsubject);
			TextView tv_attend = (TextView) row.findViewById(R.id.tvattendance);
			TextView tv_percent = (TextView) row.findViewById(R.id.tvpercent);
			TextView tv_attendance = (TextView) row.findViewById(R.id.Total);
			
			tv_sub.setText(data.name + " :");
			tv_attend.setText(data.a + "/" + data.t);
			if (data.t == 0)
				tv_percent.setText("0%");
			else {
				double d = (data.a * 100.0) / (data.t * 1.0);
				String f = String.format(Locale.US, "%.2f", d);
				tv_percent.setText(f + "%");
				if(d<75.0){
					tv_sub.setTextColor(Color.RED);
					tv_attend.setTextColor(Color.RED);
					tv_percent.setTextColor(Color.RED);
					tv_attendance.setTextColor(Color.RED);
				}else{
					tv_sub.setTextColor(Color.BLACK);
					tv_attend.setTextColor(Color.BLACK);
					tv_percent.setTextColor(Color.BLACK);
					tv_attendance.setTextColor(Color.BLACK);
				}
			}
			
			final int id = position;
			
			final Button btright = (Button) row.findViewById(R.id.btright);
			btright.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					update(id,data.a+1,data.t+1,null);
					listView.setAdapter(adapter);
				}
			});
			
			final Button btwrong = (Button) row.findViewById(R.id.btwrong);
			btwrong.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					update(id,data.a,data.t+1,null);
					listView.setAdapter(adapter);
				}
			});
			return row;
		}
	}
}

class Data {
	int a = 0, t = 0;
	String name;

	public Data(String name, int a, int t) {
		this.a = a;
		this.t = t;
		this.name = name;
	}

}
