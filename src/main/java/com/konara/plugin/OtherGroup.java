package com.konara.plugin;

public enum OtherGroup {
    Q1("A_salty_fish",2832034764L),
    Q2("Auuseul",1723249294L);




    private String RealName;
    private long QQ;
    private String Prefix;
    OtherGroup(String RealName, Long QQ, String Prefix){
        this.RealName=RealName;
        this.QQ=QQ;
        this.Prefix=Prefix;
    }

    OtherGroup(String RealName, Long QQ){
        this.RealName=RealName;
        this.QQ=QQ;
        this.Prefix=null;
    }

    public static String getName(long QQ) {
        for (OtherGroup l : OtherGroup.values()) {
            if (l.getQQ() == QQ) {
                return l.RealName;
            }
        }
        return null;
    }

    public static String getPrefix(long QQ) {
        for (OtherGroup l : OtherGroup.values()) {
            if (l.getQQ() == QQ) {
                return l.Prefix;
            }
        }
        return null;
    }

    public long getQQ() {
            return QQ;
        }
}
