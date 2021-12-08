package com.arcane22.dbdbdeep;

import com.arcane22.dbdbdeep.v1.MainApp;

/**
 * @class Main
 * @author Lee Hong Jun (arcane22, hong3883@naver.com)
 * @description
 *  - Last modified 2021. 12. 08
 */

public class Main {

    public static void main(String[] args) throws Exception {

        // Get application version from program argument
        String version = args[0];

        // Separate application version
        switch(version) {
            case "v1":
                MainApp mainApp = new MainApp();
                mainApp.run();
                System.out.println("Run game version 1");
                break;

            case "v2":
                System.out.println("Run game version 2");
                break;

            default:
                throw new Exception("UndefinedVersionTypeException");
        }
    }
}

