package com.enterprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class EnterpriseManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseManagementSystemApplication.class, args);
        openBrowser("http://localhost:8082/untitled1/");
    }

    private static void openBrowser(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Desktop is not supported. Please open the URL manually: " + url);
        }
    }

}
