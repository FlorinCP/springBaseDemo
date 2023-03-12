package com.example.springdemo2.controller;
import com.example.springdemo2.model.AchizitiiPublice;
import com.example.springdemo2.model.Employee;
import com.example.springdemo2.repository.AchizitiiPubliceRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;


@RestController
// asta e cea mai imp chestie pentru ca ne permite sa comunicam cu frontend
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v2")
public class AchizitiiPubliceControler {

    // cu autowired injectam
    @Autowired
    private AchizitiiPubliceRepository achizitiiPubliceRepository;

    @GetMapping("/achizitiipublice")
    public List<AchizitiiPublice> getAllAchizitiiPublice(){
        return achizitiiPubliceRepository.findAll();
    }


    @GetMapping("/achizitiipublice2")
    public void connectToAchizitiiPublice() throws IOException, NoSuchAlgorithmException, KeyManagementException {
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
        for (int i = 0; i < divs.size(); i=i+2) {
            AchizitiiPublice achizitiePublica = new AchizitiiPublice();

            // aici punem titlul articolului
            achizitiePublica.setAchizitieTitle(divs.get(i).text());

            // aici punem termenul articolului
            achizitiePublica.setTermen(divs.get(i+1).text());

            // Salveaza in baza de date
            achizitiiPubliceRepository.save(achizitiePublica);

            // salveaza in CSV
            achizitiiPubliceList.add(achizitiePublica);

        }
//        for (Element div : divs) {
//            AchizitiiPublice achizitiePublica = new AchizitiiPublice(div.text());
//
//            achizitiiPubliceList.add(achizitiePublica);
//
//            achizitiiPubliceRepository.save(achizitiePublica);
//        }


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
                row.add("\"" + achizitiiPublice.getTermen() + "\"");

                // printing a CSV line
                printWriter.println(String.join(",", row));
            }
        }
    }

    @GetMapping("/wow")
    public AchizitiiPublice createAchizitiiPublice(@RequestBody AchizitiiPublice achizitiiPublice){
        return achizitiiPubliceRepository.save(achizitiiPublice);
    }


}
