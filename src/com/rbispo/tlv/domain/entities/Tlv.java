package com.rbispo.tlv.domain.entities;

import com.rbispo.tlv.Util.SimpleTlvHelper;
import com.rbispo.tlv.domain.enums.TagsEnum;

import java.util.ArrayList;
import java.util.List;

public class Tlv {
    private TagsEnum tag;
    private int length;
    private String value;

    public Tlv(TagsEnum tag, int length, String value) {
        this.tag = tag;
        this.length = length;
        this.value = value;
    }

    public TagsEnum getTag() {
        return tag;
    }


    public int getLength() {
        return length;
    }

    public String getValue() {
        return value;
    }

    public static Tlv getTagFromInput(String msg, TagsEnum tag) {
        byte[][] b = SimpleTlvHelper.readTLV(msg, (int) Long.parseLong(tag.getTag(), 16));
        Tlv t = null;
        if (b.length != 0) {
            byte[] b2 = new byte[b[0].length];
            for (int i = 0; i < b[0].length; i++) {
                b2[i] = b[0][i];
            }
            t = new Tlv(tag, b[0].length, SimpleTlvHelper.encodeHexString(b2));
        }
        return t;
    }

    public static void prettyPrinter(List<Tlv> list) {
        for (Tlv item: list) {
            System.out.println(item.getTag().getTag());
        }
    }

    public static List<Tlv> fillTags(String msg) {
        List<Tlv> l = new ArrayList<>();
        Tlv t;
        for (TagsEnum item : TagsEnum.values()){
          t = getTagFromInput(msg, item);
          if (t!=null){
            l.add(t);
          }
        }
        prettyPrinter(l);
        return l;
    }

}
