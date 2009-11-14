package org.thesteve.unnag;

import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.Button;

public class UnNagActivity extends Activity
{
  @Override
    public void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      Button b = (Button)findViewById(R.id.delete_button);
      b.setOnClickListener( deleteListener );
    }

  private OnClickListener deleteListener = new OnClickListener() {
    public void onClick(View v) {
      deleteNags();
    }
  };

  private void message( String msg ) {
    Context context = getApplicationContext();
    CharSequence text = msg;
    Toast toast = Toast.makeText( context, text, Toast.LENGTH_SHORT );
    toast.show();
  }

  private void deleteNags() {
    ContentResolver cr = getContentResolver();

    Uri inbox = Uri.parse( "content://sms/inbox" );
    Cursor cursor = cr.query(
        inbox,
        new String[] { "_id", "thread_id", "body" },
        null,
        null,
        null);

    if (cursor == null)
      return;

    if (!cursor.moveToFirst())
      return;

    int count = 0;

    do {
      String body = cursor.getString( 2 );
      if( body.indexOf( "FRM:nagios@" ) == -1 )
        continue;
      long thread_id = cursor.getLong( 1 );
      Uri thread = Uri.parse( "content://sms/conversations/" + thread_id );
      cr.delete( thread, null, null );
      count++;
    } while ( cursor.moveToNext() );
    message( "Deleted: " + count );
  }
}
