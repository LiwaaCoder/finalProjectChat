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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

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
    private byte encryptionKey[] = {1,2,3,4};
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
                strMessage = dataSnapshot.getValue(String.class);
                strMessage = strMessage.substring(1,strMessage.length()-1);

                String[] strMessageArr = strMessage.split(", ");
                String[] strFinal = new String[strMessageArr.length*2];
                for(int i = 0; i < strMessageArr.length; i++){
                    String[] strKeyValue = strMessageArr[i].split("-", 2);
                    strFinal[i*2] = (String)android.text.format.DateFormat.format("dd-MM-YYYY hh:mm:ss", Long.parseLong(strKeyValue[0]));
                    try {
                        strFinal[i*2+1] = AESDecriprion(strKeyValue[1]);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, strFinal));
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