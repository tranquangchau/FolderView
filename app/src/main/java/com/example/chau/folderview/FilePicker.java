package com.example.chau.folderview;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by chau on 9/18/2016.
 */
public class FilePicker extends ListActivity {

    public final static String EXTRA_FILE_PATH = "file_path";
    public final static String EXTRA_SHOW_HIDDEN_FILES = "show_hidden_files";
    public final static String EXTRA_ACCEPTED_FILE_EXTENSIONS = "accepted_file_extensions";
    private final static String DEFAULT_INITIAL_DIRECTORY = "/";
    protected File Directory;
    protected ArrayList<File> Files;
    protected FilePickerListAdapter Adapter;
    protected boolean ShowHiddenFiles = false;
    protected String[] acceptedFileExtensions;
    LayoutInflater inflator;
    View emptyView;
    ImageView imageViewhe;
    MediaPlayer mpdefaul;
    MediaPlayer mpforpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflator = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        emptyView = inflator.inflate(R.layout.empty_view, null);
        ((ViewGroup) getListView().getParent()).addView(emptyView);
        getListView().setEmptyView(emptyView);


        // Set initial directory
        Directory = new File(DEFAULT_INITIAL_DIRECTORY);

        // Initialize the ArrayList
        Files = new ArrayList<File>();

        // Set the ListAdapter
        Adapter = new FilePickerListAdapter(this, Files);
        setListAdapter(Adapter);

        // Initialize the extensions array to allow any file extensions
        acceptedFileExtensions = new String[] {};

        // Get intent extras
        if(getIntent().hasExtra(EXTRA_FILE_PATH))
            Directory = new File(getIntent().getStringExtra(EXTRA_FILE_PATH));

        if(getIntent().hasExtra(EXTRA_SHOW_HIDDEN_FILES))
            ShowHiddenFiles = getIntent().getBooleanExtra(EXTRA_SHOW_HIDDEN_FILES, false);

