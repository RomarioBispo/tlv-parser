package com.rbispo.tlv.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Classe que codifica/decodifica mensagem no formato BER TLV.
 */
public class BerTlvHelper {

    public static Map<String, String> parseTLV(String tlv) {
        if (tlv == null || tlv.length()%2!=0) {
            throw new RuntimeException("Invalid tlv, null or odd length");
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (int i=0; i<tlv.length();) {
            try {
                String key = tlv.substring(i, i=i+2);

                if ((Integer.parseInt(key,16) & 0x1F) == 0x1F) {
                    // extra byte for TAG field
                    key += tlv.substring(i, i=i+2);
                }
                String len = tlv.substring(i, i=i+2);
                int length = Integer.parseInt(len,16);

                if (length > 127) {
                    // more than 1 byte for lenth
                    int bytesLength = length-128;
                    len = tlv.substring(i, i=i+(bytesLength*2));
                    length = Integer.parseInt(len,16);
                }
                length*=2;

                String value = tlv.substring(i, i=i+length);
                //System.out.println(key+" = "+value);
                hashMap.put(key, value);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Error parsing number",e);
            } catch (IndexOutOfBoundsException e) {
                throw new RuntimeException("Error processing field",e);
            }
        }
        printMap(hashMap);
        return hashMap;
    }

    public static void printMap(Map<String, String> mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            byte[] b = pair.getValue().toString().getBytes();
            System.out.println(b.length/2);
            System.out.println(pair.getKey() + " = " + pair.getValue());
        }
    }

    public static String getTlv(Map<String, String> mp) {
        Iterator it = mp.entrySet().iterator();
        String msg = "";
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            byte[] b = pair.getValue().toString().getBytes();
            System.out.println(b.length/2);
            System.out.println(pair.getKey() + " = " + pair.getValue());
            msg += pair.getKey() + String.format("%02d", b.length/2) + pair.getValue();
        }
        return msg;
    }
}
