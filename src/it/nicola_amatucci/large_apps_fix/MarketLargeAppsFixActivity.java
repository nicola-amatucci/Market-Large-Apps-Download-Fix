package it.nicola_amatucci.large_apps_fix;

import it.nicola_amatucci.android.utils.MessageBoxDialog;

import java.io.File;

import ru.org.amip.MarketAccess.utils.ShellInterface;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MarketLargeAppsFixActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btnApply = (Button)findViewById(R.id.button1);
        Button btnExit = (Button)findViewById(R.id.button2);
        
        btnApply.setOnClickListener( new View.OnClickListener() {
			
			public void onClick(View v) {
				if (isExternalStorageWritable())
				{
					String url = Environment.getExternalStorageDirectory().getAbsolutePath();
					
					if (ShellInterface.isSuAvailable()) {
	
						ShellInterface.runCommand("rm -R /cache/download");
						
						if (new File (url + "/market-download-cache").exists() == false)
							ShellInterface.runCommand("mkdir " + url + "/market-download-cache");
						
						ShellInterface.runCommand("ln -s " + url + "/market-download-cache /cache/download");
						ShellInterface.runCommand("chown -h system.cache /cache/download");
						
						new MessageBoxDialog(MarketLargeAppsFixActivity.this, "Script succesfully terminated!\nHave a nice day!").showWithoutException();
					}
					else
					{
						new MessageBoxDialog(MarketLargeAppsFixActivity.this, "No root permissions!").showWithoutException();
					}
				}
				else
				{
					new MessageBoxDialog(MarketLargeAppsFixActivity.this, "External storage must be writable!").showWithoutException();
				}
			}
		});
        
        btnExit.setOnClickListener( new View.OnClickListener() {
			
			public void onClick(View v) {
				showExitDialog();
			}
        });
    }
    
    public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();

		if ( Environment.MEDIA_MOUNTED.equals(state) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	showExitDialog();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}    
    
	public void showExitDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(MarketLargeAppsFixActivity.this);
		alert.setTitle(R.string.exit_confirm_title);
		alert.setMessage(R.string.exit_confirm_message);
		alert.setCancelable(false);
		alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				MarketLargeAppsFixActivity.this.finish();
			}
		});
		alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		alert.show();
	}
}