        if(getIntent().hasExtra(EXTRA_ACCEPTED_FILE_EXTENSIONS)) {

            ArrayList<String> collection =
                    getIntent().getStringArrayListExtra(EXTRA_ACCEPTED_FILE_EXTENSIONS);

            acceptedFileExtensions = (String[])
                    collection.toArray(new String[collection.size()]);
        }

//        mpdefaul.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            public void onCompletion(MediaPlayer mpdefaul) {
//                mpdefaul.release();
//                //finish(); // finish current activity
//            }
//        });

    }

    @Override
    protected void onResume() {
        refreshFilesList();
        super.onResume();
    }
    File[] files;
    protected void refreshFilesList() {

        Files.clear();
        ExtensionFilenameFilter filter =
                new ExtensionFilenameFilter(acceptedFileExtensions);

        files = Directory.listFiles(filter);

        if(files != null && files.length > 0) {

            for(File f : files) {

                if(f.isHidden() && !ShowHiddenFiles) {

                    //continue;
                    Files.add(f);
                }

                Files.add(f);
            }

            Collections.sort(Files, new FileComparator());
        }else{
            emptyView = inflator.inflate(R.layout.empty_view, null);
            ((ViewGroup) getListView().getParent()).addView(emptyView);
            getListView().setEmptyView(emptyView);
        }

        Adapter.notifyDataSetChanged();
    }
    String thuMucHienTai;
    @Override
    public void onBackPressed() {

        if(Directory.getParentFile() != null) {
            Log.d("CurrentDirec", Directory.getParentFile().toString());

            if(thuMucHienTai.equals(Directory.getParentFile().toString())){

            }else{
                stopPlaying(mpdefaul);
            }
            Directory = Directory.getParentFile();
            refreshFilesList();
            return;
        }

        super.onBackPressed();
    }
    Integer iPost;

    @Override
    protected void onListItemClick(final ListView l, View v, final int position, final long id) {

        File newFile = (File)l.getItemAtPosition(position);
        thuMucHienTai=Directory.getAbsolutePath();
        Log.d("setDirectory",thuMucHienTai);
        if(newFile.isFile()) {

//            Intent extra = new Intent();
//            extra.putExtra(EXTRA_FILE_PATH, newFile.getAbsolutePath());
//            setResult(RESULT_OK, extra);
//            finish();

            final String pathfile=newFile.getAbsolutePath();
            String extension = pathfile.substring(pathfile.lastIndexOf(".") + 1, pathfile.length());
            String filename = pathfile.substring(pathfile.lastIndexOf("/") + 1, pathfile.length());


            //Log.d("filenow", tokens.toString());
            //Log.d("filename1", tokens[tokens.length-4] +"__"+  tokens[tokens.length-3] +"__"+ tokens[tokens.length-2] +"__ "+tokens[tokens.length-1]);
            if(extension.equals("jpg") || extension.equals("JPG") || extension.equals("gif")||extension.equals("GIF")||extension.equals("png")||extension.equals("png")){
                View imgView = inflator.inflate(R.layout.imgview, null);
                ((ViewGroup) getListView().getParent()).addView(imgView);
                getListView().setEmptyView(imgView);

                final ImageView imageView= (ImageView) imgView.findViewById(R.id.imageViewhe);
                Log.d("setImageBitmap", pathfile);
                try {
                    imageView.setImageDrawable(getImageFromSdCard(pathfile));
                }catch (Exception e){
                    Toast.makeText(FilePicker.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                }

//                String[] tokens = pathfile.split("/");
//                if(tokens[tokens.length-4].equals("Learning_AudioPic")){
//                    String listl=tokens[tokens.length-3]+"/"+tokens[tokens.length-2]+"/"+tokens[tokens.length-1];
//                    String filelinm3=pathfile.replace(listl,"");//remove file jpg
//                    String folderanfa=tokens[tokens.length-2].substring(0, 1);
//                    String filenameremove=tokens[tokens.length-1].substring(0, tokens[tokens.length - 1].length() - 4);
//                    Log.d("filenameremove",filenameremove);
//                    filelinm3=filelinm3+"/Audio_Snds"+"/"+folderanfa+"_Snds/"+filenameremove+".mp3";
//                    Log.d("filelinm3", filelinm3);
//
//                }
                playforpic(pathfile);

                //set text
                final TextView tetvew= (TextView) imgView.findViewById(R.id.textView);
                tetvew.setText(filename);
                iPost=position;

                Button btn1= (Button) imgView.findViewById(R.id.button);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("btn_position", String.valueOf(iPost));
                        Log.d("btn_file", String.valueOf(files));
                        Log.d("btn_file_leng", String.valueOf(files.length));
                        try {
                            Integer icuu=iPost;
                            if(iPost>=files.length-1){
                                icuu=0;
                            }else{
                                icuu=iPost+1;
                            }
                            iPost=icuu;
                            final String pathfile=files[icuu].getAbsolutePath();
                            String extension = pathfile.substring(pathfile.lastIndexOf(".") + 1, pathfile.length());
                            String filename = pathfile.substring(pathfile.lastIndexOf("/") + 1, pathfile.length());
                            if(extension.equals("jpg")) {
                                imageView.setImageDrawable(getImageFromSdCard(pathfile));
                                playforpic(pathfile);
                            }
                            tetvew.setText(filename);
                        }catch (Exception e){
                            Toast.makeText(FilePicker.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        //Integer i1=position+1;
                        //onListItemClick(l,v,i1,id);
                    }
                });

                //Directory = newFile;
                //refreshFilesList();
                Files.clear();
                Directory = newFile;
                Adapter.notifyDataSetChanged();
            }
            if(extension.equals("mp3")||extension.equals("mid")||extension.equals("wav")||extension.equals("ogg")||extension.equals("flac")||extension.equals("3gp")||extension.equals("mp4")){

                try {

                    //mpdefaul.release();
                    if(mpdefaul!=null){
                       // mpdefaul.stop();
                        stopPlaying(mpdefaul);
                        //mpdefaul.reset();
                    }
                    mpdefaul = new MediaPlayer();
                    mpdefaul.setVolume(1.0f, 1.0f);
                    mpdefaul.setDataSource(pathfile);
                    mpdefaul.prepare();
                    mpdefaul.start();
                   // mpdefaul.release();
                    Toast.makeText(FilePicker.this, "Mp3 play", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
           // if(mpdefaul!=null){
                // mpdefaul.stop();
                //mpdefaul.prepare();

           // }
            Log.d("newFile", newFile.toString());
            Directory = newFile;
            refreshFilesList();
        }
        super.onListItemClick(l, v, position, id);
    }

    /**
     *
     * @param link_file //file jpg
     */
    private void playforpic(String link_file){
        String[] tokens = link_file.split("/");
        if(tokens[tokens.length-4].equals("Learning_AudioPic")){
            String listl=tokens[tokens.length-3]+"/"+tokens[tokens.length-2]+"/"+tokens[tokens.length-1];
            String filelinm3=link_file.replace(listl,"");//remove file jpg
            String folderanfa=tokens[tokens.length-2].substring(0, 1);
            String filenameremove=tokens[tokens.length-1].substring(0, tokens[tokens.length - 1].length() - 4);
            Log.d("filenameremove",filenameremove);
            filelinm3=filelinm3+"/Audio_Snds"+"/"+folderanfa+"_Snds/"+filenameremove+".mp3";
            Log.d("filelinm3",filelinm3);
            //playforpic(filelinm3);
            try {
                //mpdefaul.release();
                if(mpforpic!=null){
                    // mpdefaul.stop();
                    //mpdefaul.reset();
                    stopPlaying(mpforpic);
                }
                mpforpic = new MediaPlayer();
                mpforpic.setVolume(1.0f, 1.0f);
                mpforpic.setDataSource(filelinm3);
                mpforpic.prepare();
                mpforpic.start();
                // mpdefaul.release();
                //Toast.makeText(FilePicker.this, "Mp3 play", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopPlaying(MediaPlayer mp) {
        //if (mpdefaul != null) {
        try {
            //if (mpdefaul.isPlaying()) {
            if (mp.isPlaying()) mp.stop();
           // mpdefaul.stop();
               // mpdefaul.reset();
            mp.release();
            mp = null;
                //Toast.makeText(FilePicker.this, "Mp3 Stop", Toast.LENGTH_SHORT).show();

                // mpdefaul.release();
                //mpdefaul = null;
            //}
        } catch (IllegalStateException e){
            Log.d("IllegalStateException", e.toString());
        } catch (Exception e){
            Log.d("ERroti", e.toString());
        }

        //}
    }




    public Drawable getImageFromSdCard(String imageName) {
        Drawable d = null;
        try {
           // String path = Environment.getExternalStorageDirectory().toString()
                 //   + "/YourSubDirectory/";
            Bitmap bitmap = BitmapFactory.decodeFile(imageName);
            d = new BitmapDrawable(bitmap);
        } catch (IllegalArgumentException e) {
            Log.d("ErrorLoadPic", e.toString());
            // TODO: handle exception
        } catch(NullPointerException e){
            Log.d("NullPointerException", e.toString());
        } catch(Exception e){
            Log.d("Exception", e.toString());
        }
        return d;

    }


    private class FilePickerListAdapter extends ArrayAdapter<File> {

        private List<File> mObjects;

        public FilePickerListAdapter(Context context, List<File> objects) {

            super(context, R.layout.list_item, android.R.id.text1, objects);
            mObjects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = null;

            if(convertView == null) {

                LayoutInflater inflater = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                row = inflater.inflate(R.layout.list_item, parent, false);
            }
            else
                row = convertView;

            File object = mObjects.get(position);

            ImageView imageView = (ImageView)row.findViewById(R.id.file_picker_image);
            TextView textView = (TextView)row.findViewById(R.id.file_picker_text);
            textView.setSingleLine(true);
            textView.setText(object.getName());

            if(object.isFile())
                imageView.setImageResource(R.drawable.filexxhdpi);

            else
                imageView.setImageResource(R.drawable.folderxxhdpi);

            return row;
        }
    }

    private class FileComparator implements Comparator<File> {

        public int compare(File f1, File f2) {

            if(f1 == f2)
                return 0;

            if(f1.isDirectory() && f2.isFile())
                // Show directories above files
                return -1;

            if(f1.isFile() && f2.isDirectory())
                // Show files below directories
                return 1;

            // Sort the directories alphabetically
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    }

    private class ExtensionFilenameFilter implements FilenameFilter {

        private String[] Extensions;

        public ExtensionFilenameFilter(String[] extensions) {

            super();
            Extensions = extensions;
        }

        public boolean accept(File dir, String filename) {

            if(new File(dir, filename).isDirectory()) {

                // Accept all directory names
                return true;
            }

            if(Extensions != null && Extensions.length > 0) {

                for(int i = 0; i < Extensions.length; i++) {

                    if(filename.endsWith(Extensions[i])) {

                        // The filename ends with the extension
                        return true;
                    }
                }
                // The filename did not match any of the extensions
                return false;
            }
            // No extensions has been set. Accept all file extensions.
            return true;
        }
    }
}
