package pl.orellana.intentdemo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int PICK_CONTACT = 0;
	private TextView phoneText;
	private boolean hasPhone = false;
	private TextView nameText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		phoneText = (TextView) findViewById(R.id.text_phone);
		nameText = (TextView) findViewById(R.id.text_name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_contacts:
			Intent ic = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(ic, PICK_CONTACT);
			return true;
		case R.id.menu_sms:
			Intent smsIntent = new Intent(Intent.ACTION_SENDTO,
					Uri.parse("sms:" + phoneText.getText()));
			smsIntent.putExtra("sms_body", "Hello, SMS-world!");
			startActivity(smsIntent);
			return true;
		case R.id.menu_maps:
			Intent im = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("geo:0,0?q=" + "Wroclaw Rynek"));
			startActivity(im);
			return true;
		case R.id.menu_custom:
			Intent icus = new Intent(MainActivity.this, CustomActivity.class);
			startActivityForResult(icus, Activity.RESULT_FIRST_USER);
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_CONTACT:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Uri contactData = data.getData();
				ContentResolver cr = getContentResolver();
				Cursor c = managedQuery(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String name = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					String id = c.getString(c
							.getColumnIndex(ContactsContract.Contacts._ID));
					String phone;
					
					nameText.setText(name);

					if (Integer
							.parseInt(c.getString(c
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						Cursor pCur = cr.query(Phone.CONTENT_URI, null,
								Phone.CONTACT_ID + " = ?", new String[] { id },
								null);

						pCur.moveToFirst();
						phone = pCur.getString(pCur
								.getColumnIndex(Phone.NUMBER));
						phoneText.setText(phone);

						hasPhone = true;

					} else {
						Toast.makeText(this, "Contact has no phone number",
								Toast.LENGTH_LONG).show();
					}
				}
				break;
			case Activity.RESULT_CANCELED:
				Toast.makeText(this, "User cancelled the selection",
						Toast.LENGTH_LONG).show();
				break;
			default:
				Toast.makeText(this, "Unknown/Unmanaged Error",
						Toast.LENGTH_LONG).show();
			}
			break;

		case Activity.RESULT_FIRST_USER:
			((TextView) findViewById(R.id.text_grade)).setText(""
					+ data.getExtras().getFloat("rate", 0f));
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (hasPhone) {
			menu.getItem(1).setEnabled(true);
		}
		return true;
	}
}
