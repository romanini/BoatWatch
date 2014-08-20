package com.threedrunkensailors.boatwatch;

import com.google.gdata.util.ServiceException;
import com.threedrunkensailors.boatwatch.google.GoogleDocs;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleTest {
	public static void main(String[] args) {
        try {
            //GoogleDocs.uploadMetric();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}

}

