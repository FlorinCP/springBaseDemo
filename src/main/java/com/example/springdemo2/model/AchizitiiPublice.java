package com.example.springdemo2.model;

import jakarta.persistence.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "achizitiiPublice")
public class AchizitiiPublice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "a_Title")
    private String achizitieTitle;
    @Column(name = "termen")
    private String termen;

    public AchizitiiPublice() {

    }
    public AchizitiiPublice(String achizitieTitle) {
this.achizitieTitle = achizitieTitle;
    }

    public AchizitiiPublice(long id, String achizitieTitle, String termen) {
        this.id = id;
        this.achizitieTitle = achizitieTitle;
        this.termen = termen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAchizitieTitle() {
        return achizitieTitle;
    }

    public void setAchizitieTitle(String achizitieTitle) {
        this.achizitieTitle = achizitieTitle;
    }

    public String getTermen() {
        return termen;
    }

    public void setTermen(String termen) {
        this.termen = termen;
    }


    public static void connectToAchizitiiPublice() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        /*
         *  fix for
         *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
         *       sun.security.validator.ValidatorException:
         *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
         *               unable to find valid certification path to requested target
         */
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }

                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        Document doc = Jsoup.connect("https://www.cdep.ro/informatii_publice/achizitii2015.home").get();

        Elements divs = doc.getElementsByTag("b");

        List<AchizitiiPublice> achizitiiPubliceList = new ArrayList<>();
        for (Element div : divs) {
            AchizitiiPublice achizitiePublica = new AchizitiiPublice();

            String text = div.text();

            achizitiePublica.setAchizitieTitle(text);
            achizitiiPubliceList.add(achizitiePublica);
        }


        // initializing the output CSV file
        File csvFile = new File("divOutput.csv");

        // using the try-with-resources to handle the
        // release of the unused resources when the writing process ends
        try (PrintWriter printWriter = new PrintWriter(csvFile)) {
            // iterating over all quotes
            for (AchizitiiPublice achizitiiPublice : achizitiiPubliceList) {
                // converting the test data into a list of strings
                List<String> row = new ArrayList<>();

                // wrapping each field with between quotes
                // to make the CSV file more consistent
                row.add("\"" + achizitiiPublice.getAchizitieTitle() + "\"");

                // printing a CSV line
                printWriter.println(String.join(",", row));
            }
        }
    }
}
