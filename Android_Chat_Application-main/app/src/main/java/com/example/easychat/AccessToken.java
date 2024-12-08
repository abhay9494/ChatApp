package com.example.easychat;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import com.google.firebase.auth.GoogleAuthCredential;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AccessToken {

    public static final String firebaseMessagingScopr = "https://www.googleapis.com/auth/firebase.messaging";

    public String getAccessToken(){
        try{
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"abhashi\",\n" +
                    "  \"private_key_id\": \"a09d12a50637cc00020b799bc1d5f477905ebe6d\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDclb+to91aa+sG\\njhOIUcq6gZGlp8NyPEenAVA1ONqKQIsiPAKal2Z0lvbwPpCIPNto+rpKlnNqINzF\\nc6X8otWUmLQpZ0GTwWfwZfwSFLCdIghUw2ecH2GvsnFcf7SrxdrtLDw8FldvYBqY\\nTS7gEqXM7HVlm7IhGvlx3igMD0xOOC5Zs91uTjIPSbGI8eHDeh0gUjoAhU/ypj3o\\nv3oqjVuZ2Uli/cD87UqR20dkSaXxdRmg/DJ+5iQsFDBRfmw31rypSj/gYcX5dwM2\\nLgM2lZ9PQ5k3e/pdCpxdNsRqRAT64+Mh1DYkqaCw/3RC2QV6/ZiWA5Gkwj6A1v1n\\npvrOvm7HAgMBAAECggEABCga6USTy+Ir/U//viksrV9g0BHSvHXHeALyBia5WWYw\\n9t5rOb4SBozhUPkLPoaDDfETUwKxgs9clnVnVuX3S6G2whGkWRMjO6PnRqydM8la\\nsICdv06RjBy7FBFPmKDrAk13hpPOqrhzhsdBj07JlWHR2S4Vz3WxXfv5fBU52YMN\\njPFkXegoglE9ZQUnCIs0rxBYBmxijTHd3ieoN6RUgN1iZ0Brh3PfTIgfqGgPrbPO\\ngPPTjBS9dQ6bY9lV5OaJsrGkS646bjVoxbuOBJ9hz5YqkVdvHU+/HuE8RBsXyZJ5\\ndf7LyKpbZQevb4GGfvJ1lmNx9kedZMMxnl46JOdCiQKBgQD0xcZFt3NQdt98B9fs\\n5sl9AMt1Q3cL9ygYA443C/FUL2IzMvtc/EtShrJmWJNdQyL2uJWD5cDja+NiWlLo\\n6OK9btBkmn06qrWNqRtylXpaSsEcvvMNi+o9wocq0Vio8yHRSJw+YLR7b/y0Gj4T\\nSOvCM8l3ea9zsAiPgxgGfUhhzwKBgQDms/P0WpAdPtxukQoUoIZ/cHf4Vy0PiNeU\\n/jNckYXd6rhM/slFhbGdoqbTWet1KqpKOBQMVakTk0VPVRiXiiY8+zj/KGBNf8Bk\\nYOZPqxrcpSjee/m+OMfyC9s4AIv4t27a/cCpQ8CQRKnLY4hvNnsA/IBig+kB6zma\\ni3KNfNU5iQKBgQCFoU+skgnHfQ5nhVSxxhFsSDnLbCibBLUD3zUOAlwfLLs83nFy\\ng16P0G+nBmZ1fQiij4U4+/7Bag46jLtE4pee+deVhO4nUDlL/G26sbaZgsRI8Ep7\\nM86PJxtqGd6AZjQpMK30NBF0bmAU51rKZn4niORNcMaDrzANQXGlYVJZ6wKBgQCl\\nHym9vFAnzoVBUehQKTD658nGYsbqtnvutR6X48RXuJce7MGsU+tzsyYKYfKoEth9\\ndLPMvEberurNxE9sr0F9JiZ+YizDa2uNrNlLLmF0iXW+ACgZYVSixp+p7qJAazIZ\\n5cNOoyKhX7PT0jKlvu29H/zYBifw6qZ5GdPstut6WQKBgGKiiCV5vrhO6KDQSHB3\\nwirVe7V9xA4277EhPz99KDD/K9olIimADuEamjabmmmyb5bWaNaTkqJyZ0gdWJ9w\\nlzuQkr9OzOqlQJ5F6dEZD9yJiFR5YxIQ9u8Ezbl6N3fFXmzUKKAwm4qQ5OCThEzv\\nEMOWPuUHZA+/0QbxQNMXgXS3\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-dpo27@abhashi.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"112769562315997187038\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-dpo27%40abhashi.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n";

            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));

            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).createScoped(Lists.newArrayList(firebaseMessagingScopr));

            googleCredentials.refresh();

            return googleCredentials.getAccessToken().getTokenValue();
        }
        catch (Exception e){
            Log.e("ERROR", "" + e.getMessage());
            return null;
        }
    }
}
