package com.android.jrobbins.dungeonmaker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.jrobbins.dungeonmaker.hauberkMapper.Dungeon;
import com.android.jrobbins.dungeonmaker.hauberkMapper.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DungeonParamsFragment extends Fragment {

    public static final String MAP_DATA = "map_data_string";
    private static final String LOG_TAG = "DungoenParams";
    public static View rootView;

    public DungeonParamsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dungeon_params, container, false);

        Button button = (Button) rootView.findViewById(R.id.btn_generate);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RadioGroup group = (RadioGroup) rootView.findViewById(R.id.rad_group_ouptput);
                int radioButtonID = group.getCheckedRadioButtonId();
                if(radioButtonID == R.id.rad_mail)
                    new GenerateDungeonTask().execute(true);
                else
                    new GenerateDungeonTask().execute(false);
            }
        });

        return rootView;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG_TAG,"Permission is granted");
                return true;
            } else {

                Log.v(LOG_TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOG_TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(LOG_TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            if(isStoragePermissionGranted()) {
                new GenerateDungeonTask().execute(true);
            }
            else {
                Toast.makeText(getActivity(), getString(R.string.toast_access_error), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(), R.string.toast_access_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Custom Async Task used to generate dungeon
     */
    public class GenerateDungeonTask extends AsyncTask<Boolean, Void, String> {
        private final String LOG_TAG = GenerateDungeonTask.class.getSimpleName();

        ProgressDialog mProgress;
        Boolean isSendingHTML = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgress = new ProgressDialog(getActivity());
            mProgress.setTitle(getString(R.string.building));
            mProgress.setMessage(getString(R.string.creating_dungeon));
            mProgress.setCancelable(false);
            mProgress.show();
        }

        @Override
        protected String doInBackground(Boolean... params) {
            Dungeon dungeon = onGenerateMap(mProgress);
            String mapData;
            isSendingHTML = params[0];
            if(isSendingHTML)
            {
                mapData = dungeon.writeMap();
            } else {
                mapData = dungeon.displayMap();
            }

            return mapData;
        }

        @Override
        protected void onPostExecute(String mapData) {
            super.onPostExecute(mapData);

            mProgress.dismiss();

            if(isSendingHTML) {
                File file = saveHtmlFile(mapData);
                sendMapToEmail(file);
            } else {
                Intent intentDetail = new Intent(getActivity(), DungeonActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, mapData);
                startActivity(intentDetail);
            }

        }

        private void sendMapToEmail(File mapFile)
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(mapFile));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (intent.resolveActivity(rootView.getContext().getPackageManager()) != null) {
                startActivity(intent);
            }
        }

        public Dungeon onGenerateMap(ProgressDialog mProgress)
        {
            int mapWidth = getIntFromEditBox(R.id.map_width);
            int mapHeight = getIntFromEditBox(R.id.map_height);

            Stage stage = new Stage(mapWidth, mapHeight);
            Dungeon dungeon = new Dungeon();


            dungeon.windingPercent = getIntFromEditBox(R.id.map_path_winding);
            dungeon.numRoomTries = getIntFromEditBox(R.id.map_room_count);
            dungeon.roomExtraSize = getIntFromEditBox(R.id.map_room_size);
            dungeon.dungeonLevel = getIntFromEditBox(R.id.map_level);

            dungeon.generate(stage, mProgress);

            return dungeon;
        }

        private int getIntFromEditBox(int resource) {
            return Integer.parseInt(((EditText) DungeonParamsFragment.rootView.findViewById(resource)).getText().toString());
        }

        private File saveHtmlFile(String mapData) {

            String path = Environment.getExternalStorageDirectory().getPath();
            String fileName = "Dungeon_" + DateFormat.format("dd_MM_yy_hh_mm_ss", System.currentTimeMillis()).toString();
            fileName = fileName + ".html";
            File file = new File(path, fileName);
            String html = "<!doctype html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Dungeon Map</title>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body style=\"font-family:monospace\">\n" +
                    mapData +
                    "\n</body>\n" +
                    "</html>";

            try {
                FileOutputStream out = new FileOutputStream(file);
                byte[] data = html.getBytes();
                out.write(data);
                out.flush();
                out.close();
                Log.e(LOG_TAG, "File Save : " + file.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return file;
        }
    }

}
