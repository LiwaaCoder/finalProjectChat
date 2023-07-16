package com.example.finalprojectchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.SimpleDateFormat;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText editText;
    private DatabaseReference dbr;
    private String strMessage;
    //private byte encryptionKey[] = {1,2,3,4};
    private byte[] encryptionKey = new byte[16]; // 128 bits
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        dbr = FirebaseDatabase.getInstance().getReference("Message");
        try {
            cipher = Cipher.getInstance("AES");
            decipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        secretKeySpec = new SecretKeySpec(encryptionKey, "AES");

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (dataMap != null) {
                        StringBuilder strBuilder = new StringBuilder();

                        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                            String timestamp = entry.getKey();
                            String message = entry.getValue().toString();
                            try {
                                message = AESDecriprion(message);
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                            long timestampMillis = Long.parseLong(timestamp);
                            String formattedTimestamp = formatTimestamp(timestampMillis);

                            // Append the formatted message to the string builder
                            String formattedMessage = formattedTimestamp + ": " + message;
                            strBuilder.append(formattedMessage).append("\n");
                        }

                        String strMessage = strBuilder.toString();
                        // Now you can use strMessage as needed

                        // For example, if you want to update the ListView:
                        String[] strFinal = strMessage.split("\n");
                        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, strFinal));
                    }
                } else {
                    // Handle the case when the "Message" node doesn't exist or the value is null
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void findViews(){
        editText = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);
    }

    private String formatTimestamp(long timestampMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(timestampMillis));
    }

    public void sendButton(View view){
        Date date = new Date();
        dbr.child(Long.toString(date.getTime())).setValue(AESEncryption(editText.getText().toString()));
        editText.setText("");
    }

    private String AESEncryption(String str){
        String returnStr = null;
        byte[] strByte = str.getBytes();
        byte[] encryptedByte = new byte[strByte.length];
        try {
            cipher .init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedByte = cipher.doFinal(strByte);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }

        try {
            returnStr = new String(encryptedByte, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return returnStr;
    }

    private String AESDecriprion(String str) throws UnsupportedEncodingException {
        byte[] encryptedByte = str.getBytes("ISO-8859-1");
        String decryptionStr = str;
        byte[] decryption;
        try {
            decipher.init(cipher.DECRYPT_MODE, secretKeySpec);
            decryption = decipher.doFinal(encryptedByte);
            decryptionStr = new String(decryption);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return decryptionStr;
    }
}